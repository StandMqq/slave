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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 账单实体类
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Entity
@Table(name = "check_money")
@Getter
@Setter
public class CheckMoney implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 金额
	 */
	@Column(name = "money", length = 20)
	private Double money;

	/**
	 * 描述
	 */
	@Column(name = "remark", length = 255)
	private String remark;

	/**
	 * 账单类型，1：支出，2：收入，3：微信充值
	 */
	@Column(name = "type", length = 200)
	private int type;

	/**
	 * 订单信息
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	private Order order;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 10)
	private byte isDeleted;

	/**
	 * 创建用户id
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改用户ID
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "update_user_id")
	private Users updateUserId;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time", length = 50)
	private Date updateTime;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time", length = 50)
	private Date createTime;

	/**
	 * 账单状态，0：成功，1：失败，2：退款，3： 未支付
	 */
	@Column(name = "bill_status", length = 11)
	private int billStatus;

	/**
	 * 微信充值流水号
	 */
	@Column(name = "voucher", length = 255)
	private String voucher;
	
	/**
	 * '状态，0 发票未开，1，开票中，2，已开票
	 */
	@Column(name = "status")
	private Byte status;

}
