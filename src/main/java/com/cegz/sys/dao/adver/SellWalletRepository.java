package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.SellWallet;



/**
 * 销售人员账户
 * 
 * @author lijiaxin
 * @date 2018年11月1日
 */
public interface SellWalletRepository extends JpaRepository<SellWallet, Long> {

	@Query(value = "select * from sell_wallet where is_deleted = 0 and create_user_id = ?1 limit 1", nativeQuery = true)
	SellWallet getDataByUser(Long userId);
	
	@Query(value = "select * from sell_wallet where is_deleted = 0 and freeze_money > 0", nativeQuery = true)
	List<SellWallet> listDataByFreeze();
	
	@Query(value = "update sell_wallet set money = money + ?2, freeze_money = freeze_money + ?2, update_time = NOW() where id = ?1", nativeQuery = true)
	@Modifying
	int updateDataMoney(Long id, double money);
}
