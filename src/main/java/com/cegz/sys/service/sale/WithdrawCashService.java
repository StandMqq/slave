package com.cegz.sys.service.sale;

import java.util.List;
import java.util.Optional;

import com.cegz.sys.model.adver.WithdrawCash;

/**
 * 提现服务
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
public interface WithdrawCashService {

	/**
	 * 查询所有提现数据总条数
	 * 
	 * @param bankCardName     : 银行卡姓名
	 * @param openBankCardName : 银行卡开户行
	 * @param alipayName       : 支付宝姓名
	 * @param alipayAccount    : 支付宝账号
	 * @return
	 */
	Long getWithdrawCashCount(String bankCardName, String openBankCardName, String alipayName, String alipayAccount);

	/**
	 * 获取所有提现数据分页信息
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param bankCardName     : 银行卡姓名
	 * @param openBankCardName : 银行卡开户行
	 * @param alipayName       : 支付宝姓名
	 * @param alipayAccount    : 支付宝账号
	 * @return
	 */
	List<WithdrawCash> getWithdrawCashList(Integer curPage, Integer pageSize, String bankCardName,
			String openBankCardName, String alipayName, String alipayAccount);

	/**
	 * 保存提现数据
	 * 
	 * @param withdrawCash
	 * @return
	 */
	int saveWithdrawCash(WithdrawCash withdrawCash);

	/**
	 * 通过id获取提现信息
	 * 
	 * @param id
	 * @return
	 */
	Optional<WithdrawCash> getWithdrawCashById(Long id);

}
