package com.cegz.sys.service.adver;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.Advertiser;
import com.cegz.sys.model.view.adver.AdvertiserView;

/**
 * 广告主后台服务
 * 
 * @author tenglong
 * @date 2018年7月31日
 */
public interface AdvertiserService {

	/**
	 * 保存广告方信息
	 * 
	 * @param advertiser
	 * @return
	 * @author lijiaxin
	 * @date 2018年7月24日
	 */
	int save(Advertiser advertiser);

	/**
	 * 审核广告主
	 * 
	 * @param id
	 * @param status
	 * @param reason
	 * @param updateTime
	 * @return
	 */
	int advertiserStatusExamine(Long id, byte status, String reason, Date updateTime, String city, String company,
			String businessNumber, String name, String phone, String email, String address, String addressDetail);

	/**
	 * 查询广告方数据总条数
	 * 
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	Long queryAdvertiserExamineTotalCount(Integer status, String name, String phone, String registerPhone);

	/**
	 * 获取广告主未审核列表
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */
	List<Advertiser> listAdvertiserExamine(Integer curPage, Integer pageSize, Integer status, String name, String phone,
			String registerPhone);

	/**
	 * 获取广告主列表审核通过总数
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月7日
	 */
	Long queryAdvertiserTotalCount();

	/**
	 * 获取广告主详情
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月7日
	 */
	AdvertiserView getAdvertiserDetailById(Long id);
	
	
	/**
	 * 通过订单ID获取广告主信息
	 * @param id
	 * @return
	 */
	Advertiser findAdvertiserByOrderId(Long id);


	/**
	 * 通过ID获取广告信息
	 * 
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */
	Optional<Advertiser> getAdvertiserById(Long id);

	/**
	 * 校验广告主公司名称是否存在
	 * 
	 * @param company : 保荐方公司名称
	 * @return
	 * @author tenglong
	 * @date 2018年10月29日
	 */
	boolean checkCompanyNameExist(String company);
}
