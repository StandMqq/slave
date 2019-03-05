package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Sponsor;

/**
 * 保荐方持久化接口
 * 
 * @author tenglong
 * @date 2018年7月30日
 */
@Transactional
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {

	/**
	 * 查询保荐方数据总条数
	 */
	@Query(value = "select count(1) from sponsor s where 1=1 and s.is_deleted = 0 and s.status = ?1 and if(?2 != '', s.name like %?2%, 1 = 1) and if(?3 != '', s.phone like %?3%, 1 = 1)", nativeQuery = true)
	Long querySponsorExamineTotalCount(Integer status, String name, String phone);

	@Query(value = "select * from sponsor s where 1=1 and s.is_deleted = 0 and s.status = ?3 and if(?4 != '', s.name like %?4%, 1 = 1) and if(?5 != '', s.phone like %?5%, 1 = 1) order by s.create_time desc limit ?1, ?2", nativeQuery = true)
	List<Sponsor> getSponsorExamineList(Integer curPage, Integer pageSize, Integer status, String name, String phone);


	/**
	 * 审核保荐方数据
	 * 
	 * @param id
	 * @param status
	 * @param reason
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query(value = "update sponsor set status = ?2, reason = ?3, update_time = ?4, province = ?5, company = ?6, company_little = ?7, business_license = ?8, name = ?9, phone = ?10, email = ?11, address = ?12, address_detail = ?13 "
			+ "where id = ?1", nativeQuery = true)
	int statusExamine(Long id, byte status, String reason, Date updateTime, String city, String company,
			String companyLittle, String businessNumber, String name, String phone, String email, String address,
			String addressDetail);

	/**
	 * 通过保荐方公司名称获取数据
	 * 
	 * @param company ： 公司名称
	 * @return
	 */
	@Query(value = "select count(1) from sponsor s where 1=1 and s.status = 1 and s.company = ?1", nativeQuery = true)
	int checkCompanyNameExist(String company);

	/**
	 * 通过保荐方公司名称简称获取数据
	 * 
	 * @param companyLittle ： 公司名称简称
	 * @return
	 */
	@Query(value = "select count(1) from sponsor s where 1=1 and s.status = 1 and s.company_little = ?1", nativeQuery = true)
	int checkCompanyLittleNameExist(String companyLittle);

	/**
	 * @param id
	 * @param name
	 * @param phone
	 * @return
	 */

	
}
