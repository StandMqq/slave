package com.cegz.sys.model.adver;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 车主钱包
 * 
 * @author lijiaxin
 * @date 2018年12月19日
 */
@Entity
@Table(name = "contacts_wallet")
@Setter
@Getter
public class ContactsWallet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 总金额
	 */
	@Column(name = "total_money")
	private BigDecimal totalMoney;
	
	/**
	 * 可提现金额
	 */
	@Column(name = "current_money")
	private BigDecimal currentMoney;
	
	/**
	 * 冻结金额
	 */
	@Column(name = "freeze_money")
	private BigDecimal freezeMoney;
	
	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted")
	private byte isDeleted;

	/**
	 * 创建用户id
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users user;
	
	/**
	 * 车主外键
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "contacts_id")
	private Contacts contact;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;


}
