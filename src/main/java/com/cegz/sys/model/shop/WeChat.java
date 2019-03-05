package com.cegz.sys.model.shop;

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
 * 微信实体类
 * 
 * @author lijiaxin
 * @date 2018年8月1日
 */
@Entity
@Table(name = "wechat")
@Setter
@Getter
public class WeChat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 微信openId
	 */
	@Column(name = "open_id")
	private String openId;

	/**
	 * 微信sessionkey
	 */
	@Column(name = "session_key")
	private String sessionKey;

	/**
	 * 微信唯一id，微信号
	 */
	@Column(name = "union_id")
	private String unionId;

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
	 * 昵称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 用户id
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private Users users;
}
