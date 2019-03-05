package com.cegz.sys.controller.shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.shop.Integral;
import com.cegz.sys.model.shop.IntegralGoodsRecord;
import com.cegz.sys.model.shop.WeChat;
import com.cegz.sys.model.view.shop.IntegralView;
import com.cegz.sys.service.shop.IntegralExchangeService;
import com.cegz.sys.service.shop.UsersService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分兑换控制类
 * 
 * @author yucheng
 * @date 2018年11月29日
 */
@RestController
@RequestMapping("/integralExchange")
@Slf4j
public class IntegralExchangeController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private IntegralExchangeService integralExchangeService;
	
	@Autowired
	private UsersService usersService;
	
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

	@RequiresPermissions(value = { "integralExchange/getIntegralGoodsRecordList" })
	@PostMapping("getIntegralGoodsRecordList")
	public ResultData getIntegralGoodsRecordList(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, String phone, String name, String typeNo) {
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
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(typeNo)) {
			typeNo = null;
		}
		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			// 用户数据列表
			List<IntegralGoodsRecord> list = integralExchangeService.queryIntegralGoodsRecordList(startPage, pageSize,
					phone, name, typeNo);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<IntegralView> resultList = new ArrayList<>();
			Long totalCount = (long) list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (IntegralGoodsRecord idr : list) {
				IntegralView view = new IntegralView();
				view.setId(idr.getId());
				view.setCreateTime(sdf.format(idr.getCreateTime()));
				WeChat weChat = usersService.getWeChatByUsersId(idr.getUsers().getId());
				if (weChat != null) {
					view.setwXName(weChat.getName());// 微信名
				}
				view.setPhone(idr.getPhone());
				view.setGoodsName(idr.getGoods().getName());
				view.setNeedNumber(idr.getGoods().getIntegral());
				Integral integral = integralExchangeService.queryIntegralByCreatUserId(idr.getUsers().getId());
				if(integral != null) {
					view.setUsableNumber(integral.getUsableNumber());
				}
				if(idr.getUpdateTime() != null) {
					view.setUpdateTime(sdf.format(idr.getUpdateTime()));
				}
				view.setIsDeleted(idr.getIsDeleted());
				resultList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("total", totalCount);
			map.put("list", resultList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
	
	/**
	 * 确认兑换商品
	 * 
	 * @param id
	 * @author tenglong
	 * @date 2018年8月23日
	 */

	@RequiresPermissions(value = { "integralExchange/confirmExchange" })
	@PostMapping("confirmExchange")
	public ResultData confirmExchange(Long id, HttpServletRequest request, String version) {

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
			// 处理
			Optional<IntegralGoodsRecord> opt = integralExchangeService.getIntegralGoodsRecordById(id);
			IntegralGoodsRecord igr = opt.get();
			igr.setIsDeleted((byte) 1);
			igr.setUpdateTime(new Date());
			int ret = integralExchangeService.save(igr);
			if (ret != 0) {
				return serverAck.getSuccess();
			} else {
				return serverAck.getFailure();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
