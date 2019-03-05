package com.cegz.sys.controller.shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cegz.sys.model.shop.IntegralRecord;
import com.cegz.sys.model.shop.UserTicket;
import com.cegz.sys.model.shop.Users;
import com.cegz.sys.model.shop.WeChat;
import com.cegz.sys.model.view.shop.IntegralView;
import com.cegz.sys.model.view.shop.TicketView;
import com.cegz.sys.model.view.shop.UsersView;
import com.cegz.sys.service.shop.UsersService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户后台控制类
 * 
 * @author tenglong
 * @date 2018年9月25日
 */
@RestController
@RequestMapping("/shopUsers")
@Slf4j
public class ShopUsersController {
	@Autowired
	private ServerAck serverAck;

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

	/**
	 * 获取用户数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */

	@RequiresPermissions(value = { "shopUsers/getUsersList" })
	@PostMapping("getUsersList")
	public ResultData getUsersList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String phone, String startTime, String endTime) {
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
		if (StringUtils.isEmpty(startTime)) {
			startTime = "0000:00:00";
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = "9999:60:60";
		}
		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			List<UsersView> usersList = new ArrayList<>();
			Long totalCount = usersService.getUsersCount(phone, startTime, endTime);

			// 用户数据列表
			List<Users> list = usersService.getUsersList(startPage, pageSize, phone, startTime, endTime);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Users u : list) {
				UsersView view = new UsersView();
				view.setId(u.getId());
				WeChat weChat = usersService.getWeChatByUsersId(u.getId());
				if (weChat != null) {
					view.setWxName(weChat.getName());// 微信名
				}
				view.setPhone(u.getPhone());
				Long pullNum = usersService.getPullNumByUsersId(u.getId());
				view.setPullNum(pullNum);// 领取数
				Long useNum = usersService.getUseNumByUsersId(u.getId(), (byte) 1);
				view.setUseNum(useNum);// 使用数
				Integral integral = usersService.getIntegralByUsersId(u.getId());
				if (integral != null) {
					view.setIntegral(integral.getTotalNumber());// 积分
				}
				view.setLoginTime(sdf.format(u.getLoginTime()));
				usersList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", usersList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取用户下劵信息
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param usersId
	 * @return
	 */

	@RequiresPermissions(value = { "shopUsers/getUsersTicketList" })
	@PostMapping("getUsersTicketList")
	public ResultData getUsersTicketList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			Long usersId) {
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
		if (usersId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			List<TicketView> ticketList = new ArrayList<>();
			Long totalCount = usersService.getUsersTicketCount(usersId);

			// 用户下劵数据列表
			List<UserTicket> list = usersService.getUsersTicketList(startPage, pageSize, usersId);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (UserTicket ut : list) {
				TicketView view = new TicketView();
				view.setId(ut.getId());
				view.setName(ut.getTicket().getName());
				view.setTypeName(ut.getTicket().getType().getName());
				view.setCreateTime(sdf.format(ut.getCreateTime()));
				if (ut.getUpdateTime() != null) {
					view.setUpdateTime(sdf.format(ut.getUpdateTime()));
				}
				view.setTicketNo(ut.getTicketNo());
				view.setStatus(ut.getStatus());
				view.setStartTime(ut.getTicket().getStartTime());
				view.setEndTime(ut.getTicket().getEndTime());
				ticketList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", ticketList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取用户下积分数据
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param usersId
	 * @return
	 */

	@RequiresPermissions(value = { "shopUsers/getUsersIntegralList" })
	@PostMapping("getUsersIntegralList")
	public ResultData getUsersIntegralList(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, Long usersId) {
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
		if (usersId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			List<IntegralView> integralList = new ArrayList<>();
			Long totalCount = usersService.getUsersIntegralCount(usersId);
			Integer integralTotal = null;

			// 用户下积分数据列表
			List<IntegralRecord> list = usersService.getUsersIntegralList(startPage, pageSize, usersId);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (IntegralRecord ir : list) {
				IntegralView view = new IntegralView();
				view.setId(ir.getId());
				view.setCreateTime(sdf.format(ir.getCreateTime()));
				view.setName(ir.getName());
				view.setUsableNumber(ir.getNumber());
				integralList.add(view);
			}
			integralTotal = list.get(0).getIntegral().getTotalNumber();
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("integralTotal", integralTotal);
			map.put("resultList", integralList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
