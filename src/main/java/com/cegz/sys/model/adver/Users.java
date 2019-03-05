package com.cegz.sys.model.adver;
/**
 * 用户实体类
 * @author lijiaxin
 * @date 2018年7月19日
 */

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "users")
@Setter
@Getter
@Proxy(lazy = false)
public class Users implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * uuid
	 */
	@Column(name = "uuid", length = 100)
	private String uuid;

	/**
	 * 账号
	 */
	@Column(name = "username", length = 100)
	private String userName;

	/**
	 * 密码
	 */
	@Column(name = "password", length = 100)
	private String password;

	/**
	 * 二次密码
	 */
	@Column(name = "password_salt", length = 100)
	private String passwordSalt;

	/**
	 * 姓名
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 手机号
	 */
	@Column(name = "phone", length = 100)
	private String phone;

	/**
	 * 邮箱地址
	 */
	@Column(name = "email", length = 100)
	private String email;

	/**
	 * 头像地址
	 */
	@Column(name = "icon", length = 100)
	private String icon;

	/**
	 * 数据是否有效，0 有效，1无效
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
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改用户
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "update_user_id")
	private Users updateUserId;

	@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "createUserId", fetch = FetchType.EAGER)
	private Wallet wallet;

	@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "createUserId", fetch = FetchType.LAZY)
	private Contacts contact;

	@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "createUserId", fetch = FetchType.EAGER)
	private Sponsor sponsor;

	@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "createUserId", fetch = FetchType.LAZY)
	private Advertiser advertiser;

	@OneToMany(cascade = CascadeType.REFRESH, mappedBy = "createUserId", fetch = FetchType.LAZY)
	private List<Order> listOrder;

	@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "createUserId", fetch = FetchType.LAZY)
	private Agent agent;
	
	@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "createUserId", fetch = FetchType.LAZY)
	private CarCaptain carCaptain;

	@OneToMany(mappedBy = "usersId", fetch = FetchType.EAGER)
	private List<UsersRole> listRole;

	@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "users", fetch = FetchType.LAZY)
	private BankCard bankCard;

	@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "users", fetch = FetchType.LAZY)
	private PayAccount payAccount;

}
