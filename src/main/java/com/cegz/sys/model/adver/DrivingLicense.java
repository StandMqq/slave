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
 * 驾驶证实体类
 * 
 * @author lijiaxin
 * @date 2018年7月19日
 */
@Entity
@Table(name = "driving_license")
@Getter
@Setter
public class DrivingLicense implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 证件号
	 */
	@Column(name = "certificate_number", length = 100)
	private String certificateNumber;

	/**
	 * 姓名
	 */
	@Column(name = "user_name", length = 100)
	private String userName;

	/**
	 * 性别
	 */
	@Column(name = "sex", length = 10)
	private Byte sex;

	/**
	 * 国籍
	 */
	@Column(name = "country", length = 10)
	private String country;

	/**
	 * 住址
	 */
	@Column(name = "address", length = 10)
	private String address;

	/**
	 * 出生日期
	 */
	@Column(name = "birthday", length = 100)
	private Date birthday;

	/**
	 * 初次领证日期
	 */
	@Column(name = "first", length = 100)
	private String first;

	/**
	 * 准驾车型
	 */
	@Column(name = "car_type", length = 100)
	private String carType;

	/**
	 * 开始有效日期
	 */
	@Column(name = "valid_start", length = 100)
	private Date vaildStart;

	/**
	 * 结束有效日期
	 */
	@Column(name = "valid_end", length = 100)
	private Date vaildEnd;

	/**
	 * 发证机关
	 */
	@Column(name = "organization", length = 200)
	private String organization;

	/**
	 * 图片地址
	 */
	@Column(name = "picture_url", length = 200)
	private String pictureUrl;

	/**
	 * 是否有效，0有效，1无效
	 */
	@Column(name = "is_deleted", length = 100)
	private Date isDeleted;

	/**
	 * 开始有效日期
	 */
	@Column(name = "accuracy", length = 100)
	private Float accuracy;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time", length = 100)
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time", length = 100)
	private Date updateTime;

	/**
	 * 创建userId
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改userId
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "update_user_id")
	private Users updateUserId;

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

}
