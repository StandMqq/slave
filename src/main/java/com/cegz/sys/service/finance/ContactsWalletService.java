
package com.cegz.sys.service.finance;

/**
 * 车主钱包服务类
 * @author yucheng
 * @date 2019年1月8号
 */
public interface ContactsWalletService {

	/**
	 * 通过车主id，打款失败返回相应金额到车主钱包
	 * @param id
	 * @param money
	 * @return
	 */
	int updateContactsWalletByContactsId(Long id, Double money);

	
}
