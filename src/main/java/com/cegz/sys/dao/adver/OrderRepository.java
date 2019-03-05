package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Order;

/**
 * 订单持久化接口
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

	/**
	 * 查询首页数据总条数
	 * 
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	@Query(value = "select count(1) from order_advertiser oa "
			+ "left join advertisement a on oa.advertisement_id = a.id "
			+ "left join advertisement_type at on a.advertisement_type_id = at.id "
			+ "where 1=1 and oa.is_deleted = 0 and oa.status = 1 and at.type_no = '001'", nativeQuery = true)
	Long queryHomePageTotalCount();

	/**
	 * 通过设备id获取广告发布数据
	 * 
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	@Query(value = "select oa.* from order_advertiser oa " + "left join advertisement a on oa.advertisement_id = a.id "
			+ "left join advertisement_type at on a.advertisement_type_id = at.id "
			+ "where 1=1 and oa.is_deleted = 0 and oa.status = 1 and at.type_no = '001' limit ?1, ?2", nativeQuery = true)
	List<Order> queryHomePageList(int curPage, int pageSize);

	@Modifying
	@Query(value = "update order_advertiser set status = 2 where end_time < ?1 and status = 1 and is_deleted = 0", nativeQuery = true)
	int updateOrderStatusByEnd(Date endTime);
	
	/**
	 * 通过广告id获取订单数据
	 * 
	 * @author tenglong
	 * @date 2018年11月23日
	 */
	@Query(value = "select oa.* from order_advertiser oa "
			+ "left join publish_advertisement_record par on par.order_id = oa.id "
			+ "where 1=1 and oa.is_deleted = 0 and oa.advertisement_id = ?1", nativeQuery = true)
//	and par.is_deleted = 0 and oa.status = 1
	Order getOrderByAdvertisementId(Long advertisementId);

}
