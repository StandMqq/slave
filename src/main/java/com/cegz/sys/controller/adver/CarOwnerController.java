package com.cegz.sys.controller.adver;

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
import com.cegz.sys.model.adver.Contacts;
import com.cegz.sys.model.adver.DataDictionary;
import com.cegz.sys.model.adver.Device;
import com.cegz.sys.model.adver.Travel;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.adver.CarContactView;
import com.cegz.sys.model.view.adver.ContactView;
import com.cegz.sys.service.adver.ContactsService;
import com.cegz.sys.service.adver.DataDictionaryService;
import com.cegz.sys.service.adver.DeviceService;
import com.cegz.sys.service.adver.TravelService;
import com.cegz.sys.util.ExcelUtil;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import com.cegz.sys.util.URLConnectionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 车主后台控制类
 * 
 * @author tenglong
 * @date 2018年7月31日
 */
@Slf4j
@RestController
@RequestMapping("/carOwner")
public class CarOwnerController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private ContactsService contactsService;

	@Autowired
	private DataDictionaryService dataDictionaryService;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private TravelService travelService;

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
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		return null;
	}

	/**
	 * 获取车主待审核列表
	 * 
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "carOwner/getExamineList" })
	@PostMapping("getExamineList")
	public ResultData getExamineList(String version, Integer status, Integer curPage, Integer pageSize, String name,
			String phone, HttpServletRequest request) {
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
		if (status == null) {
			return serverAck.getParamError().setMessage("状态不能为空");
		}
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		try {
			int startPos = (curPage - 1) * pageSize;
			int count = contactsService.countContractsExamine(status, name, phone);
			int page = count / pageSize + 1;
			Map<String, Object> map = new HashMap<>();
			// 车主审核数据列表
			List<Contacts> contactsExamines = contactsService.listContactsExamine(startPos, pageSize, status, name,
					phone);
			if (contactsExamines == null || contactsExamines.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<CarContactView> list = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < contactsExamines.size(); i++) {
				CarContactView view = new CarContactView();
				Contacts contact = contactsExamines.get(i);
				view.setId(contact.getId());
				view.setName(contact.getName());
				view.setPhone(contact.getPhone());
				view.setTime(sdf.format(contact.getCreateTime()));
				list.add(view);
			}
			map.put("total", count);
			map.put("page", page);
			map.put("list", list);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过id获取单条车主
	 * 
	 * @param id
	 * @param token
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "carOwner/getExamineById" })
	@PostMapping("getExamineById")
	public ResultData getExamineById(String version, Long id, HttpServletRequest request) {
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
			// 车主信息审核数据
			Optional<Contacts> contactsOpt = contactsService.getContactById(id);
			if (!contactsOpt.isPresent()) {
				return serverAck.getParamError().setMessage("车主ID有误");
			}
			Contacts contacts = contactsOpt.get();
			ContactView view = new ContactView();
			view.setId(contacts.getId());
			view.setName(contacts.getName());
			view.setPhone(contacts.getPhone());
			if (contacts.getDrivingLicenseId() != null) {
				// 驾驶证
				view.setDriveLicenseImageUrl(contacts.getDrivingLicenseId().getPictureUrl());
			}
			if (contacts.getIdcardId() != null) {
				// 身份证
				String[] images = contacts.getIdcardId().getPictureUrl().split(",");
				view.setFirstImageUrl(images[0]);
				view.setSecondImageUrl(images[1]);
			}
			view.setStatus(contacts.getStatus());

			// 查询数据字典中的审核理由
			List<DataDictionary> dataDictionaryList = dataDictionaryService.getDataDictionaryByCode("001");
			Map<String, Object> map = new HashMap<>();
			map.put("dataDictionaryList", dataDictionaryList);
			map.put("view", view);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 车主信息状态审核
	 * 
	 * @param id
	 * @param token
	 * @param reason
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "carOwner/carOwnerStatusExamine" })
	@PostMapping("carOwnerStatusExamine")
	public ResultData carOwnerStatusExamine(Long id, Integer status, String reason, String version,
			HttpServletRequest request, String name, String phone) {
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
		if (id == null) {
			return serverAck.getParamError().setMessage("主键不能为空");
		}
		if (status != null && 1 == status) {// 审核通过时，校验其他信息
			if (StringUtil.isEmpty(name)) {
				return serverAck.getParamError().setMessage("联系人名称不能为空");
			}
			if (StringUtil.isEmpty(phone)) {
				return serverAck.getParamError().setMessage("联系人电话不能为空");
			}
		}
		if (status == null || status == 0) {// 审核状态 0 审核中，1 通过，2 未通过
			return serverAck.getParamError().setMessage("审核状态必填且不为0");
		}
		if (status != null && status != 0 && status == 2 && StringUtil.isEmpty(reason)) {// 审核状态为未通过原因必填
			return serverAck.getParamError().setMessage("审核未通过时原因不能为空");
		}
		try {
			// 获取车主信息
			Optional<Contacts> contactsOpt = contactsService.getContactById(id);
			if (!contactsOpt.isPresent()) {
				return serverAck.getParamError().setMessage("车主ID有误");
			}
			Contacts contacts = contactsOpt.get();

			contacts.setStatus(status.byteValue());
			contacts.setReason(status.byteValue() == 1 ? null : reason);// 如果状态为1，原因置为null
			contacts.setUpdateTime(new Date());
			int ret = contactsService.carOwnerStatusExamine(contacts.getId(), contacts.getStatus(),
					contacts.getReason(), contacts.getUpdateTime(), name, phone);
//			int ret = contactsService.insertData(contacts);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (ret == 0) {
				// 记录失败日志
				log.info("车主审核失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ contacts.getStatus() + ";" + "审核内容-->" + contacts.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->操作数据库失败;");
				return serverAck.getFailure();
			}
			// 记录车主审核成功日志
			log.info("车主审核成功：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->" + contacts.getStatus()
					+ ";" + "审核内容-->" + contacts.getReason() + ";" + "审核时间-->" + df.format(new Date()));

			int idCardRet = contactsService.idCardStatusExamine(contacts.getIdcardId().getId(), contacts.getStatus(),
					contacts.getReason(), contacts.getUpdateTime());
			if (idCardRet == 0) {
				// 记录失败日志
				log.info("身份证审核失败：操作数据id-->" + contacts.getIdcardId().getId() + ";" + "操作人id-->" + user.getId() + ";"
						+ "审核状态-->" + contacts.getStatus() + ";" + "审核内容-->" + contacts.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->操作数据库失败;");
				return serverAck.getFailure();
			}
			// 记录身份证审核成功日志
			log.info("身份证审核成功：操作数据id-->" + contacts.getIdcardId().getId() + ";" + "操作人id-->" + user.getId() + ";"
					+ "审核状态-->" + contacts.getStatus() + ";" + "审核内容-->" + contacts.getReason() + ";" + "审核时间-->"
					+ df.format(new Date()));

			int drivingLicenseRet = contactsService.drivingLicenseStatusExamine(contacts.getDrivingLicenseId().getId(),
					contacts.getStatus(), contacts.getReason(), contacts.getUpdateTime());
			if (drivingLicenseRet == 0) {
				// 记录失败日志
				log.info("驾驶证审核失败：操作数据id-->" + contacts.getIdcardId().getId() + ";" + "操作人id-->" + user.getId() + ";"
						+ "审核状态-->" + contacts.getStatus() + ";" + "审核内容-->" + contacts.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->操作数据库失败;");
				return serverAck.getFailure();
			}
			// 记录身份证审核成功日志
			log.info("驾驶证审核成功：操作数据id-->" + contacts.getIdcardId().getId() + ";" + "操作人id-->" + user.getId() + ";"
					+ "审核状态-->" + contacts.getStatus() + ";" + "审核内容-->" + contacts.getReason() + ";" + "审核时间-->"
					+ df.format(new Date()));

			// 发送短信内容组装
			Map<String, Object> paramMap = new HashMap<>(16);
			if (!StringUtils.isEmpty(contacts.getPhone())) {
				paramMap.put("mobile", contacts.getPhone());
			} else if (contacts.getCreateUserId() != null
					&& !StringUtils.isEmpty(contacts.getCreateUserId().getPhone())) {
				paramMap.put("mobile", contacts.getCreateUserId().getPhone());
			} else {
				// 记录失败日志
				log.info("车主审核通过后发送短信失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ contacts.getStatus() + ";" + "审核内容-->" + contacts.getReason() + ";" + "审核时间-->"
						+ df.format(new Date()) + ";" + "失败原因-->未找到发送短信号码;");
				return serverAck.getFailure();
			}
			paramMap.put("account", account);
			paramMap.put("pswd", pwd);
			paramMap.put("needstatus", needStatus);

			// 审核状态为通过时，发送短信通知被审核人，话术：您的广告主资质审核已通过，请前往“车而告之”微信小程序查看
			if (1 == status.byteValue()) {
				paramMap.put("msg", "您的车主信息审核已通过，请前往“车而告之”微信小程序查看");
				String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
				if (!StringUtils.isEmpty(contacts.getPhone())) {
					log.info("{}发送短信{},結果{}", contacts.getPhone(), "车主信息审核通过", retSms);
				} else {
					log.info("{}发送短信{},結果{}", contacts.getCreateUserId().getPhone(), "车主信息审核通过", retSms);
				}
			}
			// 审核状态为失败时，发送短信通知被审核人
			if (2 == status.byteValue()) {
				paramMap.put("msg", "您的车主信息审核失败，请前往“车而告之”微信小程序查看");
				String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
				if (!StringUtils.isEmpty(contacts.getPhone())) {
					log.info("{}发送短信{},結果{}", contacts.getPhone(), "车主信息审核失败", retSms);
				} else {
					log.info("{}发送短信{},結果{}", contacts.getCreateUserId().getPhone(), "车主信息审核失败", retSms);
				}
			}

			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
	/**
	 * 导出车主列表
	 * 
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author yucheng
	 * @date 2019年1月21日
	 */
	@RequestMapping("exportContacts")
	public void exportWallet(String phoneInput, String nameInput, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(phoneInput)) {
			phoneInput = null;
		}
		if (StringUtils.isEmpty(nameInput)) {
			nameInput = null;
		}
		List<Contacts> list = contactsService.queryContactsList(0, 999, phoneInput, nameInput);
		// excel标题

		String[] title = { "姓名", "手机", "时间", "设备绑定","今日开机时长（分钟）" ,"总开机时长","在线天数"};

		// excel文件名
		String fileName = "车主列表" + System.currentTimeMillis() + ".xls";

		// sheet名
		String sheetName = "车主列表";

		String[][] content = new String[list.size()][];

		if (list != null && list.size() > 0) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < list.size(); i++) {
				content[i] = new String[title.length];
				Contacts contacts = list.get(i);
					content[i][0] = contacts.getName();
					content[i][1] = contacts.getPhone();
					content[i][2] = contacts.getCreateTime().toString();
					
					// 设备号
					String devicesNum = "";
					// 查看是否绑定过设备
					List<Device> devices = deviceService.queryDevicesByContactId(contacts.getId());
					if (devices != null && devices.size() > 0) {
						content[i][3] = "已绑定";
						Double openTimeLength = 0D;
						Double totalOpenTimeLength = 0D;
						int onLineDay = 0;
						List<String> dateTags = new ArrayList<String>();
						for (Device device : devices) {
							String imei = device.getImei();
							// 设备号
							devicesNum += imei + ",";

							// 开机时长 currentDate : yyyy-MM-dd
							String currentDate = df.format(new Date());
							List<Travel> openTimeLengths = travelService.queryOpenTimeLength(imei, currentDate);
							if (openTimeLengths != null && openTimeLengths.size() > 0) {
								for (Travel travel : openTimeLengths) {
									openTimeLength += travel.getDriveTime();
								}
							}
							String openTime = new BigDecimal(openTimeLength / 60).setScale(2, BigDecimal.ROUND_HALF_UP)
							.toString();
							content[i][4] = openTime;

							// 总开机时长
							List<Travel> totalOpenTimeLengths = travelService.queryOpenTimeLength(imei, null);
							if (totalOpenTimeLengths != null && totalOpenTimeLengths.size() > 0) {
								for (Travel travel : totalOpenTimeLengths) {
									totalOpenTimeLength += travel.getDriveTime();
								}
							}
							String totalOpenTime = new BigDecimal(totalOpenTimeLength / 60 / 60)
							.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
							content[i][5] = totalOpenTime;
//							view.setTotalOpenTimeLength(new BigDecimal(totalOpenTimeLength / 60 / 60)
//									.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

							// 在线天数
							List<String> onLineDays = travelService.queryOnLineDay(imei);
							System.out.println("onLineDays:=========" + onLineDays);
							if (onLineDays != null && onLineDays.size() > 0) {
								for (String s : onLineDays) {
									System.out.println("s:=========" + s);
									int indexOf = dateTags.indexOf(s.toString());
									if (dateTags == null || indexOf <= 0) {
										onLineDay++;
										dateTags.add(s.toString());
									}
								}
							}
							content[i][6] = String.valueOf(onLineDay) ;
							//view.setOnLineDay(onLineDay);
						}
					} 
					else {
						// 查看是否绑定过设备
						content[i][3] = "未绑定";
						//view.setBindDevice("未绑定");
						// 开机时长
						content[i][4] = "0";
						//view.setOpenTimeLength(0D);
						// 总开机时长
						content[i][5] = "0";
						//view.setTotalOpenTimeLength(0D);
						// 在线天数
						content[i][6] = "0";
						//view.setOnLineDay(0);
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
