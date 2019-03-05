package com.cegz.sys.dao.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.Users;

/**
 * 全城惠用户持久化接口
 * 
 * @author tenglong
 * @date 2018年9月24日
 */
public interface ShopUsersRepository extends JpaRepository<Users, Long> {

	/**
	 * 查询所有用户数据总条数
	 * 
	 * @param phone
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Query(value = "select count(1) from users u where 1=1 and u.is_deleted = 0 "
			+ "and if(?1 != '', u.phone like %?1%, 1 = 1) "
			+ "and if(?2 != '', u.last_login_time >= ?2, 1 = 1) and if(?3 != '', u.last_login_time <= ?3, 1 = 1) ", nativeQuery = true)
	Long getUsersCount(String phone, String startTime, String endTime);

	/**
	 * 获取所有用户数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param phone
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Query(value = "select * from users u where 1=1 and u.is_deleted = 0 "
			+ "and if(?3 != '', u.phone like %?3%, 1 = 1) "
			+ "and if(?4 != '', u.last_login_time >= ?4, 1 = 1) and if(?5 != '', u.last_login_time <= ?5, 1 = 1) "
			+ "order by u.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<Users> getUsersList(Integer curPage, Integer pageSize, String phone, String startTime, String endTime);

	/**
	 * 通过id获取用户信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<Users> getUsersById(Long id);
}
