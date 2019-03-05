package com.cegz.sys.dao.adver;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Wallet;

/**
 * 钱包持久化接口
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Transactional
public interface WalletRepository extends JpaRepository<Wallet, Long> {

	/**
	 * 通过钱包中创建者外键查询钱包数据
	 * 
	 * @param id : 创建者id
	 * @return
	 */
	@Query(value = "select * from wallet w where 1=1 ", nativeQuery = true)
	Wallet getWalletByCreateUserIds(Long id);

	/**
	 * 保存钱包数据
	 * 
	 * @param money
	 * @param createUserId
	 * @param updateUserId
	 * @param remark
	 * @return
	 */
	@Modifying
	@Query(value = "insert into wallet(money,create_user_id,update_user_id,remark,is_deleted,create_time,update_time,wallet_no) values(?1,?2,?3,?4,0,now(),now(),?5)", nativeQuery = true)
	int insertWallet(Double money, Long createUserId, Long updateUserId, String remark, String walletNo);

	/**
	 * 修改钱包数据
	 * 
	 * @param money
	 * @param createUserId
	 * @param updateUserId
	 * @param remark
	 * @return
	 */
	@Modifying
	@Query(value = "update wallet set money = ?2, update_user_id = ?3, update_time = now() where id = ?1", nativeQuery = true)
	int editWallet(Long id, Double money, Long updateUserId);

	/**
	 * 查询钱包数据总条数
	 */
	@Query(value = "select count(1) from wallet w left join users u on w.create_user_id = u.id where 1=1 and w.is_deleted = 0 "
			+ "and if(?1 != '', u.phone like %?1%, 1 = 1) "
			+ "and if(?2 != '', w.update_time >= ?2, 1 = 1) and if(?3 != '', w.update_time <= ?3, 1 = 1) ", nativeQuery = true)
	Long queryWalletTotalCount(String phone, String startTime, String endTime);

	/**
	 * 查询钱包数据列表
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param status
	 * @param name
	 * @param phone
	 * @return
	 */
	@Query(value = "select * from wallet w left join users u on w.create_user_id = u.id where 1=1 and w.is_deleted = 0 "
			+ "and if(?3 != '', u.phone like %?3%, 1 = 1) "
			+ "and if(?4 != '', w.update_time >= ?4, 1 = 1) and if(?5 != '', w.update_time <= ?5, 1 = 1) "
			+ "order by w.update_time desc limit ?1, ?2", nativeQuery = true)
	List<Wallet> queryWalletList(Integer curPage, Integer pageSize, String phone, String startTime, String endTime);
}
