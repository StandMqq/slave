package com.cegz.sys.service.sale;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.CheckMoney;
import com.cegz.sys.model.adver.SellBindUser;
import com.cegz.sys.model.adver.SellWallet;

/**
 * 销售绑定服务
 * 
 * @author tenglong
 * @date 2018年10月18日
 */
public interface SaleBindService {

	Optional<SellBindUser> getSellBindUserById(Long id);

	/**
	 * 查询所有销售绑定数据总条数
	 * 
	 * @param phone
	 * @return
	 */

	Long getSaleBindCount(Long userId, String phone);

	/**
	 * 获取所有销售绑定数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param phone
	 * @return
	 */
	List<SellBindUser> getSaleBindList(Integer curPage, Integer pageSize, Long userId, String phone);

	/**
	 * 通过销售人id查询绑定人数
	 * 
	 * @param sellUserId
	 * @return
	 */

	/**
	 * 查询所有绑定客户数据总条数
	 * 
	 * @param sellUserId 绑定人id
	 * @return
	 */

	Long getBindClientsCount(Long sellUserId);

	/**
	 * 获取所有绑定客户数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param sellUserId 绑定人id
	 * @return
	 */
	List<SellBindUser> getBindClientsList(Integer curPage, Integer pageSize, Long sellUserId);

	/**
	 * 通过绑定人查询绑定人数
	 * 
	 * @param sellUserId 绑定人id
	 * @return
	 */
	Long querySaleBindNum(Long sellUserId);

	/**
	 * 查询被绑定人累计充值金额
	 * 
	 * @param userId 被绑定人id
	 * @return
	 */
	Double queryBindAccountRechargeMoney(Long userId);

	/**
	 * 查询被绑定人最后一次充值时间
	 * 
	 * @param userId 被绑定人id
	 * @return
	 */
	CheckMoney queryBindAccountLastRechargeTime(Long userId);
	
	/**
	 * 获取绑定信息
	 * @param userId
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月8日
	 */
	SellBindUser getSellBindByUser(Long userId);
	
	/**
	 * 保存销售钱包信息
	 * @param sellwallet
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月8日
	 */
	SellWallet insertSellWallet(SellWallet sellWallet);
	
	/**
	 * 获取销售钱包
	 * @param useId
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月8日
	 */
	SellWallet getSellWalletByUser(Long useId);
	
	
	/**
	 * 新增money
	 * @param walletId
	 * @param money
	 * @return
	 * @author lijiaxin
	 * @date 2018年11月8日
	 */
	int addSellWalleMoney(Long walletId, double money);

}
