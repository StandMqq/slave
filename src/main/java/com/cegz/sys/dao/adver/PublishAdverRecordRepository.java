package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.PublishAdverRecord;

/**
 * 广告发布记录
 * 
 * @author lijiaxin
 * @date 2018年7月30日
 */
public interface PublishAdverRecordRepository extends JpaRepository<PublishAdverRecord, Long> {
	@Modifying
	@Query(value = "update publish_advertisement_record set status = ?1, update_time = ?2 where id = ?3", nativeQuery = true)
	int updateByStatus(int status, Date updateTime, Long id);

	@Query(value = "select count(id) from publish_advertisement_record where is_deleted = 0 and device_id = ?1", nativeQuery = true)
	int countDataByDevices(Long id);

	List<PublishAdverRecord> findByDeviceIdAndIsDeleted(Long id, byte isDeleted);

	@Modifying
	@Query(value = "update publish_advertisement_record set is_deleted = 1 where end_time < ?1", nativeQuery = true)
	int updateStatusByDate(Date time);

	/**
	 * 通过设备id获取广告发布数据
	 * 
	 * @param deviceId 设备id
	 * @return
	 */
	@Query(value = "select * from publish_advertisement_record where is_deleted = 0 and device_id = ?1", nativeQuery = true)
	List<PublishAdverRecord> listPublishAdverByDeviceId(Long deviceId);

	/**
	 * 查询首页数据总条数
	 * 
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	@Query(value = "select count(1) from publish_advertisement_record par "
			+ "left join advertisement a on par.advertisement_id = a.id "
			+ "left join advertisement_type at on a.advertisement_type_id = at.id and at.type_no = '001' "
			+ "where 1=1 and par.is_deleted = 0 and par.status = 1", nativeQuery = true)
	Long queryHomePageTotalCount();

	/**
	 * 通过设备id获取广告发布数据
	 * 
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	@Query(value = "select * from publish_advertisement_record par "
			+ "left join advertisement a on par.advertisement_id = a.id "
			+ "left join advertisement_type at on a.advertisement_type_id = at.id and at.type_no = '001' "
			+ "where 1=1 and par.is_deleted = 0 and par.status = 1 limit ?1, ?2", nativeQuery = true)
	List<PublishAdverRecord> queryHomePageList(int curPage, int pageSize);

	@Query(value = "select * from  publish_advertisement_record  where is_deleted = 0 and end_time < ?1", nativeQuery = true)
	List<PublishAdverRecord> listPublishByEndTime(Date time);

	/**
	 * 广告下架，通过广告id和订单id修改发布数据
	 * 
	 * @param advertisementId
	 * @param orderId
	 * @param isDeleted
	 * @param updateTime
	 * @param updateUserId
	 * @return
	 */
	@Modifying
	@Query(value = "update publish_advertisement_record set is_deleted = ?3, update_time = ?4, update_user_id = ?5, end_time = ?4 where 1=1 and advertisement_id = ?1 and order_id = ?2", nativeQuery = true)
	int editPublishRecord(Long advertisementId, Long orderId, int isDeleted, Date updateTime, Long updateUserId);

}
