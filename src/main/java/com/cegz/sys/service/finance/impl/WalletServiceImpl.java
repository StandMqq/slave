package com.cegz.sys.service.finance.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.CheckMoneyRepository;
import com.cegz.sys.dao.adver.WalletRepository;
import com.cegz.sys.model.adver.CheckMoney;
import com.cegz.sys.model.adver.Wallet;
import com.cegz.sys.service.finance.WalletService;
import com.cegz.sys.util.TokenUtil;
import com.cegz.sys.weixin.util.Tool;

/**
 * 钱包后台服务
 *
 * @author tenglong
 * @date 2018年8月1日
 */
@Service("walletService")
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private CheckMoneyRepository checkMoneyRepository;

	@Override
	public Wallet getWalletByCreateUserId(Long id) {
		return walletRepository.getWalletByCreateUserIds(id);
	}

	@Override
	public int insertOrEditWallet(Long id, Double oldMoney, Double money, Long createUserId, Long updateUserId,
			String remark, Double giveProportion) {
		int ret = 0;

		// 充值时赠送比例不为空时，计算赠送金额和充值金额相加为真实充值金额
		if (id == null) {
			if (giveProportion != null) {// 充值时赠送比例不为空时，计算赠送金额和充值金额相加为真实充值金额
				ret = walletRepository.insertWallet(money * giveProportion / 100 + money, createUserId, updateUserId, remark, TokenUtil.getUUID());
			}else {
				ret = walletRepository.insertWallet(money, createUserId, updateUserId, remark, TokenUtil.getUUID());
			}
		} else {
			if (giveProportion != null) {// 充值时赠送比例不为空时，计算赠送金额和充值金额相加为真实充值金额
				ret = walletRepository.editWallet(id, oldMoney + money * giveProportion / 100 + money, updateUserId);
			}else {
				ret = walletRepository.editWallet(id, oldMoney + money, updateUserId);
			}
		}
		if (ret != 0) {
//			CheckMoney check = new CheckMoney();
//			check.setCreateTime(new Date());
//			check.setMoney(money);
//			check.setRemark("后台充值");
			// 类型 1 支出，2 收入
//			check.setType(2);
//			check.setCreateUserId(createUserId);
//			check.setUpdateUserId(updateUserId);
//			checkMoneyRepository.save(check);
			// 充值流水号
			String voucher = System.currentTimeMillis() + Tool.getRandomNum(5);
			ret = checkMoneyRepository.saveCheckMoney(new Date(), money, "后台充值", 2, createUserId, updateUserId,
					voucher);
			if (giveProportion != null) {
				ret = checkMoneyRepository.saveCheckMoney(new Date(), money * giveProportion / 100,
						"后台充值赠送" + giveProportion + "%", 5, createUserId, updateUserId, voucher);
			}
//			return 1;
		}
		return ret;
	}

	@Override
	public Long queryWalletTotalCount(String phone, String startTime, String endTime) {
		return walletRepository.queryWalletTotalCount(phone, startTime, endTime);
	}

	@Override
	public List<Wallet> queryWalletList(Integer curPage, Integer pageSize, String phone, String startTime,
			String endTime) {
		return walletRepository.queryWalletList(curPage, pageSize, phone, startTime, endTime);
	}

	@Override
	public Double getRechargeMoney(Long userId) {
		return checkMoneyRepository.getRechargeMoney(userId);
	}

	@Override
	public Long getRechargeCount(Long userId) {
		return checkMoneyRepository.getRechargeCount(userId);
	}

	@Override
	public Long queryCheckMoneyCountByCreateUserId(Long createUserId, String startTime, String endTime, Long category, Long setMeal) {
		return checkMoneyRepository.queryCheckMoneyCountByCreateUserId(createUserId, startTime, endTime, category,setMeal);
	}

	@Override
	public List<CheckMoney> queryCheckMoneyListByCreateUserId(Integer curPage, Integer pageSize, Long createUserId,
			String startTime, String endTime, Long category, Long setMeal) {
		return checkMoneyRepository.queryCheckMoneyListByCreateUserId(curPage, pageSize, createUserId, startTime,
				endTime ,category, setMeal );
	}

}
