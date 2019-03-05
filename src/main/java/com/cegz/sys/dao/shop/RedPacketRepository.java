package com.cegz.sys.dao.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.shop.RedPacket;

/**
 * 红包持久化服务
 * 
 * @author tenglong
 * @date 2018年10月11日
 */
public interface RedPacketRepository extends JpaRepository<RedPacket, Long> {

	@Query(value = "select sum(rp.total) from red_packet rp "
			+ "where 1=1 and rp.is_deleted = 0 and rp.adver_id = ?1 and rp.type = ?2", nativeQuery = true)
	Long getRedPacketTotalNum(Long adverId, int type);

	/**
	 *  获取地图红包广告总数
	 * @param i 
	 * @param name
	 * @return
	 */
	@Query(value = "select count(*) from ( select distinct rp.* from cegz_cli.red_packet rp "
			+ " inner join cegz.advertisement a on rp.adver_id = a.id "
			+ " left join cegz.publish_advertisement_record par on par.advertisement_id = a.id "
			+ " where rp.type = ?1 and if(?2 != '', a.title like %?2%, 1 = 1)"
			+ " )p", nativeQuery = true)
	Long getRedPacketCount(int i, String name);

	/**
	 *  获取地图红包广告列表
	 * @param startPage
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@Query(value = "select distinct rp.* ,a.title, par.start_time from cegz_cli.red_packet rp "
			+ " inner join cegz.advertisement a on rp.adver_id = a.id "
			+ " left join cegz.publish_advertisement_record par on par.advertisement_id = a.id "
			+ " where rp.type = ?3 and if(?4 != '', a.title like %?4%, 1 = 1) limit ?1,?2", nativeQuery = true)
	List<RedPacket> getRedPacketList(Integer startPage, Integer pageSize,int i, String name);

	/**
	 * 通过广告Id获取红包广告
	 * @param advertisementId
	 * @return
	 */

	@Query(value = "select rp.* from cegz_cli.red_packet rp "
			+ "where 1=1 and rp.is_deleted = 0 and rp.adver_id = ?1 and rp.type = ?2", nativeQuery = true)
	
	RedPacket getRedPacketByAdvertisementId(Long advertisementId,int type);

}
