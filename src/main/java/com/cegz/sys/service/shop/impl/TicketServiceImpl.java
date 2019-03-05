package com.cegz.sys.service.shop.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.shop.TicketRepository;
import com.cegz.sys.dao.shop.TicketTypeRepository;
import com.cegz.sys.dao.shop.UserTicketRepository;
import com.cegz.sys.model.shop.Ticket;
import com.cegz.sys.model.shop.TicketType;
import com.cegz.sys.model.shop.UserTicket;
import com.cegz.sys.service.shop.TicketService;

/**
 * 优惠券服务
 * 
 * @author tenglong
 * @date 2018年9月5日
 */
@Service("ticketService")
@Transactional
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private TicketTypeRepository ticketTypeRepository;

	@Autowired
	private UserTicketRepository userTicketRepository;

	@Override
	public Long getTicketCount(String name, Long businessId, Long ticketTypeId) {
		return ticketRepository.getTicketCount(name, businessId, ticketTypeId);
	}

	@Override
	public List<Ticket> getTicketList(Integer curPage, Integer pageSize, String name, Long businessId,
			Long ticketTypeId) {
		return ticketRepository.getTicketList(curPage, pageSize, name, businessId, ticketTypeId);
	}

	@Override
	public int saveTicket(Ticket ticket) {
		ticketRepository.save(ticket);
		return 1;
	}

	@Override
	public int saveTicketType(TicketType ticketType) {
		ticketTypeRepository.save(ticketType);
		return 1;
	}

	@Override
	public Long getTicketTypeCount(String name) {
		return ticketTypeRepository.getTicketTypeCount(name);
	}

	@Override
	public List<TicketType> getTicketTypeList(Integer curPage, Integer pageSize, String name) {
		return ticketTypeRepository.getTicketTypeList(curPage, pageSize, name);
	}

	@Override
	public Optional<TicketType> getTicketTypeById(Long id) {
		return ticketTypeRepository.findById(id);
	}

	@Override
	public Optional<Ticket> getTicketById(Long id) {
		return ticketRepository.findById(id);
	}

	@Override
	public List<Ticket> listTicketByAdvertId(Long advertisementId) {
		return ticketRepository.listTicketByAdvertId(advertisementId);
	}

	@Override
	public Long gainTicketUserDetailCount(Long ticketId, String phone) {
		return userTicketRepository.gainTicketUserDetailCount(ticketId, phone);
	}

	@Override
	public List<UserTicket> gainTicketUserDetail(Integer curPage, Integer pageSize, Long ticketId, String phone) {
		return userTicketRepository.gainTicketUserDetail(curPage, pageSize, ticketId, phone);
	}

	@Override
	public List<Ticket> getTicketListByBusinessId(Long businessId) {
		return ticketRepository.getTicketListByBusinessId(businessId);
	}
}
