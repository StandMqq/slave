package com.cegz.sys.dao.adver;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.ContactsWallet;

/**
 * 车主钱包持久化类
 * @author yucheng
 * @date 2019年1月8日
 */
@Transactional
public interface ContactsWalletRepository extends JpaRepository<ContactsWallet, Long> {

	/**
	 * 通过车主id，打款失败返回相应金额到车主钱包
	 * @param contactId
	 * @param money
	 * @return
	 */
	@Modifying
	@Query(value = "update contacts_wallet cw set current_money = current_money + ?2 where cw.contacts_id = ?1", nativeQuery = true)
	int updateContactsWalletByContactsId(Long contactId, Double money);

	
}
