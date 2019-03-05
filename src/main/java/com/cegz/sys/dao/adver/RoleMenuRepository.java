package com.cegz.sys.dao.adver;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.RoleMenu;

/**
 * 角色菜单中间表持久化服务
 * 
 * @author tenglong
 * @date 2018年10月18日
 */
@Transactional
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {

	@Query(value = "select * from role_menu where 1=1 and is_deleted = 0 and role_id = ?1 order by id", nativeQuery = true)
	List<RoleMenu> getRoleMenuListByRoleId(Long roleId);

	@Query(value = "select rm.* from role_menu rm left join menu m on rm.menu_id = m.id"
			+ " where 1=1 and rm.is_deleted = 0 and m.pid is null and rm.role_id = ?1 order by m.id", nativeQuery = true)
	List<RoleMenu> getRoleOneMenuListByRoleId(Long roleId);

	@Query(value = "select rm.* from role_menu rm left join menu m on rm.menu_id = m.id where 1=1 "
			+ "and rm.is_deleted = 0 and rm.role_id = ?1 and m.pid = ?2 order by rm.id", nativeQuery = true)
	List<RoleMenu> getTwoMenuListByRoleIdAndPId(Long roleId, Long pid);

	@Modifying
	@Query(value = "update role_menu set is_deleted = 1,update_user_id = ?2,update_time = now() where role_id = ?1", nativeQuery = true)
	int deleteMenusByRoleId(Long roleId, Long updateUserId);

	@Modifying
	@Query(value = "insert into role_menu(role_id,menu_id,create_user_id,create_time) values(?1,?2,?3,now())", nativeQuery = true)
	int insertRoleMenu(Long roleId, Long menuId, Long createUserId);
}
