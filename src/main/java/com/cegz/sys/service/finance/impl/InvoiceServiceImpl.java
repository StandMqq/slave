package com.cegz.sys.service.finance.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.InvoiceRepository;
import com.cegz.sys.model.adver.Invoice;
import com.cegz.sys.service.finance.InvoiceService;

/**
 * 发票
 * 
 * @author tenglong
 * @date 2018年11月20日
 */
@Service("invoiceService")
public class InvoiceServiceImpl implements InvoiceService {
	@Autowired
	private InvoiceRepository invoiceRepository;

	@Override
	public Long queryInvoiceTotalCount(String startTime, String endTime, String company) {
		return invoiceRepository.queryInvoiceTotalCount(startTime, endTime, company);
	}

	@Override
	public List<Invoice> queryInvoiceList(Integer curPage, Integer pageSize, String startTime, String endTime,
			String company) {
		return invoiceRepository.queryInvoiceList(curPage, pageSize, startTime, endTime, company);
	}

	@Override
	public Optional<Invoice> queryInvoiceById(Long id) {
		return invoiceRepository.findById(id);
	}

	@Override
	public int saveInvoice(Invoice invoice) {
		invoiceRepository.save(invoice);
		return 1;
	}

}
