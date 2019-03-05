package com.cegz.sys.controller.shop;

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
import com.cegz.sys.model.adver.Advertisement;
import com.cegz.sys.model.adver.Device;
import com.cegz.sys.model.shop.RedPacket;
import com.cegz.sys.model.shop.UserRedPacket;
import com.cegz.sys.model.shop.Users;
import com.cegz.sys.model.shop.WithdrawCash;
import com.cegz.sys.model.view.shop.UserRedPacketView;
import com.cegz.sys.model.view.shop.WithdrawCashView;
import com.cegz.sys.service.adver.AdvertisementService;
import com.cegz.sys.service.adver.DeviceService;
import com.cegz.sys.service.shop.RedPacketService;
import com.cegz.sys.service.shop.WithdrawCashService;
import com.cegz.sys.util.ExcelUtil;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import com.cegz.sys.util.URLConnectionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 全城惠提现后台控制类
 * 
 * @author tenglong
 * @date 2018年11月6日
 */
@RestController
@RequestMapping("/shopWithdrawCash")
@Slf4j
public class ShopWithdrawCashController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private WithdrawCashService withdrawCashService;

	@Autowired
	private RedPacketService redpacketService;

	@Autowired
	private AdvertisementService advertisementService;

	@Autowired
	private DeviceService deviceService;

	/**
	 * 版本号
	 */
	@Value("${server.version}")
	private String serverVersion;

	@Value("${message.url}")
	private String messageUrl;

	@Value("${message.account}")
	private String account;

	@Value("${message.pwd}")
	private String pwd;

	@Value("${message.status}")
	private Boolean needStatus;

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
	 * 获取全城惠提现记录数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param wxName        : 微信名
	 * @param alipayAccount : 支付宝账号
	 * @return
	 */

	@RequiresPermissions(value = { "shopWithdrawCash/getWithdrawCashList" })
	@PostMapping("getWithdrawCashList")
	public ResultData getWithdrawCashList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			Byte status, String payAccount, String startTime, String endTime) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (curPage == null) {
			curPage = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		if (status == null) {
			return serverAck.getParamError().setMessage("状态错误");
		}
		if (StringUtils.isEmpty(startTime)) {
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}
		try {
			int count = withdrawCashService.countCashByStatus(status, payAccount, startTime, endTime);
			if (count <= 0) {
				return serverAck.getEmptyData();
			}
			// 获取总金额
			Double totalMoney = withdrawCashService.queryTotalMoneyByStatus(status, payAccount, startTime, endTime);

			int startPos = (curPage - 1) * pageSize;
			List<WithdrawCash> listData = withdrawCashService.listCashByStatus(status, startPos, pageSize, payAccount,
					startTime, endTime);
			if (listData == null || listData.isEmpty()) {
				return serverAck.getEmptyData();
			}
			int length = listData.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<WithdrawCashView> listView = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				WithdrawCash cash = listData.get(i);
				WithdrawCashView view = new WithdrawCashView();
				Users user = cash.getUser();
				// 获取用户信息
				view.setAlipayCount(user.getPayAccount());
				view.setPutMoney(cash.getMoney());
				view.setStatus(cash.getStatus());
				view.setCreateTime(sdf.format(cash.getCreateTime()));
				view.setId(cash.getId());
				view.setWxName(user.getWechat() == null ? null : user.getWechat().getName());
				if (cash.getUpdateTime() != null) {
					view.setUpdateTime(sdf.format(cash.getUpdateTime()));
				}
				view.setRemark(cash.getRemark());
				view.setName(user.getName());
				view.setPhone(user.getPhone());
				listView.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("total", count);
			map.put("totalMoney", totalMoney);
			map.put("list", listView);
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

	@RequiresPermissions(value = { "shopWithdrawCash/confirmPayMoney" })
	@PostMapping("confirmPayMoney")
	@Transactional
	public ResultData confirmPayMoney(HttpServletRequest request, String version, Long id, Byte status, String remark) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		String tokens = request.getHeader("token");
		if (StringUtil.isEmpty(tokens)) {
			return serverAck.getLoginTimeOutError();
		}
		Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
		com.cegz.sys.model.adver.Users user = (com.cegz.sys.model.adver.Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("id不能为空");
		}
		if (status == null) {
			return serverAck.getParamError().setMessage("状态不能为空");
		}
		try {
			Optional<WithdrawCash> opt = withdrawCashService.getWithdrawCashById(id);
			// 处理
			if (!opt.isPresent()) {
				return serverAck.getParamError().setMessage("id错误");
			}
			WithdrawCash withdrawCash = opt.get();
			if (withdrawCash.getStatus() == 1) {
				return serverAck.getFailure().setMessage("提现记录状态有误");
			}
			withdrawCash.setUpdateTime(new Date());
			withdrawCash.setStatus(status);
			withdrawCash.setRemark(remark);
			WithdrawCash withRet = withdrawCashService.saveWithdrawCash(withdrawCash);
			// 处理用户红包状态
			if (withRet == null) {
				return serverAck.getFailure();
			}
			// 修改红包状态 0 未提现，1 提现中 2 提现完成，3 提现失败
			if (withRet.getStatus() == 1) {
				withdrawCashService.updateUserPacketStatusByCash(withRet.getId(), (byte) 2);
			} else if (withRet.getStatus() == 2) {
				withdrawCashService.updateUserPacketStatusByCash(withRet.getId(), (byte) 3);
			}

			// 发送短信内容组装
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, Object> paramMap = new HashMap<>(16);
			if (withRet.getUser() != null && !StringUtil.isEmpty(withRet.getUser().getPhone())) {
				paramMap.put("mobile", withRet.getUser().getPhone());
			} else {
				// 记录失败日志
				log.info("红包转账发送短信失败：操作数据id-->" + id + ";" + "转账人id-->" + user.getId() + ";" + "转账状态-->"
						+ withRet.getStatus() + ";" + ";" + "转账时间-->" + df.format(new Date()) + ";"
						+ "失败原因-->未找到发送短信号码;");
				return serverAck.getFailure().setMessage("发送短信号码有误");
			}
			paramMap.put("account", account);
			paramMap.put("pswd", pwd);
			paramMap.put("needstatus", needStatus);
			if (withRet.getStatus() == 1) {
				// 发送提现成功短信
				paramMap.put("msg", "您在全城惠小程序的现金红包提现金额已到账，请注意查收！");
			} else if (withRet.getStatus() == 2) {
				// 发送提现失败短信
				paramMap.put("msg", "您在全城惠小程序的现金红包提现操作失败，请进入小程序查看详情！");
			}
			String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
			log.info("{}发送短信{},結果{}", withRet.getUser().getPhone(), "全城惠小程序的现金红包提现", retSms);

			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 导出提现记录列表
	 * 
	 * @param type          : 类型、状态
	 * @param alipayAccount : 支付宝账号
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author tenglong
	 * @date 2018年12月17日
	 */
	@RequestMapping("exportShopWithdrawCash")
	public void exportShopWithdrawCash(byte type, String alipayAccount, String startTime, String endTime,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(alipayAccount)) {
			alipayAccount = null;
		}
		if (StringUtils.isEmpty(startTime)) {
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}
		List<WithdrawCash> list = withdrawCashService.listCashByStatus(type, 0, 999999999, alipayAccount, startTime,
				endTime);
		// excel标题
		String[] title = new String[9];
		if (type == 0) {
			title[0] = "姓名";
			title[1] = "手机号";
			title[2] = "微信名";
			title[3] = "支付宝账号";
			title[4] = "提现金额";
			title[5] = "提现状态";
			title[6] = "提现时间";
		} else if (type == 1) {
			title[0] = "姓名";
			title[1] = "手机号";
			title[2] = "微信名";
			title[3] = "支付宝账号";
			title[4] = "提现金额";
			title[5] = "提现状态";
			title[6] = "提现时间";
			title[7] = "操作时间";
		} else if (type == 2) {
			title[0] = "姓名";
			title[1] = "手机号";
			title[2] = "微信名";
			title[3] = "支付宝账号";
			title[4] = "提现金额";
			title[5] = "提现状态";
			title[6] = "提现时间";
			title[7] = "操作时间";
			title[8] = "备注";
		}

		// excel文件名
		String fileName = "提现记录列表" + System.currentTimeMillis() + ".xls";

		// sheet名
		String sheetName = "提现记录列表";

		String[][] content = new String[list.size()][];

		if (list != null && list.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < list.size(); i++) {
				content[i] = new String[title.length];
				WithdrawCash cash = list.get(i);
				if (cash != null) {
					Users user = cash.getUser();
					if (user != null) {
						content[i][0] = user.getName();
						content[i][1] = user.getPhone();
						content[i][2] = user.getWechat() == null ? "" : user.getWechat().getName();
						content[i][3] = user.getPayAccount();
					} else {
						content[i][0] = "";
						content[i][1] = "";
						content[i][2] = "";
						content[i][3] = "";
					}
					content[i][4] = cash.getMoney() + "";
					if (cash.getStatus() == 0) {
						content[i][5] = "提现中";
					} else if (cash.getStatus() == 1) {
						content[i][5] = "成功";
					} else if (cash.getStatus() == 2) {
						content[i][5] = "失败";
					}
					content[i][6] = sdf.format(cash.getCreateTime());
					if (type == 1) {
						if (cash.getUpdateTime() != null) {
							content[i][7] = sdf.format(cash.getUpdateTime());
						} else {
							content[i][7] = "";
						}
					}
					if (type == 2) {
						if (cash.getUpdateTime() != null) {
							content[i][7] = sdf.format(cash.getUpdateTime());
						} else {
							content[i][7] = "";
						}
						content[i][8] = cash.getRemark();
					}
				} else {
					content[i][0] = "";
					content[i][1] = "";
					content[i][2] = "";
					content[i][3] = "";
					content[i][4] = "";
					content[i][5] = "";
					content[i][6] = "";
					if (type == 1) {
						content[i][7] = "";
					}
					if (type == 2) {
						content[i][7] = "";
						content[i][8] = "";
					}
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
	 * 获取全城惠提现记录详细数据列表
	 * 
	 * @param token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @return
	 */

	@RequiresPermissions(value = { "shopWithdrawCash/getWithdrawCashInfo" })
	@PostMapping("getWithdrawCashInfo")
	public ResultData getWithdrawCashInfo(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			Long cashId, String startTime, String endTime) {
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
		if (cashId == null) {
			return serverAck.getParamError().setMessage("提现id不能为空");
		}
		if (StringUtils.isEmpty(startTime)) {
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}
		try {
			int count = withdrawCashService.countUserRedPacketByCashId(cashId, startTime, endTime);
			if (count <= 0) {
				return serverAck.getEmptyData();
			}
			int startPos = (curPage - 1) * pageSize;
			List<UserRedPacket> listData = withdrawCashService.listUserRedPacketByCashId(startPos, pageSize, cashId,
					startTime, endTime);
			if (listData == null || listData.isEmpty()) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<UserRedPacketView> listView = new ArrayList<>();
			for (UserRedPacket urp : listData) {
				UserRedPacketView view = new UserRedPacketView();
				view.setId(urp.getId());
				view.setCreateTime(sdf.format(urp.getCreateTime()));
				// 广告名称
				RedPacket redPacket = null;
				if (urp.getRedPacket() != null) {
					Optional<RedPacket> rpOpt = redpacketService.getRedPacketById(urp.getRedPacket().getId());
					if (rpOpt != null && rpOpt.get() != null) {
						redPacket = rpOpt.get();
						Long adverId = redPacket.getAdverId();
						if (adverId != null) {
							Optional<Advertisement> aOpt = advertisementService.getAdvertisementById(adverId);
							if (aOpt != null && aOpt.get() != null) {
								Advertisement advertisement = aOpt.get();
								String title = advertisement.getTitle();
								view.setAdvertisementTitle(title);
							}
						}
					}
				}
				// 车牌号
				if (!StringUtil.isEmpty(urp.getImei())) {
					Device device = deviceService.getDeviceByImei(urp.getImei());
					if (device != null && device.getDrivingRegistration() != null) {
						String plateNumber = device.getDrivingRegistration().getPlateNumber();
						if (!StringUtil.isEmpty(plateNumber)) {
							view.setPlateNumber(plateNumber);
						}
					}
				}
				// 领取金额
				if (redPacket != null && redPacket.getMoney() != null) {
					view.setMoney(Double.parseDouble(redPacket.getMoney().toString()));
				}
				if (urp.getWithdrawCash() != null) {
					view.setWcStatus(urp.getWithdrawCash().getStatus());
				}
				listView.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("total", count);
			map.put("list", listView);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
