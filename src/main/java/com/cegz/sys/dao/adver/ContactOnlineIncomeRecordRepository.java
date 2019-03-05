/**
 * 
 */
package com.cegz.sys.dao.adver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.ContactOnlineIncomeRecord;

/**
 * 车主在线时长持久化接口
 * @author yucheng
 * @date 2019年01月03号
 */
public interface ContactOnlineIncomeRecordRepository extends JpaRepository<ContactOnlineIncomeRecord, Long> {

	/**
	 * 通过车辆id获取车主上周在线时长收益记录
	 * @param id
	 * @param geLastWeekMonday 
	 * @return
	 */
	@Query(value = "select coir.* from contact_online_income_record coir where 1=1 and coir.is_deleted = 0"
			+ " and coir.driving_registration_id = ?1 and date_format(coir.create_time,'%Y-%m-%d')= ?2", nativeQuery = true)
	ContactOnlineIncomeRecord queryContactOnlineIncomeRecordByCarId(Long id, String geLastWeekMonday);

}
