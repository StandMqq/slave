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
 * 广告发布实体类
 * 
 * @author lijiaxin
 * @date 2018年7月30日
 */
@Entity
@Table(name = "publish_advertisement_record")
@Getter
@Setter
public class PublishAdverRecord implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 车辆类型 1 网约车 2 教练车
	 */
	@JoinColumn(name = "publish_car_type")
	private Integer carType;

	/**
	 * 发布天数
	 */
	@JoinColumn(name = "publish_day")
	private int publishDay;

	/**
	 * 状态，0 无效，1有效
	 */
	@JoinColumn(name = "status")
	private int status;

	/**
	 * 发布开始时间
	 */
	@JoinColumn(name = "start_time")
	private Date startTime;

	/**
	 * 发布结束时间
	 */
	@JoinColumn(name = "end_time")
	private Date endTime;

	/**
	 * 广告信息
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "advertisement_id")
	private Advertisement advertisement;

	/**
	 * 订单信息
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	/**
	 * 设备信息
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "device_id")
	private Device device;

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

}
