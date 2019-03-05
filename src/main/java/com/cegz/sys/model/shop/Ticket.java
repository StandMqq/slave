package com.cegz.sys.model.shop;

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
 * 券实体类
 * 
 * @author lijiaxin
 * @date 2018年8月24日
 */
@Entity
@Table(name = "ticket")
@Setter
@Getter
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 编号
	 */
	@Column(name = "ticket_no")
	private String ticketNo;

	/**
	 * 总记录数
	 */
	@Column(name = "total")
	private Integer total;

	/**
	 * 当前记录数
	 */
	@Column(name = "current_total")
	private Integer currentTotal;

	/**
	 * 有效天数
	 */
	@Column(name = "days")
	private Integer days;

	/**
	 * 简介
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time")
	private String startTime;

	/**
	 * 结束时间
	 */
	@Column(name = "end_time")
	private String endTime;

	/**
	 * 券类型
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private TicketType type;

	/**
	 * 创建者
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUser;

	/**
	 * 修改者
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "update_user_id")
	private Users updateUser;

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
	 * 金额
	 */
	@Column(name = "money")
	private BigDecimal money;

	/**
	 * 图片地址
	 */
	@Column(name = "picture_url")
	private String pictureUrl;

	/**
	 * 商户类型
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "business_id")
	private Business business;

}
