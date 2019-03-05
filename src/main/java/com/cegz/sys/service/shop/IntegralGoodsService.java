package com.cegz.sys.service.shop;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.shop.IntegralGoods;

/**
 * 积分商品服务
 * 
 * @author tenglong
 * @date 2018年11月29日
 */
public interface IntegralGoodsService {

	/**
	 * 查询所有积分商品数据总条数
	 * 
	 * @param name
	 * @return
	 */

	Long getIntegralGoodsCount(String name, String isDeleted);

	/**
	 * 获取所有积分商品数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */
	List<IntegralGoods> getIntegralGoodsList(Integer curPage, Integer pageSize, String name, String isDeleted);

	/**
	 * 保存积分商品
	 * 
	 * @param integralGoods
	 * @return
	 */
	int saveIntegralGoods(IntegralGoods integralGoods);

	/**
	 * 通过id获取积分商品信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<IntegralGoods> getIntegralGoodsById(Long id);

}
