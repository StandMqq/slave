package com.cegz.sys.service.sale.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.WithdrawCashRepository;
import com.cegz.sys.model.adver.WithdrawCash;
import com.cegz.sys.service.sale.WithdrawCashService;

/**
 * 提现服务
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
@Service("withdrawCashService")
@Transactional
public class WithdrawCashServiceImpl implements WithdrawCashService {

	@Autowired
	private WithdrawCashRepository withdrawCashRepository;

	@Override
	public Long getWithdrawCashCount(String bankCardName, String openBankCardName, String alipayName,
			String alipayAccount) {
		return withdrawCashRepository.getWithdrawCashCount(bankCardName, openBankCardName, alipayName, alipayAccount);
	}

	@Override
	public List<WithdrawCash> getWithdrawCashList(Integer curPage, Integer pageSize, String bankCardName,
			String openBankCardName, String alipayName, String alipayAccount) {
		return withdrawCashRepository.getWithdrawCashList(curPage, pageSize, bankCardName, openBankCardName, alipayName,
				alipayAccount);
	}

	@Override
	public int saveWithdrawCash(WithdrawCash withdrawCash) {
		withdrawCashRepository.save(withdrawCash);
		return 1;
	}

	@Override
	public Optional<WithdrawCash> getWithdrawCashById(Long id) {
		return withdrawCashRepository.findById(id);
	}
	
}
