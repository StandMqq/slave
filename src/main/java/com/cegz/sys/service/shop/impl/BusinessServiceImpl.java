package com.cegz.sys.service.shop.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.shop.BusinessRepository;
import com.cegz.sys.model.shop.Business;
import com.cegz.sys.service.shop.BusinessService;

/**
 * 商户服务
 * 
 * @author tenglong
 * @date 2018年9月18日
 */
@Service("businessService")
@Transactional
public class BusinessServiceImpl implements BusinessService {

	@Autowired
	private BusinessRepository businessRepository;

	@Override
	public int saveBusiness(Business business) {
		businessRepository.save(business);
		return 1;
	}

	@Override
	public Long getBusinessCount(String name) {
		return businessRepository.getBusinessCount(name);
	}

	@Override
	public List<Business> getBusinessList(Integer curPage, Integer pageSize, String name) {
		return businessRepository.getBusinessList(curPage, pageSize, name);
	}

	@Override
	public Optional<Business> getBusinessById(Long id) {
		return businessRepository.findById(id);
	}
}
