package com.cegz.sys.service.shop.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.shop.IntegralRecordRepository;
import com.cegz.sys.dao.shop.IntegralRepository;
import com.cegz.sys.dao.shop.ShopUsersRepository;
import com.cegz.sys.dao.shop.UserTicketRepository;
import com.cegz.sys.dao.shop.WeChatRepository;
import com.cegz.sys.model.shop.Integral;
import com.cegz.sys.model.shop.IntegralRecord;
import com.cegz.sys.model.shop.UserTicket;
import com.cegz.sys.model.shop.Users;
import com.cegz.sys.model.shop.WeChat;
import com.cegz.sys.service.shop.UsersService;

/**
 * 商户服务
 * 
 * @author tenglong
 * @date 2018年9月18日
 */
@Service("usersService")
@Transactional
public class UsersServiceImpl implements UsersService {

	@Autowired
	private ShopUsersRepository shopUsersRepository;
	@Autowired
	private WeChatRepository weChatRepository;
	@Autowired
	private IntegralRepository integralRepository;
	@Autowired
	private UserTicketRepository userTicketRepository;
	@Autowired
	private IntegralRecordRepository integralRecordRepository;

	@Override
	public Long getUsersCount(String phone, String startTime, String endTime) {
		return shopUsersRepository.getUsersCount(phone, startTime, endTime);
	}

	@Override
	public List<Users> getUsersList(Integer curPage, Integer pageSize, String phone, String startTime, String endTime) {
		return shopUsersRepository.getUsersList(curPage, pageSize, phone, startTime, endTime);
	}

	@Override
	public Optional<Users> getUsersById(Long id) {
		return shopUsersRepository.findById(id);
	}

	@Override
	public WeChat getWeChatByUsersId(Long userId) {
		return weChatRepository.getWeChatByUsersId(userId);
	}

	@Override
	public Integral getIntegralByUsersId(Long userId) {
		return integralRepository.getIntegralByUsersId(userId);
	}

	@Override
	public Long getPullNumByUsersId(Long userId) {
		return userTicketRepository.getPullNumByUsersId(userId);
	}

	@Override
	public Long getUseNumByUsersId(Long userId, byte status) {
		return userTicketRepository.getUseNumByUsersId(userId, status);
	}

	@Override
	public Long getUsersTicketCount(Long usersId) {
		return userTicketRepository.getUsersTicketCount(usersId);
	}

	@Override
	public List<UserTicket> getUsersTicketList(Integer curPage, Integer pageSize, Long usersId) {
		return userTicketRepository.getUsersTicketList(curPage, pageSize, usersId);
	}

	@Override
	public Long getUsersIntegralCount(Long usersId) {
		return integralRecordRepository.getUsersIntegralCount(usersId);
	}

	@Override
	public List<IntegralRecord> getUsersIntegralList(Integer curPage, Integer pageSize, Long usersId) {
		return integralRecordRepository.getUsersIntegralList(curPage, pageSize, usersId);
	}
}
