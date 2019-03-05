package com.cegz.sys.service.shop;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.shop.Integral;
import com.cegz.sys.model.shop.IntegralRecord;
import com.cegz.sys.model.shop.UserTicket;
import com.cegz.sys.model.shop.Users;
import com.cegz.sys.model.shop.WeChat;

/**
 * 用户服务
 * 
 * @author tenglong
 * @date 2018年9月25日
 */
public interface UsersService {

	/**
	 * 查询所有用户数据总条数
	 * 
	 * @param phone
	 * @param startTime
	 * @param endTime
	 * @return
	 */

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
	List<Users> getUsersList(Integer curPage, Integer pageSize, String phone, String startTime, String endTime);

	/**
	 * 通过id获取用户信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<Users> getUsersById(Long id);

	/**
	 * 通过用户id获取微信信息
	 * 
	 * @param userId
	 * @return
	 */
	WeChat getWeChatByUsersId(Long userId);

	/**
	 * 通过用户id获取积分信息
	 * 
	 * @param userId
	 * @return
	 */
	Integral getIntegralByUsersId(Long userId);

	/**
	 * 通过用户id获取劵领取数
	 * 
	 * @param userId
	 * @return
	 */
	Long getPullNumByUsersId(Long userId);

	/**
	 * 通过用户id获取劵使用数
	 * 
	 * @param userId
	 * @param status 状态，0 未使用，1 已使用，2 已过期
	 * @return
	 */
	Long getUseNumByUsersId(Long userId, byte status);

	/**
	 * 查询所有用户下劵数据总条数
	 * 
	 * @param usersId
	 * @return
	 */

	Long getUsersTicketCount(Long usersId);

	/**
	 * 获取所有用户下劵数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param usersId
	 * @return
	 */
	List<UserTicket> getUsersTicketList(Integer curPage, Integer pageSize, Long usersId);

	/**
	 * 查询所有用户下积分数据总条数
	 * 
	 * @param usersId
	 * @return
	 */

	Long getUsersIntegralCount(Long usersId);

	/**
	 * 获取所有用户下积分数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param usersId
	 * @return
	 */
	List<IntegralRecord> getUsersIntegralList(Integer curPage, Integer pageSize, Long usersId);

}
