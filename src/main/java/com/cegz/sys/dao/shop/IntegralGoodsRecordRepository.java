/**
 * 
 */
package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.IntegralGoodsRecord;

/**
 * 积分兑换持久化服务
 * @author yucheng
 * @date 2018年11月29号
 */
public interface IntegralGoodsRecordRepository extends JpaRepository<IntegralGoodsRecord, Long> {

	/**
	 * 获取积分兑换详情列表
	 * @param startPage
	 * @param pageSize
	 * @param phone
	 * @param name
	 * @param typeNo
	 * @return
	 */
	@Query(value = 
			
			" SELECT * FROM integral_goods_record igr " + 
			" LEFT JOIN users u ON igr.create_user_id = u.id " + 
			" LEFT JOIN integral i ON i.create_user_id = u.id " + 
			" LEFT JOIN integral_goods ig ON igr.goods_id = ig.id " + 
			" LEFT JOIN wechat w ON u.id = w.user_id "
			+ " where if(?3 != '', igr.phone like %?3%, 1 = 1) "
			+ " and if(?4 != '', ig.name like %?4%, 1 = 1) "
			+ " and if(?5 != '' and ?5 != '002', igr.is_deleted = 1, 1 = 1) "
			+ " and if(?5 != '' and ?5 != '001', igr.is_deleted = 0, 1 = 1) " + " order by igr.create_time limit ?1, ?2", nativeQuery = true)
	List<IntegralGoodsRecord> queryIntegralGoodsRecordList(Integer startPage, Integer pageSize, String phone,
			String name, String typeNo);
	

	
}
