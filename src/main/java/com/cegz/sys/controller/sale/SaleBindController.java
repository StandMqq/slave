package com.cegz.sys.controller.sale;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.adver.CheckMoney;
import com.cegz.sys.model.adver.SellBindUser;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.sale.SellBindUserView;
import com.cegz.sys.service.sale.SaleBindService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 销售绑定控制类
 * 
 * @author tenglong
 * @date 2018年10月18日
 */
@RestController
@RequestMapping("/saleBind")
@Slf4j
public class SaleBindController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private SaleBindService saleBindService;

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
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		return null;
	}

	/**
	 * 获取所有销售绑定列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param phone    :账号
	 * @return
	 */
	@Transactional
	@PostMapping("getSaleBindAllList")
	public ResultData getSaleBindAllList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String phone) {
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
		try {
			List<SellBindUserView> sellBindUserList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = saleBindService.getSaleBindCount(null, phone);

			// 销售绑定数据列表
			List<SellBindUser> list = saleBindService.getSaleBindList(startPage, pageSize, null, phone);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (SellBindUser sbu : list) {
				SellBindUserView view = new SellBindUserView();
				view.setId(sbu.getId());
				view.setPhone(sbu.getSellUser().getUserName());
				view.setCreateTime(sdf.format(sbu.getCreateTime()));
				// 查询绑定人数
				Long bindNum = saleBindService.querySaleBindNum(sbu.getSellUser().getId());
				view.setBindNum(bindNum);
				sellBindUserList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", sellBindUserList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取个人销售绑定列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param phone    :账号
	 * @return
	 */
	@PostMapping("getSaleBindUnitList")
	public ResultData getSaleBindUnitList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String phone) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		String tokens = request.getHeader("token");
		if (StringUtil.isEmpty(tokens)) {
			return serverAck.getLoginTimeOutError();
		}
		Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
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
		try {
			List<SellBindUserView> sellBindUserList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = saleBindService.getSaleBindCount(user.getId(), phone);

			// 销售绑定数据列表
			List<SellBindUser> list = saleBindService.getSaleBindList(startPage, pageSize, user.getId(), phone);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (SellBindUser sbu : list) {
				SellBindUserView view = new SellBindUserView();
				view.setId(sbu.getId());
				view.setPhone(sbu.getSellUser().getUserName());
				view.setCreateTime(sdf.format(sbu.getCreateTime()));
				// 查询绑定人数
				Long bindNum = saleBindService.querySaleBindNum(sbu.getSellUser().getId());
				view.setBindNum(bindNum);
				sellBindUserList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", sellBindUserList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取绑定客户列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param sellBindUserId :绑定人id
	 * @return
	 */
	@Transactional
	@PostMapping("getBindClientsList")
	public ResultData getBindClientsList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			Long sellBindUserId) {
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
		if (StringUtils.isEmpty(sellBindUserId)) {
			return serverAck.getParamError().setMessage("绑定人id不能为空");
		}
		try {
			List<SellBindUserView> sellBindUserList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页

			Optional<SellBindUser> opt = saleBindService.getSellBindUserById(sellBindUserId);
			SellBindUser sellBindUser = opt.get();
			if (sellBindUser == null) {
				return serverAck.getEmptyData();
			}

			Long totalCount = saleBindService.getBindClientsCount(sellBindUser.getSellUser().getId());

			// 绑定客户数据列表
			List<SellBindUser> list = saleBindService.getBindClientsList(startPage, pageSize,
					sellBindUser.getSellUser().getId());
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (SellBindUser sbu : list) {
				SellBindUserView view = new SellBindUserView();
				view.setId(sbu.getId());
				view.setPhone(sbu.getUser().getUserName());
				view.setCreateTime(sdf.format(sbu.getCreateTime()));
				view.setUserId(sbu.getUser().getId());
				// 查询被绑定人累计充值金额
				Double rechargeMoneySum = saleBindService.queryBindAccountRechargeMoney(sbu.getUser().getId());
				if (rechargeMoneySum == null) {
					view.setRechargeMoneySum(0D);
				} else {
					view.setRechargeMoneySum(rechargeMoneySum);
				}
				// 查询被绑定人最后一次充值时间
				CheckMoney checkMoney = saleBindService.queryBindAccountLastRechargeTime(sbu.getUser().getId());
				if (checkMoney != null) {
					view.setLastRechargeTime(sdf.format(checkMoney.getCreateTime()));
				} else {
					view.setLastRechargeTime("暂未充值");
				}
				sellBindUserList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", sellBindUserList);
			map.put("sellUserName", sellBindUser.getSellUser().getUserName());
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
