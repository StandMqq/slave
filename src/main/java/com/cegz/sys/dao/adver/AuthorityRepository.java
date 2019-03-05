package com.cegz.sys.dao.adver;

import java.util.List;

import com.cegz.sys.model.adver.Contacts;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.model.adver.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Authority;
import org.springframework.transaction.annotation.Transactional;

/**
 * 黑名单持久类
 * 
 * @author tenglong
 * @date 2018年8月20日
 */
@Transactional
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	/**
	 * 获取黑名单记录数（不带黑名单等级参数）
	 */
	@Query(value = "select count(id) from authority a where 1=1 and a.is_deleted = 0 and a.name like %?1%", nativeQuery = true)
	Long getAuthorityCount(String name);

	/**
	 * 获取黑名单记录数（带黑名单等级参数）
	 */
	@Query(value = "select count(id) from authority a where 1=1 and a.is_deleted = 0 and a.grade = ?1 and a.name like %?2%", nativeQuery = true)
	Long getAuthorityCount(Integer grade, String name);

	/**
	 * 获取黑名单列表（不带黑名单等级参数）
	 */
	@Query(value = "select * from authority a where 1=1 and a.is_deleted = 0 and a.name like %?3% order by a.create_time desc limit ?1, ?2", nativeQuery = true)
	List<Authority> getAuthorityList(Integer curPage, Integer pageSize, String name);

	/**
	 * 获取黑名单列表（带黑名单等级参数）
	 */
	@Query(value = "select * from authority a where 1=1 and a.is_deleted = 0 and a.grade = ?3 and a.name like %?4% order by a.create_time desc limit ?1, ?2", nativeQuery = true)
	List<Authority> getAuthorityList(Integer curPage, Integer pageSize, Integer grade, String name);

	/**
	 * 获取黑名单列表（带黑名单等级参数）
	 */
	@Query(value = "select * from authority a where 1=1 and a.is_deleted = 0 and a.create_user_id = ?1", nativeQuery = true)
	List<Authority> getAuthorityListByCreateId(Long createId);

	/**
	 * 查询等级为4且有效的名单数据条数
	 * @param grade
	 * @return
	 */
	@Query(value = "select count(a.id) from authority a where a.grade = ?1 and a.is_deleted = 0 ", nativeQuery = true)
	Long getAuthorityCountByGrade(Byte grade);

	/**
	 * 获取名单等级为4且有效的数据列表
	 * @param startPage
	 * @param pageSize
	 * @param grade
	 * @return
	 */
	@Query(value = "select a.* from authority a where a.grade = ?3 and a.is_deleted = 0 order by a.create_time desc limit ?1,?2 ", nativeQuery = true)
	List<Authority> getAuthorityListByGrade(Integer startPage, Integer pageSize, Byte grade);

	/**
	 *根据车主名字和车主电话模糊查询获得和名单数据条数
	 * @param name
	 * @param phone
	 * @return
	 */
	@Query(value = "select count(c.id) from contacts c " +
			"left join driving_license dl on c.driving_license_id = dl.id " +
			"where 1=1 " +
			"and if(?1 != '', c.name like %?1%, 1=1) " +
			"and if(?2 != '', c.phone like %?2%, 1=1) ", nativeQuery = true)
	Long queryAuthorityCountByConditions(String name, String phone);

	/**
	 *根据车主姓名和车主电话模糊查询获取车主信息列表,分页展示
	 * @param startPage
	 * @param pageSize
	 * @param name
	 * @param phone
	 * @return
	 */
	@Query(value = "select * from contacts c " +
			"left join driving_license dl on c.driving_license_id = dl.id " +
			"left join driving_registration dr on dr.contact_id = c.id " +
			"where c.name = ?3 and c.phone = ?4 " +
			"order by c.create_time desc limit ?1,?2 ", nativeQuery = true)
	List<Contacts> getAuthorityListByConditions(Integer startPage, Integer pageSize, String name, String phone);

	/**
	 * 根据contactId获取车牌信息
	 * @param contactId
	 * @return
	 */
	@Query(value = "select dr.* from driving_registration dr where dr.contact_id = ?1 ", nativeQuery = true)
	List<DrivingRegistration> getPlateNumberByContactId(Long contactId);

	/**
	 * 根据车牌号查询信息
	 * @param plateNumber
	 * @return
	 */
	@Query(value = "select dr.* from driving_registration dr where dr.plate_number = ?1 ", nativeQuery = true)
	DrivingRegistration queryDrivingRegistrationByPlateNumber(String plateNumber);


	/**
	 * 根据contactId获取车主信息
	 * @param contactId
	 * @return
	 */
	@Query(value = "select c.name,c.phone,dl.sex from contacts c left join driving_license dl on c.driving_license_id = dl.id " +
			"where c.id = ?1 ", nativeQuery = true)
	Contacts queryContactBycontactId(Long contactId);

	/**
	 * 根据id查询审核者电话
	 * @param updateUserId
	 * @return
	 */
	@Query(value = "select u.phone from users u where u.id = ?1 ", nativeQuery = true)
	Users getUpdateUserPhoneById(Long updateUserId);

	/**
	 * 新增车主到黑名单
	 * @param name
	 * @param grade
	 * @param date
	 * @param createUserId
	 * @param remark
	 */
	@Modifying
	@Query(value = "insert into authority (name, grade, create_time, create_user_id, remark, is_deleted, update_user_id) values(?1,?2,?3,?4,?5,?6,?7) ", nativeQuery = true)
	void addContactsToAuthority(String name, Byte grade, String date, Long createUserId, String remark, Byte isDeleted, Long updateUserId);

	/**
	 * 通过车主名字查询车主电话
	 * @param name
	 * @return
	 */
	@Query(value = "select c.phone from contacts c where c.name = ?1 ", nativeQuery = true)
	List<String> getPhoneByName(String name);

	/**
	 * 通过车主名字和电话查询车牌号
	 * @param name
	 * @param phone
	 * @return
	 * //select dr.plate_number from contacts c left join driving_registration dr on c.id = dr.contact_id where c.phone = ?2 and c.name = ?1
	 */
	@Query(value = "select dr.plate_number from contacts c left join driving_registration dr on c.id = dr.contact_id " +
			"where 1=1 and if(?1 != '', c.name = ?1, 1=1) and if(?2 != '', c.phone = ?2, 1=1) ", nativeQuery = true)
	List<String> getPlateNumberByNameAndPhone(String name, String phone);

	/**
	 * 根据id将车主移出黑名单
	 * @param id
	 */
	@Modifying
	@Query(value = "update authority a set a.is_deleted = 1 where id = ?1 ", nativeQuery = true)
	void relieveAuthorityById(Long id);


	/**
	 * 根据车主名字查询黑名单中时候存在该车主
	 * @param contactsName
	 * @return
	 */
	@Query(value = "select a.* from authority a where a.name = ?1 ", nativeQuery = true)
	Authority getOneByContactsName(String contactsName);


    /**
     * 根据名字或者电话查询有效且等级为4的数据
     * @param name
     * @return
     */
    @Query(value = "select a.* from  authority a where a.name = ?1 and a.grade = 4 and a.is_deleted = 0 ", nativeQuery = true)
    Authority getAuthorityByName(String name);
}
