package com.cegz.sys.service.adver;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.*;

/**
 * 黑名单服务
 * 
 * @author tenglong
 * @date 2018年8月21日
 */
public interface AuthorityService {

	/**
	 * 通过主键查询黑名单
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月23日
	 */
	Optional<Authority> getAuthorityById(Long id);

	/**
	 * 保存黑名单
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月21日
	 */
	int save(Authority authority);

	/**
	 * 获取黑名单记录数（不带黑名单等级参数）
	 * 
	 * @return
	 */
	Long getAuthorityCount(String name);

	/**
	 * 获取黑名单记录数（带黑名单等级参数）
	 * 
	 * @return
	 */
	Long getAuthorityCount(Integer grade, String name);

	/**
	 * 获取黑名单列表（不带黑名单等级参数）
	 * 
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	List<Authority> getAuthorityList(Integer curPage, Integer pageSize, String name);

	/**
	 * 获取黑名单列表（带黑名单等级参数）
	 * 
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	List<Authority> getAuthorityList(Integer curPage, Integer pageSize, Integer grade, String name);

	/**
	 * 通过用户id（被操作人id）获取黑名单列表
	 * 
	 * @return
	 */
	List<Authority> getAuthorityListByCreateId(Long createUserId);

	/**
	 * 通过用户id获取List<UsersRole>
	 * 
	 * @return
	 */
	List<UsersRole> getUsersRoleListByUserId(Long userId);

	/**
	 * 通过角色id获取List<RoleMenu>
	 * 
	 * @return
	 */
	List<RoleMenu> getRoleMenuListByRoleId(Long roleId);

	/**
	 * 通过角色id获取一级List<RoleMenu>
	 * 
	 * @return
	 */
	List<RoleMenu> getRoleOneMenuListByRoleId(Long roleId);

	/**
	 * 通过角色id和菜单父级id获取List<RoleMenu>
	 * 
	 * @return
	 */
	List<RoleMenu> getTwoMenuListByRoleIdAndPId(Long roleId, Long pid);

	/**
	 * 获取所有的角色总数
	 * 
	 * @return
	 */
	int getRoleCount();

	/**
	 * 获取所有的角色List<Role>
	 * 
	 * @return
	 */
	List<Role> getRoleList();

	/**
	 * 通过用户id删除对应角色
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年10月31日
	 */
	int deleteRolesByUserId(Long userId, Long updateUserId);

	/**
	 * 通过用户id删除对应角色
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年10月31日
	 */
	int insertUsersRole(Long userId, Long roleId, Long createUserId);

	/**
	 * 获取所有的菜单List<Role>
	 * 
	 * @return
	 */
	List<Menu> getMenuList();

	/**
	 * 通过用户id删除对应角色
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年10月31日
	 */
	int deleteMenusByRoleId(Long roleId, Long updateUserId);

	/**
	 * 通过用户id删除对应角色
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年10月31日
	 */
	int insertRoleMenu(Long roleId, Long menuId, Long createUserId);

	/**
	 * 查询等级为4的名单数据数量
	 * @param grade
	 * @return
	 * @author maqianqian
	 * @date 2019年02月26日
	 */
	Long getAuthorityCountByGrade(Byte grade);

	/**
	 * 获取名单等级为4的数据列表
	 * @param startPage
	 * @param pageSize
	 * @param grade
	 * @return
	 * @author maqianqian
	 * @date 2019年02月26日
	 */
	List<Authority> getAuthorityListByGrade(Integer startPage, Integer pageSize, Byte grade);

	/**
	 * 根据车主名字和车主电话模糊查询获得和名单数据条数
	 * @param name
	 * @param phone
	 * @return
	 * @author maqianqian
	 * @date 2019年02月26日
	 */
	Long queryAuthorityCountByConditions(String name, String phone);

	/**
	 * 根据车主姓名和车主电话模糊查询获取车主信息列表,分页展示
	 * @param startPage
	 * @param pageSize
	 * @param name
	 * @param phone
	 * @return
	 * @author maqianqian
	 * @date 2019年02月26日
	 */
	List<Contacts> getAuthorityListByConditions(Integer startPage, Integer pageSize, String name, String phone);

	/**
	 * 根据contactId查询车牌信息
	 * @param contactId
	 * @return
	 * @author maqianqian
	 * @date 2019年02月26日
	 */
	List<DrivingRegistration> getPlateNumberByContactId(Long contactId);

	/**
	 *根据车牌号查询行驶证信息
	 * @param plateNumber
	 * @return
	 * @author maqianqian
	 * @date 2019年02月26日
	 */
	DrivingRegistration queryDrivingRegistrationByPlateNumber(String plateNumber);

	/**
	 * 根据该contactId获取对应的车主信息
	 * @return
	 * @author maqianqian
	 * @date 2019年02月26日
	 */
	Contacts queryContactBycontactId(Long contactId);

	/**
	 * 查询审核者电话
	 * @param updateUserId
	 * @return
	 * @author maqianqian
	 * @date 2019年02月27日
	 */
	Users getUpdateUserPhoneById(Long updateUserId);

	/**
	 * 新增车主到黑名单
	 * @param name
	 * @param grade
	 * @param date
	 * @param remark
	 * @param createUserId
	 * @param remark
	 * @author maqianqian
	 * @date 2019年02月27日
	 */
	void addContactsToAuthority(String name, Byte grade, String date, Long createUserId, String remark, Byte isDeleted, Long updateUserId);

	/**
	 * 通过车主名字查询车主电话
	 * @param name
	 * @return
	 * @author maqianqian
	 * @date 2019年02月27日
	 */
	List<String> getPhoneByName(String name);

	/**
	 * 通过车主名字和电话查询车牌号
	 * @param name
	 * @param phone
	 * @return
	 * @author maqianqian
	 * @date 2019年02月26日
	 */
	List<String> getPlateNumberByNameAndPhone(String name, String phone);

	/**
	 * 根据id将车主移出黑名单
	 * @param id
	 */
	void relieveAuthorityById(Long id);

	/**
	 * 根据车主名字查询黑名单中时候存在该车主
	 * @param contactsName
	 * @return
	 */
	Authority getOneByContactsName(String contactsName);

	/**
	 * 根据名字查询有效且等级为4的数据
	 * @param name
	 * @return
	 */
	Authority getAuthorityByName(String name);


}
