package com.cegz.sys.service.adver;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.Advertisement;
import com.cegz.sys.model.adver.AdvertisementType;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.model.adver.Price;
import com.cegz.sys.model.view.adver.AdvertisementView;

/**
 * 广告后台服务
 * 
 * @author tenglong
 * @date 2018年8月6日
 */
public interface AdvertisementService {

	/**
	 * 保存订单
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月1日
	 */
	int save(Advertisement advertisement);

	/**
	 * 审核广告数据
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月1日
	 */
	int advertisementStatusExamine(Long id, Integer status, String reason, Date updateTime);

	/**
	 * 查询所有广告数据总条数
	 * 
	 * @param status          广告状态
	 * @param name            广告名称
	 * @param advertiserName  广告主名称
	 * @param advertiserPhone 广告主电话
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	Long queryAdvertisementCount(Integer status, String name, String advertiserName, String advertiserPhone,
			String typeNo);

	/**
	 * 获取所有广告分页信息
	 * 
	 * @param status          广告状态
	 * @param name            广告名称
	 * @param advertiserName  广告主名称
	 * @param advertiserPhone 广告主电话
	 * @return
	 * @author tenglong
	 * @date 2018年8月2日
	 */
	List<Advertisement> getAdvertisementList(Integer curPage, Integer pageSize, Integer status, String name,
			String advertiserName, String advertiserPhone, String typeNo);

	/**
	 * 获取单条广告信息
	 * 
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年8月16日
	 */
	Optional<Advertisement> getAdvertisementById(Long id);

	/**
	 * 获取单条广告信息V2
	 * 
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年8月16日
	 */
	Advertisement getAdvertisementByIdV2(Long id);

	/**
	 * 通过广告主id获取广告列表
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月7日
	 */
	List<AdvertisementView> getAdvertisementsByAdvertiserId(Long advertiserId);

	/**
	 * 通过广告主id获取广告列表分页
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月7日
	 */
	List<AdvertisementView> getAdvertisementsByAdvertiserIdLimit(Long advertiserId, Integer status, Integer curPage,
			Integer pageSize);

	/**
	 * 通过广告主id获取广告列表数量
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月7日
	 */
	Long queryAdvertisementCountByAdvertiserId(Long advertiserId, Integer status);

	/**
	 * 获取套餐列表
	 * 
	 * @return
	 * @author lijiaxin
	 * @date 2018年8月20日
	 */
	List<Price> listPrice();

	/**
	 * 获取广告类型
	 * 
	 * @param id
	 * @return
	 * @author lijiaxin
	 * @date 2018年8月21日
	 */
	Optional<AdvertisementType> getAdverTypeById(Long id);

	/**
	 * 获取套餐
	 * 
	 * @param id
	 * @return
	 * @author lijiaxin
	 * @date 2018年8月21日
	 */
	Optional<Price> getPriceById(Long id);

	/**
	 * 通过广告主id获取广告列表数量
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月7日
	 */
	Long queryCarCountByAdvertisementId(Long advertiserId, String plateNumber);

	/**
	 * 通过广告id获取已发布广告挂载的车辆信息
	 * 
	 * @param id 广告id
	 * @return
	 * @author tenglong
	 * @date 2018年8月22日
	 */
	List<DrivingRegistration> getCarListByAdvertisementId(Integer curPage, Integer pageSize, Long id,
			String plateNumber);

	/**
	 * 通过车辆id获取广告总数
	 * 
	 * @param startPage
	 * @param pageSize
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Long queryAdvertisementCountByCarId(Long id, String startTime, String endTime, String status, String typeNo,
			String name);

	/**
	 * 通过车辆id获取广告列表
	 * 
	 * @param startPage
	 * @param pageSize
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<Advertisement> queryAdvertisementListByCarId(Integer startPage, Integer pageSize, Long id, String startTime,
			String endTime, String status, String typeNo, String name);

	/**
	 * 修改广告发布信息
	 * 
	 * @param advertisementId 广告id
	 * @param orderId         订单id
	 * @param updateUserId    修改人id
	 * @return
	 * @author tenglong
	 * @date 2018年11月28日
	 */
	int editPublishRecord(Long advertisementId, Long orderId, Long updateUserId);

//	/**
//	 * 通过车辆id获取广告总数
//	 * @param id
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 */
//	Long getAdvertisementCountByCarId(Long id, String startTime, String endTime);
//
//	/**
//	 * 通过车辆id获取广告列表
//	 * @param curPage
//	 * @param pageSize
//	 * @param id
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 */
//	List<Advertisement> getAdvertisementListByCarId(Integer curPage, Integer pageSize, Long id, String startTime,
//			String endTime);

}
