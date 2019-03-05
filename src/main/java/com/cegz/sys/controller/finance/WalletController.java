package com.cegz.sys.controller.finance;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.cegz.sys.model.adver.Advertiser;
import com.cegz.sys.model.adver.CheckMoney;
import com.cegz.sys.model.adver.Order;
import com.cegz.sys.model.adver.Price;
import com.cegz.sys.model.adver.SellBindUser;
import com.cegz.sys.model.adver.SellWallet;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.adver.Wallet;
import com.cegz.sys.model.view.adver.CheckMoneyView;
import com.cegz.sys.model.view.adver.WalletView;
import com.cegz.sys.service.adver.AccountService;
import com.cegz.sys.service.adver.AdvertiserService;
import com.cegz.sys.service.finance.WalletService;
import com.cegz.sys.service.sale.SaleBindService;
import com.cegz.sys.util.ExcelUtil;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 财务后台控制类
 * 
 * @author tenglong
 * @date 2018年7月19日
 */
@Slf4j
@RestController
@RequestMapping("/wallet")
public class WalletController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private AccountService accountService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private SaleBindService saleBindService;
	
	@Autowired
	private AdvertiserService advertiserService;

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
	 * 充值
	 * 
	 * @param token
	 * @param version
	 * @param phone
	 * @param money
	 * @return
	 * @author lijiaxin
	 * @date 2018年8月23日
	 */

	@RequiresPermissions(value = { "wallet/addWallet" })
	@PostMapping("addWallet")
	@Transactional
	public ResultData addWallet(HttpServletRequest request, String version, String phone, Double money,
			Double giveProportion) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		String tokens = request.getHeader("token");
		if (StringUtil.isEmpty(tokens)) {
			return serverAck.getLoginTimeOutError();
		}
		Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
		Users users = (Users) session.getAttribute("user");
		if (users == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		if (!StringUtil.isMobile(phone)) {
			return serverAck.getParamError().setMessage("手机号格式错误");
		}
		if (money == null) {
			return serverAck.getParamError().setMessage("充值金额不能为空");
		}
		if (money <= 0) {
			return serverAck.getParamError().setMessage("充值金额不能小于等于0");
		}
		if (money > 1000000) {
			return serverAck.getParamError().setMessage("充值金额过大");
		}

		try {
			Users user = accountService.getUserByName(phone);
			if (user == null || user.getId() == null) {
				return serverAck.getParamError().setMessage("充值账号不存在");
			}
			int ret = 0;
			Wallet wallet = user.getWallet();
			if (wallet == null || wallet.getId() == null) {
//				wallet = new Wallet();
//				wallet.setCreateTime(new Date());
//				wallet.setMoney(Double.parseDouble(money));
//				wallet.setCreateUserId(user);
//				wallet.setUpdateUserId(users);
//				wallet.setRemark("后台充值新建");
//				ret = accountService.InsertWallet(wallet, Double.parseDouble(money));
				ret = walletService.insertOrEditWallet(null, null, money, user.getId(), users.getId(), "后台充值新建",
						giveProportion);
			} else {
//				wallet.setUpdateTime(new Date());
//				wallet.setUpdateUserId(user);
				double oldMoney = wallet.getMoney();
				ret = walletService.insertOrEditWallet(wallet.getId(), oldMoney, money,
						wallet.getCreateUserId().getId(), users.getId(), null, giveProportion);
			}
			// 验证绑定信息
			SellBindUser sbu = saleBindService.getSellBindByUser(user.getId());
			if (sbu != null && sbu.getId() != null) {
				// 获取销售钱包，乘以0.1：代表销售提现分成改为百分之10
				double myMoney = money * 0.1;
				SellWallet sw = saleBindService.getSellWalletByUser(sbu.getSellUser().getId());
				if (sw == null || sw.getId() == null) {
					// 新建

					sw = new SellWallet();
					sw.setCreateTime(new Date());
					sw.setIsDeleted((byte) 0);
					sw.setTotalMoney(myMoney);
					sw.setFreezeMoney(myMoney);
					sw.setCurrentMoney((double) 0);
					sw.setUser(sbu.getSellUser());

				} else {
					sw.setUpdateTime(new Date());
					sw.setTotalMoney(sw.getTotalMoney() + myMoney);
					sw.setFreezeMoney(sw.getFreezeMoney() + myMoney);
				}
				saleBindService.insertSellWallet(sw);
			}
			if (ret > 0) {
				log.info("管理员：{}，对用户：{}充值{}成功", users.getUserName(), user.getUserName(), money);
				return serverAck.getSuccess();
			} else {
				log.error("管理员：{}，对用户：{}充值{}失败", users.getUserName(), user.getUserName(), money);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
		return null;
	}

	/**
	 * 获取充值列表
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年10月9日
	 */

	@RequiresPermissions(value = { "wallet/getWalletList" })
	@PostMapping("getWalletList")
	public ResultData getWalletList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
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
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}

		try {
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = walletService.queryWalletTotalCount(phone, startTime, endTime);
			// 钱包数据列表
			List<Wallet> list = walletService.queryWalletList(startPage, pageSize, phone, startTime, endTime);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<WalletView> result = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Wallet wallet : list) {
				WalletView view = new WalletView();
				view.setId(wallet.getId());
				Users createUser = wallet.getCreateUserId();
				if (createUser != null) {
					view.setCreateUserId(createUser.getId());
					view.setAccountNum(createUser.getPhone());
					// 充值总金额
					Double rechargeMoney = walletService.getRechargeMoney(createUser.getId());
					view.setRechargeMoney(rechargeMoney == null ? 0 : rechargeMoney);
					// 充值次数
					Long rechargeCount = walletService.getRechargeCount(createUser.getId());
					view.setRechargeCount(rechargeCount == null ? 0 : rechargeCount);
				}
				view.setMoney(wallet.getMoney());
				if (wallet.getUpdateTime() != null) {
					view.setUpdateTime(sdf.format(wallet.getUpdateTime()));
				}
				result.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("result", result);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 导出充值列表
	 * 
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author tenglong
	 * @date 2018年11月13日
	 */
	@RequestMapping("exportWallet")
	public void exportWallet(String phone, String startTime, String endTime, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		if (StringUtils.isEmpty(startTime)) {
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}
		List<Wallet> list = walletService.queryWalletList(0, 999999999, phone, startTime, endTime);
		// excel标题

		String[] title = { "账号", "充值总金额（元）", "充值次数", "账户余额（元）", "上次充值时间" };

		// excel文件名
		String fileName = "充值列表" + System.currentTimeMillis() + ".xls";

		// sheet名
		String sheetName = "充值列表";

		String[][] content = new String[list.size()][];

		if (list != null && list.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < list.size(); i++) {
				content[i] = new String[title.length];
				Wallet wallet = list.get(i);
				Users createUser = wallet.getCreateUserId();
				if (createUser != null) {
					content[i][0] = createUser.getPhone();
					// 充值总金额
					Double rechargeMoney = walletService.getRechargeMoney(createUser.getId());
					content[i][1] = rechargeMoney == null ? "0" : rechargeMoney + "";
					// 充值次数
					Long rechargeCount = walletService.getRechargeCount(createUser.getId());
					content[i][2] = rechargeCount == null ? "0" : rechargeCount + "";
				} else {
					content[i][0] = "";
					content[i][1] = "";
					content[i][2] = "";
				}
				content[i][3] = wallet.getMoney() + "";
				if (wallet.getUpdateTime() != null) {
					content[i][4] = sdf.format(wallet.getUpdateTime());
				} else {
					content[i][4] = "暂未充值";
				}
			}
		}

		// 创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

		// 响应到客户端
		try {
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/vnd.ms-excel; charset=gbk");// 导出的文件类型，次数为excel
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取充值详情列表
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年10月9日
	 */

	@RequiresPermissions(value = { "wallet/getCheckMoneyList" })
	@PostMapping("getCheckMoneyList")
	public ResultData getCheckMoneyList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			Long createUserId, String startTime, String endTime, Long category, Long setMeal) {
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
		if (createUserId == null) {
			return serverAck.getParamError().setMessage("被充值人id不能为空");
		}
		if (StringUtils.isEmpty(startTime)) {
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}
		if (StringUtils.isEmpty(category)) {
			category = null;
		}
		if (StringUtils.isEmpty(setMeal)) {
			setMeal = null;
		}
		try {
			Optional<Users> userOpt = accountService.getUserById(createUserId);
			Users userResult = userOpt.get();

			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = walletService.queryCheckMoneyCountByCreateUserId(createUserId, startTime, endTime,category, setMeal);
			// 钱包明细数据列表
			List<CheckMoney> list = walletService.queryCheckMoneyListByCreateUserId(startPage, pageSize, createUserId,
					startTime, endTime,category, setMeal);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<CheckMoneyView> result = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (CheckMoney checkMoney : list) {
				CheckMoneyView view = new CheckMoneyView();
				view.setId(checkMoney.getId());
				view.setVoucher(checkMoney.getVoucher());
				Users updateUser = checkMoney.getUpdateUserId();
				if (updateUser != null) {
					view.setOperator(updateUser.getPhone());
				} else {
					Users createUser = checkMoney.getCreateUserId();
					if (createUser != null) {
						view.setOperator(createUser.getPhone());
					}
				}
				Order order = checkMoney.getOrder();
				if(order != null) {
					Advertiser advertiser = advertiserService.findAdvertiserByOrderId(order.getId());
					
					if(order.getPrice() != null) {
						String setMeal1 = order.getPrice().getRemark();
						view.setSetMeal(setMeal1);
					}
					
					if(advertiser == null) {
						view.setAdvertiser(" ");
					} else {
						view.setAdvertiser(advertiser.getPhone());
					}
				}else {
					view.setAdvertiser(" ");
					view.setSetMeal(" ");
				}
				
				view.setType(checkMoney.getType());
				view.setBillStatus(checkMoney.getBillStatus());
				view.setRemark(checkMoney.getRemark());
				view.setMoney(checkMoney.getMoney());
				view.setCreateTime(sdf.format(checkMoney.getCreateTime()));
				result.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("result", result);
			map.put("createUserId", createUserId);
			if (userResult != null) {
				map.put("account", userResult.getPhone());
			}
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	public static void main(String[] args) {
		int n = 7;
		n = n << 3;
		n = n | 32 & n;
		System.out.println(n);
	}
}
