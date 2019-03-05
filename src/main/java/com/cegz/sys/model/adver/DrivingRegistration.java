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
 * 行驶证实体类
 * 
 * @author lijiaxin
 * @date 2018年7月19日
 */
@Entity
@Table(name = "driving_registration")
@Getter
@Setter
public class DrivingRegistration implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 车牌号码
	 */
	@Column(name = "plate_number", length = 100)
	private String plateNumber;

	/**
	 * 车辆类型
	 */
	@Column(name = "vehicle_type", length = 100)
	private String vehicle_type;

	/**
	 * 所有者
	 */
	@Column(name = "owner", length = 100)
	private String owner;

	/**
	 * 住址
	 */
	@Column(name = "address", length = 100)
	private String address;

	/**
	 * 使用性质
	 */
	@Column(name = "use_character", length = 100)
	private String useCharacter;

	/**
	 * 品牌
	 */
	@Column(name = "model", length = 100)
	private String model;

	/**
	 * 车架号
	 */
	@Column(name = "vin", length = 100)
	private String vin;

	/**
	 * 发动机号
	 */
	@Column(name = "engine_number", length = 100)
	private String engineNumber;

	/**
	 * 注册日期
	 */
	@Column(name = "register_date", length = 100)
	private Date registerDate;

	/**
	 * 发证日期
	 */
	@Column(name = "issue_date", length = 100)
	private Date issueDate;

	/**
	 * 识别度
	 */
	@Column(name = "accuracy", length = 100)
	private float accuracy;

	/**
	 * 图片地址
	 */
	@Column(name = "picture_url", length = 200)
	private String pictureUrl;

	/**
	 * 数据状态 0 有效 1 无效
	 */
	@Column(name = "is_deleted", length = 100)
	private byte isDeleted;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time", length = 100)
	private Date updateTime;

	/**
	 * 车辆出厂日期
	 */
	@Column(name = "car_birthday", length = 100)
	private Date carBirthday;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time", length = 100)
	private Date createTime;

	/**
	 * 创建用户
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改用户
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "update_user_id")
	private Users updateUserId;

	/**
	 * 保荐方信息
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "sponsor_id")
	private Sponsor sponsor;

	/**
	 * 联系人信息
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "contact_id")
	private Contacts contact;

	/**
	 * 审核状态 0 审核中，1 成功，2失败
	 */
	@Column(name = "status", length = 11)
	private int status;

	/**
	 * 审核结果原因
	 */
	@Column(name = "reason", length = 11)
	private String reason;

	/**
	 * 安装时间
	 */
	@Column(name = "install_time", length = 100)
	private Date installTime;

	/**
	 * 车队长信息ID
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "carcaptain_id")
	private CarCaptain carCaptain;
}
