package com.cegz.sys.service.shop.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.shop.RedPacketRepository;
import com.cegz.sys.dao.shop.UserRedPacketRepository;
import com.cegz.sys.model.shop.RedPacket;
import com.cegz.sys.model.shop.UserRedPacket;
import com.cegz.sys.service.shop.RedPacketService;

/**
 * 红包服务
 * 
 * @author tenglong
 * @date 2018年9月5日
 */
@Service("redPacketService")
@Transactional
public class RedPacketServiceImpl implements RedPacketService {

	@Autowired
	private RedPacketRepository redPacketRepository;

	@Autowired
	private UserRedPacketRepository userRedPacketRepository;

	@Override
	public Long getUserReceiveRedPacketCount(Long adverId) {
		return userRedPacketRepository.getUserReceiveRedPacketCount(adverId);
	}

	@Override
	public List<UserRedPacket> getUserReceiveRedPacketList(Integer curPage, Integer pageSize, Long adverId) {
		return userRedPacketRepository.getUserReceiveRedPacketList(curPage, pageSize, adverId);
	}

	@Override
	public Long getRedPacketReceiveNum(Long adverId, int type) {
		return userRedPacketRepository.getRedPacketReceiveNum(adverId, type);
	}

	@Override
	public Long getRedPacketTotalNum(Long adverId, int type) {
		return redPacketRepository.getRedPacketTotalNum(adverId, type);
	}

	@Override
	public Optional<UserRedPacket> getUserReceiveRedPacketById(Long userRedPacketId) {
		return userRedPacketRepository.findById(userRedPacketId);
	}

	@Override
	public int financePayConfirm(Long userRedPacketId) {
		return userRedPacketRepository.financePayConfirm(userRedPacketId);
	}

	@Override
	public Optional<RedPacket> getRedPacketById(Long redPacketId) {
		return redPacketRepository.findById(redPacketId);
	}

	@Override
	public Long getRedPacketCount(int i ,String name) {
		
		return redPacketRepository.getRedPacketCount(i,name);
	}

	@Override
	public List<RedPacket> getRedPacketList(Integer startPage, Integer pageSize,int i, String name) {
		return redPacketRepository.getRedPacketList(startPage,pageSize,i,name);
	}

	@Override
	public RedPacket getRedPacketByAdvertisementId(Long advertisementId,int type) {
		return redPacketRepository.getRedPacketByAdvertisementId(advertisementId,type);
	}
}
