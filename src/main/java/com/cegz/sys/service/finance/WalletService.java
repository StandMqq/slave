package com.cegz.sys.service.finance;

import java.util.List;

import com.cegz.sys.model.adver.CheckMoney;
import com.cegz.sys.model.adver.Wallet;

/**
 * 钱包后台服务
 * 
 *
 * @author tenglong
 * @date 2018年8月1日
 */
public interface WalletService {
	/**
	 * 通过钱包中创建者外键查询数据
	 */
	Wallet getWalletByCreateUserId(Long id);

	/**
	 * 新增（充值） / 修改
	 * 
	 * @param money
	 * @param createUserId
	 * @param updateUserId
	 * @param remark
	 * @return
	 */
	int insertOrEditWallet(Long id, Double oldMoney, Double money, Long createUserId, Long updateUserId, String remark,
			Double giveProportion);

	/**
	 * 获取钱包数据总数
	 * 
	 * @param phone
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Long queryWalletTotalCount(String phone, String startTime, String endTime);

	/**
	 * 获取钱包分页数据
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param phone
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<Wallet> queryWalletList(Integer curPage, Integer pageSize, String phone, String startTime, String endTime);

	/**
	 * 通过用户id查询收入（充值）金额
	 * 
	 * @param userId
	 * @return
	 */
	Double getRechargeMoney(Long userId);

	/**
	 * 通过用户id查询收入（充值）次数
	 * 
	 * @param userId
	 * @return
	 */
	Long getRechargeCount(Long userId);

	/**
	 * 获取钱包数据总数
	 * 
	 * @param phone
	 * @param startTime
	 * @param endTime
	 * @param setMeal 
	 * @param category 
	 * @return
	 */
	Long queryCheckMoneyCountByCreateUserId(Long createUserId, String startTime, String endTime, Long category, Long setMeal);

	/**
	 * 获取钱包分页数据
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param phone
	 * @param startTime
	 * @param endTime
	 * @param setMeal 
	 * @param category 
	 * @return
	 */
	List<CheckMoney> queryCheckMoneyListByCreateUserId(Integer curPage, Integer pageSize, Long createUserId,
			String startTime, String endTime, Long category, Long setMeal);

}
