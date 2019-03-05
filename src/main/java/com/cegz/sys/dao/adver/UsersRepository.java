package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Users;

/**
 * 用户持久化服务
 * 
 * @author lijiaxin
 * @date 2018年7月20日
 */
public interface UsersRepository extends JpaRepository<Users, Long> {
	int countByUserNameAndPassword(String userName, String password);

	Users findByUserName(String userName);

	Users findByUserNameAndPasswordSalt(String userName, String password);

	@Query(value = "select count(1) from users where 1=1 and is_deleted = 0 and if(?1 != '', name like %?1%, 1 = 1) and if(?2 != '', phone like %?2%, 1 = 1)", nativeQuery = true)
	Long getUsersCount(String userName, String phone);

	@Query(value = "select * from users where 1=1 and is_deleted = 0 and if(?3 != '', name like %?3%, 1 = 1) and if(?4 != '', phone like %?4%, 1 = 1) order by create_time desc limit ?1, ?2", nativeQuery = true)
	List<Users> getUsersList(Integer curPage, Integer pageSize, String userName, String phone);
}
