package com.cegz.sys.model.adver;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 销售钱包
 * 
 * @author lijiaxin
 * @date 2018年11月1日
 */
@Entity
@Table(name = "sell_wallet")
@Setter
@Getter
public class SellWallet implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;
	
	/**
	 * 总额
	 */
	@Column(name = "money")
	private Double totalMoney;
	
	/**
	 * 当前剩余未提现金额
	 */
	@Column(name = "current_money")
	private Double currentMoney;
	
	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted")
	private Byte isDeleted;

	/**
	 * 用户
	 */
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users user;


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
	
	/**
	 * 冻结金额
	 */
	@Column(name = "freeze_money")
	private Double freezeMoney;
}
