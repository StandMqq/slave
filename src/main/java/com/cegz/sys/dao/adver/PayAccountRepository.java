package com.cegz.sys.dao.adver;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cegz.sys.model.adver.PayAccount;

/**
 * 支付宝账号
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
public interface PayAccountRepository extends JpaRepository<PayAccount, Long> {

}
