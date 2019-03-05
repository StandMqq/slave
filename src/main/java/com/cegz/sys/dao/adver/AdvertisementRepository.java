package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Advertisement;

/**
 * 广告持久化接口
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Transactional
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

	/**
	 * 查询单条广告数据
	 * 
	 * @param id
	 * @return
	 */
	@Query(value = "select * from advertisement where 1=1 and id = ?1", nativeQuery = true)
	Advertisement selectById(Long id);

	/**
	 * 查询广告分页
	 * 
	 * @param size
	 * @return
	 */
	@Query(value = "select am.* from advertisement am " + "left join advertiser a on a.id = am.advertiser_id "
			+ "left join advertisement_type at on at.id = am.advertisement_type_id "
			+ "where 1=1 and am.is_deleted = 0 and am.status = ?3 " + "and if(?4 != '', am.title like %?4%, 1 = 1) "
			+ "and if(?5 != '', a.name like %?5%, 1 = 1) " + "and if(?6 != '', a.phone like %?6%, 1 = 1) "
			+ "and if(?7 != '', at.type_no = ?7, 1 = 1) "
			+ "order by am.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<Advertisement> queryAdvertisementByLimit(Integer curPage, Integer pageSize, Integer status, String name,
			String advertiserName, String advertiserPhone, String typeNo);

	/**
	 * 查询广告数据总条数
	 */
	@Query(value = "select count(1) from advertisement am " + "left join advertiser a on a.id = am.advertiser_id "
			+ "left join advertisement_type at on at.id = am.advertisement_type_id "
			+ "where 1=1 and am.is_deleted = 0 and am.status = ?1 " + "and if(?2 != '', am.title like %?2%, 1 = 1) "
			+ "and if(?3 != '', a.name like %?3%, 1 = 1) " + "and if(?4 != '', a.phone like %?4%, 1 = 1) "
			+ "and if(?5 != '', at.type_no = ?5, 1 = 1) ", nativeQuery = true)
	Long queryAdvertisementCount(Integer status, String name, String advertiserName, String advertiserPhone,
			String typeNo);

	/**
	 * 通过广告方id查询广告列表
	 * 
	 * @param advertiserId广告方id
	 * @return
	 */
	@Query(value = "select * from advertisement am where 1=1 and am.advertiser_id = ?1", nativeQuery = true)
	List<Advertisement> findAllByAdvertiserId(Long advertiserId);

	/**
	 * 通过广告方id查询广告列表分页
	 * 
	 * @param advertiserId广告方id
	 * @return
	 */
	@Query(value = "select * from advertisement am where 1=1 and am.advertiser_id = ?1 and am.status = ?2 order by am.create_time desc limit ?3, ?4", nativeQuery = true)
	List<Advertisement> findAllByAdvertiserIdLimit(Long advertiserId, Integer status, Integer curPage,
			Integer pageSize);

	/**
	 * 通过广告方id查询广告列表数量
	 * 
	 * @param advertiserId : 广告方id
	 * @return
	 */
	@Query(value = "select Count(1) from advertisement am where 1=1 and am.advertiser_id = ?1 and am.status = ?2", nativeQuery = true)
	Long queryAdvertisementCountByAdvertiserId(Long advertiserId, Integer status);

	/**
	 * 通过广告id查询广告信息
	 * 
	 * @param id 广告id
	 * @return
	 */
	@Query(value = "select * from advertisement am where 1=1 and am.id = ?1", nativeQuery = true)
	Advertisement getAdvertisementById(Long id);

	/**
	 * 审核广告数据
	 * 
	 * @param id
	 * @param status
	 * @param reason
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query(value = "update advertisement set status = ?2, reason = ?3, update_time = ?4 where id = ?1", nativeQuery = true)
	int advertisementStatusExamine(Long id, Integer status, String reason, Date updateTime);

	/**
	 * 通过车辆id查询搭载广告
	 * 
	 * @param startPage
	 * @param pageSize
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
//	@Query(value = "select count(t.id) from advertisement t where t.id in ( "
//			+ " select distinct car.adver_id from device d "
//			+ " left join click_adver_record car on d.id = car.device_id "
//			+ " left join publish_advertisement_record par on d.id = par.device_id "
//			+ " where d.driving_registration_id = ?1  and par.status = 1 "
//			+ " and if(?2 != '', par.start_time >= ?2, 1 = 1) and if(?3 != '', par.end_time <= ?3, 1 = 1)"
//			+ ")", nativeQuery = true)
	@Query(value = "select count(1) from (" + "select a.* from advertisement a "
			+ "left join advertisement_type at on at.id = a.advertisement_type_id "
			+ "left join publish_advertisement_record par on a.id = par.advertisement_id "
			+ "left join device d on d.id = par.device_id "
			+ "left join driving_registration dr on d.driving_registration_id = dr.id "
			+ "where 1 = 1 and a.is_deleted = 0 and dr.id = ?1 "
			+ "and if(?2 != '', par.create_time >= ?2, 1 = 1) and if(?3 != '', par.end_time <= ?3, 1 = 1) "
			+ "and if(?4 != '', a.status = ?4, 1 = 1) and if(?5 != '', at.type_no = ?5, 1 = 1) "
			+ "and if(?6 != '', a.title like %?6%, 1 = 1) "
			+ "group by a.id ) t", nativeQuery = true)
	Long queryAdvertisementCountByCarId(Long carId, String startTime, String endTime, String status, String typeNo,
			String name);

	/**
	 * 通过车辆id查询搭载广告
	 * 
	 * @param startPage
	 * @param pageSize
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
//	@Query(value = "select * from advertisement t where t.id in ( select distinct car.adver_id from device d "
//			+ " left join click_adver_record car on d.id = car.device_id "
//			+ " left join publish_advertisement_record par on d.id = par.device_id "
//			+ " where d.driving_registration_id = ?3  and par.status = 1 "
//			+ " and if(?4 != '', par.start_time >= ?4, 1 = 1) and if(?5 != '', par.end_time <= ?5, 1 = 1)"
//			+ ") limit ?1, ?2", nativeQuery = true)
	@Query(value = "select a.* from advertisement a "
			+ "left join advertisement_type at on at.id = a.advertisement_type_id "
			+ "left join publish_advertisement_record par on a.id = par.advertisement_id "
			+ "left join device d on d.id = par.device_id "
			+ "left join driving_registration dr on d.driving_registration_id = dr.id "
			+ "where 1 = 1 and a.is_deleted = 0 and dr.id = ?3 "
			+ "and if(?4 != '', par.create_time >= ?4, 1 = 1) and if(?5 != '', par.end_time <= ?5, 1 = 1) "
			+ "and if(?6 != '', a.status = ?6, 1 = 1) and if(?7 != '', at.type_no = ?7, 1 = 1) "
			+ "and if(?8 != '', a.title like %?8%, 1 = 1) "
			+ "group by a.id limit ?1, ?2", nativeQuery = true)
	List<Advertisement> queryAdvertisementListByCarId(Integer startPage, Integer pageSize, Long carId, String startTime,
			String endTime, String status, String typeNo, String name);

//	/**
//	 * 通过车辆id查询搭载广告
//	 * 
//	 * @param id
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 */
//	@Query(value = "select count(*) from (select a.id from advertisement a "
//			+ " left join order_advertiser oa on oa.advertisement_id = a.id "
//			+ " left join contact_income_record cir on cir.order_id = oa.id "
//			+ " left join driving_registration dr on cir.driving_registration_id = dr.id where dr.id = ?1 "
//			+ " and if(?2 != '', oa.create_time >= ?2, 1 = 1) and if(?3 != '', oa.end_time <= ?3, 1 = 1) "
//			+ " GROUP BY a.id)p ", nativeQuery = true)
//	Long getAdvertisementCountByCarId(Long id, String startTime, String endTime);
//
//	/**
//	 * 通过车辆id查询搭载广告
//	 * 
//	 * @param curPage
//	 * @param pageSize
//	 * @param id
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 */
//	@Query(value = "select a.* from advertisement a left join order_advertiser oa on oa.advertisement_id = a.id "
//			+ " left join contact_income_record cir on cir.order_id = oa.id "
//			+ " left join driving_registration dr on cir.driving_registration_id = dr.id where dr.id = ?3 "
//			+ " and if(?4 != '', oa.create_time >= ?4, 1 = 1) and if(?5 != '', oa.end_time <= ?5, 1 = 1) "
//			+ " GROUP BY a.id limit ?1, ?2", nativeQuery = true)
//	List<Advertisement> getAdvertisementListByCarId(Integer curPage, Integer pageSize, Long id, String startTime,
//			String endTime);

}
