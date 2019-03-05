package com.cegz.sys.service.adver;

import java.util.List;

import com.cegz.sys.model.shop.UserRedPacket;

/**
 * 用户红包服务类
 * @author yucheng
 * @date 2018年12年14号
 */
public interface UserRedPacketService {

	/**
	 * 通过红包id获取用户领取红包详情
	 * @param id
	 * @return yucheng
	 * @date 2018年12月14号
	 */
	Long getUserRedPacketCountByRedPcaketId(Long id);

	/**
	 * 通过红包id获取用户领取红包详情
	 * @param curPage
	 * @param pageSize
	 * @param id
	 * @return
	 */
	List<UserRedPacket> getUserRedPacketListByRedPcaketId(Integer curPage, Integer pageSize, Long id);

}
