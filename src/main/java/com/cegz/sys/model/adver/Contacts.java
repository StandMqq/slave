package com.cegz.sys.model.adver;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 联系人实体类
 * 
 * @author lijiaxin
 * @date 2018年7月19日
 */
@Entity
@Table(name = "contacts")
@Getter
@Setter
public class Contacts implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 手机号
	 */
	@Column(name = "phone", length = 20)
	private String phone;

	/**
	 * 驾驶证id
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "driving_license_id")
	private DrivingLicense drivingLicenseId;

	/**
	 * 姓名
	 */
	@Column(name = "name", length = 200)
	private String name;

	/**
	 * 身份证id
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcard_id")
	private IdCard idcardId;

	/**
	 * 审核状态
	 */
	@Column(name = "status", length = 10)
	private byte status;

	/**
	 * 审核结论
	 */
	@Column(name = "reason", length = 200)
	private String reason;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 10)
	private byte isDeleted;

	/**
	 * 创建用户id
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改用户ID
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
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
	 * 车辆信息列表
	 */
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private List<DrivingRegistration> listDrivingRegistration;



}
