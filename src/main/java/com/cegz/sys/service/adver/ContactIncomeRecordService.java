package com.cegz.sys.service.adver;

import java.util.List;

import com.cegz.sys.model.adver.ContactIncomeRecord;

/**
 * 车主收益服务类
 * 
 * @author yucheng
 * @date 2018年12月24号
 */
public interface ContactIncomeRecordService {

	/**
	 * 通过订单Id和车辆id获取车主收益列表
	 * 
	 * @param orderId
	 * @param carId
	 * @return
	 */
	List<ContactIncomeRecord> findListByOrderIdAndCarId(Long orderId, Long carId);


}
