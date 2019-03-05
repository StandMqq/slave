package com.cegz.sys.controller.adver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

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
import com.cegz.sys.model.adver.CarCaptain;
import com.cegz.sys.model.adver.Contacts;
import com.cegz.sys.model.adver.DataDictionary;
import com.cegz.sys.model.adver.Device;
import com.cegz.sys.model.adver.DeviceAbnormalRecord;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.model.adver.Sponsor;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.shop.WithdrawCash;
import com.cegz.sys.model.view.adver.CarView;
import com.cegz.sys.model.view.adver.DeviceView;
import com.cegz.sys.model.view.adver.DrivingRegistrationView;
import com.cegz.sys.service.adver.DataDictionaryService;
import com.cegz.sys.service.adver.DeviceAbnormalRecordService;
import com.cegz.sys.service.adver.DeviceService;
import com.cegz.sys.service.adver.DrivingRegistrationService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import com.cegz.sys.util.URLConnectionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 车辆后台控制类
 * 
 * @author tenglong
 * @date 2018年8月14日
 */
@Slf4j
@RestController
@RequestMapping("/car")
public class CarController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private DrivingRegistrationService drivingRegistrationService;

	@Autowired
	private DataDictionaryService dataDictionaryService;
	
	@Autowired
	private DeviceAbnormalRecordService deviceAbnormalRecordService;

	/**
	 * 版本号
	 */
	@Value("${server.version}")
	private String serverVersion;

	@Value("${message.account}")
	private String account;

	@Value("${message.pwd}")
	private String pwd;

	@Value("${message.status}")
	private Boolean needStatus;

	@Value("${message.url}")
	private String messageUrl;

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
	 * 获取车辆列表
	 * 
	 * @param index
	 * @param size
	 * @param version
	 * @param token
	 * @param status
	 * @return
	 * @author Administrator
	 * @date 2018年8月7日
	 */

	@RequiresPermissions(value = { "car/getCarList" })
	@PostMapping("getCarList")
	public ResultData listCar(Integer index, Integer size, String version, Byte status, String plateNumber,
			String model, HttpServletRequest request) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (index == null) {
			return serverAck.getParamError().setMessage("当前页面不能为空");
		}
		if (size == null) {
			return serverAck.getParamError().setMessage("记录数不能为空");
		}
		if (status == null) {
			return serverAck.getParamError().setMessage("状态不能为空");
		}
		if (StringUtils.isEmpty(plateNumber)) {
			plateNumber = null;
		}
		if (StringUtils.isEmpty(model)) {
			model = null;
		}
		try {
			int count = drivingRegistrationService.countCarByStatus(status, plateNumber, model);
			if (count == 0) {
				return serverAck.getEmptyData();
			}
			int startPos = (index - 1) * size;
			List<DrivingRegistration> list = drivingRegistrationService.listPageCar(startPos, size, status, plateNumber,
					model);
			List<CarView> resultList = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < list.size(); i++) {
				DrivingRegistration dr = list.get(i);
				CarView view = new CarView();
				view.setCarNumber(dr.getPlateNumber());
				view.setTime(sdf.format(dr.getCreateTime()));
				view.setId(dr.getId());
				view.setCarOwner(dr.getContact().getName());
				resultList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("total", count);
			map.put("list", resultList);
			return serverAck.getSuccess().setData(map);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取车辆信息
	 * 
	 * @param id
	 * @param token
	 * @param version
	 * @return
	 * @author Administrator
	 * @date 2018年8月7日
	 */

	@RequiresPermissions(value = { "car/getCarById" })
	@PostMapping("getCarById")
	public ResultData getCarById(Long id, String version, HttpServletRequest request) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (id == null) {
			return serverAck.getParamError().setMessage("id不能为空");
		}
		try {
			Optional<DrivingRegistration> opt = drivingRegistrationService.getDrivingRegistrationById(id);
			if (!opt.isPresent()) {
				return serverAck.getEmptyData();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DrivingRegistration dr = opt.get();
			DrivingRegistrationView view = new DrivingRegistrationView();
			view.setPlateNumber(dr.getPlateNumber());
			view.setId(dr.getId());
			view.setModel(dr.getModel());
			view.setPictureUrl(dr.getPictureUrl());
			view.setCarBirthday(sdf.format(dr.getCarBirthday()));
			view.setCompany(dr.getSponsor().getCompany());
			view.setStatus(dr.getStatus());
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
	 * 车辆信息状态审核
	 * 
	 * @param id
	 * @param token
	 * @param reason
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */

	@RequiresPermissions(value = { "car/carsStatusExamine" })
	@PostMapping("carsStatusExamine")
	public ResultData carsStatusExamine(Long id, Integer status, String reason, String version,
			HttpServletRequest request, String carNumber, String model, String birthDate) {
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
		if (status == null || status == 0) {// 审核状态 0 审核中，1 通过，2 未通过
			return serverAck.getParamError().setMessage("审核状态必填且不为0");
		}
		if (status != null && status != 0 && status == 2 && StringUtil.isEmpty(reason)) {// 审核状态为未通过原因必填
			return serverAck.getParamError().setMessage("审核未通过时原因不能为空");
		}
		if (status != null && 1 == status) {// 审核通过时，校验其他信息
			if (StringUtils.isEmpty(carNumber)) {
				return serverAck.getParamError().setMessage("车牌号不能为空");
			}
			if (StringUtils.isEmpty(model)) {
				return serverAck.getParamError().setMessage("品牌不能为空");
			}
			if (StringUtils.isEmpty(birthDate)) {
				return serverAck.getParamError().setMessage("出厂日期不能为空");
			}
		}
		try {
			// 获取车辆信息
			Optional<DrivingRegistration> drOpt = drivingRegistrationService.getDrivingRegistrationById(id);
			if (!drOpt.isPresent()) {
				return serverAck.getParamError().setMessage("行驶证ID有误");
			}
			DrivingRegistration drivingRegistration = drOpt.get();
			drivingRegistration.setStatus(status.byteValue());
			drivingRegistration.setReason(status.byteValue() == 1 ? null : reason);// 如果状态为1，原因置为null
			drivingRegistration.setUpdateTime(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date carBirthDay = null;
			if (!StringUtil.isEmpty(birthDate)) {
				carBirthDay = sdf.parse(birthDate);
			}
			int ret = drivingRegistrationService.carsStatusExamine(drivingRegistration.getId(),
					drivingRegistration.getStatus(), drivingRegistration.getReason(),
					drivingRegistration.getUpdateTime(), carNumber, model, carBirthDay);
//			int ret = drivingRegistrationService.save(drivingRegistration);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (ret == 0) {
				// 记录失败日志
				log.info("车辆审核失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ drivingRegistration.getStatus() + ";" + "审核内容-->" + drivingRegistration.getReason() + ";"
						+ "审核时间-->" + df.format(new Date()) + ";" + "失败原因-->操作数据库失败;");
				return serverAck.getFailure();
			}
			// 记录成功日志
			log.info("车辆审核成功：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
					+ drivingRegistration.getStatus() + ";" + "审核内容-->" + drivingRegistration.getReason() + ";"
					+ "审核时间-->" + df.format(new Date()));

			// 发送短信内容组装
			Map<String, Object> paramMap = new HashMap<>(16);
			if (drivingRegistration.getContact() != null
					&& !StringUtils.isEmpty(drivingRegistration.getContact().getPhone())) {
				paramMap.put("mobile", drivingRegistration.getContact().getPhone());
			} else if (drivingRegistration.getCreateUserId() != null
					&& !StringUtils.isEmpty(drivingRegistration.getCreateUserId().getPhone())) {
				paramMap.put("mobile", drivingRegistration.getCreateUserId().getPhone());
			} else {
				// 记录失败日志
				log.info("车辆审核通过后发送短信失败：操作数据id-->" + id + ";" + "操作人id-->" + user.getId() + ";" + "审核状态-->"
						+ drivingRegistration.getStatus() + ";" + "审核内容-->" + drivingRegistration.getReason() + ";"
						+ "审核时间-->" + df.format(new Date()) + ";" + "失败原因-->未找到发送短信号码;");
				return serverAck.getFailure();
			}
			paramMap.put("account", account);
			paramMap.put("pswd", pwd);
			paramMap.put("needstatus", needStatus);

			// 审核状态为通过时，发送短信通知被审核人，话术：您的车辆审核已通过，请前往“车而告之”微信小程序查看
			if (1 == status.byteValue()) {
				paramMap.put("msg", "您的车辆信息审核已通过，请前往“车而告之”微信小程序查看");
				String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
				if (drivingRegistration.getContact() != null
						&& !StringUtils.isEmpty(drivingRegistration.getContact().getPhone())) {
					log.info("{}发送短信{},結果{}", drivingRegistration.getContact().getPhone(), "车辆审核通过", retSms);
				} else {
					log.info("{}发送短信{},結果{}", drivingRegistration.getCreateUserId().getPhone(), "车辆审核通过", retSms);
				}
			}
			// 审核状态为失败时，发送短信通知被审核人
			if (2 == status.byteValue()) {
				paramMap.put("msg", "您的车辆信息审核失败，请前往“车而告之”微信小程序查看");
				String retSms = URLConnectionUtil.doGet(messageUrl, paramMap);
				if (drivingRegistration.getContact() != null
						&& !StringUtils.isEmpty(drivingRegistration.getContact().getPhone())) {
					log.info("{}发送短信{},結果{}", drivingRegistration.getContact().getPhone(), "车辆审核失败", retSms);
				} else {
					log.info("{}发送短信{},結果{}", drivingRegistration.getCreateUserId().getPhone(), "车辆审核失败", retSms);
				}
			}

			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取车辆及相关信息列表数据（只查询审核通过的车辆）
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param version
	 * @param token
	 * @param status
	 * @return
	 * @author tenglong
	 * @date 2018年8月14日
	 */

	@RequiresPermissions(value = { "car/getCarRelationList" })
	@PostMapping("getCarRelationList")
	public ResultData getCarRelationList(Integer curPage, Integer pageSize, String version, String plateNumber,
			String model, HttpServletRequest request) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (curPage == null) {
			return serverAck.getParamError().setMessage("当前页面不能为空");
		}
		if (pageSize == null) {
			return serverAck.getParamError().setMessage("记录数不能为空");
		}
		if (StringUtils.isEmpty(plateNumber)) {
			plateNumber = null;
		}
		if (StringUtils.isEmpty(model)) {
			model = null;
		}
		try {
			int count = drivingRegistrationService.countCarByStatus((byte) 1, plateNumber, model);// 获取车辆及相关信息审核通过的车辆数
			if (count == 0) {
				return serverAck.getEmptyData();
			}
			int startPage = (curPage - 1) * pageSize;
			List<DrivingRegistration> list = drivingRegistrationService.listPageCar(startPage, pageSize, (byte) 1,
					plateNumber, model);// 获取车辆及相关信息列表数据
			List<CarView> resultList = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (list != null && list.size() > 0) {
				for (DrivingRegistration dr : list) {
					if (dr != null) {
						CarView view = new CarView();
						view.setId(dr.getId());
						Contacts contact = dr.getContact();
						if (contact != null) {
							view.setCarOwner(contact.getName());
						}
						Sponsor sponsor = dr.getSponsor();
						if (sponsor != null) {
							view.setProvince(sponsor.getProvince());
							view.setSponsorName(sponsor.getName());
							view.setSponsorPhone(sponsor.getPhone());
							view.setSponsorEmail(sponsor.getEmail());
							view.setSponsorAddress(sponsor.getAddress());
						}
						view.setCarNumber(dr.getPlateNumber());
						view.setCarBirthday(sdf.format(dr.getCarBirthday()));
						view.setStatus(dr.getStatus());
						view.setTime(sdf.format(dr.getCreateTime()));
						// 搭载广告数量
						int advertisementCount = deviceService.queryAdvertisementCountByCarId(dr.getId());
						view.setCarryAdvertisementNum(advertisementCount);
						resultList.add(view);
					}
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("total", count);
			map.put("list", resultList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
	/**
	 * 获取异常设备详情
	 * 
	 * @param token
	 * @param type
	 * @param version
	 * @param plateNumber 车牌号
	 * @param model       品牌
	 * @param type        1 网约车租赁房，2 驾校
	 * @return
	 * @author yucheng
	 * @date 2018年12月06日
	 */

	@RequiresPermissions(value = { "car/getAbnormityCarList" })
	@PostMapping("getAbnormityCarList")
	public ResultData getAbnormityCarList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String contactName,Byte status, String plateNum, String phone, String sponsorName) {
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
		if (StringUtils.isEmpty(contactName)) {
			contactName = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		if (StringUtils.isEmpty(sponsorName)) {
			sponsorName = null;
		}
		if (StringUtils.isEmpty(plateNum)) {
			plateNum = null;
		}
		
		try {

			Long totalCount = deviceService.countDevices(status, contactName, phone, plateNum, sponsorName);
			if (totalCount == null || totalCount == 0) {                                
				return serverAck.getEmptyData();
			}
			curPage = (curPage - 1) * pageSize;
			List<Device> list = deviceService.getListDevices(curPage, pageSize ,status ,contactName, phone, plateNum, sponsorName);
			List<DeviceView> resultList = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < list.size(); i++) {
				Device d = list.get(i); 
				DeviceView view = new DeviceView();
				if(d.getDrivingRegistration().getPlateNumber() != null) {
					view.setCarNumber(d.getDrivingRegistration().getPlateNumber());
				}
				CarCaptain carCaptain = d.getDrivingRegistration().getCarCaptain();
				if (carCaptain != null) {
					view.setCarCaptainName(carCaptain.getName());
				}
				//保荐方名称
				String sponsor = d.getDrivingRegistration().getSponsor().getCompany();
				if(sponsor != null) {
					view.setSponsorName(sponsor);
				}
				DeviceAbnormalRecord deviceAbnormalRecord = deviceAbnormalRecordService.findDeviceAbnormalRecordByImei(d.getImei(),status);
				if(deviceAbnormalRecord != null) {
					view.setOnLineTime(sdf.format(deviceAbnormalRecord.getOnLineTime()));
					view.setOffLineTime(sdf.format(deviceAbnormalRecord.getOffLineTime()));
					if (deviceAbnormalRecord.getContactTime() != null) {
						view.setContactTime(sdf.format(deviceAbnormalRecord.getContactTime()));
					}
					view.setRemark(deviceAbnormalRecord.getRemark());
					view.setId(deviceAbnormalRecord.getId());
				}
				view.setImei(d.getImei());	
				if (d.getDrivingRegistration().getContact() != null) {
					view.setName(d.getDrivingRegistration().getContact().getName());
					view.setPhone(d.getDrivingRegistration().getContact().getPhone());
				}
				
				resultList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", resultList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
	/**
	 * 处理异常设备
	 * 
	 * @param token
	 * @param version
	 * @param id
	 * @return yucheng
	 * @date 2018年12月26号
	 */

	@RequiresPermissions(value = { "car/confirmAbnormityDevice"})
	@PostMapping("confirmAbnormityDevice")
	@Transactional
	public ResultData confirmAbnormityDevice(HttpServletRequest request, String version, Long id, Byte status, String remark) {
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
			Optional<DeviceAbnormalRecord> opt = deviceAbnormalRecordService.getDeviceAbnormalRecordById(id);
			// 处理
			if (!opt.isPresent()) {
				return serverAck.getParamError().setMessage("id错误");
			}
			DeviceAbnormalRecord deviceAbnormalRecord = opt.get();
			if (deviceAbnormalRecord.getStatus() == 1) {
				return serverAck.getFailure().setMessage("该设备已通知车主，请联系管理员");
			}
			deviceAbnormalRecord.setUpdateTime(new Date());
			deviceAbnormalRecord.setStatus(status);
			deviceAbnormalRecord.setRemark(remark);
			deviceAbnormalRecord.setContactTime(new Date());
			DeviceAbnormalRecord newdeviceAbnormalRecord = deviceAbnormalRecordService.saveDeviceAbnormalRecord(deviceAbnormalRecord);
			// 处理异常设备状态
			if (newdeviceAbnormalRecord == null) {
				return serverAck.getFailure();
			}
			/*// 修改异常设备状态 0 未处理，1已处理 
			if (newdeviceAbnormalRecord.getStatus() == 0) {
				deviceAbnormalRecordService.updarBynewdeviceAbnormalRecord(newdeviceAbnormalRecord.getId(), (byte) 1);
			} */
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
