package com.cegz.sys.model.shop;

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
 * 用户红包
 * 
 * @author lijiaxin
 * @date 2018年10月10日
 */
@Entity
@Table(name = "user_red_packet")
@Getter
@Setter
public class UserRedPacket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 状态 0 未使用，1 已使用
	 */
	@Column(name = "status")
	private byte status;

	/**
	 * 红包实体
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "red_packet_id")
	private RedPacket redPacket;
	
	/**
	 * 提现实体
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "cash_id")
	private WithdrawCash withdrawCash;

	/**
	 * 创建者
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUser;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted")
	private byte isDeleted;

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
	 * 支付宝账号
	 */
	@Column(name = "pay_account")
	private String payAccount;
	
	/**
	 * 设备号
	 */
	@Column(name = "imei")
	private String imei;

}
