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
 * 身份证实体类
 * 
 * @author lijiaxin
 * @date 2018年7月19日
 */
@Entity
@Table(name = "id_card")
@Getter
@Setter
public class IdCard implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 姓名
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 性别
	 */
	@Column(name = "sex", length = 100)
	private String sex;

	/**
	 * 民族
	 */
	@Column(name = "nation", length = 100)
	private String nation;

	/**
	 * 生日
	 */
	@Column(name = "birth", length = 100)
	private Date birth;

	/**
	 * 住址
	 */
	@Column(name = "address", length = 200)
	private String address;

	/**
	 * 证件号
	 */
	@Column(name = "num", length = 100)
	private String num;

	/**
	 * 发证机关
	 */
	@Column(name = "authority", length = 100)
	private String authority;

	/**
	 * 证件有效期
	 */
	@Column(name = "valid_date", length = 100)
	private String vaildDate;

	/**
	 * 证件图片地址
	 */
	@Column(name = "picture_url", length = 100)
	private String pictureUrl;

	/**
	 * 数据状态 0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 100)
	private byte isDeleted;

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
	 * 创建用户
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改用户
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
