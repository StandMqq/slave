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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单实体类
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Entity
@Table(name = "order_advertiser")
@Getter
@Setter
public class Order implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 编号
	 */
	@Column(name = "order_no", length = 255)
	private String orderNo;

	/**
	 * 名称
	 */
	@Column(name = "order_name", length = 255)
	private String name;

	/**
	 * 订单状态 0 审核中，1进行中，2 完成，3 失败
	 */
	@Column(name = "status", length = 10)
	private byte status;

	/**
	 * 审核结论
	 */
	@Column(name = "reason", length = 255)
	private String reason;

	/**
	 * 发布广告的设备数量
	 */
	@Column(name = "total_advertisement", length = 255)
	private Integer totalAdvertisement;

	/**
	 * 总金额
	 */
	@Column(name = "total_money", length = 255)
	private Double totalMoney;

	/**
	 * 实际总金额
	 */
	@Column(name = "real_money", length = 255)
	private Double realMoney;

	/**
	 * 开始投放时间
	 */
	@Column(name = "start_time", length = 255)
	private Date startTime;

	/**
	 * 结束投放时间
	 */
	@Column(name = "end_time", length = 255)
	private Date endTime;

	/**
	 * 价格信息
	 */
	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@JoinColumn(name = "price_id")
	private Price price;

	/**
	 * 广告信息
	 */
	@ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@JoinColumn(name = "advertisement_id")
	private Advertisement advertisement;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 10)
	private byte isDeleted;

	/**
	 * 创建用户id
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改用户ID
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
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
	 * 投放天数
	 */
	@Column(name = "days", length = 50)
	private Integer days;

	/**
	 * 投放车种，1 网约车，2 教练车 ，3 都是
	 */
	@Column(name = "car_type", length = 50)
	private Integer carType;
	
	/**
	 * 区域
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "region_id")
	private Region regionId;


}
