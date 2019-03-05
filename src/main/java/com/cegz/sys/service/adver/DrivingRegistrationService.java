package com.cegz.sys.service.adver;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.DrivingRegistration;

/**
 * 行驶证后台服务
 * 
 * @author tenglong
 * @date 2018年7月31日
 */
public interface DrivingRegistrationService {
	/**
	 * 获取行驶证单条数据
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */
	Optional<DrivingRegistration> getDrivingRegistrationById(Long id);

	/**
	 * 添加行驶证信息
	 * 
	 * @param drivingRegistration
	 * @return
	 * @author tenglong
	 * @date 2018年7月31日
	 */
	int save(DrivingRegistration drivingRegistration);

	/**
	 * 审核行驶证信息
	 * 
	 * @param id
	 * @param status
	 * @param reason
	 * @param updateTime
	 * @return
	 */
	int carsStatusExamine(Long id, Integer status, String reason, Date updateTime, String carNumber, String model,
			Date birthDate);

	/**
	 * 获取记录数
	 * 
	 * @param status
	 * @return
	 * @author Administrator
	 * @date 2018年8月7日
	 */
	int countCarByStatus(Byte status, String plateNumber, String model);

	/**
	 * 分页获取数据
	 * 
	 * @param startPos
	 * @param size
	 * @param Status
	 * @return
	 * @author Administrator
	 * @date 2018年8月7日
	 */
	List<DrivingRegistration> listPageCar(int startPos, int size, Byte status, String plateNumber, String model);

	/**
	 * 获取车辆详情
	 * 
	 * @param id
	 * @return
	 * @author tenglong
	 * @date 2018年8月15日
	 */
	Optional<DrivingRegistration> getById(Long id);

	/**
	 * 通过车牌号获取行驶证数据
	 * 
	 * @param plateNumber
	 * @return
	 * @author tenglong
	 * @date 2018年8月1日
	 */
	DrivingRegistration getDrivingRegistrationByPlateNumber(String plateNumber);

	/**
	 * 通过保荐id获取名下车辆数量
	 * 
	 * @param id
	 * @param name
	 * @param phone
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author yucheng
	 * @date 2018年11月27日
	 */
	Long getCarCountBySponsorId(Long id, String name, String phone);

	/**
	 * 通过保荐id获取名下车辆列表
	 * 
	 * @param id
	 * @param name
	 * @param phone
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author yucheng
	 * @date 2018年11月27日
	 */
	List<DrivingRegistration> getCarListBySponsorId(Long id, String name, String phone, Integer curPage,
			Integer pageSize);

	/**
	 *通过设备号获取车辆信息
	 * @param imei
	 * @return yucheng
	 * @date 2018年12月14日
	 */
	DrivingRegistration queryPlateNumberByDeviceImei(String imei);

	/**
	 * 通过车主id获取名下车辆总数
	 * @param id
	 * @return
	 */
	Long countRegistrationByContactId(Long id);

	/**
	 * 通过车主id获取名下车辆
	 * @param startPage
	 * @param pageSize
	 * @param id
	 * @return
	 */
	List<DrivingRegistration> listRegistrationByContactId(Integer startPage, Integer pageSize, Long id);

	/**
	 * 通过车主id获取名下车辆
	 * @param id
	 * @return
	 */
	List<DrivingRegistration> getDrivingRegistrationByContactId(Long id);

	/**
	 * 根据contacts_id查询车牌号
	 * @param contactsId
	 * @return
	 */
    List<String> queryPlateNumberByContactsId(Long contactsId);

	/**
	 * 根据车牌号查询contacts_id
	 * @param plateNumber
	 * @return
	 */
	DrivingRegistration queryDrivingRegistrationByPlateNumber(String plateNumber);

	/**
	 * 根据准确车牌查询
	 * @param plateNumber
	 * @return
	 */
	DrivingRegistration findContactsIdByPlateNumber(String plateNumber);

	/**
	 * 根据车牌号模糊查询，分页展示
	 * @param startPage
	 * @param pageSize
	 * @param plateNumber
	 * @return
	 */
	List<DrivingRegistration> queryDrivingRegistrationListByPlateNumber(Integer startPage, Integer pageSize, String plateNumber);

	/**
	 * 根据车牌获取记录总数
	 * @param plateNumber
	 * @return
	 */
	Long queryCountByPlateNumber(String plateNumber);

}
