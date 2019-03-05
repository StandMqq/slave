package com.cegz.sys.service.adver.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.DeviceRepository;
import com.cegz.sys.model.adver.Device;
import com.cegz.sys.service.adver.DeviceService;

/**
 * 
 * 设备服务
 * 
 * @author tenglong
 * @date 2018年8月15日
 */
@Service("deviceService")
@Transactional
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private DeviceRepository deviceRepository;

	@Override
	public Optional<Device> getDeviceById(Long id) {
		return deviceRepository.findById(id);
	}

	@Override
	public int save(Device device) {
		deviceRepository.save(device);
		return 1;
	}

	@Override
	public int queryAdvertisementCountByCarId(Long id) {
		return deviceRepository.queryAdvertisementCountByCarId(id);
	}

	@Override
	public int queryDeviceCount(String imei, String number, String typeNo, String startTime, String endTime) {
		return deviceRepository.queryDeviceCount(imei, number, typeNo, startTime, endTime);
	}

	@Override
	public List<Device> queryDeviceListByPage(Integer curPage, Integer pageSize, String imei, String number,
			String typeNo, String startTime, String endTime) {
		return deviceRepository.queryDeviceListByPage(curPage, pageSize, imei, number, typeNo, startTime, endTime);
	}

	@Override
	public int queryDeviceCountByCarId(Long carId) {
		return deviceRepository.queryDeviceCountByCarId(carId);
	}

	@Override
	public List<Device> queryDeviceListByCarId(Long carId) {
		return deviceRepository.queryDeviceListByCarId(carId);
	}

	@Override
	public List<Device> queryDevicesByContactId(Long contactId) {
		return deviceRepository.queryDevicesByContactId(contactId);
	}

	@Override
	public List<Device> queryDeviceListByDrivingRegistrationId(Long id) {
		return deviceRepository.queryDeviceListByDrivingRegistrationId(id);
	}

	@Override
	public int editTotalOrScriptTotal(Long advertisementId, String advertisementType) {
		if ("001".equals(advertisementType)) {
			deviceRepository.editTotal(advertisementId);
		} else if ("002".equals(advertisementType)) {
			deviceRepository.editScriptTotal(advertisementId);
		}
		return 0;
	}

	@Override
	public int queryRemainingAdvertisementPosition(String queryType, int allocationNum) {
		if ("1".equals(queryType)) {
			return deviceRepository.queryRemainingTotal(allocationNum);
		} else if ("2".equals(queryType)) {
			return deviceRepository.queryRemainingScriptTotal(allocationNum);
		}
		return 0;
	}

	@Override
	public Device getDeviceByImei(String imei) {
		return deviceRepository.getDeviceByImei(imei);
	}
	
	@Override
	public Long countDevicesBySponsorId(Long sponsorId) {
		return deviceRepository.countDevicesBySponsorId(sponsorId);
	}

	@Override
	public List<Device> queryDevicesBySponsorId(Long sponsorId) {
		return deviceRepository.queryDevicesBySponsorId(sponsorId);
	}
	
	@Override
	public List<Device> queryDevicesBySponsorId(Integer curPage, Integer pageSize, Long sponsorId) {
		return deviceRepository.queryDevicesBySponsorId(curPage,pageSize,sponsorId);
	}

	@Override
	public Long countDevices(Byte status ,String contactName, String phone, String plateNum, String sponsorName) {
		return deviceRepository.queryDevices(status, contactName, phone, plateNum, sponsorName);
	}

	@Override
	public List<Device> getListDevices(Integer curPage, Integer pageSize, Byte status, String contactName, String phone, String plateNum, String sponsorName) {
		return deviceRepository.getListDevices(curPage,pageSize, status, contactName, phone, plateNum, sponsorName);
	}

}
