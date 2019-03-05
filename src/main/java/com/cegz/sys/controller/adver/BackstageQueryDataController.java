package com.cegz.sys.controller.adver;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cegz.sys.model.adver.Advertisement;
import com.cegz.sys.model.adver.Advertiser;
import com.cegz.sys.model.adver.Agent;
import com.cegz.sys.model.adver.Authority;
import com.cegz.sys.model.adver.ContactIncomeRecord;
import com.cegz.sys.model.adver.Contacts;
import com.cegz.sys.model.adver.Device;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.model.adver.Order;
import com.cegz.sys.model.adver.Sponsor;
import com.cegz.sys.model.adver.Travel;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.adver.AdvertisementView;
import com.cegz.sys.model.view.adver.AdvertiserView;
import com.cegz.sys.model.view.adver.AgentView;
import com.cegz.sys.model.view.adver.AuthorityView;
import com.cegz.sys.model.view.adver.CarContactView;
import com.cegz.sys.model.view.adver.DrivingRegistrationView;
import com.cegz.sys.model.view.adver.SponsorView;
import com.cegz.sys.redis.RedisUtil;
import com.cegz.sys.service.adver.AdvertisementService;
import com.cegz.sys.service.adver.AdvertiserService;
import com.cegz.sys.service.adver.AgentService;
import com.cegz.sys.service.adver.AuthorityService;
import com.cegz.sys.service.adver.ContactIncomeRecordService;
import com.cegz.sys.service.adver.ContactsService;
import com.cegz.sys.service.adver.DeviceService;
import com.cegz.sys.service.adver.DrivingRegistrationService;
import com.cegz.sys.service.adver.OrderService;
import com.cegz.sys.service.adver.SponsorService;
import com.cegz.sys.service.adver.TravelService;
import com.cegz.sys.util.ExcelUtil;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 后台查询数据控制类
 * 
 * @author tenglong
 * @date 2018年8月17日
 */
