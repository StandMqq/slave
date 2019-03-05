package com.cegz.sys.model.shop;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "users")
@Setter
@Getter
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 账号名
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 最后登录时间
	 */
	@Column(name = "last_login_time")
	private Date loginTime;

	/**
	 * 创建ip
	 */
	@Column(name = "create_ip")
	private String createIp;

	/**
	 * 登录ip
	 */
	@Column(name = "last_login_ip")
	private String loginIp;

	/**
	 * 手机号
	 */
	@Column(name = "phone")
	private String phone;
	
	/**
	 * 姓名
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 邮箱
	 */
	@Column(name = "email")
	private String email;

	/**
	 * 用户uuid
	 */
	@Column(name = "uuid")
	private String uuid;

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
	 * 支付宝账号
	 */
	@Column(name = "pay_account")
	private String payAccount;
	
	@OneToOne(mappedBy = "users", fetch = FetchType.EAGER)
	private WeChat wechat;

}
