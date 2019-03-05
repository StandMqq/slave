package com.cegz.sys.service.shop;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.shop.Ticket;
import com.cegz.sys.model.shop.TicketType;
import com.cegz.sys.model.shop.UserTicket;

/**
 * 优惠券服务
 * 
 * @author tenglong
 * @date 2018年9月5日
 */
public interface TicketService {
	/**
	 * 查询所有劵数据总条数
	 * 
	 * @param name
	 * @return
	 */

	Long getTicketCount(String name, Long businessId, Long ticketTypeId);

	/**
	 * 获取所有劵数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */
	List<Ticket> getTicketList(Integer curPage, Integer pageSize, String name, Long businessId, Long ticketTypeId);

	/**
	 * 查询所有劵类型数据总条数
	 * 
	 * @param name
	 * @return
	 */

	Long getTicketTypeCount(String name);

	/**
	 * 获取所有劵类型数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */
	List<TicketType> getTicketTypeList(Integer curPage, Integer pageSize, String name);

	/**
	 * 保存优惠券
	 * 
	 * @param ticket
	 * @return
	 */
	int saveTicket(Ticket ticket);

	/**
	 * 保存优惠券类型
	 * 
	 * @param ticketType
	 * @return
	 */
	int saveTicketType(TicketType ticketType);

	/**
	 * 通过id获取劵信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<Ticket> getTicketById(Long id);

	/**
	 * 通过id获取劵类型信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<TicketType> getTicketTypeById(Long id);

	/**
	 * 通过广告id查询广告下关联的劵
	 * 
	 * @param advertisementId
	 * @return
	 */
	List<Ticket> listTicketByAdvertId(Long advertisementId);

	/**
	 * 通过劵id获取领取人详情总数
	 * 
	 * @param ticketId
	 * @param phone
	 * @return
	 */
	Long gainTicketUserDetailCount(Long ticketId, String phone);

	/**
	 * 通过劵id获取领取人详情列表
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param ticketId
	 * @param phone
	 * @return
	 */
	List<UserTicket> gainTicketUserDetail(Integer curPage, Integer pageSize, Long ticketId, String phone);

	/**
	 * 通过商户id获取所有劵数据
	 * 
	 * @param businessId
	 * @return
	 */
	List<Ticket> getTicketListByBusinessId(Long businessId);
}
