package com.cegz.sys.service.adver.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.DeviceUntieRecordRepository;
import com.cegz.sys.model.adver.DeviceUntieRecord;
import com.cegz.sys.service.adver.DeviceUntieRecordService;

/**
 * 
 * 设备解绑记录服务
 * 
 * @author tenglong
 * @date 2018年11月21日
 */
@Service("deviceUntieRecordService")
@Transactional
public class DeviceUntieRecordServiceImpl implements DeviceUntieRecordService {

	@Autowired
	private DeviceUntieRecordRepository deviceUntieRecordRepository;

	@Override
	public int save(DeviceUntieRecord deviceUntieRecord) {
		deviceUntieRecordRepository.save(deviceUntieRecord);
		return 1;
	}

}
