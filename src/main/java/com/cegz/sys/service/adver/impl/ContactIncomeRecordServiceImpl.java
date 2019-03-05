package com.cegz.sys.service.adver.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.ContactIncomeRecordRepository;
import com.cegz.sys.model.adver.ContactIncomeRecord;
import com.cegz.sys.service.adver.ContactIncomeRecordService;

/**
 * 车主收益服务类
 * 
 * @author yucheng
 * @date 2018年12月24号
 */
@Service("contactIncomeRecordService")
public class ContactIncomeRecordServiceImpl implements ContactIncomeRecordService {

	@Autowired
	private ContactIncomeRecordRepository contactIncomeRecordRepository;
	


	@Override
	public List<ContactIncomeRecord> findListByOrderIdAndCarId(Long orderId, Long carId) {
		return contactIncomeRecordRepository.findAllByOrderIdAndCarId(orderId, carId);
	}


}
