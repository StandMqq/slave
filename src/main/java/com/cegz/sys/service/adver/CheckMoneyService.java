/**
 * 
 */
package com.cegz.sys.service.adver;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.CheckMoney;

/**
 *充值记录服务类
 * @author yucheng
 * @date 2019年1月17号
 */
public interface CheckMoneyService {

	/**
	 * 通过用户ID获取充值记录
	 * @param id
	 * @param startTime 
	 * @param endTime 
	 * @param pageSize 
	 * @param startPage 
	 * @return 
	 */
	List<CheckMoney> findByUserId(Long id, String startTime, String endTime, Integer startPage, Integer pageSize);

	/**
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @return 
	 */
	Long queryCheckMoneyTotalCount(Long id, String startTime, String endTime);
	
	/**
	 * 通过用户id查询收入（充值）金额
	 * 
	 * @param userId
	 * @return
	 */
	Double getRechargeMoney(Long userId);

	/**
	 * 通过用户id获取账单开票详情数量
	 * @param id
	 * @param status
	 * @return
	 */
	Long queryCheckMoneyInvoiceTotalCount(Long id, Integer status);

	/**
	 * 通过用户id获取账单开票详情
	 * @param id
	 * @param status
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	List<CheckMoney> findCheckMoneyInvoiceByUserId(Long id, Integer status, Integer startPage, Integer pageSize);

	/**
	 * 通过Id获取该账单信息
	 * @param id
	 * @return
	 */
	Optional<CheckMoney> getCheckMoneyById(Long id);

	/**
	 * 通过ID修改账单状态到开票中
	 * @param id
	 * @param status
	 * @return
	 */
	int checkMoneyStatusExamine(Long id, Byte status);
	
	/**
	 * 保存账单
	 * @param check
	 * @return
	 * @author lijiaxin
	 * @date 2018年10月9日
	 */
	CheckMoney insertCheckMoney(CheckMoney check);
	/**
	 * 修改账号状态
	 * @param billStatus
	 * @param isDeleted
	 * @return
	 * @author lijiaxin
	 * @date 2018年10月9日
	 */
	int updateCheckMOney(int billStatus, String order);
	
	/**
	 * 通过订单获取记录
	 * @param order
	 * @return
	 * @author lijiaxin
	 * @date 2018年10月9日
	 */
	CheckMoney getCheckMoneyByOrder(String order);
	
	/**
	 * 用户充值记录数
	 * @param userId
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月1日
	 */
	int countInputMoneyByUser(Long userId);
	
	/**
	 * 统计金额
	 * @param userId
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月2日
	 */
	double sumMoneyByUser(Long userId);
	
	/**
	 * 列表
	 * @param userId
	 * @param startPos
	 * @param size
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月2日
	 */
	List<CheckMoney> listCheckByUser(Long userId, int startPos, int size);

	/**
	 * @param userId
	 * @return
	 */
	List<CheckMoney> listCheckMoneyByUserId(Long userId);

	/**
	 * 保存账单状态为开票完成状态
	 * @param check
	 */
	CheckMoney saveStatus(CheckMoney check);

}
