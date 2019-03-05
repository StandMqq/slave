package com.cegz.sys.dao.adver;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.Travel;

/**
 * 车辆移动持久化接口
 * 
 * @author tenglong
 * @date 2018年11月15日
 */
@Transactional
public interface TravelRepository extends JpaRepository<Travel, Long> {

	/**
	 * 查询开机时长
	 * 
	 * @author tenglong
	 * @date 2018年11月15日
	 * @param imeis: 设备号
	 * @param createTime: 查询日期，选传
	 */
	@Query(value = "select t.* from travel t where 1=1 and t.is_deleted = 0 "
			+ "and t.imei = ?1 and if(?2 != '', date_format(t.create_time,'%Y-%m-%d') = ?2, 1 = 1) ", nativeQuery = true)
	List<Travel> queryOpenTimeLength(String imeis, String createTime);

	/**
	 * 查询在线天数
	 * 
	 * @author tenglong
	 * @date 2018年11月15日
	 * @param imeis: 设备号
	 */
	@Query(value = "select date_format(t.create_time,'%Y-%m-%d') as createTime from travel t where 1=1 and t.is_deleted = 0 "
			+ "and t.imei = ?1 group by createTime ", nativeQuery = true)
	List<String> queryOnLineDay(String imeis);

	/**
	 * 查询设备当月开机时长
	 * @param imei
	 * @return
	 */
	@Query(value = "select t.* from travel t where 1=1 and t.is_deleted = 0 "
			+ "and t.imei = ?1 and date_format(t.create_time,'%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') ", nativeQuery = true)
	List<Travel> queryOpenTimeLength(String imei);

	/**
	 * 查询设备上周的开机时长
	 * @param imei
	 * @param currentDate
	 * @return
	 */
	@Query(value = "select t.* from travel t where 1=1 and t.is_deleted = 0 "
			+ "and t.imei = ?1 and YEARWEEK(date_format(t.create_time,'%Y-%m-%d')) = YEARWEEK(now())-1 ", nativeQuery = true)
	List<Travel> getLastWeekOpenTimeLengths(String imei, String currentDate);
}
