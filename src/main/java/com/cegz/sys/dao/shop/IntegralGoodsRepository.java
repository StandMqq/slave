package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.IntegralGoods;

/**
 * 积分商品持久化服务
 * 
 * @author tenglong
 * @date 2018年11月29日
 */
public interface IntegralGoodsRepository extends JpaRepository<IntegralGoods, Long> {

	/**
	 * 获取所有积分商品数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @param isDeleted
	 * @return
	 */
	@Query(value = "select * from integral_goods b where 1=1 and if(?3 != '', b.name like %?3%, 1 = 1) "
			+ "and if(?4 != '' and ?4 = '0', b.is_deleted = 0, 1 = 1) and if(?4 != '' and ?4 = '1', b.is_deleted = 1, 1 = 1) "
			+ "order by b.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<IntegralGoods> getIntegralGoodsList(Integer curPage, Integer pageSize, String name, String isDeleted);

	/**
	 * 查询所有积分商品数据总条数
	 * 
	 * @param name
	 * @param isDeleted
	 * @return
	 */
	@Query(value = "select count(1) from integral_goods b where 1=1 and if(?1 != '', b.name like %?1%, 1 = 1) "
			+ "and if(?2 != '' and ?2 = '0', b.is_deleted = 0, 1 = 1) and if(?2 != '' and ?2 = '1', b.is_deleted = 1, 1 = 1) ", nativeQuery = true)
	Long getIntegralGoodsCount(String name, String isDeleted);
}
