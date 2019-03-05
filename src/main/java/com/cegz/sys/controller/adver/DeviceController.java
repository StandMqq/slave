package com.cegz.sys.controller.adver;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.adver.Device;
import com.cegz.sys.model.adver.DeviceUntieRecord;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.model.adver.Sponsor;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.view.adver.DeviceView;
import com.cegz.sys.service.adver.DeviceService;
import com.cegz.sys.service.adver.DeviceUntieRecordService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 设备后台控制类
 * 
 * @author tenglong
 * @date 2018年8月15日
 */
@Slf4j
@RestController
@RequestMapping("/device")
public class DeviceController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private DeviceUntieRecordService deviceUntieRecordService;

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
	 * 获取设备列表数据
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param version
	 * @param token
	 * @param status
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */

	@RequiresPermissions(value = { "device/getDeviceList" })
	@PostMapping("getDeviceList")
	public ResultData getDeviceList(Integer curPage, Integer pageSize, String version, HttpServletRequest request,
			String imei, String number, String typeNo, String startTime, String endTime) {
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
		if (StringUtil.isEmpty(imei)) {
			imei = null;
		}
		if (StringUtil.isEmpty(number)) {
			number = null;
		}

		try {
			int count = deviceService.queryDeviceCount(imei, number, typeNo, startTime, endTime);// 获取设备数
			if (count == 0) {
				return serverAck.getEmptyData();
			}
			int startPage = (curPage - 1) * pageSize;
			List<Device> list = deviceService.queryDeviceListByPage(startPage, pageSize, imei, number, typeNo,
					startTime, endTime);// 获取设备列表数据
			List<DeviceView> resultList = new ArrayList<>();
			if (list != null && list.size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (Device d : list) {
					DeviceView view = new DeviceView();
					view.setId(d.getId());
					view.setImei(d.getImei());
					DrivingRegistration dr = d.getDrivingRegistration();
					if (dr != null) {
						view.setCarNumber(dr.getPlateNumber());
						Sponsor s = dr.getSponsor();
						if (s != null) {
							view.setSponsorName(s.getName());
							view.setSponsorPhone(s.getPhone());
							view.setSponsorEmail(s.getEmail());
							view.setSponsorAddress(s.getAddress());
						}

						// 搭载广告数量
						view.setCarryAdvertisementNum(d.getTotal());
						view.setNumber(d.getNumber());
						view.setTotal(d.getTotal());
						view.setScriptTotal(d.getScriptTotal());
						if (d.getBindTime() != null) {
							view.setBindTime(sdf.format(d.getBindTime()));
						}
						if (d.getDrivingRegistration() != null) {
							view.setDrivingRegistration("已绑定");
						}
						view.setStatus(d.getStatus());
					}
					resultList.add(view);
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
	 * 通过车辆id获取设备列表数据
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param version
	 * @param token
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年11月21日
	 */

	@RequiresPermissions(value = { "device/getDeviceListByCarId" })
	@PostMapping("getDeviceListByCarId")
	public ResultData getDeviceListByCarId(String version, HttpServletRequest request, Long carId) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (carId == null) {
			return serverAck.getParamError().setMessage("车辆id不能为空");
		}
		try {
			int count = deviceService.queryDeviceCountByCarId(carId);// 通过车辆id获取设备数
			if (count == 0) {
				return serverAck.getEmptyData();
			}
			List<Device> list = deviceService.queryDeviceListByCarId(carId);// 通过车辆id获取设备列表数据
			List<DeviceView> resultList = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (list != null && list.size() > 0) {
				for (Device d : list) {
					DeviceView view = new DeviceView();
					view.setId(d.getId());
					view.setImei(d.getImei());
					view.setNumber(d.getNumber());
					view.setTotal(d.getTotal());
					view.setScriptTotal(d.getScriptTotal());
					if (d.getBindTime() != null) {
						view.setBindTime(sdf.format(d.getBindTime()));
					}
					view.setStatus(d.getStatus());
					resultList.add(view);
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
	 * 车辆解绑设备
	 * 
	 * @param id
	 * @author tenglong
	 * @date 2018年8月23日
	 */

	@RequiresPermissions(value = { "device/untie" })
	@PostMapping("untie")
	public ResultData untie(Long id, HttpServletRequest request, String version) {

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
		try {
			// 处理
			Optional<Device> deviceOpt = deviceService.getDeviceById(id);
			Device device = deviceOpt.get();
			DrivingRegistration drivingRegistration = device.getDrivingRegistration();
			Date bindTime = device.getBindTime();
			device.setDrivingRegistration(null);
			device.setBindTime(null);
			int save = deviceService.save(device);
			if (save != 0) {
				// 保存解绑记录
				DeviceUntieRecord dur = new DeviceUntieRecord();
				dur.setDevice(device);
				dur.setDrivingRegistration(drivingRegistration);
				dur.setBindTime(bindTime);
				dur.setUntieTime(new Date());
				dur.setCreateTime(new Date());
				dur.setCreateUserId(user);
				int ret = deviceUntieRecordService.save(dur);
				if (ret != 0) {
					return serverAck.getSuccess();
				} else {
					return serverAck.getFailure();
				}
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 设备更换物联网卡号
	 * 
	 * @param id
	 * @author tenglong
	 * @date 2018年11月23日
	 */

	@RequiresPermissions(value = { "device/updateNumber" })
	@PostMapping("updateNumber")
	public ResultData updateNumber(Long id, String number, HttpServletRequest request, String version) {

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
		if (number == null) {
			return serverAck.getParamError().setMessage("物联网卡号不能为空");
		}
		try {
			// 处理
			Optional<Device> deviceOpt = deviceService.getDeviceById(id);
			Device device = deviceOpt.get();
			device.setNumber(number);
			device.setUpdateTime(new Date());
			device.setUpdateUserId(user);
			int save = deviceService.save(device);
			if (save != 0) {
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