@Slf4j
@RestController
@RequestMapping("/backstageQueryData")
public class BackstageQueryDataController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private DrivingRegistrationService drivingRegistrationService;

	@Autowired
	private SponsorService sponsorService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private AdvertiserService advertiserService;

	@Autowired
	private AdvertisementService advertisementService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private ContactsService contactsService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private TravelService travelService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private ContactIncomeRecordService contactIncomeRecordService;

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
	 * 获取所有已审核保荐方数据
	 * 

	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageQueryData/getSponsorList" })
	@PostMapping("getSponsorList")
	public ResultData getSponsorList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String name, String phone) {
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
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		try {
			Long totalCount = sponsorService.querySponsorExamineTotalCount(1, name, phone);
			curPage = (curPage - 1) * pageSize;// 计算起始页
			List<Sponsor> list = sponsorService.listSponsorExamine(curPage, pageSize, 1, name, phone);
			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<SponsorView> resultList = new ArrayList<>();
			int length = list.size();
			for (int i = 0; i < length; i++) {
				Sponsor sponsor = list.get(i);
				SponsorView view = new SponsorView();
				view.setAddress(sponsor.getAddress());
				view.setAddressDetail(sponsor.getAddressDetail());
				view.setBusinessNumber(sponsor.getBusinessLicense());
				view.setCompany(sponsor.getCompany());
				view.setCompanyLittle(sponsor.getCompanyLittle());
				view.setEmail(sponsor.getEmail());
				view.setId(sponsor.getId());
				view.setLicenseImgUrl(sponsor.getPictureUrl());
				view.setName(sponsor.getName());
				view.setPhone(sponsor.getPhone());
				view.setProvince(sponsor.getProvince());
				view.setStatus(sponsor.getStatus());
				view.setReason(sponsor.getReason());
				view.setType(sponsor.getType());
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
	 * 保荐方名下详情
	 * 
	 * @param request
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @param phone
	 * @return yucheng
	 * @data 2018年11月26号
	 */
	@RequiresPermissions(value = { "backstageQueryData/sponsorDetail" })
	@PostMapping("sponsorDetail")
	public ResultData getSponsorDetailList(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, Long id, String name, String phone) {
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
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		try {
			Long totalCount = drivingRegistrationService.getCarCountBySponsorId(id, name, phone);
			curPage = (curPage - 1) * pageSize;// 计算起始页
			List<DrivingRegistrationView> resultList = new ArrayList<>();
			List<DrivingRegistration> list = drivingRegistrationService.getCarListBySponsorId(id, name, phone, curPage,
					pageSize);
			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			for (int i = 0; i < list.size(); i++) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				DrivingRegistration dr = list.get(i);
				DrivingRegistrationView view = new DrivingRegistrationView();
				view.setId(dr.getId());
				view.setPlateNumber(dr.getPlateNumber());
				view.setCreateTime(sdf.format(dr.getCreateTime()));
				if (dr.getContact() != null) {
					view.setName(dr.getContact().getName());
					view.setPhone(dr.getContact().getPhone());
				}
				// 广告挂载数量
//				List<Device> deviceList = deviceService.queryDeviceListByDrivingRegistrationId(dr.getId());
//				if (deviceList != null && deviceList.size() > 0) {
//					view.setTotal(deviceList.get(0).getTotal());
//					view.setScripttotal(deviceList.get(0).getScriptTotal());
//				} else {
//					view.setTotal(0);
//					view.setScripttotal(0);
//				}
				Long advertisementCount = advertisementService.queryAdvertisementCountByCarId(dr.getId(), null, null,
						null, null, null);
				if (advertisementCount != null && advertisementCount > 0) {
					view.setCarryAdvertisementNum(String.valueOf(advertisementCount));
				} else {
					view.setCarryAdvertisementNum(null);
				}

				// 通过车辆id获取绑定的设备是否在线
				List<Device> devices = deviceService.queryDeviceListByCarId(dr.getId());
				if (devices != null && devices.size() > 0) {
					int deviceNum = 1;
					String deviceOnLineDetail = "";
					for (Device device : devices) {
						if (device.getStatus() == 1) {
							deviceOnLineDetail += "设备" + deviceNum + "：在线；";
//							view.setDeviceOnLineDetail("设备" + deviceNum + "：在线；");
							deviceNum++;
						} else {
							deviceOnLineDetail += "设备" + deviceNum + "：离线；";
//							view.setDeviceOnLineDetail("设备" + deviceNum + "：离线；");
							deviceNum++;
						}
						view.setDeviceOnLineDetail(deviceOnLineDetail);
					}
				} else {
					view.setDeviceOnLineDetail(null);
				}
				resultList.add(view);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("result", resultList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过车辆id获取挂载的广告列表
	 * 
	 * @param request
	 * @param version
	 * @param curPage
	 * @param pageSize
	 * @param carId     车辆id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequiresPermissions(value = { "backstageQueryData/queryAdvertisementListByCarId" })
	@PostMapping("queryAdvertisementListByCarId")
	public ResultData queryAdvertisementListByCarId(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, Long carId, String startTime, String endTime, String status, String typeNo, String name) {
		String tokens = request.getHeader("token");
		if (StringUtil.isEmpty(tokens)) {
			return serverAck.getLoginTimeOutError();
		}
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
		if (carId == null) {
			return serverAck.getParamError().setMessage("车辆id不能为空");
		}
		if (StringUtils.isEmpty(startTime)) {
			startTime = null;
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = null;
		}
		if (StringUtils.isEmpty(status)) {
			status = null;
		}
		if (StringUtils.isEmpty(typeNo)) {
			typeNo = null;
		}
		if (StringUtils.isEmpty(name)) {
			name = null;
		}

		try {
			Long totalCount = advertisementService.queryAdvertisementCountByCarId(carId, startTime, endTime, status,
					typeNo, name);
			Integer startPage = (curPage - 1) * pageSize;// 计算起始页
			// 广告数据列表
			List<Advertisement> list = advertisementService.queryAdvertisementListByCarId(startPage, pageSize, carId,
					startTime, endTime, status, typeNo, name);
			if (list == null || list.size() <= 0) {
				return serverAck.getEmptyData();
			}
			List<AdvertisementView> result = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Advertisement advertisement : list) {
				AdvertisementView view = new AdvertisementView();
				view.setTitle(advertisement.getTitle());
				view.setContent(advertisement.getContent());
				view.setAdvertisementType(advertisement.getAdvertisementType().getTypeNo());
				view.setAdvertisementTypeName(advertisement.getAdvertisementType().getName());
				// 获取广告发布时间和结束时间
				Order order = orderService.getOrderByAdvertisementId(advertisement.getId());
				if (order != null) {
					if (order.getStartTime() != null) {
						view.setPublishStartTime(sdf.format(order.getStartTime()));
					}
					if (order.getEndTime() != null) {
						view.setPublishEndTime(sdf.format(order.getEndTime()));
					}
					view.setPublishDay(order.getDays());
				} else {
					view.setPublishStartTime(null);
					view.setPublishEndTime(null);
					view.setPublishDay(0);
				}
				// 广告点击数
				if (!"002".equals(advertisement.getAdvertisementType().getTypeNo())) {
					String adverIdStr = String.valueOf(advertisement.getId());
					log.info("id: {}", adverIdStr);
					String clickNumStr = String.valueOf(redisUtil.get(adverIdStr));
					log.info("点击量：{}", clickNumStr);
					if (StringUtil.isEmpty(clickNumStr)) {
						view.setClickNum(0);
					} else {
						view.setClickNum(Integer.parseInt(clickNumStr));
					}
				}

//				String plateNumber = "";
//				// 广告挂载车辆牌照（通过广告发布表查询出设备，找到关联的车辆）
//				Long carCount = advertisementService.queryCarCountByAdvertisementId(advertisement.getId(), plateNumber);
//				// 拼接牌照号
//				if (carCount != null && carCount > 0) {
//					plateNumber = String.valueOf(carCount);
//				}
//				view.setPlateNumber(plateNumber);
				view.setStatus(advertisement.getStatus());
				view.setContent(advertisement.getContent());
				// 获取广告发布时间和结束时间
				if (order != null) {
					// 获取广告收益
					List<ContactIncomeRecord> cirList = contactIncomeRecordService
							.findListByOrderIdAndCarId(order.getId(), carId);
					Double money = 0.0;
					for (ContactIncomeRecord cir : cirList) {
						money += cir.getMoney().doubleValue();
					}
					DecimalFormat df = new DecimalFormat("0.00");
					view.setIncome(df.format(BigDecimal.valueOf(money)));
				}
				// 付费区分
				if (advertisement.getPrice() != null) {
					if (advertisement.getPrice().getPrice() == 0) {
						view.setPayDistinguish("免费");
					} else {
						view.setPayDistinguish("收费");
					}
				} else {
					view.setPayDistinguish("免费");
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
	 * 获取所有已审核车辆数据
	 * 
	 * @param
	 * @param
	 * @param version
	 * @param plateNumber 车牌号
	 * @param model       品牌
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "backstageQueryData/getCarList" })
	@PostMapping("getCarList")
	public ResultData getCarList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String plateNumber, String model) {
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
		if (StringUtils.isEmpty(plateNumber)) {
			plateNumber = null;
		}
		if (StringUtils.isEmpty(model)) {
			model = null;
		}
		try {
			int totalCount = drivingRegistrationService.countCarByStatus((byte) 1, plateNumber, model);
			curPage = (curPage - 1) * pageSize;// 计算起始页
			List<DrivingRegistration> list = drivingRegistrationService.listPageCar(curPage, pageSize, (byte) 1,
					plateNumber, model);
			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<DrivingRegistrationView> resultList = new ArrayList<>();
			int length = list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < length; i++) {
				DrivingRegistration dr = list.get(i);
				DrivingRegistrationView view = new DrivingRegistrationView();
				view.setId(dr.getId());
				view.setPlateNumber(dr.getPlateNumber());
				view.setModel(dr.getModel());
				// 搭载广告数量
//				int advertisementCount = deviceService.queryAdvertisementCountByCarId(dr.getId());
 				Long advertisementCount = advertisementService.queryAdvertisementCountByCarId(dr.getId(), null, null,
						null, null, null);
				if (advertisementCount != null && advertisementCount > 0) {
					view.setCarryAdvertisementNum(String.valueOf(advertisementCount));
				} else {
					view.setCarryAdvertisementNum(null);
				}
				if (dr.getCreateTime() != null) {
					view.setCreateTime(sdf.format(dr.getCreateTime()));
				}
				if (dr.getRegisterDate() != null) {
					view.setRegisterDate(sdf.format(dr.getRegisterDate()));
				}
				if (dr.getCarBirthday() != null) {
					view.setCarBirthday(sdf.format(dr.getCarBirthday()));
				}
				// 通过车辆id获取绑定的设备是否在线
				List<Device> devices = deviceService.queryDeviceListByCarId(dr.getId());
				if (devices != null && devices.size() > 0) {
					int deviceNum = 1;
					String deviceOnLineDetail = "";
					for (Device device : devices) {
						if (device.getStatus() == 1) {
							deviceOnLineDetail += "设备" + deviceNum + "：在线；";
//							view.setDeviceOnLineDetail("设备" + deviceNum + "：在线；");
							deviceNum++;
						} else {
							deviceOnLineDetail += "设备" + deviceNum + "：离线；";
//							view.setDeviceOnLineDetail("设备" + deviceNum + "：离线；");
							deviceNum++;
						}
						view.setDeviceOnLineDetail(deviceOnLineDetail);
					}
				} else {
					view.setDeviceOnLineDetail(null);
				}
				if (dr.getContact() != null) {
					view.setName(dr.getContact().getName());
					view.setPhone(dr.getContact().getPhone());
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
	 * 获取所有已审核广告主数据
	 *
	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月17日
	 */

	@RequiresPermissions(value = { "backstageQueryData/getAdvertiserList" })
	@PostMapping("getAdvertiserList")
	public ResultData getAdvertiserList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String name, String phone, String registerPhone) {
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
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		if (StringUtils.isEmpty(registerPhone)) {
			registerPhone = null;
		}
		try {
			Long totalCount = advertiserService.queryAdvertiserExamineTotalCount(1, name, phone, registerPhone);
			curPage = (curPage - 1) * pageSize;// 计算起始页
			List<Advertiser> list = advertiserService.listAdvertiserExamine(curPage, pageSize, 1, name, phone,
					registerPhone);
			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<AdvertiserView> resultList = new ArrayList<>();
			int length = list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < length; i++) {
				Advertiser advertiser = list.get(i);
				AdvertiserView view = new AdvertiserView();
				view.setId(advertiser.getId());
				view.setName(advertiser.getName());
				view.setPhone(advertiser.getPhone());
				if (advertiser.getCreateUserId() != null) {
					view.setRegisterPhone(advertiser.getCreateUserId().getPhone());
				}
				view.setCompany(advertiser.getCompany());
				view.setCity(advertiser.getCity());
				view.setBusinessLicense(advertiser.getBusinessLicense());
				view.setAddress(advertiser.getAddress());
				view.setAddressDetail(advertiser.getAddressDetail());
				view.setEmail(advertiser.getEmail());
				view.setCreateTime(sdf.format(advertiser.getCreateTime()));
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
	 * 导出所有已审核广告主数据
	 * 
	 * @param name
	 * @param phone
	 * @return
	 * @author tenglong
	 * @date 2018年11月13日
	 */
	@RequestMapping("exportAdvertiser")
	public void exportAdvertiser(String name, String phone, String registerPhone, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		List<Advertiser> list = advertiserService.listAdvertiserExamine(0, 999999999, 1, name, phone, registerPhone);
		// excel标题
		String[] title = { "姓名", "手机", "公司", "城市", "执照号码", "地址", "详细地址", "邮箱", "时间" };

		// excel文件名
		String fileName = "广告主信息表" + System.currentTimeMillis() + ".xls";

		// sheet名
		String sheetName = "广告主信息表";

		String[][] content = new String[list.size()][];

		if (list != null && list.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < list.size(); i++) {
				content[i] = new String[title.length];
				Advertiser advertiser = list.get(i);
				content[i][0] = advertiser.getName();
				content[i][1] = advertiser.getPhone();
				content[i][2] = advertiser.getCompany();
				content[i][3] = advertiser.getCity();
				content[i][4] = advertiser.getBusinessLicense();
				content[i][5] = advertiser.getAddress();
				content[i][6] = advertiser.getAddressDetail();
				content[i][7] = advertiser.getEmail();
				content[i][8] = sdf.format(advertiser.getCreateTime());
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
	 * 获取所有已审核广告数据
	 *
	 * @param version
	 * @param name            广告标题
	 * @param advertiserName  广告主名称
	 * @param advertiserPhone 广告主电话
	 * @return
	 * @author tenglong
	 * @date 2018年8月17日
	 */

	@RequiresPermissions(value = { "backstageQueryData/getAdvertisementList" })
	@PostMapping("getAdvertisementList")
	public ResultData getAdvertisementList(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, String name, String advertiserName, String advertiserPhone, Integer status,
			String typeNo) {
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
		if (StringUtils.isEmpty(advertiserName)) {
			advertiserName = null;
		}
		if (StringUtils.isEmpty(advertiserPhone)) {
			advertiserPhone = null;
		}
		if (StringUtils.isEmpty(typeNo)) {
			typeNo = null;
		}
		try {
			curPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = advertisementService.queryAdvertisementCount(status, name, advertiserName,
					advertiserPhone, typeNo);
			List<Advertisement> list = advertisementService.getAdvertisementList(curPage, pageSize, status, name,
					advertiserName, advertiserPhone, typeNo);

			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<AdvertisementView> resultList = new ArrayList<>();
			int length = list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < length; i++) {
				Advertisement advertisement = list.get(i);
				AdvertisementView view = new AdvertisementView();
				view.setId(advertisement.getId());
				view.setTitle(advertisement.getTitle());
				view.setAdvertisementTypeName(advertisement.getAdvertisementType().getName());
				if (advertisement.getAdvertisementType() != null) {
					view.setAdvertisementTypeName(advertisement.getAdvertisementType().getName());
					view.setAdvertisementType(advertisement.getAdvertisementType().getTypeNo());
				}
				if (advertisement.getAdvertiser() != null) {
					view.setAdvertiserName(advertisement.getAdvertiser().getName());
					view.setAdvertiserPhone(advertisement.getAdvertiser().getPhone());
				}
				if (advertisement.getCreateTime() != null) {
					view.setCreateTime(sdf.format(advertisement.getCreateTime()));
				}
				if (advertisement.getCreateUserId() != null) {
					view.setCreatePhone(advertisement.getCreateUserId().getPhone());
				}
				// 广告点击数
				if (!"002".equals(advertisement.getAdvertisementType().getTypeNo())) {
					String adverIdStr = String.valueOf(advertisement.getId());
					log.info("id: {}", adverIdStr);
					String clickNumStr = String.valueOf(redisUtil.get(adverIdStr));
					log.info("点击量：{}", clickNumStr);
					if (StringUtil.isEmpty(clickNumStr)) {
						view.setClickNum(0);
					} else {
						view.setClickNum(Integer.parseInt(clickNumStr));
					}
				}

				String plateNumber = "";
				// 广告挂载车辆牌照（通过广告发布表查询出设备，找到关联的车辆）
				Long carCount = advertisementService.queryCarCountByAdvertisementId(advertisement.getId(), plateNumber);
				// 拼接牌照号
				if (carCount != null && carCount > 0) {
//					for (DrivingRegistration drivingRegistration : carList) {
//						plateNumber += drivingRegistration.getPlateNumber() + "、";
//					}
					plateNumber = String.valueOf(carCount);
				}
				// 处理拼接后的牌照号最后的顿号；
//				if (!StringUtils.isEmpty(plateNumber)) {
//					plateNumber = plateNumber.substring(0, plateNumber.length() - 1);
//				}
				view.setPlateNumber(plateNumber);
				view.setStatus(advertisement.getStatus());
				view.setContent(advertisement.getContent());
				// 获取广告发布时间和结束时间
				Order order = orderService.getOrderByAdvertisementId(advertisement.getId());
				if (order != null) {
					if (order.getStartTime() != null) {
						view.setPublishStartTime(sdf.format(order.getStartTime()));
					}
					if (order.getEndTime() != null) {
						view.setPublishEndTime(sdf.format(order.getEndTime()));
					}

				}

				// 付费区分
				if (advertisement.getPrice() != null) {
					if (advertisement.getPrice().getPrice() == 0) {
						view.setPayDistinguish("免费");
					} else {
						view.setPayDistinguish("收费");
					}
				}

				resultList.add(view);
			}

			// 查询剩余图片广告位：1代表图片；18代表目前配置最大数
			int total = deviceService.queryRemainingAdvertisementPosition("1", 18);
			// 查询剩余文字广告位：2代表文字；20代表目前配置最大数
			int scriptTotal = deviceService.queryRemainingAdvertisementPosition("2", 20);

			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", totalCount);
			map.put("resultList", resultList);
			map.put("total", total);
			map.put("scriptTotal", scriptTotal);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过广告id获取该广告关联的车辆信息
	 * 

	 * @param version
	 * @return
	 * @author tenglong
	 * @date 2018年8月17日
	 */

	@RequiresPermissions(value = { "backstageQueryData/queryPlateNumberInfo" })
	@PostMapping("queryPlateNumberInfo")
	public ResultData queryPlateNumberInfo(HttpServletRequest request, String version, Integer curPage,
			Integer pageSize, Long advertisementId, String plateNumber) {
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
		if (StringUtils.isEmpty(plateNumber)) {
			plateNumber = null;
		}
		try {
			curPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = advertisementService.queryCarCountByAdvertisementId(advertisementId, plateNumber);
			// 广告挂载车辆牌照（通过广告发布表查询出设备，找到关联的车辆）
			List<DrivingRegistration> list = advertisementService.getCarListByAdvertisementId(curPage, pageSize,
					advertisementId, plateNumber);

			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<DrivingRegistrationView> resultList = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < list.size(); i++) {
				DrivingRegistration dr = list.get(i);
				DrivingRegistrationView view = new DrivingRegistrationView();
				view.setId(dr.getId());
				view.setPlateNumber(dr.getPlateNumber());
				view.setModel(dr.getModel());
				view.setPictureUrl(dr.getPictureUrl());
				view.setCarBirthday(sdf.format(dr.getCarBirthday()));
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
	 * 获取所有已审核代理商数据
	 * 

	 * @param version
	 * @param name            广告标题
	 * @return
	 * @author tenglong
	 * @date 2018年8月17日
	 */

	@RequiresPermissions(value = { "backstageQueryData/getAgentList" })
	@PostMapping("getAgentList")
	public ResultData getAgentList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String name, String phone) {
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
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		try {
			curPage = (curPage - 1) * pageSize;// 计算起始页
			Long totalCount = agentService.queryAgentExamineTotalCount(1, name, phone);
			List<Agent> list = agentService.listAgentExamine(curPage, pageSize, 1, name, phone);

			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<AgentView> resultList = new ArrayList<>();
			int length = list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < length; i++) {
				Agent agent = list.get(i);
				AgentView view = new AgentView();
				view.setId(agent.getId());
				view.setCompany(agent.getCompany());
				view.setCity(agent.getCity());
				view.setName(agent.getName());
				view.setPhone(agent.getPhone());
				view.setAddress(agent.getAddress());
				view.setAddressDetail(agent.getAddressDetail());
				view.setCreateTime(sdf.format(agent.getCreateTime()));
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
	 * 获取所有已审核车主数据
	 *
	 * @param version
	 * @param name            广告标题
	 * @return
	 * @author tenglong
	 * @date 2018年8月17日
	 */

	@RequiresPermissions(value = { "backstageQueryData/getCarOwnerList" })
	@PostMapping("getCarOwnerList")
	public ResultData getCarOwnerList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String name, String phone) {
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
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		if (StringUtils.isEmpty(phone)) {
			phone = null;
		}
		try {
			curPage = (curPage - 1) * pageSize;// 计算起始页
			int totalCount = contactsService.countContractsExamine(1, name, phone);
			List<Contacts> list = contactsService.listContactsExamine(curPage, pageSize, 1, name, phone);

			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<CarContactView> resultList = new ArrayList<>();
			int length = list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			for (int i = 0; i < length; i++) {
				Contacts contact = list.get(i);
				CarContactView view = new CarContactView();
				view.setId(contact.getId());
				view.setName(contact.getName());
				view.setPhone(contact.getPhone());
				view.setTime(sdf.format(contact.getCreateTime()));
				// 设备号
				String devicesNum = "";
				// 查看是否绑定过设备
				List<Device> devices = deviceService.queryDevicesByContactId(contact.getId());
				if (devices != null && devices.size() > 0) {
					view.setBindDevice("已绑定");
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
						view.setOpenTimeLength(new BigDecimal(openTimeLength / 60).setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue());

						// 总开机时长
						List<Travel> totalOpenTimeLengths = travelService.queryOpenTimeLength(imei, null);
						if (totalOpenTimeLengths != null && totalOpenTimeLengths.size() > 0) {
							for (Travel travel : totalOpenTimeLengths) {
								totalOpenTimeLength += travel.getDriveTime();
							}
						}
						view.setTotalOpenTimeLength(new BigDecimal(totalOpenTimeLength / 60 / 60)
								.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

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
						view.setOnLineDay(onLineDay);
					}
					if (!StringUtils.isEmpty(devicesNum)) {
						devicesNum = devicesNum.substring(0, devicesNum.length() - 1);
						view.setDevicesNum(devicesNum);
					} else {
						view.setDevicesNum(devicesNum);
					}
				} else {
					// 查看是否绑定过设备
					view.setBindDevice("未绑定");
					// 开机时长
					view.setOpenTimeLength(0D);
					// 总开机时长
					view.setTotalOpenTimeLength(0D);
					// 在线天数
					view.setOnLineDay(0);
					view.setDevicesNum(devicesNum);
				}
				resultList.add(view);
			}
			//根据开机总时长排序
			Collections.sort(resultList, new Comparator<CarContactView>() {

				@Override
				public int compare(CarContactView arg0, CarContactView arg1) {
					if(arg0.getTotalOpenTimeLength() > arg1.getTotalOpenTimeLength()) {
						return 0;
					}
					if(arg0.getTotalOpenTimeLength() == arg1.getTotalOpenTimeLength()) {
						return 1;
					}
					return -1;
				}
			});
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
	 * 获取车辆广告详细信息
	 *
	 * @param version
	 * @param grade   名单等级
	 * @return
	 * @author yucheng
	 * @date 2018年12月24日
	 */

	@RequiresPermissions(value = { "backstageQueryData/getAuthorityList" })
	@PostMapping("getAuthorityList")
	public ResultData getAuthorityList(HttpServletRequest request, String version, Integer curPage, Integer pageSize,
			String name, Integer grade) {
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
			curPage = (curPage - 1) * pageSize;// 计算起始页

			Long totalCount = 0L;
			List<Authority> list = new ArrayList<>();
			if (grade != null) {
				totalCount = authorityService.getAuthorityCount(grade, name);
				list = authorityService.getAuthorityList(curPage, pageSize, grade, name);
			} else {
				totalCount = authorityService.getAuthorityCount(name);
				list = authorityService.getAuthorityList(curPage, pageSize, name);
			}
			if (list == null || list.size() == 0) {
				return serverAck.getEmptyData();
			}
			List<AuthorityView> resultList = new ArrayList<>();
			int length = list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < length; i++) {
				Authority authority = list.get(i);
				AuthorityView view = new AuthorityView();
				view.setId(authority.getId());
				view.setName(authority.getName());
				view.setAuthNo(authority.getAuthNo());
				view.setGrade(authority.getGrade());
				view.setIsDeleted(authority.getIsDeleted());
				Users createUser = authority.getCreateUserId();
				String phone = createUser.getPhone();
				if (createUser != null && createUser.getId() != null && !StringUtils.isEmpty(phone)) {
					view.setBeiOperatorPhone(phone);
				}
				Users updateUser = authority.getUpdateUserId();
				if (updateUser != null && !StringUtils.isEmpty(updateUser.getPhone())) {
					view.setOperatorPhone(updateUser.getPhone());
				}
				if (authority.getCreateTime() != null) {
					view.setCreateTime(sdf.format(authority.getCreateTime()));
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
}
