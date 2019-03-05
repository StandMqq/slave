package com.cegz.sys.controller.sale;

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
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.adver.WithdrawCash;
import com.cegz.sys.model.view.adver.WithdrawCashView;
import com.cegz.sys.service.sale.WithdrawCashService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 提现后台控制类
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
@RestController
@RequestMapping("/withdrawCash")
@Slf4j
public class WithdrawCashController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private WithdrawCashService withdrawCashService;

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
	 * 获取提现记录数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param bankCardName     : 银行卡姓名
	 * @param openBankCardName : 银行卡开户行
	 * @param alipayName       : 支付宝姓名
	 * @param alipayAccount    : 支付宝账号
	 * @return
	 */

	@RequiresPermissions(value = { "withdrawCash/getWithdrawCashList" })
	@PostMapping("getWithdrawCashList")
	public ResultData getWithdrawCashList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String bankCardName, String openBankCardName, String alipayName, String alipayAccount) {
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
		if (StringUtils.isEmpty(bankCardName)) {
			bankCardName = null;
		}
		if (StringUtils.isEmpty(openBankCardName)) {
			openBankCardName = null;
		}
		if (StringUtils.isEmpty(alipayName)) {
			alipayName = null;
		}
		if (StringUtils.isEmpty(alipayAccount)) {
			alipayAccount = null;
		}
		try {
			List<WithdrawCashView> withdrawCashList = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = withdrawCashService.getWithdrawCashCount(bankCardName, openBankCardName, alipayName,
					alipayAccount);
			// 提现数据列表
			List<WithdrawCash> list = withdrawCashService.getWithdrawCashList(startPage, pageSize, bankCardName,
					openBankCardName, alipayName, alipayAccount);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (WithdrawCash wc : list) {
				WithdrawCashView view = new WithdrawCashView();
				view.setId(wc.getId());
				view.setType(wc.getType());
				if (wc.getUser() != null) {
					Users user = wc.getUser();
					if (wc.getType() == 1 && user.getPayAccount() != null) {
						view.setAlipayName(user.getPayAccount().getName());
						view.setAlipayCount(user.getPayAccount().getPayAccount());
					}
					if (wc.getType() == 2 && user.getBankCard() != null) {
						view.setBankCardName(user.getBankCard().getName());
						view.setOpeningBank(user.getBankCard().getBank());
						view.setBankNum(user.getBankCard().getCardNo());
					}
				}
				view.setStatus(wc.getStatus());
				view.setPutMoney(wc.getMoney());
				view.setCreateTime(sdf.format(wc.getCreateTime()));
				withdrawCashList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", withdrawCashList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 确认打款
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return
	 */

	@RequiresPermissions(value = { "withdrawCash/confirmPayMoney" })
	@PostMapping("confirmPayMoney")
	public ResultData confirmPayMoney(HttpServletRequest request, String version, Long id) {
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
			Optional<WithdrawCash> opt = withdrawCashService.getWithdrawCashById(id);
			// 处理
			WithdrawCash withdrawCash = opt.get();
			withdrawCash.setUpdateTime(new Date());
			withdrawCash.setStatus((byte) 1);
			int ret = withdrawCashService.saveWithdrawCash(withdrawCash);
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
