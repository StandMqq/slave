package com.cegz.sys.service.shop;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.shop.UserRedPacket;
import com.cegz.sys.model.shop.WithdrawCash;

/**
 * 提现服务
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
public interface WithdrawCashService {

	/**
	 * 保存提现数据
	 * 
	 * @param withdrawCash
	 * @return
	 */
	WithdrawCash saveWithdrawCash(WithdrawCash withdrawCash);

	/**
	 * 通过id获取提现信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<WithdrawCash> getWithdrawCashById(Long id);

	/**
	 * 获取总金额
	 * 
	 * @param status
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月8日
	 */
	Double queryTotalMoneyByStatus(byte status, String payAccount, String startTime, String endTime);
	
	/**
	 * 获取总记录数
	 * 
	 * @param status
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月8日
	 */
	int countCashByStatus(byte status, String payAccount, String startTime, String endTime);

	/**
	 * 分页获取数据列表
	 * 
	 * @param status
	 * @param startPos
	 * @param size
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月8日
	 */
	List<WithdrawCash> listCashByStatus(byte status, int startPos, int size, String payAccount, String startTime,
			String endTime);

	/**
	 * 修改红包状态
	 * 
	 * @param cashId
	 * @param status
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月8日
	 */
	int updateUserPacketStatusByCash(Long cashId, byte status);

	/**
	 * 通过提现id获取红包总记录数
	 */
	int countUserRedPacketByCashId(Long cashId, String startTime, String endTime);

	/**
	 * 通过提现id分页获取红包数据列表
	 */
	List<UserRedPacket> listUserRedPacketByCashId(int startPos, int pageSize, Long cashId, String startTime,
			String endTime);

	/**
	 * 通过提现id获取车主提现总记录数
	 * @param status
	 * @param contactName
	 * @param plateNumber
	 * @param sponsorName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int countWithDrawCashByStatus(Byte status, String contactName, String sponsorName,
			String startTime, String endTime);

	/**
	 * 通过提现id获取车主提现数据列表
	 * @param status
	 * @param startPos
	 * @param pageSize
	 * @param contactName
	 * @param plateNumber
	 * @param sponsorName
	 * @param startTime
	 * @param endTime 
	 * @return
	 */
	List<com.cegz.sys.model.adver.WithdrawCash> listWithDrawByStatus(Byte status, int startPage, Integer pageSize, String contactName,
			 String sponsorName, String startTime, String endTime);

}
