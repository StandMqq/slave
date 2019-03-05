package com.cegz.sys.model.adver;
/**
 * 资源实体类
 * @author tenglong
 * @date 2018年10月18日
 */

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

@Entity
@Table(name = "resource")
@Setter
@Getter
public class Resource implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 资源名称
	 */
	@Column(name = "name", length = 255)
	private String name;

	/**
	 * 资源地址
	 */
	@Column(name = "url", length = 255)
	private String url;

	/**
	 * 排序
	 */
	@Column(name = "sort", length = 11)
	private int sort;

	/**
	 * 数据是否有效，0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 4)
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

	/**
	 * 备注说明
	 */
	@Column(name = "remark", length = 255)
	private String remark;

}
