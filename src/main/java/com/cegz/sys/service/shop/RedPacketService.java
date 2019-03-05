package com.cegz.sys.service.shop;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.shop.Business;
import com.cegz.sys.model.shop.RedPacket;
import com.cegz.sys.model.shop.UserRedPacket;

/**
 * 红包服务
 * 
 * @author tenglong
 * @date 2018年10月11日
 */
public interface RedPacketService {
	/**
	 * 查询用户领取红包数据总条数
	 * 
	 * @param adverId 广告id
	 * @return
	 */

	Long getUserReceiveRedPacketCount(Long adverId);

	/**
	 * 获取用户领取红包数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param adverId  广告id
	 * @return
	 */
	List<UserRedPacket> getUserReceiveRedPacketList(Integer curPage, Integer pageSize, Long adverId);

	/**
	 * 通过广告id获取红包领取数
	 * 
	 * @param adverId 广告id
	 * @param type    红包类型，1 企业红包，2 商家红包
	 * @return
	 */

	Long getRedPacketReceiveNum(Long adverId, int type);

	/**
	 * 通过广告id获取红包总数
	 * 
	 * @param adverId 广告id
	 * @param type    红包类型，1 企业红包，2 商家红包
	 * @return
	 */

	Long getRedPacketTotalNum(Long adverId, int type);

	/**
	 * 通过id获取红包领取人信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<UserRedPacket> getUserReceiveRedPacketById(Long userRedPacketId);
	
	/**
	 * 通过id获取红包信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<RedPacket> getRedPacketById(Long redPacketId);

	/**
	 * 
	 */
	int financePayConfirm(Long userRedPacketId);

	/**
	 * 获取地图红包广告总数
	 * @param i 
	 * @param name
	 * @return
	 */
	Long getRedPacketCount(int i, String name);

	/**
	 * 获取地图红包广告列表
	 * @param startPage
	 * @param pageSize
	 * @param i 
	 * @param name
	 * @return
	 */
	List<RedPacket> getRedPacketList(Integer startPage, Integer pageSize, int i, String name);

	/**
	 * 通过广告Id获取红包广告
	 * @param id
	 * @param i 
	 * @return 
	 */
	RedPacket getRedPacketByAdvertisementId(Long id, int i);

}
