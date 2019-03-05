package com.cegz.sys.service.shop.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.shop.IntegralGoodsRepository;
import com.cegz.sys.model.shop.IntegralGoods;
import com.cegz.sys.service.shop.IntegralGoodsService;

/**
 * 积分商品服务
 * 
 * @author tenglong
 * @date 2018年11月29日
 */
@Service("integralGoodsService")
@Transactional
public class IntegralGoodsServiceImpl implements IntegralGoodsService {

	@Autowired
	private IntegralGoodsRepository integralGoodsRepository;

	@Override
	public int saveIntegralGoods(IntegralGoods integralGoods) {
		integralGoodsRepository.save(integralGoods);
		return 1;
	}

	@Override
	public Long getIntegralGoodsCount(String name, String isDeleted) {
		return integralGoodsRepository.getIntegralGoodsCount(name, isDeleted);
	}

	@Override
	public List<IntegralGoods> getIntegralGoodsList(Integer curPage, Integer pageSize, String name, String isDeleted) {
		return integralGoodsRepository.getIntegralGoodsList(curPage, pageSize, name, isDeleted);
	}

	@Override
	public Optional<IntegralGoods> getIntegralGoodsById(Long id) {
		return integralGoodsRepository.findById(id);
	}
}
