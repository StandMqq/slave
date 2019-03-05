package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.SellBindUser;

/**
 * 销售持久化服务
 * 
 * @author tenglong
 * @date 2018年9月18日
 */
public interface SaleRepository extends JpaRepository<SellBindUser, Long> {

	/**
	 * 查询所有销售绑定数据总条数
	 * 
	 * @param phone
	 * @return
	 */
	@Query(value = "select count(t.id) from ( "
			+ "select sbu.id from sell_bind_user sbu left join users u on u.id = sbu.sell_user_id where 1=1 and "
			+ "if(?1 != '', sbu.sell_user_id = ?1, 1 = 1) and " + "if(?2 != '', u.username like %?2%, 1 = 1) "
			+ "group by sbu.sell_user_id order by sbu.create_time desc) t ", nativeQuery = true)
	Long getSaleBindCount(Long userId, String phone);

	/**
	 * 获取所有销售绑定数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param phone
	 * @return
	 */
	@Query(value = "select sbu.* from sell_bind_user sbu left join users u on u.id = sbu.sell_user_id where 1=1 and "
			+ "if(?3 != '', sbu.sell_user_id = ?3, 1 = 1) and " + "if(?4 != '', u.username like %?4%, 1 = 1) "
			+ "group by sbu.sell_user_id order by sbu.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<SellBindUser> getSaleBindList(Integer curPage, Integer pageSize, Long userId, String phone);

	/**
	 * 通过销售人id查询绑定人数
	 * 
	 * @param sellUserId
	 * @return
	 */
	@Query(value = "select count(sbu.id) from sell_bind_user sbu where 1=1 and sbu.sell_user_id = ?1 ", nativeQuery = true)
	Long querySaleBindNum(Long sellUserId);

	/**
	 * 查询所有绑定客户数据总条数
	 * 
	 * @param sellUserId 绑定人id
	 * @return
	 */
	@Query(value = "select count(id) from sell_bind_user sbu where 1=1 and sell_user_id = ?1 ", nativeQuery = true)
	Long getBindClientsCount(Long sellUserId);

	/**
	 * 获取所有绑定客户数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param sellUserId 绑定人id
	 * @return
	 */
	@Query(value = "select * from sell_bind_user sbu where 1=1 and sell_user_id = ?3 order by sbu.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<SellBindUser> getBindClientsList(Integer curPage, Integer pageSize, Long sellUserId);
	
	@Query(value = "select * from sell_bind_user where user_id = ?1 and is_deleted = 0", nativeQuery = true)
	SellBindUser getDataByUser(Long useId);
}
