
package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.ContactIncomeRecord;

/**
 * 车主收益持久化接口
 * 
 * @author yucheng
 * @date
 */
public interface ContactIncomeRecordRepository extends JpaRepository<ContactIncomeRecord, Long> {

	/**
	 * 通过订单Id获取车主收益列表
	 * 
	 * @param orderId
	 * @param id
	 * @return
	 */
	@Query(value = "select * from contact_income_record cir where 1=1 and cir.is_deleted = 0 and cir.order_id = ?1 and cir.driving_registration_id = ?2", nativeQuery = true)
	List<ContactIncomeRecord> findAllByOrderIdAndCarId(Long orderId, Long carId);

}
