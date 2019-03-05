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
 * 代理商实体类
 * 
 * @author lijiaxin
 * @date 2018年8月1日
 */
@Entity
@Table(name = "agent")
@Getter
@Setter
public class Agent implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 手机号
	 */
	@Column(name = "phone", length = 20)
	private String phone;

	/**
	 * 城市
	 */
	@Column(name = "city", length = 20)
	private String city;

	/**
	 * 姓名
	 */
	@Column(name = "name", length = 200)
	private String name;

	/**
	 * 省份
	 */
	@Column(name = "province", length = 50)
	private String province;

	/**
	 * 公司名称
	 */
	@Column(name = "company", length = 100)
	private String company;

	/**
	 * 营业执照编号
	 */
	@Column(name = "business", length = 100)
	private String business;

	/**
	 * 住址
	 */
	@Column(name = "address", length = 100)
	private String address;

	/**
	 * 营业执照图片
	 */
	@Column(name = "picture_url", length = 100)
	private String pictureUrl;

	/**
	 * email 邮箱
	 */
	@Column(name = "email", length = 100)
	private String email;

	/**
	 * 详细住址
	 */
	@Column(name = "address_detail", length = 100)
	private String addressDetail;

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
	 * 区域
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "region_id")
	private Region regionId;
}
