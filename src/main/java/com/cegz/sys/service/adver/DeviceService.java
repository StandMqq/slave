package com.cegz.sys.service.adver;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.Device;

/**
 * 设备服务
 * 
 * @author tenglong
 * @date 2018年8月15日
 */
public interface DeviceService {

	/**
	 * 通过id获取设备数据
	 * 
	 * @param id：设备id
	 * @return
	 * @author tenglong
	 * @date 2018年11月21日
	 */
	Optional<Device> getDeviceById(Long id);
	
	/**
	 * 通过设备号获取设备数据
	 * 
	 * @param mei：设备号
	 * @return
	 * @author tenglong
	 * @date 2018年12月4日
	 */
	Device getDeviceByImei(String imei);

	/**
	 * 保存设备数据
	 * 
	 * @param device：设备数据
	 * @return
	 * @author tenglong
	 * @date 2018年11月21日
	 */
	int save(Device device);

	/**
	 * 通过车辆id获取设备上搭载的广告数量
	 * 
	 * @param id：车辆id
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */
	int queryAdvertisementCountByCarId(Long id);

	/**
	 * 查询设备数量
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */
	int queryDeviceCount(String imei, String number, String typeNo, String startTime, String endTime);

	/**
	 * 查询设备分页列表
	 * 
	 * @return
	 * @author tenglong
	 * @param endTime
	 * @param startTime
	 * @param typeNo
	 * @date 2018年8月15日
	 */
	List<Device> queryDeviceListByPage(Integer curPage, Integer pageSize, String imei, String number, String typeNo,
			String startTime, String endTime);

	/**
	 * 通过车辆id查询设备数量
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年11月21日
	 */
	int queryDeviceCountByCarId(Long carId);

	/**
	 * 通过车辆id查询关联的设备列表
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年10月30日
	 */
	List<Device> queryDeviceListByCarId(Long carId);

	/**
	 * 通过车主id查询关联的设备列表
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年11月13日
	 */
	List<Device> queryDevicesByContactId(Long contactId);

	/**
	 * 通过车辆id查询关联的设备列表
	 * 
	 * @author yucheng
	 * @date 2018年11月26日
	 */
	List<Device> queryDeviceListByDrivingRegistrationId(Long id);

	/**
	 * 通过设备id查询广告发布列表
	 * 
	 * @author yucheng
	 * @date 2018年11月26日
	 */
	// PublishAdverRecord getPublishAdverRecordByDeviceId(Long id);

	/**
	 * 通过广告id更改设备上的广告挂载数量
	 * 
	 * @param advertisementId：广告id
	 * @return
	 * @author tenglong
	 * @date 2018年11月21日
	 */
	int editTotalOrScriptTotal(Long advertisementId, String advertisementType);

	/**
	 * 查询所有设备剩余广告位数量
	 * 
	 * @param queryType：图片广告或者文字广告（1 ， 2）
	 * @param allocationNum：配置的最大安装数量
	 * @author tenglong
	 * @date 2018年11月29日
	 */
	int queryRemainingAdvertisementPosition(String queryType, int allocationNum);
	/**
	 * 查询所有异常设备的个数
	 * @param id
	 * @return
	 */
	Long countDevicesBySponsorId(Long sponsorId);

	/**
	 * 通过保荐方id查询所有设备 
	 * @param id
	 * @return
	 */
	List<Device> queryDevicesBySponsorId(Long sponsorId);
	
	/**
	 * 通过保荐方id查询所有异常设备
	 * @param pageSize 
	 * @param curPage 
	 * 
	 * @return
	 */
	List<Device> queryDevicesBySponsorId(Integer curPage, Integer pageSize, Long sponsorId);

	/**
	 * 查询所有异常设备总数 
	 * @param status 
	 * @param name 
	 * @param sponsorName 
	 * @param plateNum 
	 * @param phone 
	 * @return
	 */
	Long countDevices(Byte status, String contactName, String phone, String plateNum, String sponsorName);

	/**
	 * 查询所有异常设备
	 * @param curPage
	 * @param pageSize
	 * @param status 
	 * @param name 
	 * @param sponsorName 
	 * @param plateNum 
	 * @param phone 
	 * @return
	 */
	List<Device> getListDevices(Integer curPage, Integer pageSize, Byte status, String contactName, String phone, String plateNum, String sponsorName);
}
