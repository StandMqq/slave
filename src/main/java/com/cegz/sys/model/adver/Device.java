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
 * 设备实体类
 *
 * @author lijiaxin
 * @date 2018年7月26日
 */
@Entity
@Table(name = "device")
@Getter
@Setter
public class Device implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 设备号
	 */
	@Column(name = "imei", length = 20)
	private String imei;

	/**
	 * 图片广告总数
	 */
	@Column(name = "total")
	private int total;
	
	/**
	 *文字广告总数
	 */
	@Column(name = "script_total")
	private int scriptTotal;

	/**
	 * 物联网卡号
	 */
	@Column(name = "number", length = 20)
	private String number;

	/**
	 * 设备状态 0 正常 1 异常
	 */
	@Column(name = "status", length = 20)
	private int status;

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
	 * 行驶证
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "driving_registration_id")
	private DrivingRegistration drivingRegistration;

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
	 * 绑定时间
	 */
	@Column(name = "bind_time", length = 50)
	private Date bindTime;

}
