/**
 * 
 */
package com.cegz.sys.controller.finance;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import com.cegz.sys.model.adver.ContactOnlineIncomeRecord;
import com.cegz.sys.model.adver.Contacts;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.adver.WithdrawCash;
import com.cegz.sys.model.view.adver.ContactsView;
import com.cegz.sys.model.view.shop.WithdrawCashView;
import com.cegz.sys.service.adver.AdverWithdrawCashService;
import com.cegz.sys.service.adver.DrivingRegistrationService;
import com.cegz.sys.service.finance.ContactOnlineIncomeRecordService;
import com.cegz.sys.service.finance.ContactsWalletService;
import com.cegz.sys.util.DateUtil;
import com.cegz.sys.util.ExcelUtil;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import com.cegz.sys.util.URLConnectionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 车主提现记录控制类
 * 
 * @author yucheng
 * @date 2018年12月25号
 */
@RestController
@RequestMapping("/contactIncomeRecord")
@Slf4j
public class ContactIncomeRecordController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private AdverWithdrawCashService adverwithdrawCashService;

	@Autowired
	private DrivingRegistrationService drivingRegistrationService;

	@Autowired
	private ContactOnlineIncomeRecordService contactOnlineIncomeRecordService;
	
	@Autowired
	private ContactsWalletService contactsWalletService;


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
	 * 车主提现记录列表
	 * 
	 * @param request //token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @return yucheng
	 * @date 2018年12月26日
	 */

	@RequiresPermissions(value = { "contactIncomeRecord/getContactIncomeRecordList" })
	@PostMapping("getContactIncomeRecordList")
	public ResultData getContactIncomeRecordList(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, Byte status, String contactName, String sponsorName, String startTime, String endTime) {
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
		if (StringUtils.isEmpty(contactName)) {
			contactName = null;
		}
		if (StringUtils.isEmpty(sponsorName)) {
			sponsorName = null;
		}
		if (StringUtils.isEmpty(startTime)) {
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}
		try {
			// 车主提现记录,类型为3的提现
			Long count = adverwithdrawCashService.countWithDrawCashByStatus(status, contactName, sponsorName, startTime,
					endTime);
			if (count == null || count <= 0) {
				return serverAck.getEmptyData();
			}

			//分页查询
			int startPage = (curPage - 1) * pageSize;
			List<WithdrawCash> listData = adverwithdrawCashService.listWithDrawByStatus(status, startPage, pageSize,
					contactName, sponsorName, startTime, endTime);
			if (listData == null || listData.isEmpty()) {
				return serverAck.getEmptyData();
			}
			//车主提现总金额
			double totalPutMoney = 0;
			//车主提现的总金额求和
			//totalPutMoney += cash.getMoney();
			totalPutMoney = adverwithdrawCashService.queryTotalMoneyByStatus(status);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<WithdrawCashView> listView = new ArrayList<>();
			for (int i = 0; i < listData.size(); i++) {
				WithdrawCash cash = listData.get(i);
				WithdrawCashView view = new WithdrawCashView();
				view.setId(cash.getId());
				view.setCreateTime(sdf.format(cash.getCreateTime()));
				view.setContactName(cash.getUser().getContact() == null ? null : cash.getUser().getContact().getName());
				view.setPutMoney(cash.getMoney());
				//获取联系人
				Contacts contact = cash.getUser().getContact();
				 if(contact != null) {
				 	//获取联系人名下车辆信息
					List<DrivingRegistration> carList =  drivingRegistrationService.getDrivingRegistrationByContactId(contact.getId());
					 String sponsorNameTemp = "";
						if (carList != null && carList.size() > 0) {
							for (DrivingRegistration dr : carList) {
								sponsorNameTemp += dr.getSponsor().getCompany() + ",";
							}
						}
						if (!StringUtils.isEmpty(sponsorNameTemp)) {
							sponsorNameTemp = sponsorNameTemp.substring(0, sponsorNameTemp.length() - 1);
						}
						view.setSponsorName(sponsorNameTemp);
				 }
				
				
				
				//view.setSponsorName(cash.getUser().getSponsor() == null ? null : cash.getUser().getSponsor().getCompany());
				view.setPutMoney(cash.getMoney());
				view.setAlipayCount(
						cash.getUser().getPayAccount() == null ? null : cash.getUser().getPayAccount().getPayAccount());
				if (cash.getUser().getContact().getId() != null) {
					view.setContactId(cash.getUser().getContact().getId());
				} else {
					return serverAck.getParamError().setMessage("id错误");
				}
				view.setIncomePropertion(cash.getUser().getCarCaptain() == null ? null
						: cash.getUser().getCarCaptain().getNowPropertion());
				if (cash.getUpdateTime() != null) {
					view.setUpdateTime(sdf.format(cash.getUpdateTime()));
				}
				view.setRemark(cash.getRemark());
				view.setStatus(cash.getStatus());
				listView.add(view);
			}

			Map<String, Object> map = new HashMap<>();
			map.put("total", count);
//			map.put("totalMoney", totalMoney);
			map.put("list", listView);
			map.put("totalPutMoney", totalPutMoney);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 车主提现确认打款
	 * 
	 * @param request//token
	 * @param version
	 * @param id
	 * @return yucheng
	 * @date 2018年12月27号
	 */

	@RequiresPermissions(value = { "contactIncomeRecord/confirmPayMoney" })
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
			
			Optional<WithdrawCash> opt = adverwithdrawCashService.getWithdrawCashById(id);
			// 处理
			if (!opt.isPresent()) {
				return serverAck.getParamError().setMessage("id错误");
			}
			WithdrawCash withdrawCash = opt.get();
			if(status == 2) {
				Users user2 = withdrawCash.getUser();
				if(user2.getContact() != null) {
					int ret = contactsWalletService.updateContactsWalletByContactsId(user2.getContact().getId(),withdrawCash.getMoney());
					if(ret > 0) {
						log.info(user2.getPhone() + "转账失败，钱已退回该账户");
					}
				}
			}
			if (withdrawCash.getStatus() == 1) {
				return serverAck.getFailure().setMessage("提现记录状态有误");
			}
			withdrawCash.setUpdateTime(new Date());
			withdrawCash.setStatus(status);
			withdrawCash.setRemark(remark);
			WithdrawCash withRet = adverwithdrawCashService.saveWithdrawCash(withdrawCash);
			// 处理车主提现状态
			if (withRet == null) {
				return serverAck.getFailure();
			}
			// 修改车主提现状态 0 未提现，1 提现中 2 提现完成，3 提现失败
			if (withRet.getStatus() == 1) {
				adverwithdrawCashService.updateUserPacketStatusByCash(withRet.getId(), (byte) 2);
			} else if (withRet.getStatus() == 2) {
				adverwithdrawCashService.updateUserPacketStatusByCash(withRet.getId(), (byte) 3);
			}
			
			// 发送短信内容组装
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, Object> paramMap = new HashMap<>(16);
			if (withRet.getUser() != null && !StringUtil.isEmpty(withRet.getUser().getPhone())) {
				 paramMap.put("mobile", withRet.getUser().getPhone());
			} else {
				// 记录失败日志
				log.info("车主提现发送短信失败：操作数据id-->" + id + ";" + "转账人id-->" + user.getId() + ";" + "转账状态-->"
						+ withRet.getStatus() + ";" + ";" + "转账时间-->" + df.format(new Date()) + ";"
						+ "失败原因-->未找到发送短信号码;");
				return serverAck.getFailure().setMessage("发送短信号码有误");
			}
			//引入的是application.yml配置文件中message的信息
			paramMap.put("account", account);
			paramMap.put("pswd", pwd);
			paramMap.put("needstatus", needStatus);
			if (withRet.getStatus() == 1) {
				// 发送提现成功短信
				paramMap.put("msg", "您在车而告之小程序的车主提现金额已到账，请注意查收！");
			} else if (withRet.getStatus() == 2) {
				// 发送提现失败短信
				paramMap.put("msg", "您在车而告之小程序的车主提现操作失败，请进入小程序查看详情！");
			}
			String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
			log.info("{}发送短信{},結果{}", withRet.getUser().getPhone(), "车而告之小程序的车主提现", retSms);

			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取车主提现详情
	 * 
	 * @param request //token
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param id
	 * @return
	 */
	@RequiresPermissions(value = { "contactIncomeRecord/getContactIncomeRecordDetail" })
	@PostMapping("getContactIncomeRecordDetail")
	@Transactional
	public ResultData getContactIncomeRecordDetail(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, Long id) {
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
		try {
			List<ContactsView> listView = new ArrayList<>();
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			//根据id查询车主名下车辆数量
			Long totalCount = drivingRegistrationService.countRegistrationByContactId(id);
			if (totalCount == null || totalCount <= 0) {
				//如果没有，这返回空
				return serverAck.getEmptyData();
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			//根据id查询车主名下车辆信息，分页展示
			List<DrivingRegistration> list = drivingRegistrationService.listRegistrationByContactId(startPage, pageSize,
					id);
			//遍历，将查询出来的车辆信息封装到View中
			for (DrivingRegistration dr : list) {
				ContactsView view = new ContactsView();
				view.setName(dr.getContact().getName());
				view.setPlateNumber(dr.getPlateNumber());
				// 设备号
				// String devicesNum = "";
				// 获取车辆上周的在线时长
				// 获取上周周一的时间
				Date date = DateUtil.geLastWeekMonday(new Date());
				String getLastWeekMonday = df.format(date);
				System.out.println(getLastWeekMonday);
				ContactOnlineIncomeRecord contactOnlineIncomeRecord = contactOnlineIncomeRecordService
						.queryContactOnlineIncomeRecordByCarId(dr.getId(), getLastWeekMonday);
				if (contactOnlineIncomeRecord != null) {
					view.setTotalOpenTimeLength(new BigDecimal(contactOnlineIncomeRecord.getOnlineTime() / 60 / 60)
							.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				} else {
					view.setTotalOpenTimeLength(0.00);
				}
				// 获取车辆的当月在线时长
				// List<Device> devices = deviceService.queryDeviceListByCarId(dr.getId());
				listView.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", listView);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
	/**
	 * 导出车主提现记录列表
	 * 
	 * @param type        : 类型、状态
	 * @param contactName : 车主姓名
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author yucheng
	 * @date 2018年01月07日
	 */
	@RequestMapping("exportContactIncomeRecord")
	public void exportShopWithdrawCash(byte type, String contactName, String startTime, String endTime,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(contactName)) {
			contactName = null;
		}
		if (StringUtils.isEmpty(startTime)) {
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}
		List<WithdrawCash> list = adverwithdrawCashService.listWithDrawByStatus(type, 0, 999999999, contactName, null, startTime,
				endTime);
		// excel标题
		String[] title = new String[9];
		if (type == 0) {
			title[0] = "申请时间";
			title[1] = "车主姓名";
			title[2] = "提现金额";
			title[3] = "保荐方名称";
			title[4] = "车主收益比例";
			title[5] = "提现账号";
			title[6] = "提现状态";
		} else if (type == 1) {
			title[0] = "申请时间";
			title[1] = "车主姓名";
			title[2] = "提现金额";
			title[3] = "保荐方名称";
			title[4] = "车主收益比例";
			title[5] = "提现账号";
			title[6] = "操作时间";
			title[7] = "提现状态";
		} else if (type == 2) {
			title[0] = "申请时间";
			title[1] = "车主姓名";
			title[2] = "提现金额";
			title[3] = "保荐方名称";
			title[4] = "车主收益比例";
			title[5] = "提现账号";
			title[6] = "操作时间";
			title[7] = "备注";
			title[8] = "提现状态";
		}

		// excel文件名,获取时间
		String fileName = "车主提现记录列表" + System.currentTimeMillis() + ".xls";

		// sheet名
		String sheetName = "车主提现记录列表";

		String[][] content = new String[list.size()][];

		if (list != null && list.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < list.size(); i++) {
				content[i] = new String[title.length];
				WithdrawCash cash = list.get(i);
				if (cash != null) {
					content[i][0] = sdf.format(cash.getCreateTime());
					content[i][2] = cash.getMoney() + "";
					Users user = cash.getUser();
					if (user != null) {
						content[i][1] = user.getContact() != null ? user.getContact().getName() : "";
						content[i][4] = user.getCarCaptain() != null ? user.getCarCaptain().getNowPropertion().toString() + "%" : "";
						content[i][5] = user.getPayAccount() != null ? user.getPayAccount().getPayAccount() : "";
						 Contacts contact = cash.getUser().getContact();
						 if(contact != null) {
							List<DrivingRegistration> carList =  drivingRegistrationService.getDrivingRegistrationByContactId(contact.getId());
							 String sponsorNameTemp = "";
								if (carList != null && carList.size() > 0) {
									for (DrivingRegistration dr : carList) {
										sponsorNameTemp += dr.getSponsor().getCompany() + ",";
									}
								}
								if (!StringUtils.isEmpty(sponsorNameTemp)) {
									sponsorNameTemp = sponsorNameTemp.substring(0, sponsorNameTemp.length() - 1);
								}
								content[i][3] = sponsorNameTemp;
						 }else {
							 content[i][3] = "";
						 }
					} 
					
					if (cash.getStatus() == 0) {
						content[i][6] = "提现中";
					} else if (cash.getStatus() == 1) {
						content[i][7] = "成功";
					} else if (cash.getStatus() == 2) {
						content[i][8] = "失败";
					}
					if (type == 1) {
						if (cash.getUpdateTime() != null) {
							content[i][6] = sdf.format(cash.getUpdateTime());
						} else {
							content[i][6] = "";
						}
					}
					if (type == 2) {
						if (cash.getUpdateTime() != null) {
							content[i][6] = sdf.format(cash.getUpdateTime());
						} else {
							content[i][6] = "";
						}
						content[i][7] = cash.getRemark();
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
}
