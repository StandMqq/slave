package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.IntegralRecord;

/**
 * 用户积分中间表持久化接口
 * 
 * @author tenglong
 * @date 2018年9月24日
 */
public interface IntegralRecordRepository extends JpaRepository<IntegralRecord, Long> {

	/**
	 * 通过用户id获取积分数据总数
	 * 
	 * @param usersId
	 * @return
	 */
	@Query(value = "select count(ir.id) from integral_record ir left join integral i on i.id = ir.integral_id where 1=1 "
			+ "and i.create_user_id = ?1 and ir.is_deleted = 0", nativeQuery = true)
	Long getUsersIntegralCount(Long usersId);

	/**
	 * 通过用户id获取积分数据详情列表
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param usersId
	 * @return
	 */
	@Query(value = "select ir.* from integral_record ir left join integral i on i.id = ir.integral_id where 1=1 "
			+ "and i.create_user_id = ?3 and ir.is_deleted = 0 order by ir.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<IntegralRecord> getUsersIntegralList(Integer curPage, Integer pageSize, Long usersId);
}
