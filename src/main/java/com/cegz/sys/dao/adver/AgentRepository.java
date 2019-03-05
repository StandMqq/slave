package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Agent;

/**
 * 
 * 代理商持久接口
 * 
 * @author lijiaxin
 * @date 2018年8月1日
 */
@Transactional
public interface AgentRepository extends JpaRepository<Agent, Long> {

	/**
	 * 查询保荐方数据总条数
	 */
	@Query(value = "select count(1) from agent a where 1=1 and a.is_deleted = 0 and a.status = ?1 and if(?2 != '', a.name like %?2%, 1 = 1) and if(?3 != '', a.phone like %?3%, 1 = 1)", nativeQuery = true)
	Long queryAgentExamineTotalCount(Integer status, String name, String phone);

	@Query(value = "select * from agent a where 1=1 and a.is_deleted = 0 and a.status = ?3 and if(?4 != '', a.name like %?4%, 1 = 1) and if(?5 != '', a.phone like %?5%, 1 = 1) order by a.create_time desc limit ?1, ?2", nativeQuery = true)
	public List<Agent> getAgentExamineList(Integer curPage, Integer pageSize, Integer status, String name,
			String phone);

	/**
	 * 审核代理商数据
	 * 
	 * @param id
	 * @param status
	 * @param reason
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query(value = "update agent set status = ?2, reason = ?3, update_time = ?4, city = ?5, company = ?6, business = ?7, name = ?8, phone = ?9, email = ?10, address = ?11, address_detail = ?12 "
			+ "where id = ?1", nativeQuery = true)
	int statusExamine(Long id, byte status, String reason, Date updateTime, String city, String company,
			String businessNumber, String name, String phone, String email, String address, String addressDetail);

}
