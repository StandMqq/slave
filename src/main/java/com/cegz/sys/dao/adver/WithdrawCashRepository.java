package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.WithdrawCash;

/**
 * 
 * 提现账单
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
public interface WithdrawCashRepository extends JpaRepository<WithdrawCash, Long> {
	@Query(value = "select count(wc.id) from withdraw_cash wc "
			+ "left join pay_account pa on pa.create_user_id = wc.create_user_id and wc.type = 1 "
			+ "left join bank_card bc on bc.create_user_id = wc.create_user_id and wc.type = 2 "
			+ "where 1=1 and wc.is_deleted = 0 and (wc.type = 1 or wc.type = 2) "
			+ "and if(?1 != '', bc.name like %?1%, 1 = 1) and if(?2 != '', bc.bank like %?2%, 1 = 1) "
			+ "and if(?3 != '', pa.name like %?3%, 1 = 1) and if(?4 != '', pa.pay_account like %?4%, 1 = 1) "
			+ "order by wc.create_time desc ", nativeQuery = true)
	Long getWithdrawCashCount(String bankCardName, String openBankCardName, String alipayName, String alipayAccount);

	@Query(value = "select wc.* from withdraw_cash wc "
			+ "left join pay_account pa on pa.create_user_id = wc.create_user_id and wc.type = 1 "
			+ "left join bank_card bc on bc.create_user_id = wc.create_user_id and wc.type = 2 "
			+ "where 1=1 and wc.is_deleted = 0  and (wc.type = 1 or wc.type = 2) "
			+ "and if(?3 != '', bc.name like %?3%, 1 = 1) and if(?4 != '', bc.bank like %?4%, 1 = 1) "
			+ "and if(?5 != '', pa.name like %?5%, 1 = 1) and if(?6 != '', pa.pay_account like %?6%, 1 = 1) "
			+ "order by wc.create_time desc limit ?1,?2", nativeQuery = true)
	List<WithdrawCash> getWithdrawCashList(Integer curPage, Integer pageSize, String bankCardName,
			String openBankCardName, String alipayName, String alipayAccount);

	/**
	 * 通过提现id获取车主提现总记录数
	 * @param status
	 * @param contactName
	 * * @param sponsorName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Query(value = "select count(*) from (select wc.* from cegz.withdraw_cash wc " + 
			" left join cegz.pay_account pa on pa.create_user_id = wc.create_user_id " + 
			" left join cegz.contacts c on wc.create_user_id = c.create_user_id " + 
			" left join cegz.sponsor s on s.create_user_id = wc.create_user_id " + 
			" left join cegz.car_captain cc on wc.create_user_id = cc.create_user_id " + 
			" where wc.type = 3 and wc.is_deleted = 0 and wc.status= ?1 and if(?2 != '', c.name like %?2%, 1 = 1) and if(?3 != '', s.company like %?3%, 1 = 1) "
			+ " and if(?4 != '', wc.create_time >= ?4, 1 = 1) and if(?5 != '', wc.update_time < ?5, 1 = 1) "
			+ " group by wc.id ) p ", nativeQuery = true)
	Long countWithDrawCashByStatus(Byte status, String contactName, String sponsorName,
			String startTime, String endTime);

	/**
	 * 通过提现id获取车主提现数据列表
	 * @param status
	 * @param startPos
	 * @param pageSize
	 * @param contactName
	 * @param sponsorName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Query(value = "select wc.* from cegz.withdraw_cash wc " + 
			" left join cegz.pay_account pa on pa.create_user_id = wc.create_user_id " + 
			" left join cegz.contacts c on wc.create_user_id = c.create_user_id " + 
			" left join cegz.sponsor s on s.create_user_id = wc.create_user_id " + 
			" left join cegz.car_captain cc on wc.create_user_id = cc.create_user_id " + 
			" where wc.type = 3 and wc.is_deleted = 0 and wc.status= ?1 "
			+ " and if(?4 != '', c.name like %?4%, 1 = 1) "
			+ " and if(?5 != '', s.company like %?5%, 1 = 1) "
			+ " and if(?6 != '', wc.create_time >= ?6, 1 = 1) and if(?7 != '', wc.update_time < ?7, 1 = 1) "
			+ " group by wc.id order by wc.create_time desc limit ?2,?3", nativeQuery = true)
	List<com.cegz.sys.model.adver.WithdrawCash> listWithDrawByStatus(Byte status, int startPos, Integer pageSize, String contactName,
			String sponsorName, String startTime, String endTime);

	/**
	 * 根据状态查询提现总金额
	 * @param status
	 * @return
	 */
	@Query(value = "select sum(money) from withdraw_cash where status = ?1 and is_deleted = 0 and type = 3",nativeQuery = true)
	double queryTotalMoneyByStatus(byte status);
}
