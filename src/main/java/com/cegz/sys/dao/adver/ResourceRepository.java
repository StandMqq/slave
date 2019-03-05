package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Resource;

/**
 * 资源表持久化服务
 * 
 * @author tenglong
 * @date 2018年10月18日
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {

	@Query(value = "select * from resource where 1=1 and is_deleted = 0 and menu_id = ?1", nativeQuery = true)
	List<Resource> getResourceListByMenuId(Long menuId);
}
