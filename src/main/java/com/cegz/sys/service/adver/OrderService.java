package com.cegz.sys.service.adver;

import com.cegz.sys.model.adver.Order;

/**
 * 订单后台服务
 * 
 * @author tenglong
 * @date 2018年8月1日
 */
public interface OrderService {

	/**
	 * 保存订单
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月1日
	 */
	int save(Order order);

	/**
	 * 通过广告Id获取订单信息
	 * @param id
	 * @return 
	 */
	Order getOrderByAdvertisementId(Long advertisementId);

}
