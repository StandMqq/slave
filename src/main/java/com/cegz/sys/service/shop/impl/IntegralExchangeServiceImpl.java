/**
 * 
 */
package com.cegz.sys.service.shop.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.shop.IntegralGoodsRecordRepository;
import com.cegz.sys.dao.shop.IntegralRepository;
import com.cegz.sys.model.shop.Integral;
import com.cegz.sys.model.shop.IntegralGoodsRecord;
import com.cegz.sys.service.shop.IntegralExchangeService;

/**
 * 积分兑换服务
 * @author yucheng
 * @date 2018年11月29号
 */
@Service("integralExchangeService")
@Transactional
public class IntegralExchangeServiceImpl implements IntegralExchangeService {

	@Autowired
	private IntegralGoodsRecordRepository integralGoodsRecordRepository;
	
	@Autowired
	private IntegralRepository integralRepository;
	
	@Override
	public List<IntegralGoodsRecord> queryIntegralGoodsRecordList(Integer startPage, Integer pageSize, String phone,
			String name, String typeNo) {
		
		return integralGoodsRecordRepository.queryIntegralGoodsRecordList(startPage,pageSize,phone,name,typeNo);
	}

	@Override
	public Integral queryIntegralByCreatUserId(Long userId) {
		return integralRepository.getIntegralByUsersId(userId);
	}

	@Override
	public Optional<IntegralGoodsRecord> getIntegralGoodsRecordById(Long id) {
		return integralGoodsRecordRepository.findById(id);
	}

	@Override
	public int save(IntegralGoodsRecord integralGoodsRecord) {
		integralGoodsRecordRepository.save(integralGoodsRecord);
		return 1;
	}

}
