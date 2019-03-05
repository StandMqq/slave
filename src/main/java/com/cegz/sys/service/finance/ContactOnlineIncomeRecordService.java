/**
 * 
 */
package com.cegz.sys.service.finance;

import com.cegz.sys.model.adver.ContactOnlineIncomeRecord;

/**
 * 车主在线时长服务类
 * @author yucheng
 * @date 2018年01月03号
 */
public interface ContactOnlineIncomeRecordService {

	/**
	 * 通过车辆id获取车主上周在线时长收益记录
	 * @param id
	 * @param geLastWeekMonday
	 * @return
	 */
	ContactOnlineIncomeRecord queryContactOnlineIncomeRecordByCarId(Long id, String geLastWeekMonday);

}
