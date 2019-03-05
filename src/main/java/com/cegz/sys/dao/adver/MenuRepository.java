package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Menu;

/**
 * 菜单持久化服务
 * 
 * @author tenglong
 * @date 2018年10月31日
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {

	@Query(value = "select * from menu where 1=1 and is_deleted = 0 order by case when pid is null then 1 else pid end asc,pid asc ", nativeQuery = true)
	List<Menu> getMenuList();

}
