package com.cegz.sys.dao.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.Integral;

/**
 * 积分持久化接口
 * 
 * @author tenglong
 * @date 2018年9月24日
 */
public interface IntegralRepository extends JpaRepository<Integral, Long> {

	@Query(value = "select * from integral where create_user_id = ?1 and is_deleted = 0", nativeQuery = true)
	Integral getIntegralByUsersId(Long userId);
}
