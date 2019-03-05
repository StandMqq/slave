package com.cegz.sys.service.sale.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.CheckMoneyRepository;
import com.cegz.sys.dao.adver.SaleRepository;
import com.cegz.sys.dao.adver.SellWalletRepository;
import com.cegz.sys.model.adver.CheckMoney;
import com.cegz.sys.model.adver.SellBindUser;
import com.cegz.sys.model.adver.SellWallet;
import com.cegz.sys.service.sale.SaleBindService;

/**
 * 销售绑定服务
 * 
 * @author tenglong
 * @date 2018年10月18日
 */
@Service("saleBindService")
@Transactional
public class SaleBindServiceImpl implements SaleBindService {

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private CheckMoneyRepository checkMoneyRepository;
	
	@Autowired
	private SellWalletRepository sellWalletRepository;

	@Override
	public Optional<SellBindUser> getSellBindUserById(Long id) {
		return saleRepository.findById(id);
	}

	@Override
	public Long getSaleBindCount(Long userId, String phone) {
		return saleRepository.getSaleBindCount(userId, phone);
	}

	@Override
	public List<SellBindUser> getSaleBindList(Integer curPage, Integer pageSize, Long userId, String phone) {
		return saleRepository.getSaleBindList(curPage, pageSize, userId, phone);
	}

	@Override
	public Long querySaleBindNum(Long sellUserId) {
		return saleRepository.querySaleBindNum(sellUserId);
	}

	@Override
	public Long getBindClientsCount(Long sellUserId) {
		return saleRepository.getBindClientsCount(sellUserId);
	}

	@Override
	public List<SellBindUser> getBindClientsList(Integer curPage, Integer pageSize, Long sellUserId) {
		return saleRepository.getBindClientsList(curPage, pageSize, sellUserId);
	}

	@Override
	public Double queryBindAccountRechargeMoney(Long userId) {
		return checkMoneyRepository.getRechargeMoney(userId);
	}

	@Override
	public CheckMoney queryBindAccountLastRechargeTime(Long userId) {
		List<CheckMoney> list = checkMoneyRepository.queryCheckMoneyListByCreateUserId(0, 1, userId, null, null,null,null);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public SellBindUser getSellBindByUser(Long userId) {
		return saleRepository.getDataByUser(userId);
	}

	@Override
	public SellWallet insertSellWallet(SellWallet sellWallet) {
		return sellWalletRepository.save(sellWallet);
	}

	@Override
	public SellWallet getSellWalletByUser(Long useId) {
		return sellWalletRepository.getDataByUser(useId);
	}

	@Override
	public int addSellWalleMoney(Long walletId, double money) {
		return sellWalletRepository.updateDataMoney(walletId, money);
	}
}
