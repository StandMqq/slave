/**
 * 
 */
package com.cegz.sys.service.adver.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cegz.sys.dao.adver.DeviceAbnormalRecordRepository;
import com.cegz.sys.model.adver.DeviceAbnormalRecord;
import com.cegz.sys.service.adver.DeviceAbnormalRecordService;

/**
 * 设备异常记录服务类
 * @author yucheng
 * @date 2018年12月20号
 */
@Service("deviceAbnormalRecordService")
@Transactional("transactionManagerSecondary")
public class DeviceAbnormalRecordServiceImpl implements DeviceAbnormalRecordService {

	@Autowired
	private  DeviceAbnormalRecordRepository deviceAbnormalRecordRepository;
	
	@Override
	public int save(DeviceAbnormalRecord dar) {
		deviceAbnormalRecordRepository.save(dar);
		return 1;
	}

	@Override
	public DeviceAbnormalRecord findDeviceAbnormalRecordByImei(String imei, Byte status) {
		return deviceAbnormalRecordRepository.findAllByImei(imei,status);
	}

	@Override
	public int updateDeviceAbnormalRecord(Long deviceAbnormalRecordId) {
		return deviceAbnormalRecordRepository.updateDeviceAbnormalRecord(deviceAbnormalRecordId);
	}

	@Override
	public int updateDeviceAbnormalRecordIsdeleted(Long deviceAbnormalRecordId) {
		return deviceAbnormalRecordRepository.updateDeviceAbnormalRecordIsdeleted(deviceAbnormalRecordId);
	}

	@Override
	public Optional<DeviceAbnormalRecord> getDeviceAbnormalRecordById(Long id) {
		return deviceAbnormalRecordRepository.findById(id);
	}

	@Override
	public DeviceAbnormalRecord saveDeviceAbnormalRecord(DeviceAbnormalRecord deviceAbnormalRecord) {
		return deviceAbnormalRecordRepository.save(deviceAbnormalRecord);
	}

}
