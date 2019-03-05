package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.Business;

/**
 * 商户持久化服务
 * 
 * @author tenglong
 * @date 2018年9月18日
 */
public interface BusinessRepository extends JpaRepository<Business, Long> {

	/**
	 * 获取所有商户数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@Query(value = "select * from business b where 1=1 and b.is_deleted = 0 and if(?3 != '', b.name like %?3%, 1 = 1) "
			+ "order by b.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<Business> getBusinessList(Integer curPage, Integer pageSize, String name);

	/**
	 * 查询所有商户数据总条数
	 * 
	 * @param name
	 * @return
	 */
	@Query(value = "select count(1) from business b where 1=1 and b.is_deleted = 0 and if(?1 != '', b.name like %?1%, 1 = 1) ", nativeQuery = true)
	Long getBusinessCount(String name);
}
