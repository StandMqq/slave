package com.cegz.sys.service.adver.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.WithdrawCashRepository;
import com.cegz.sys.dao.shop.PacketWithdrawCashRepository;
import com.cegz.sys.dao.shop.UserRedPacketRepository;
import com.cegz.sys.model.adver.WithdrawCash;
import com.cegz.sys.service.adver.AdverWithdrawCashService;

/**
 * 提现服务
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
@Service("adverWithdrawCashService")
@Transactional
public class AdverWithdrawCashServiceImpl implements AdverWithdrawCashService {

	@Autowired
	private PacketWithdrawCashRepository packetCashRepository;

	@Autowired
	private UserRedPacketRepository uprRepository;
	
	@Autowired
	private WithdrawCashRepository withdrawCashRepository;


	@Override
	public WithdrawCash saveWithdrawCash(WithdrawCash withdrawCash) {
		return withdrawCashRepository.save(withdrawCash);
	}

	@Override
	public Optional<WithdrawCash> getWithdrawCashById(Long id) {
		return withdrawCashRepository.findById(id);
	}

	@Override
	public int updateUserPacketStatusByCash(Long cashId, byte status) {
		return uprRepository.updateDataByCash(cashId, status);
	}


	@Override
	public Long countWithDrawCashByStatus(Byte status, String contactName, String sponsorName,
			String startTime, String endTime) {
		return withdrawCashRepository.countWithDrawCashByStatus(status, contactName, sponsorName, startTime, endTime);
	}

	@Override
	public List<com.cegz.sys.model.adver.WithdrawCash> listWithDrawByStatus(Byte status, int startPos, Integer pageSize, String contactName,
			String sponsorName, String startTime, String endTime) {
		return withdrawCashRepository.listWithDrawByStatus(status, startPos, pageSize, contactName,sponsorName, startTime, endTime);
		}

	@Override
	public double queryTotalMoneyByStatus(byte status) {
		return withdrawCashRepository.queryTotalMoneyByStatus(status);
	}

}
