package com.cegz.sys.service.adver;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.Users;

/**
 * 账号服务
 * 
 * @author lijiaxin
 * @date 2018年7月19日
 */
public interface AccountService {
	/**
	 * 注册账号
	 * 
	 * @param user
	 * @return
	 * @author lijiaxin
	 * @date 2018年7月19日
	 */
	int regist(Users user);

	/**
	 * 登陆
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @author lijiaxin
	 * @date 2018年7月20日
	 */
	Users login(String userName, String password);

	/**
	 * 获取账号信息
	 * 
	 * @param userName
	 * @return
	 * @author lijiaxin
	 * @date 2018年7月20日
	 */
	Users getUserByName(String userName);

	/**
	 * 通过id获取账号信息
	 * 
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */
	Optional<Users> getUserById(Long id);

	/**
	 * 获取账号记录数
	 * 
	 * @param userName
	 * @param phone
	 * @return
	 */
	Long getUsersCount(String userName, String phone);

	/**
	 * 获取账号列表
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param userName
	 * @param phone
	 * @return
	 */
	List<Users> getUsersList(Integer curPage, Integer pageSize, String userName, String phone);

}
