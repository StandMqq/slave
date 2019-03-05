package com.cegz.sys.controller.shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.shop.RedPacket;
import com.cegz.sys.model.shop.UserRedPacket;
import com.cegz.sys.model.shop.WeChat;
import com.cegz.sys.model.view.shop.UserRedPacketView;
import com.cegz.sys.service.shop.RedPacketService;
import com.cegz.sys.service.shop.UsersService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 红包后台控制类
 * 
 * @author tenglong
 * @date 2018年10月11日
 */
@RestController
@RequestMapping("/redPacket")
@Slf4j
public class RedPacketController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private UsersService usersService;

	@Autowired
	private RedPacketService redPacketService;

	/**
	 * 版本号
	 */
	@Value("${server.version}")
	private String serverVersion;

	/**
	 * 校验version
	 * 
	 * @param version
	 * @return
	 */
	private ResultData checkVersion(String version) {
		if (StringUtil.isEmpty(version)) {
			return serverAck.getParamError().setMessage("版本号不能为空");
		}
		if (serverVersion != null && !serverVersion.equals(version)) {
			return serverAck.getParamError().setMessage("版本错误");
		}
		return null;
	}

	/**
	 * 校验token
	 * 
	 * @param request
	 * @return
	 */
	private ResultData checkToken(HttpServletRequest request) {
		String tokens = request.getHeader("token");
		if (StringUtil.isEmpty(tokens)) {
			return serverAck.getLoginTimeOutError();
		}
		Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
		com.cegz.sys.model.adver.Users user = (com.cegz.sys.model.adver.Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		return null;
	}

	/**
	 * 通过广告id获取用户领取红包数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@PostMapping("getUserReceiveRedPacketListByAdverId")
	public ResultData getUserReceiveRedPacketListByAdverId(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, Long advertisementId) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (curPage == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		if (pageSize == null) {
			return serverAck.getParamError().setMessage("分页参数不能为空");
		}
		if (advertisementId == null) {
			return serverAck.getParamError().setMessage("广告id不能为空");
		}
		try {
			List<UserRedPacketView> resultList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = redPacketService.getUserReceiveRedPacketCount(advertisementId);

			// 用户领取红包数据列表
			List<UserRedPacket> list = redPacketService.getUserReceiveRedPacketList(startPage, pageSize,
					advertisementId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (UserRedPacket u : list) {
				UserRedPacketView view = new UserRedPacketView();
				view.setId(u.getId());
				com.cegz.sys.model.shop.Users createUser = u.getCreateUser();
				if (createUser != null) {
					WeChat weChat = usersService.getWeChatByUsersId(createUser.getId());
					if (weChat != null) {
						view.setWxName(weChat.getName());// 微信名
					}
				}
				RedPacket redPacket = u.getRedPacket();
				if (redPacket != null) {
					view.setMoney(redPacket.getMoney() == null ? 0 : redPacket.getMoney().doubleValue());
					view.setType(redPacket.getType());
				}
				view.setCreateTime(sdf.format(u.getCreateTime()));
				view.setPayAccount(u.getPayAccount());
				view.setStatus(u.getStatus());
				resultList.add(view);
			}

			// 企业红包：领取数/总数 type：红包类型，1 企业红包，2 商家红包
			Long redPacketReceiveQY = redPacketService.getRedPacketReceiveNum(advertisementId, 1);
			Long redPacketTotalQY = redPacketService.getRedPacketTotalNum(advertisementId, 1);
			redPacketReceiveQY = redPacketReceiveQY == null ? 0 : redPacketReceiveQY;
			redPacketTotalQY = redPacketTotalQY == null ? 0 : redPacketTotalQY;

			// 商家红包：领取数/总数
			Long redPacketReceiveSJ = redPacketService.getRedPacketReceiveNum(advertisementId, 2);
			Long redPacketTotalSJ = redPacketService.getRedPacketTotalNum(advertisementId, 2);
			redPacketReceiveSJ = redPacketReceiveSJ == null ? 0 : redPacketReceiveSJ;
			redPacketTotalSJ = redPacketTotalSJ == null ? 0 : redPacketTotalSJ;

			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", resultList);
			map.put("redPacketQY", redPacketReceiveQY + " / " + redPacketTotalQY);
			map.put("redPacketSJ", redPacketReceiveSJ + " / " + redPacketTotalSJ);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 财务打款确认操作
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 */
	@PostMapping("financePayConfirm")
	public ResultData financePayConfirm(HttpServletRequest request, String version, Long id) {

		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("主键不能为空");
		}
		try {
			Optional<UserRedPacket> opt = redPacketService.getUserReceiveRedPacketById(id);
			UserRedPacket userRedPacket = opt.get();
			if (userRedPacket == null) {
				return serverAck.getParamError().setMessage("未找到可操作数据");
			}
			int ret = redPacketService.financePayConfirm(id);
			if (ret != 0) {
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
