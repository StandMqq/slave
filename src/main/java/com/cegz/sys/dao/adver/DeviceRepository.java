package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Device;

/**
 * 设备持久化接口
 * 
 * @author tenglong
 * @date 2018年7月24日
 */
@Transactional
public interface DeviceRepository extends JpaRepository<Device, Long> {

	/**
	 * 查询设备分页列表
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param endTime
	 * @param startTime
	 * @param typeNo
	 * @param status
	 * @return
	 */
	@Query(value = "select * from device d " + " where 1=1 and if(?3 != '', d.imei like %?3%, 1 = 1) "
			+ " and if(?4 != '', d.number like %?4%, 1 = 1) "
			+ " and if(?5 != '' and ?5 != '002', d.driving_registration_id , 1 = 1) "
			+ " and if(?5 = '002',d.driving_registration_id is null , 1 = 1) "
			+ " and if(?6 != '', d.bind_time >= ?6, 1 = 1) and if(?7 != '', d.bind_time <= ?7, 1 = 1)"
			+ " order by d.create_time limit ?1, ?2", nativeQuery = true)
	List<Device> queryDeviceListByPage(Integer curPage, Integer pageSize, String imei, String number, String typeNo,
			String startTime, String endTime);

	/**
	 * 查询设备数量
	 * 
	 * @param status
	 * @return
	 */
	@Query(value = "select count(id) from device d where 1=1 "
			+ "and if(?1 != '', d.imei like %?1%, 1 = 1) and if(?2 != '', d.number like %?2%, 1 = 1)"
			+ " and if(?3 != '' and ?3 != '002', d.driving_registration_id , 1 = 1) "
			+ " and if(?3 = '002',d.driving_registration_id is null , 1 = 1) "
			+ " and if(?4 != '', d.bind_time >= ?4, 1 = 1) and if(?5 != '', d.bind_time <= ?5, 1 = 1)", nativeQuery = true)
	int queryDeviceCount(String imei, String number, String typeNo, String startTime, String endTime);

	@Query(value = "select * from device d  where d.total < ?2 limit  ?1", nativeQuery = true)
	List<Device> listDeviceByLimit(Long size, int total);

	@Query(value = "select * from device d where d.imei = ?1", nativeQuery = true)
	Device getDeviceByImei(String imei);

	@Modifying
	@Query(value = "update device set status = ?1, update_time = ?2 where id = ?3", nativeQuery = true)
	int updateByStatus(int status, Date updateTime, Long id);

	/**
	 * 通过行驶证id获取设备
	 * 
	 * @param drivingRegistrationId 行驶证id
	 * @return
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	@Query(value = "select * from device d where 1=1 and is_deleted = 0 and d.driving_registration_id = ?1", nativeQuery = true)
	Device getDeviceByDrivingRegistrationId(Long drivingRegistrationId);

	@Modifying
	@Query(value = "update device set total = total + ?1, update_time = ?2 where id = ?3 and total >= ?4", nativeQuery = true)
	int updateDeviceTotal(int total, Date updateTime, Long id, int status);

	@Query(value = "select count(id) from device where is_deleted = 0 and total < total", nativeQuery = true)
	int countByTotal(int total);

	@Query(value = "select coalesce(sum(d.total),0) from device d "
			+ "left join driving_registration dr on d.driving_registration_id = dr.id "
			+ "where 1=1 and d.is_deleted = 0 and dr.id = ?1", nativeQuery = true)
	int queryAdvertisementCountByCarId(Long id);

	/**
	 * 通过车辆id查询设备数量
	 * 
	 * @param status
	 * @return
	 */
	@Query(value = "select count(id) from device d where d.driving_registration_id = ?1", nativeQuery = true)
	int queryDeviceCountByCarId(Long carId);

	/**
	 * 通过车辆id查询关联设备列表
	 * 
	 * @param carId : 车辆id
	 * @return
	 */
	@Query(value = "select * from device d where 1=1 and d.driving_registration_id = ?1 order by d.create_time", nativeQuery = true)
	List<Device> queryDeviceListByCarId(Long carId);

	/**
	 * 通过车主id查询关联设备列表
	 * 
	 * @param contactId : 车主id
	 * @return
	 */
	@Query(value = "select d.* from device d left join driving_registration dr on d.driving_registration_id = dr.id "
			+ "where dr.contact_id = ?1 order by d.create_time", nativeQuery = true)
	List<Device> queryDevicesByContactId(Long contactId);

	/**
	 * 通过车辆id查询关联设备列表
	 * 
	 * @param id ： 车辆id
	 * @return
	 */
	@Query(value = "select * from device d where 1=1 and d.driving_registration_id = ?1", nativeQuery = true)
	List<Device> queryDeviceListByDrivingRegistrationId(Long id);

	/**
	 * 通过广告id修改设备图片广告数减1
	 * 
	 * @param advertisementId : 广告id
	 * @return
	 */
	@Modifying
	@Query(value = "update device d,publish_advertisement_record par set d.total = d.total - 1 "
			+ "where 1=1 and d.id = par.device_id and d.total > 0 and par.advertisement_id = ?1", nativeQuery = true)
	int editTotal(Long advertisementId);

