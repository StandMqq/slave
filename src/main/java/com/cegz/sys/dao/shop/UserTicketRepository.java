package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.UserTicket;

/**
 * 用户和劵关系表持久化接口
 * 
 * @author tenglong
 * @date 2018年9月12日
 */
public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {

	/**
	 * 通过劵id获取领取人详情总数
	 * 
	 * @param ticketId
	 * @param phone
	 * @return
	 */
	@Query(value = "select count(1) from user_ticket ut left join users u on ut.create_user_id = u.id "
			+ "where 1=1 and ut.ticket_id = ?1 and if(?2 != '', u.user_name like %?2%, 1 = 1) "
			+ "and ut.is_deleted = 0", nativeQuery = true)
	Long gainTicketUserDetailCount(Long ticketId, String phone);

	/**
	 * 通过劵id获取领取人详情列表
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param ticketId
	 * @param phone
	 * @return
	 */
	@Query(value = "select * from user_ticket ut left join users u on ut.create_user_id = u.id "
			+ "where 1=1 and ut.ticket_id = ?3 and if(?4 != '', u.user_name like %?4%, 1 = 1) "
			+ "and ut.is_deleted = 0 order by ut.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<UserTicket> gainTicketUserDetail(Integer curPage, Integer pageSize, Long ticketId, String phone);

	/**
	 * 通过用户id获取劵领取数
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "select count(id) from user_ticket ut where 1=1 "
			+ "and ut.create_user_id = ?1 and ut.is_deleted = 0 ", nativeQuery = true)
	Long getPullNumByUsersId(Long userId);

	/**
	 * 通过用户id获取劵使用数
	 * 
	 * @param userId
	 * @param status 状态，0 未使用，1 已使用，2 已过期
	 * @return
	 */
	@Query(value = "select count(id) from user_ticket ut where 1=1 "
			+ "and ut.create_user_id = ?1 and ut.status = ?2 and ut.is_deleted = 0 ", nativeQuery = true)
	Long getUseNumByUsersId(Long userId, byte status);

	/**
	 * 通过用户id获取劵数据总数
	 * 
	 * @param usersId
	 * @return
	 */
	@Query(value = "select count(ut.id) from user_ticket ut where 1=1 "
			+ "and ut.create_user_id = ?1 and ut.is_deleted = 0", nativeQuery = true)
	Long getUsersTicketCount(Long usersId);

	/**
	 * 通过用户id获取劵数据详情列表
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param usersId
	 * @return
	 */
	@Query(value = "select * from user_ticket ut where 1=1 "
			+ "and ut.create_user_id = ?3 and ut.is_deleted = 0 order by ut.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<UserTicket> getUsersTicketList(Integer curPage, Integer pageSize, Long usersId);
}
