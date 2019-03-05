package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.UserRedPacket;

/**
 * 红包持久化接口
 * 
 * @author tenglong
 * @date 2018年10月11日
 */
public interface UserRedPacketRepository extends JpaRepository<UserRedPacket, Long> {
	@Query(value = "select count(urp.id) from user_red_packet urp left join red_packet rp on rp.id = urp.red_packet_id "
			+ "where 1=1 and rp.is_deleted = 0 and rp.adver_id = ?1", nativeQuery = true)
	Long getUserReceiveRedPacketCount(Long adverId);

	@Query(value = "select urp.* from user_red_packet urp left join red_packet rp on rp.id = urp.red_packet_id "
			+ "where 1=1 and rp.is_deleted = 0 and rp.adver_id = ?3 order by urp.create_time desc limit ?1,?2", nativeQuery = true)
	List<UserRedPacket> getUserReceiveRedPacketList(int startPos, int size, Long adverId);

	@Query(value = "select count(urp.id) from user_red_packet urp left join red_packet rp on rp.id = urp.red_packet_id "
			+ "where 1=1 and rp.is_deleted = 0 and rp.adver_id = ?1 and rp.type = ?2", nativeQuery = true)
	Long getRedPacketReceiveNum(Long adverId, int type);

	/**
	 * 财务修改领取红包打款状态
	 * 
	 * @param userRedPacketId 红包领取数据id
	 * @return
	 */
	@Modifying
	@Query(value = "update user_red_packet set status = 1 where id = ?1", nativeQuery = true)
	int financePayConfirm(Long userRedPacketId);

	@Modifying
	@Query(value = "update user_red_packet set status = ?2 where cash_id = ?1 and is_deleted = 0", nativeQuery = true)
	int updateDataByCash(Long cashId, byte status);

	@Query(value = "select count(urp.id) from user_red_packet urp left join withdraw_cash wc on urp.cash_id = wc.id "
			+ "where 1=1 and wc.is_deleted = 0 and urp.is_deleted = 0 and wc.id = ?1 "
			+ "and if(?2 != '', wc.create_time >= ?2, 1 = 1) and if(?3 != '', wc.create_time <= ?3, 1 = 1) order by wc.create_time desc", nativeQuery = true)
	int countUserRedPacketByCashId(Long cashId, String startTime, String endTime);

	@Query(value = "select urp.* from user_red_packet urp left join withdraw_cash wc on urp.cash_id = wc.id "
			+ "where 1=1 and wc.is_deleted = 0 and urp.is_deleted = 0 and wc.id = ?3 "
			+ "and if(?4 != '', wc.create_time >= ?4, 1 = 1) and if(?5 != '', wc.create_time <= ?5, 1 = 1) "
			+ "order by urp.create_time desc limit ?1,?2", nativeQuery = true)
	List<UserRedPacket> listUserRedPacketByCashId(int startPos, int pageSize, Long cashId, String startTime,
			String endTime);


	/**
	 * 通过红包id获取用户领取红包详情总数
	 * @param id
	 * @return
	 */
	@Query(value = "select count(urp.id) from user_red_packet urp left join red_packet rp on rp.id = urp.red_packet_id "
			+ "where 1=1 and rp.is_deleted = 0 and urp.red_packet_id= ?1", nativeQuery = true)
	Long getUserRedPacketCountByRedPcaketId(Long id);

	/**
	 * 通过红包id获取用户领取红包详情
	 * @param curPage
	 * @param pageSize
	 * @param id
	 * @return
	 */
	@Query(value = "select urp.* from user_red_packet urp left join red_packet rp on rp.id = urp.red_packet_id "
			+ "where 1=1 and rp.is_deleted = 0 and urp.red_packet_id= ?3 order by urp.create_time desc limit ?1,?2", nativeQuery = true)
	List<UserRedPacket> getUserRedPacketCountByRedPcaketId(Integer curPage, Integer pageSize, Long id);
}
