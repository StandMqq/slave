package com.cegz.sys.service.shop.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.shop.PacketWithdrawCashRepository;
import com.cegz.sys.dao.shop.UserRedPacketRepository;
import com.cegz.sys.model.shop.UserRedPacket;
import com.cegz.sys.model.shop.WithdrawCash;
import com.cegz.sys.service.shop.WithdrawCashService;

/**
 * 提现服务
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
@Service("shopWithdrawCashService")
@Transactional
public class WithdrawCashServiceImpl implements WithdrawCashService {

	@Autowired
	private PacketWithdrawCashRepository packetCashRepository;

	@Autowired
	private UserRedPacketRepository uprRepository;

	@Override
	public Double queryTotalMoneyByStatus(byte status, String payAccount, String startTime, String endTime) {
		return packetCashRepository.queryTotalMoneyByStatus(status, payAccount, startTime, endTime);
	}
	
	@Override
	public int countCashByStatus(byte status, String payAccount, String startTime, String endTime) {
		return packetCashRepository.countDataByrStatus(status, payAccount, startTime, endTime);
	}

	@Override
	public List<com.cegz.sys.model.shop.WithdrawCash> listCashByStatus(byte status, int startPos, int size,
			String payAccount, String startTime, String endTime) {
		return packetCashRepository.listDataByStatus(status, startPos, size, payAccount, startTime, endTime);
	}

	@Override
	public WithdrawCash saveWithdrawCash(WithdrawCash withdrawCash) {
		return packetCashRepository.save(withdrawCash);
	}

	@Override
	public Optional<WithdrawCash> getWithdrawCashById(Long id) {
		return packetCashRepository.findById(id);
	}

	@Override
	public int updateUserPacketStatusByCash(Long cashId, byte status) {
		return uprRepository.updateDataByCash(cashId, status);
	}

	@Override
	public int countUserRedPacketByCashId(Long cashId, String startTime, String endTime) {
		return uprRepository.countUserRedPacketByCashId(cashId, startTime, endTime);
	}

	@Override
	public List<UserRedPacket> listUserRedPacketByCashId(int startPos, int pageSize, Long cashId, String startTime,
			String endTime) {
		return uprRepository.listUserRedPacketByCashId(startPos, pageSize, cashId, startTime, endTime);
	}

	@Override
	public int countWithDrawCashByStatus(Byte status, String contactName, String sponsorName,
			String startTime, String endTime) {
		return packetCashRepository.countWithDrawCashByStatus(status, contactName, sponsorName, startTime, endTime);
	}

	@Override
	public List<com.cegz.sys.model.adver.WithdrawCash> listWithDrawByStatus(Byte status, int startPos, Integer pageSize, String contactName,
			String sponsorName, String startTime, String endTime) {
		return packetCashRepository.listWithDrawByStatus(status, startPos, pageSize, contactName,sponsorName, startTime, endTime);
		}
}
