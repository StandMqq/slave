package com.cegz.sys.service.adver;

import java.util.List;

import com.cegz.sys.model.adver.Travel;

/**
 * 车辆移动后台服务
 * 
 * @author tenglong
 * @date 2018年11月15日
 */
public interface TravelService {

	/**
	 * 查询开机时长
	 * 
	 * @author tenglong
	 * @date 2018年11月15日
	 * @param imeis: 设备号
	 * @param createTime: 查询日期，选传
	 */
	List<Travel> queryOpenTimeLength(String imeis, String createTime);

	/**
	 * 查询在线天数
	 * 
	 * @author tenglong
	 * @date 2018年11月15日
	 * @param imeis: 设备号
	 */
	List<String> queryOnLineDay(String imeis);

	/**
	 * 查询设备当月开机时长
	 * @param imei
	 * @return
	 */
	List<Travel> queryOpenTimeLength(String imei);

	/**
	 * 查询设备上周的开机时长
	 * @param imei
	 * @param currentDate
	 * @return
	 */
	List<Travel> getLastWeekOpenTimeLengths(String imei, String currentDate);

}
