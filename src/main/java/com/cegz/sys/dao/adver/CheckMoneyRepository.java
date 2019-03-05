package com.cegz.sys.dao.adver;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.CheckMoney;

/**
 * 账单持久化接口
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Transactional
public interface CheckMoneyRepository extends JpaRepository<CheckMoney, Long> {
	/**
	 * 通过用户id获取账单明细列表
	 */
	@Query(value = "select * from check_money c where 1=1 and c.is_deleted = 0 and c.create_user_id = ?1", nativeQuery = true)
	public List<CheckMoney> listCheckMoneyByUserId(Long userId);

	/**
	 * 保存账单
	 * 
	 * @param createTime
	 * @param money
	 * @param remark
	 * @param type         账单类型 1 支出， 2 收入
	 * @param createUserId
	 * @param updateUserId
	 * @param bill_status  账单状态，0：成功，1：失败，2：微信取消
	 * @return
	 */
	@Modifying
	@Query(value = "insert into check_money(create_time,money,remark,type,is_deleted,create_user_id,update_user_id,voucher,bill_status) values(?1,?2,?3,?4,0,?5,?6,?7,0)", nativeQuery = true)
	int saveCheckMoney(Date createTime, Double money, String remark, int type, Long createUserId, Long updateUserId,
			String voucher);


	/**
	 * 通过用户id获取账单明细列表
	 * @param startTime 
	 * @param endTime 
	 * @param pageSize 
	 * @param startPage 
	 */
	@Query(value = "select * from check_money c where 1=1 and c.is_deleted = 0 and c.create_user_id = ?1"
			+ " and if(?2 != '', c.create_time >= ?2, 1 = 1) and if(?3 != '', c.create_time <= ?3, 1 = 1) "
			+ " order by c.create_time desc limit ?4, ?5", nativeQuery = true)
	public List<CheckMoney> listCheckMoneyByUserId(Long userId, String startTime, String endTime, Integer startPage, Integer pageSize);


	/**
	 * 通过用户id查询收入（充值）金额
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "select sum(c.money) from check_money c where 1=1 and c.is_deleted = 0 and c.bill_status = 0 and c.create_user_id = ?1", nativeQuery = true)
	Double getRechargeMoney(Long userId);

	/**
	 * 通过用户id查询收入（充值）次数
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "select count(id) from check_money c where 1=1 and c.is_deleted = 0 and c.type != 1 and c.bill_status = 0 and c.create_user_id = ?1", nativeQuery = true)
	Long getRechargeCount(Long userId);

	/**
	 * 查询钱包明细数据总条数
	 * 
	 * @param createUserId 被操作人
	 * @return
	 */
	@Query(value = "select count(1) from check_money c "
			+ " left join order_advertiser oa on c.order_id = oa.id "
			+ " left join price p on oa.price_id = p.id "
			+ " where 1=1 and c.is_deleted = 0 "
			+ " and c.create_user_id = ?1 "
			+ " and if(?2 != '', c.create_time >= ?2, 1 = 1) and if(?3 != '', c.create_time <= ?3, 1 = 1)"
			+ " and if(?4 = 0, c.type != 1, 1 = 1) "
			+ " and if(?4 = 1, c.type = 1, 1 = 1) "
			+ " and if(?5 !='', oa.price_id = ?5, 1 = 1)", nativeQuery = true)
	Long queryCheckMoneyCountByCreateUserId(Long createUserId, String startTime, String endTime,Long category, Long setMeal);

	/**
	 * 查询钱包明细数据列表
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param createUserId 被操作人
	 * @return
	 */
	@Query(value = "select * from check_money c "
			+ " left join order_advertiser oa on c.order_id = oa.id "
			+ " left join price p on oa.price_id = p.id "
			+ " where 1=1 and c.is_deleted = 0 "
			+ " and c.create_user_id = ?3 "
			+ " and if(?4 != '', c.create_time >= ?4, 1 = 1) and if(?5 != '', c.create_time <= ?5, 1 = 1) "
			+ " and if(?6 = 0, c.type != 1, 1 = 1) "
			+ " and if(?6 = 1, c.type = 1, 1 = 1) "
			+ " and if(?7 !='', oa.price_id = ?7, 1 = 1)"
			+ " order by c.create_time desc limit ?1, ?2", nativeQuery = true)
	List<CheckMoney> queryCheckMoneyListByCreateUserId(Integer curPage, Integer pageSize, Long createUserId,
			String startTime, String endTime, Long category, Long setMeal);

	/**
	 * 通过用户id获取账单明细总数
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Query(value = "select count(1) from check_money c where 1=1 and c.is_deleted = 0 and c.create_user_id = ?1"
			+ " and if(?2 != '', c.create_time >= ?2, 1 = 1) and if(?3 != '', c.create_time <= ?3, 1 = 1) ", nativeQuery = true)
	public Long queryCheckMoneyTotalCount(Long id, String startTime, String endTime);

	/**
	 * 通过用户id获取账单开票详情数量
	 * @param id
	 * @param status
	 * @return
	 */
	@Query(value = "select count(1) from check_money c where 1=1 and c.is_deleted = 0 and c.create_user_id = ?1"
			+ " and c.status = ?2 and c.type != 1 ORDER BY c.create_time DESC", nativeQuery = true)
	public Long queryCheckMoneyTotalCount(Long id, Integer status);

	/**
	 * 通过用户id获取账单开票详情
	 * @param id
	 * @param status
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	@Query(value = "select * from check_money c where 1=1 and c.is_deleted = 0 and c.create_user_id = ?1"
			+ " and c.status = ?2 and c.type != 1 ORDER BY c.create_time DESC"
			+ " limit ?3, ?4", nativeQuery = true)
	public List<CheckMoney> findCheckMoneyInvoiceByUserId(Long id, Integer status, Integer startPage, Integer pageSize);

	/**
	 * 通过ID修改账单状态到开票中
	 * @param id
	 * @param status
	 * @return
	 */
	@Modifying
	@Query(value = "update check_money set status = ?2 "
			+ "where id = ?1", nativeQuery = true)
	public int checkMoneyStatusExamine(Long id, Byte status);
	
	
	@Query(value = "update check_money set bill_status = ?1 where voucher = ?2", nativeQuery = true)
	@Modifying
	public int updateCheckStatus(int billStatus, String order);
	
	@Query(value = "select * from check_money where voucher = ?1", nativeQuery = true)
	public CheckMoney getCheckByOrder(String order);
	
	@Query(value ="select count(id) from check_money where (type = 2 or type = 3) and create_user_id = ?1 and is_deleted = 0", nativeQuery = true)
	public int countDataByUserAndInput(Long userId);
	
	@Query(value ="select * from check_money where (type = 2 or type = 3) and create_user_id = ?1 and is_deleted = 0 order by create_time desc limit ?2,?3", nativeQuery = true)
	List<CheckMoney> listDataByUser(Long userId, int startPos, int size);
	
	@Query(value = "select sum(money) from check_money where (type = 2 or type = 3) and create_user_id = ?1 and is_deleted = 0", nativeQuery = true)
	double sumDataByUser(Long userId);

}
