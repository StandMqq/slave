package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.Ticket;

/**
 * 优惠券持久化服务
 * 
 * @author tenglong
 * @date 2018年9月5日
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	/**
	 * 获取所有劵数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@Query(value = "select * from ticket t where 1=1 "
			+ "and if(?3 != '', t.name like %?3%, 1 = 1) and if(?4 != '', t.business_id = ?4, 1 = 1) "
			+ "and if(?5 != '', t.type_id = ?5, 1 = 1) order by t.is_deleted, t.create_time desc limit ?1, ?2 ", nativeQuery = true)
	List<Ticket> getTicketList(Integer curPage, Integer pageSize, String name, Long businessId, Long ticketTypeId);

	/**
	 * 查询所有劵数据总条数
	 * 
	 * @param name
	 * @return
	 */
	@Query(value = "select count(1) from ticket t where 1=1 "
			+ "and if(?1 != '', t.name like %?1%, 1 = 1) and if(?2 != '', t.business_id = ?2, 1 = 1) "
			+ "and if(?3 != '', t.type_id = ?3, 1 = 1) ", nativeQuery = true)
	Long getTicketCount(String name, Long businessId, Long ticketTypeId);

	/**
	 * 通过广告id查询广告下关联的劵
	 * 
	 * @param advertisementId
	 * @return
	 */
	@Query(value = "select distinct t.* from ticket t "
			+ "left join ticket_advertisement_order tao on t.id = tao.ticket_id "
			+ "where 1=1 and t.is_deleted = 0 and tao.advertisement_id = ?1 order by t.create_time desc ", nativeQuery = true)
	List<Ticket> listTicketByAdvertId(Long advertisementId);

	/**
	 * 通过商户id获取所有劵数据
	 * 
	 * @param businessId
	 * @return
	 */
	@Query(value = "select * from ticket t "
			+ "where 1=1 and t.business_id = ?1 order by t.is_deleted, t.create_time desc ", nativeQuery = true)
	List<Ticket> getTicketListByBusinessId(Long businessId);

}
