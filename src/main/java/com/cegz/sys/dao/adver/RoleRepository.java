package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Role;

/**
 * 角色持久化服务
 * 
 * @author tenglong
 * @date 2018年10月18日
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query(value = "select * from role where 1=1 and is_deleted = 0 order by sort", nativeQuery = true)
	List<Role> getRoleList();
	
	@Query(value = "select count(id) from role where 1=1 and is_deleted = 0", nativeQuery = true)
	int getRoleCount();
}
