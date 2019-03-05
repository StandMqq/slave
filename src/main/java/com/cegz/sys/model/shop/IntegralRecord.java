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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 券实体类
 * 
 * @author lijiaxin
 * @date 2018年8月24日
 */
@Entity
@Table(name = "Integral_record")
@Setter
@Getter
public class IntegralRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 积分
	 */
	@Column(name = "number")
	private Integer number;

	/**
	 * 类型 1 收入，2 支出
	 */
	@Column(name = "type")
	private Byte type;

	/**
	 * 积分
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "integral_id")
	private Integral integral;

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

}
