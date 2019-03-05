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
 * 提现记录
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
@Entity
@Table(name = "withdraw_cash")
@Setter
@Getter
public class WithdrawCash implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 类型 1 支付宝，2 账号
	 */
	@Column(name = "type")
	private Byte type;
	
	/**
	 * 提现状态，0 提现中，1 成功，2 失败
	 */
	@Column(name = "status")
	private Byte status;

	/**
	 * 金额
	 */
	@Column(name = "money")
	private Double money;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted")
	private Byte isDeleted;

	/**
	 * 用户信息
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "create_user_id")
	private Users user;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
	
}
