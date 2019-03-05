package com.cegz.sys.model.adver;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 发票
 * 
 * @author tenglong
 * @date 2018年11月20日
 */
@Entity
@Table(name = "invoice")
@Setter
@Getter
public class Invoice implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 发票抬头类型 1 企业，2 个人
	 */
	@Column(name = "type")
	private Byte type;

	/**
	 * 抬头名称
	 */
	@Column(name = "company")
	private String company;

	/**
	 * 税号
	 */
	@Column(name = "number")
	private String number;

	/**
	 * 发票内容
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 发票金额
	 */
	@Column(name = "money")
	private BigDecimal money;

	/**
	 * 电子邮箱
	 */
	@Column(name = "email")
	private String email;

	/**
	 * 订单
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	/**
	 * 是否有效，0 有效，1 无效；
	 */
	@Column(name = "is_deleted")
	private Byte isDeleted;

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
	 * 创建用户
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users user;

	/**
	 * 税费支付账单
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "check_id")
	private CheckMoney check;

	/**
	 * 发票图片
	 */
	@Column(name = "img_url")
	private String imgUrl;

	/**
	 * 状态，0 未完成，1 处理中，2 完成，3 失败
	 */
	@Column(name = "status")
	private Byte status;

}
