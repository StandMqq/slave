package com.cegz.sys.service.adver.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.OrderRepository;
import com.cegz.sys.model.adver.Order;
import com.cegz.sys.service.adver.OrderService;

/**
 * 订单后台服务
 * 
 * @author tenglong
 * @date 2018年8月1日
 */
@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public int save(Order order) {
		orderRepository.save(order);
		return 1;
	}

	@Override
	public Order getOrderByAdvertisementId(Long advertisementId) {
		Order order = orderRepository.getOrderByAdvertisementId(advertisementId);
		return order;
	}

}
