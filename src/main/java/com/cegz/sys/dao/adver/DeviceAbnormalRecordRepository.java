/**
 * 
 */
package com.cegz.sys.dao.adver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.DeviceAbnormalRecord;

/**
 * 异常设备持久 化接口
 * @author yucheng
 * @date 2018年12月20号
 */
public interface DeviceAbnormalRecordRepository extends JpaRepository<DeviceAbnormalRecord, Long> {

	/**
	 * 通过设备号获取设备异常记录中该设备最近的一条记录
	 * @param imei 
	 * @param status 
	 * @return
	 */
	@Query(value = "select d.* from device_abnormal_record d where d.imei = ?1 and d.is_deleted = 0 and d.status = ?2 ORDER BY ABS(NOW() - d.update_time) ASC" + 
			" limit 1", nativeQuery = true)
	DeviceAbnormalRecord findAllByImei(String imei, Byte status);

	/**
	 * 更新设备异常当前时间
	 * @param deviceAbnormalRecordId 
	 * @return
	 */
	@Modifying
	@Query(value = "update device_abnormal_record set off_line_time = now(), update_time = now() where id = ?1", nativeQuery = true)
	int updateDeviceAbnormalRecord(Long deviceAbnormalRecordId);

	/**
	 * 更新设备异常当前数据是否有效
	 * @param deviceAbnormalRecordId
	 * @return
	 */
	@Modifying
	@Query(value = "update device_abnormal_record set is_deleted = 1 where id = ?1", nativeQuery = true)
	int updateDeviceAbnormalRecordIsdeleted(Long deviceAbnormalRecordId);

}
