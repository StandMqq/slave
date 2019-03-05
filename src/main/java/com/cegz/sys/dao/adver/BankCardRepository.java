package com.cegz.sys.dao.adver;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cegz.sys.model.adver.BankCard;

/**
 * 银行卡
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
public interface BankCardRepository extends JpaRepository<BankCard, Long> {

}
