package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Invoice;

/**
 * 发票
 * 
 * @author tenglong
 * @date 2018年11月20日
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	/**
	 * 查询发票数据总条数
	 */
	@Query(value = "select count(1) from invoice i where 1=1 and i.is_deleted = 0 "
			+ "and if(?1 != '', i.create_time >= ?1, 1 = 1) and if(?2 != '', i.create_time <= ?2, 1 = 1) "
			+ "and if(?3 != '', i.company like %?3%, 1 = 1) ", nativeQuery = true)
	Long queryInvoiceTotalCount(String startTime, String endTime, String company);

	/**
	 * 查询发票数据列表
	 */
	@Query(value = "select * from invoice i where 1=1 and i.is_deleted = 0 "
			+ "and if(?3 != '', i.create_time >= ?3, 1 = 1) and if(?4 != '', i.create_time <= ?4, 1 = 1) "
			+ "and if(?5 != '', i.company like %?5%, 1 = 1) "
			+ "order by i.create_time desc limit ?1, ?2", nativeQuery = true)
	List<Invoice> queryInvoiceList(Integer curPage, Integer pageSize, String startTime, String endTime, String company);

}
