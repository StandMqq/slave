package com.cegz.sys.service.finance;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.Invoice;

/**
 * 发票
 * 
 * @author tenglong
 * @date 2018年11月20日
 */

public interface InvoiceService {

	/**
	 * 保存发票
	 * 
	 * @param invoice
	 * @return
	 */
	int saveInvoice(Invoice invoice);

	Long queryInvoiceTotalCount(String startTime, String endTime, String company);

	/**
	 * 发票数据列表
	 */
	List<Invoice> queryInvoiceList(Integer curPage, Integer pageSize, String startTime, String endTime, String company);

	Optional<Invoice> queryInvoiceById(Long id);

}
