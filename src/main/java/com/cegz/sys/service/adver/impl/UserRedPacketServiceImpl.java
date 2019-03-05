/**
 * 
 */
package com.cegz.sys.service.adver.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.shop.UserRedPacketRepository;
import com.cegz.sys.model.shop.UserRedPacket;
import com.cegz.sys.service.adver.UserRedPacketService;

/**
 * 用户红包服务
 * @author yucheng
 * @date 2018年12月14日
 */
@Service("UserRedPacketService")
public class UserRedPacketServiceImpl implements UserRedPacketService {
	
	@Autowired
	private UserRedPacketRepository userRedPacketRepository;

	@Override
	public Long getUserRedPacketCountByRedPcaketId(Long id) {
		return userRedPacketRepository.getUserRedPacketCountByRedPcaketId(id);
	}

	@Override
	public List<UserRedPacket> getUserRedPacketListByRedPcaketId(Integer curPage, Integer pageSize, Long id) {
		return userRedPacketRepository.getUserRedPacketCountByRedPcaketId(curPage,pageSize,id);
	}

}
