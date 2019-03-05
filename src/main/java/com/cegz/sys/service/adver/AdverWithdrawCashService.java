package com.cegz.sys.service.adver;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.WithdrawCash;



/**
 * 车主提现服务
 * 
 * @author yucheng
 * @date 2018年11月5日
 */
public interface AdverWithdrawCashService {

	/**
	 * 保存提现数据1
	 * 
	 * @param withdrawCash
	 * @return
	 */
	WithdrawCash saveWithdrawCash(WithdrawCash withdrawCash);

	/**
	 * 通过id获取提现信息2
	 * 
	 * @param id
	 * @return
	 */
	Optional<WithdrawCash> getWithdrawCashById(Long id);


	/**
	 * 修改红包状态
	 * 
	 * @param cashId
	 * @param status
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月8日a
	 */
	int updateUserPacketStatusByCash(Long cashId, byte status);

	/**
	 * 通过提现id获取车主提现总记录数
	 * @param status
	 * @param contactName
	 * @param sponsorName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Long countWithDrawCashByStatus(Byte status, String contactName, String sponsorName,
			String startTime, String endTime);

	/**
	 * 通过提现id获取车主提现数据列表
	 * @param status
	 * @param pageSize
	 * @param contactName
	 * @param sponsorName
	 * @param startTime
	 * @param endTime 
	 * @return
	 */
	List<com.cegz.sys.model.adver.WithdrawCash> listWithDrawByStatus(Byte status, int startPage, Integer pageSize, String contactName,
			 String sponsorName, String startTime, String endTime);


	/**
	 * 获取为is_delete为0时不同状态下的总金额
	 *
	 * @param status
	 * @return
	 * @author maqianqian
	 * @date 2019年02月13日
	 */
	double queryTotalMoneyByStatus(byte status);
}
