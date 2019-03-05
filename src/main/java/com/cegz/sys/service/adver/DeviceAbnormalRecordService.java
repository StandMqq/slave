/**
 * 
 */
package com.cegz.sys.service.adver;

import java.util.Optional;

import com.cegz.sys.model.adver.DeviceAbnormalRecord;

/**
 * 设备异常记录服务类
 * @author yucheng
 * @date 2018年12月20号
 */
public interface DeviceAbnormalRecordService {

	/**
	 * 保存设备异常记录
	 * @param dar
	 */
	int save(DeviceAbnormalRecord dar);

	/**
	 * 通过设备号获取设备异常记录中该设备最近的一条记录
	 * @param imei
	 * @param status 
	 * @return 
	 */
	DeviceAbnormalRecord findDeviceAbnormalRecordByImei(String imei, Byte status);

	/**
	 * 更新设备异常当前时间
	 * @param deviceAbnormalRecordId 
	 */
	int updateDeviceAbnormalRecord(Long deviceAbnormalRecordId);

	/**
	 * 更新设备异常当前数据是否有效
	 * @param id
	 */
	int updateDeviceAbnormalRecordIsdeleted(Long deviceAbnormalRecordId);

	/**
	 * 通过ID获取异常车辆信息
	 * @param id
	 * @return
	 */
	Optional<DeviceAbnormalRecord> getDeviceAbnormalRecordById(Long id);

	/**
	 * 保存已经处理的异常设备
	 * @param deviceAbnormalRecord
	 * @return
	 */
	DeviceAbnormalRecord saveDeviceAbnormalRecord(DeviceAbnormalRecord deviceAbnormalRecord);

}
