package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.WithdrawCash;

/**
 * 
 * 提现账单
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
public interface PacketWithdrawCashRepository extends JpaRepository<WithdrawCash, Long> {

	@Query(value = "select w.* from withdraw_cash w left join users u on w.create_user_id = u.id "
			+ "where 1=1 and w.is_deleted = 0 and w.status= ?1 and if(?4 != '', u.pay_account like %?4%, 1 = 1) "
			+ "and if(?5 != '', w.update_time >= ?5, 1 = 1) and if(?6 != '', w.update_time <= ?6, 1 = 1) "
			+ "order by w.update_time desc, w.create_time desc limit ?2,?3", nativeQuery = true)
	List<WithdrawCash> listDataByStatus(byte status, int startPos, int size, String payAccount, String startTime,
			String endTime);

	@Query(value = "select count(w.id) from withdraw_cash w left join users u on w.create_user_id = u.id "
			+ "where 1=1 and w.is_deleted = 0 and w.status = ?1 and if(?2 != '', u.pay_account like %?2%, 1 = 1) "
			+ "and if(?3 != '', w.update_time >= ?3, 1 = 1) and if(?4 != '', w.update_time <= ?4, 1 = 1) ", nativeQuery = true)
	int countDataByrStatus(byte status, String payAccount, String startTime, String endTime);
	
	@Query(value = "select sum(w.money) from withdraw_cash w left join users u on w.create_user_id = u.id "
			+ "where 1=1 and w.is_deleted = 0 and w.status = ?1 and if(?2 != '', u.pay_account like %?2%, 1 = 1) "
			+ "and if(?3 != '', w.update_time >= ?3, 1 = 1) and if(?4 != '', w.update_time <= ?4, 1 = 1) ", nativeQuery = true)
	Double queryTotalMoneyByStatus(byte status, String payAccount, String startTime, String endTime);

	/**
	 * 通过提现id获取车主提现总记录数
	 * @param status
	 * @param contactName
	 * @param plateNumber
	 * @param sponsorName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Query(value = "select count(wc.id) from cegz.withdraw_cash wc " + 
			" left join cegz.pay_account pa on pa.create_user_id = wc.create_user_id " + 
			" left join cegz.contacts c on wc.create_user_id = c.create_user_id " + 
			" left join cegz.sponsor s on s.create_user_id = wc.create_user_id " + 
			" left join cegz.car_captain cc on cc.create_user_id = wc.create_user_id " + 
			" where wc.type = 3 and wc.is_deleted = 0 and wc.status= ?1 and if(?2 != '', c.name like %?2%, 1 = 1) and if(?3 != '', s.company like %?3%, 1 = 1) "
			+ " and if(?4 != '', wc.create_time >= ?4, 1 = 1) and if(?5 != '', wc.update_time <= ?5, 1 = 1) "
			+ "group by wc.id order by wc.update_time desc ", nativeQuery = true)
	int countWithDrawCashByStatus(Byte status, String contactName, String sponsorName,
			String startTime, String endTime);

	/**
	 * 通过提现id获取车主提现数据列表
	 * @param status
	 * @param startPos
	 * @param pageSize
	 * @param contactName
	 * @param plateNumber
	 * @param sponsorName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Query(value = "select wc.* from cegz.withdraw_cash wc " + 
			" left join cegz.pay_account pa on pa.create_user_id = wc.create_user_id " + 
			" left join cegz.contacts c on wc.create_user_id = c.create_user_id " + 
			" left join cegz.sponsor s on s.create_user_id = wc.create_user_id " + 
			" left join cegz.car_captain cc on cc.create_user_id = wc.create_user_id " + 
			" where wc.type = 3 and wc.is_deleted = 0 and wc.status= ?1 "
			+ " and if(?4 != '', c.name like %?4%, 1 = 1) "
			+ " and if(?5 != '', s.company like %?5%, 1 = 1) "
			+ " and if(?6 != '', wc.create_time >= ?6, 1 = 1) and if(?7 != '', wc.update_time <= ?7, 1 = 1) "
			+ " group by wc.id order by wc.update_time desc limit ?2,?3", nativeQuery = true)
	List<com.cegz.sys.model.adver.WithdrawCash> listWithDrawByStatus(Byte status, int startPos, Integer pageSize, String contactName,
			String sponsorName, String startTime, String endTime);
}
