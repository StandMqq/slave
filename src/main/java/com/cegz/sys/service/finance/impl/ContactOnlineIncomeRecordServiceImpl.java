/**
 * 
 */
package com.cegz.sys.service.finance.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.ContactOnlineIncomeRecordRepository;
import com.cegz.sys.model.adver.ContactOnlineIncomeRecord;
import com.cegz.sys.service.finance.ContactOnlineIncomeRecordService;

/**
 * 车主在线时长服务类
 * @author yucheng
 * @date 2018年01月03号
 */
@Service("contactOnlineIncomeRecordService")
public class ContactOnlineIncomeRecordServiceImpl implements ContactOnlineIncomeRecordService {

	@Autowired
	private ContactOnlineIncomeRecordRepository contactOnlineIncomeRecordRepository;
	
	@Override
	public ContactOnlineIncomeRecord queryContactOnlineIncomeRecordByCarId(Long id, String geLastWeekMonday) {
		return contactOnlineIncomeRecordRepository.queryContactOnlineIncomeRecordByCarId(id,geLastWeekMonday);
	}

	
}
