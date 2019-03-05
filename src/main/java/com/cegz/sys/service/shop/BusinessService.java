package com.cegz.sys.service.shop;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.shop.Business;

/**
 * 商户服务
 * 
 * @author tenglong
 * @date 2018年9月18日
 */
public interface BusinessService {

	/**
	 * 查询所有商户数据总条数
	 * 
	 * @param name
	 * @return
	 */

	Long getBusinessCount(String name);

	/**
	 * 获取所有商户数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param name
	 * @return
	 */
	List<Business> getBusinessList(Integer curPage, Integer pageSize, String name);

	/**
	 * 保存商户
	 * 
	 * @param business
	 * @return
	 */
	int saveBusiness(Business business);

	/**
	 * 通过id获取商户信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<Business> getBusinessById(Long id);

}
