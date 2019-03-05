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
 * 数据字典实体类
 * 
 * @author tenglong
 * @date 2018年8月14日
 */
@Entity
@Table(name = "data_dictionary")
@Getter
@Setter
public class DataDictionary implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 编码
	 */
	@Column(name = "code", length = 20)
	private String code;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 20)
	private String name;

	/**
	 * 值
	 */
	@Column(name = "value", length = 100)
	private String value;

	/**
	 * 排序
	 */
	@Column(name = "sort", length = 4)
	private int sort;

	/**
	 * 详情
	 */
	@Column(name = "detail", length = 255)
	private String detail;

	/**
	 * 创建用户id
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改用户id
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

}
