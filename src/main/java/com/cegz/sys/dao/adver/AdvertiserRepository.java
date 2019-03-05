package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Advertiser;

/**
 * 广告方持久化接口
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Transactional
public interface AdvertiserRepository extends JpaRepository<Advertiser, Long> {

	/**
	 * 查询广告主数据总条数
	 */
	@Query(value = "select count(1) from advertiser a left join users u on a.create_user_id = u.id where 1=1 "
			+ "and a.is_deleted = 0 and a.status = ?1 "
			+ "and if(?2 != '', a.name like %?2%, 1 = 1) and if(?3 != '', a.phone like %?3%, 1 = 1) "
			+ "and if(?4 != '', u.phone like %?4%, 1 = 1)", nativeQuery = true)
	Long queryAdvertiserExamineTotalCount(Integer status, String name, String phone, String registerPhone);

	/**
	 * 查询广告主审核列表
	 */
	@Query(value = "select * from advertiser a left join users u on a.create_user_id = u.id where 1=1 "
			+ "and a.is_deleted = 0 and a.status = ?3 "
			+ "and if(?4 != '', a.name like %?4%, 1 = 1) and if(?5 != '', a.phone like %?5%, 1 = 1) "
			+ "and if(?6 != '', u.phone like %?6%, 1 = 1)"
			+ "order by a.create_time desc limit ?1, ?2", nativeQuery = true)
	public List<Advertiser> getAdvertiserExamineList(Integer curPage, Integer pageSize, Integer status, String name,
			String phone, String registerPhone);

	/**
	 * 审核广告主数据
	 * 
	 * @param id
	 * @param status
	 * @param reason
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query(value = "update advertiser set status = ?2, reason = ?3, update_time = ?4, city = ?5, company = ?6, business_license = ?7, name = ?8, phone = ?9, email = ?10, address = ?11, address_detail = ?12 "
			+ "where id = ?1", nativeQuery = true)
	int advertiserStatusExamine(Long id, byte status, String reason, Date updateTime, String city, String company,
			String businessNumber, String name, String phone, String email, String address, String addressDetail);

	/**
	 * 通过广告主公司名称获取数据
	 * 
	 * @param company ： 公司名称
	 * @return
	 */
	@Query(value = "select count(1) from advertiser a where 1=1 and a.status = 1 and a.company = ?1", nativeQuery = true)
	int checkCompanyNameExist(String company);
	
	/**
	 * 通过订单ID获取广告主信息
	 * @param id
	 * @return
	 */
	@Query(value = 
			" SELECT a.* from advertiser a " + 
			" LEFT JOIN advertisement am on am.advertiser_id = a.id " + 
			" LEFT JOIN order_advertiser oa on oa.advertisement_id = am.id " + 
			" LEFT JOIN check_money cm on cm.order_id = oa.id " + 
			" where " + 
			" oa.id = ?1 group by a.id " , nativeQuery = true)
	Advertiser findAdvertiserByOrderId(Long id);

}
