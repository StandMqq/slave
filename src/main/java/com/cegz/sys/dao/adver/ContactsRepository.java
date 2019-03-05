package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Contacts;

@Transactional
public interface ContactsRepository extends JpaRepository<Contacts, Long> {

	/**
	 * 查询车主审核列表
	 * 
	 * @return
	 */
	@Query(value = "select * from contacts c where 1=1 and c.status = ?3 and if(?4 != '', c.name like %?4%, 1 = 1) and if(?5 != '', c.phone like %?5%, 1 = 1) and c.is_deleted = 0 order by c.create_time desc limit ?1,?2", nativeQuery = true)
	List<Contacts> getContactsExamineList(int startPos, int size, Integer status, String name, String phone);

	@Query(value = "select count(id) from contacts c where 1=1 and c.status = ?1 and if(?2 != '', c.name like %?2%, 1 = 1) and if(?3 != '', c.phone like %?3%, 1 = 1) and c.is_deleted = 0", nativeQuery = true)
	int countByStatus(Integer status, String name, String phone);

	@Query(value = "select * from contacts c where 1=1 and c.create_user_id = ?1", nativeQuery = true)
	Contacts getContactsByUsersId(Long usersId);

	/**
	 * 审核车主主数据
	 */
	@Modifying
	@Query(value = "update contacts set status = ?2, reason = ?3, update_time = ?4, name = ?5, phone = ?6 where id = ?1", nativeQuery = true)
	int carOwnerStatusExamine(Long id, byte status, String reason, Date updateTime, String name, String phone);

	/**
	 * 获取车主数据
	 * @param i
	 * @param j
	 * @param phoneInput
	 * @param nameInput
	 * @return
	 */
	@Query(value = "select c.* from contacts c where 1=1 and if(?3 != '', c.phone like %?3%, 1 = 1) and if(?4 != '', c.name like %?4%, 1 = 1) and c.is_deleted = 0 limit ?1,?2", nativeQuery = true)
	List<Contacts> queryContactsList(int i, int j, String phoneInput, String nameInput);

	/**
	 * 根据名字或者电话查询记录数
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
	 * 根据名字或电话查询数据列表
	 * @param startPage
	 * @param pageSize
	 * @param name
	 * @param phone
	 * @return
	 */
	@Query(value = "select c.* from contacts c where 1=1 and if(?3 != '', c.name like %?3%, 1=1) and if(?4 != '', c.phone like %?4%, 1=1) order by c.create_time desc limit ?1,?2 ", nativeQuery = true)
	List<Contacts> getContactsListByNameOrPhone(Integer startPage, Integer pageSize, String name, String phone);

	/**
	 * 根据contacts_id获取名字和电话
	 * @param contactId
	 * @return
	 */
	@Query(value = "select c.* from contacts c where c.id = ?1 ", nativeQuery = true)
	Contacts getOneByContactId(Long contactId);

	/**
	 * 根据id获取contacts_id
	 * @param id
	 * @return
	 */
	@Query(value = "select dr.contact_id from driving_registration dr where dr.id = ?1 ", nativeQuery = true)
	Long getContactsIdByid(Long id);
}
