/**
 * 
 */
package com.cegz.sys.service.shop;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.shop.Integral;
import com.cegz.sys.model.shop.IntegralGoodsRecord;

/**
 * 积分兑换服务
 * @author yucheng
 * @date 2018年11月29号
 */
public interface IntegralExchangeService {

	/**
	 * 获取积分兑换列表
	 * @param startPage
	 * @param pageSize
	 * @param phone
	 * @param name
	 * @param typeNo
	 * @return
	 */
	List<IntegralGoodsRecord> queryIntegralGoodsRecordList(Integer startPage, Integer pageSize, String phone,
			String name, String typeNo);

	/**
	 * 获取用户积分信息
	 */
	Integral queryIntegralByCreatUserId(Long userId);
	
	/**
	 * 通过id获取积分兑换
	 * 
	 * @param id: 积分兑换id
	 * @return
	 * @author tenglong
	 * @date 2018年11月21日
	 */
	Optional<IntegralGoodsRecord> getIntegralGoodsRecordById(Long id);

	/**
	 * 保存积分兑换数据
	 * 
	 * @param integralGoodsRecord：积分兑换数据
	 * @return
	 * @author tenglong
	 * @date 2018年11月21日
	 */
	int save(IntegralGoodsRecord integralGoodsRecord);

	
}
