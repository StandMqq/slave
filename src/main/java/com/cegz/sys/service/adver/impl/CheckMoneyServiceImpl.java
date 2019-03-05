package com.cegz.sys.service.adver.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.CheckMoneyRepository;
import com.cegz.sys.model.adver.CheckMoney;
import com.cegz.sys.service.adver.CheckMoneyService;

/**
 * 充值记录服务类
 * @author yucheng
 * @date 2019年1月17号
 */
@Service("checkMoneyService")
@Transactional
public class CheckMoneyServiceImpl implements CheckMoneyService{

	@Autowired
	private CheckMoneyRepository checkMoneyRepository;
	
	@Override
	public List<CheckMoney> findByUserId(Long id ,String startTime ,String endTime ,Integer startPage, Integer pageSize) {
		return checkMoneyRepository.listCheckMoneyByUserId(id , startTime ,endTime ,startPage, pageSize);
	}

	@Override
	public Long queryCheckMoneyTotalCount(Long id, String startTime, String endTime) {
		return checkMoneyRepository.queryCheckMoneyTotalCount(id , startTime ,endTime );
	}
	
	@Override
	public Double getRechargeMoney(Long userId) {
		return checkMoneyRepository.getRechargeMoney(userId);
	}

	@Override
	public Long queryCheckMoneyInvoiceTotalCount(Long id, Integer status) {
		return checkMoneyRepository.queryCheckMoneyTotalCount(id , status);
	}

	@Override
	public List<CheckMoney> findCheckMoneyInvoiceByUserId(Long id, Integer status, Integer startPage,
			Integer pageSize) {
		return checkMoneyRepository.findCheckMoneyInvoiceByUserId(id , status,startPage, pageSize);
	}

	@Override
	public Optional<CheckMoney> getCheckMoneyById(Long id) {
		return  checkMoneyRepository.findById(id);
	}

	@Override
	public int checkMoneyStatusExamine(Long id, Byte status) {
		return checkMoneyRepository.checkMoneyStatusExamine(id, status);
	}
	
	@Override
	public List<CheckMoney> listCheckMoneyByUserId(Long userId) {
		return checkMoneyRepository.listCheckMoneyByUserId(userId);
	}
	

	@Override
	public CheckMoney insertCheckMoney(CheckMoney check) {
		return checkMoneyRepository.save(check);
	}


	@Override
	public int updateCheckMOney(int billStatus, String order) {
		
		return checkMoneyRepository.updateCheckStatus(billStatus, order);
	}


	@Override
	public CheckMoney getCheckMoneyByOrder(String order) {
		return checkMoneyRepository.getCheckByOrder(order);
	}


	@Override
	public int countInputMoneyByUser(Long userId) {
		return checkMoneyRepository.countDataByUserAndInput(userId);
	}


	@Override
	public double sumMoneyByUser(Long userId) {
		return checkMoneyRepository.sumDataByUser(userId);
	}


	@Override
	public List<CheckMoney> listCheckByUser(Long userId, int startPos, int size) {
		return checkMoneyRepository.listDataByUser(userId, startPos, size);
	}

	@Override
	public CheckMoney saveStatus(CheckMoney check) {
		return checkMoneyRepository.save(check);
	}
	
	

}
