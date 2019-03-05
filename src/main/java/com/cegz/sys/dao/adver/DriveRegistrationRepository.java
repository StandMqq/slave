package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.DrivingRegistration;

/**
 * 行驶证持久化接口
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Transactional
public interface DriveRegistrationRepository extends JpaRepository<DrivingRegistration, Long> {

	/**
	 * 通过车牌号查找驾驶证
	 * 
	 * @param plateNumber
	 * @author tenglong
	 * @date 2018年8月1日
	 * @return
	 */
	@Query(value = "select * from driving_registration dr where dr.plate_number = ?1", nativeQuery = true)
	DrivingRegistration getDrivingRegistrationByPlateNumber(String plateNumber);

	/**
	 * 查询保荐方下的所有车辆信息
	 * 
	 * @param size
	 * @return
	 */
	@Query(value = "select * from driving_registration dr left join sponsor s on s.id = dr.sponsor_id where 1=1 and sponsor_id = ?1", nativeQuery = true)
	List<DrivingRegistration> listDrivingRegistration(Long sponsorId);

	/**
	 * 
	 * @param status：车辆状态
	 * @param type： 保荐方类型
	 * @return
	 */
	@Query(value = "select * from driving_registration dr left join sponsor s on s.id = dr.sponsor_id where dr.is_deleted = 0 and dr.status = ?3 and if(?4 != '', dr.plate_number like %?4%, 1 = 1) and if(?5 != '', dr.model like %?5%, 1 = 1) order by dr.create_time desc limit ?1, ?2", nativeQuery = true)
	List<DrivingRegistration> listDriveByPage(int startPos, int size, Byte status, String plateNumber, String model);

	@Query(value = "select count(dr.id) from driving_registration dr left join sponsor s on s.id = dr.sponsor_id where dr.is_deleted = 0 and dr.status = ?1 and if(?2 != '', dr.plate_number like %?2%, 1 = 1) and if(?3 != '', dr.model like %?3%, 1 = 1)", nativeQuery = true)
	int contByStatus(Byte status, String plateNumber, String model);

	/**
	 * 获取车辆及相关信息审核通过的车辆数
	 * 
	 * @param      status：车辆状态
	 * @param type
	 * @return
	 */
	@Query(value = "select count(1) from driving_registration dr left join contacts c on dr.contact_id = c.id "
			+ "left join sponsor s on s.id = dr.sponsor_id "
			+ "where 1=1 and dr.status = ?1 and s.type = ?2", nativeQuery = true)
	int getCarRelationCount(Byte status, Integer type);

	/**
	 * 通过广告id获取该广告挂载的车辆总数
	 * 
	 * @return
	 */
	@Query(value = "select count(dl.id) from publish_advertisement_record par "
			+ "left join device d on d.id = par.device_id "
			+ "left join driving_registration dl on dl.id = d.driving_registration_id "
			+ "where 1=1 and dl.id is not null "
			+ "and par.advertisement_id = ?1 and if(?2 != '', dl.plate_number like %?2%, 1 = 1)", nativeQuery = true)
	Long queryCarCountByAdvertisementId(Long advertisementId, String plateNumber);

	/**
	 * 通过广告id获取该广告挂载的车辆列表
	 * 
	 * @return
	 */
	@Query(value = "select dl.* from publish_advertisement_record par left join device d on d.id = par.device_id "
			+ "left join driving_registration dl on dl.id = d.driving_registration_id "
			+ "where 1=1 and dl.id is not null "
			+ "and par.advertisement_id = ?3 and if(?4 != '', dl.plate_number like %?4%, 1 = 1) limit ?1, ?2", nativeQuery = true)
	List<DrivingRegistration> getCarListByAdvertisementId(Integer curPage, Integer pageSize, Long advertisementId,
			String plateNumber);

	/**
	 * 审核车辆（行驶证）数据
	 * 
	 * @param id
	 * @param status
	 * @param reason
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query(value = "update driving_registration set status = ?2, reason = ?3, update_time = ?4, plate_number = ?5, model = ?6, car_birthday = ?7 "
			+ "where id = ?1", nativeQuery = true)
	int carsStatusExamine(Long id, Integer status, String reason, Date updateTime, String carNumber, String model,
			Date birthDate);

	/**
	 * 保荐方名下（联系人，车辆）数据数量
	 * 
	 * @param id
	 * @param name
	 * @param phone
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@Query(value = "select count(d.id) from driving_registration d left join contacts c on d.contact_id = c.id "
			+ "where d.sponsor_id = ?1 and if(?2!='',c.name like %?2%,1=1) and if(?3!='',c.phone like %?3%,1=1)", nativeQuery = true)
	Long getCarCountBySponsorId(Long id, String name, String phone);

	/**
	 * 保荐方名下（联系人，车辆）数据
	 * 
	 * @param id
	 * @param name
	 * @param phone
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@Query(value = "select d.* from driving_registration d left join contacts c on d.contact_id = c.id "
			+ "where d.sponsor_id = ?1 and if(?2!='',c.name like %?2%,1=1) and if(?3!='',c.phone like %?3%,1=1) limit ?4,?5", nativeQuery = true)
	List<DrivingRegistration> getCarListBySponsorId(Long id, String name, String phone, Integer curPage,
			Integer pageSize);

	/**
	 * 通过设备号获取车辆信息
	 * @param imei
	 * @return
	 */
	@Query(value = "select dr.* from driving_registration dr " 
			+ " left join device d on d.driving_registration_id = dr.id " 
			+ " left join cegz_cli.user_red_packet urp on urp.imei = d.imei where urp.imei = ?1 " , nativeQuery = true)
	DrivingRegistration queryPlateNumberByDeviceImei(String imei);

	/**
	 * 通过车主id获取名下车辆总数
	 * @param id
	 * @return
	 */
	@Query(value = "select count(d.id) from driving_registration d left join contacts c on d.contact_id = c.id "
			+ "where d.contact_id = ?1 ", nativeQuery = true)
	Long countRegistrationByContactId(Long id);

	/**
	 * 通过车主id获取名下车辆
	 * @param startPage
	 * @param pageSize
	 * @param id
	 * @return
	 */
	@Query(value = "select d.* from driving_registration d left join contacts c on d.contact_id = c.id "
			+ "where d.contact_id = ?3 limit ?1,?2", nativeQuery = true)
	List<DrivingRegistration> listRegistrationByContactId(Integer startPage, Integer pageSize, Long id);

	/**
	 *  通过车主id获取名下车辆
	 * @param id
	 * @return
	 */
	@Query(value = "select d.* from driving_registration d left join contacts c on d.contact_id = c.id "
			+ "where d.contact_id = ?1 ", nativeQuery = true)
	List<DrivingRegistration> getDrivingRegistrationByContactId(Long id);

	/**
	 * 根据contacts_id查询车牌号
	 * @param contactsId
	 * @return
	 */
	@Query(value = "select dr.plate_number from driving_registration dr where dr.contact_id = ?1 ", nativeQuery = true)
    List<String> queryPlateNumberByContactsId(Long contactsId);

	/**
	 * 根据车牌号查询contacts_id
	 * @param plateNumber
	 * @return
	 */
	@Query(value = "select dr.*  from driving_registration dr where dr.plate_number like %?1% ", nativeQuery = true)
	DrivingRegistration queryDrivingRegistrationByPlateNumber(String plateNumber);

	/**
	 * 根据车牌获取contacts_id
	 * @param plateNumber
	 * @return
	 */
	@Query(value = "select * from driving_registration dr where dr.plate_number = ?1 ", nativeQuery = true)
	DrivingRegistration findContactsIdByPlateNumber(String plateNumber);


	/**
	 * 根据车牌号模糊查询
	 * @param plateNumber
	 * @return
	 */
	@Query(value = "select dr.* from driving_registration dr where dr.plate_number like %?3% order by dr.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<DrivingRegistration> queryDrivingRegistrationListByPlateNumber(Integer startPage, Integer pageSize, String plateNumber);

	/**
	 * 根据车牌模糊查记录数
	 * @param plateNumber
	 * @return
	 */
	@Query(value = "SELECT count(dr.id) FROM `driving_registration` dr where dr.plate_number like %?1% ", nativeQuery = true)
	Long queryCountByPlateNumber(String plateNumber);
}