	/**
	 * 通过广告id修改设备文字广告数减1
	 * 
	 * @param advertisementId : 广告id
	 * @return
	 */
	@Modifying
	@Query(value = "update device d,publish_advertisement_record par set d.script_total = d.script_total - 1 "
			+ "where 1=1 and d.id = par.device_id and d.script_total > 0 and par.advertisement_id = ?1", nativeQuery = true)
	int editScriptTotal(Long advertisementId);

	/**
	 * 查询所有设备剩余图片广告位数量
	 * 
	 * @param allocationNum：配置的最大安装数量
	 * @author tenglong
	 * @date 2018年11月29日
	 */
	@Query(value = "select count(d.id) from device d where 1=1 and d.driving_registration_id is not null and d.total < ?1", nativeQuery = true)
	int queryRemainingTotal(int allocationNum);

	/**
	 * 查询所有设备剩余文字广告位数量
	 * 
	 * @param allocationNum：配置的最大安装数量
	 * @author tenglong
	 * @date 2018年11月29日
	 */
	@Query(value = "select count(d.id) from device d where 1=1 and d.driving_registration_id is not null and d.script_total < ?1", nativeQuery = true)
	int queryRemainingScriptTotal(int allocationNum);
	
	/**
	 * 通过保荐方id查询所有异常设备的总数
	 * @param sponsorId
	 * @return
	 */
	@Query(value = "select count(*) from ( select d.* from device d left join driving_registration dr on d.driving_registration_id = dr.id "
			+ " left join device_abnormal_record dar on dar.imei = d.imei"
			+ " where 1=1 and d.driving_registration_id is not null and dar.imei and dar.is_deleted = 0 and dr.sponsor_id = ?1 GROUP BY d.id )p", nativeQuery = true)
	Long countDevicesBySponsorId(Long 查询所有异常设备的总数);

	/**
	 * 通过保荐方id查询所有设备 
	 * @param sponsorId
	 * @return
	 */
	@Query(value = "select d.* from device d left join driving_registration dr on d.driving_registration_id = dr.id "
			+ " where 1=1 and d.driving_registration_id is not null and dr.sponsor_id = ?1 GROUP BY d.id ", nativeQuery = true)
	List<Device> queryDevicesBySponsorId(Long sponsorId);
	
	/**
	 * 通过保荐方id查询所有异常设备
	 * @param pageSize 
	 * @param curPage 
	 * @return
	 */
	@Query(value = "select d.* from device d left join driving_registration dr on d.driving_registration_id = dr.id "
			+ " left join device_abnormal_record dar on dar.imei = d.imei"
			+ " where 1=1 and d.driving_registration_id is not null and dar.imei and dar.is_deleted = 0 and dr.sponsor_id = ?3 GROUP BY d.id limit ?1,?2", nativeQuery = true)
	List<Device> queryDevicesBySponsorId(Integer curPage, Integer pageSize, Long sponsorId);

	/**
	 * 查询所有异常设备的总数
	 * @param name 
	 * @param sponsorName 
	 * @param plateNum 
	 * @param phone 
	 * @return
	 */
	@Query(value = "select count(*) from ( select d.* from device d left join driving_registration dr on d.driving_registration_id = dr.id "
			+ " left join contacts c on c.id = dr.contact_id "
			+ " left join sponsor s on dr.sponsor_id = s.id "
			+ " left join device_abnormal_record dar on dar.imei = d.imei"
			+ " where d.driving_registration_id is not null and dar.imei and dar.is_deleted = 0 and dar.status = ?1 "
			+ " and if(?2 != '', c.name like %?2%, 1 = 1) "
			+ " and if(?3 != '', c.phone like %?3%, 1 = 1) "
			+ " and if(?4 != '', dr.plate_number like %?4%, 1 = 1) "
			+ " and if(?5 != '', s.company like %?5%, 1 = 1) "
			+ " GROUP BY d.id )p", nativeQuery = true)
	Long queryDevices(Byte status, String name, String phone, String plateNum, String sponsorName);

	/**
	 * 查询所有异常设备
	 * @param curPage
	 * @param pageSize
	 * @param name 
	 * @param sponsorName 
	 * @param plateNum 
	 * @param phone 
	 * @return
	 */
	@Query(value = "select * from device d left join driving_registration dr on d.driving_registration_id = dr.id "
			+ " left join contacts c on c.id = dr.contact_id "
			+ " left join sponsor s on dr.sponsor_id = s.id "
			+ " left join device_abnormal_record dar on dar.imei = d.imei "
			+ " where d.driving_registration_id is not null and dar.is_deleted = 0 and dar.status = ?3 "
			+ " and if(?4 != '', c.name like %?4%, 1 = 1) "
			+ " and if(?5 != '', c.phone like %?5%, 1 = 1) "
			+ " and if(?6 != '', dr.plate_number like %?6%, 1 = 1) "
			+ " and if(?7 != '', s.company like %?7%, 1 = 1) "
			+ " GROUP BY d.id limit ?1,?2", nativeQuery = true)
	List<Device> getListDevices(Integer curPage, Integer pageSize,Byte status, String name, String phone, String plateNum, String sponsorName);
}
