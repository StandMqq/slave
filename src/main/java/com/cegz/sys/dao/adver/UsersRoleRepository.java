package com.cegz.sys.dao.adver;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.UsersRole;

/**
 * 用户角色中间表持久化服务
 * 
 * @author tenglong
 * @date 2018年10月18日
 */
@Transactional
public interface UsersRoleRepository extends JpaRepository<UsersRole, Long> {

	@Query(value = "select * from users_role where 1=1 and is_deleted = 0 and users_id = ?1", nativeQuery = true)
	List<UsersRole> getUsersRoleListByUserId(Long userId);

	@Modifying
	@Query(value = "update users_role set is_deleted = 1,update_user_id = ?2,update_time = now() where users_id = ?1", nativeQuery = true)
	int deleteRolesByUserId(Long userId, Long updateUserId);

	@Modifying
	@Query(value = "insert into users_role(users_id,role_id,create_user_id,create_time) values(?1,?2,?3,now())", nativeQuery = true)
	int insertUsersRole(Long userId, Long roleId, Long createUserId);
}
