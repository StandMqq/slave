
package com.cegz.sys.service.finance.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.ContactsWalletRepository;

/**
 * 车主钱包服务类
 * @author yucheng
 * @date 2019年1月8号
 */
@Service("contactsWalletServiceService")
public class ContactsWalletService implements com.cegz.sys.service.finance.ContactsWalletService {

	@Autowired
	private ContactsWalletRepository contactsWalletRepository;
	
	
	@Override
	public int updateContactsWalletByContactsId(Long contactId, Double money) {
		return contactsWalletRepository.updateContactsWalletByContactsId(contactId, money);
	}

	
}
