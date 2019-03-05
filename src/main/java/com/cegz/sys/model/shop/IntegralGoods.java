package com.cegz.sys.model.shop;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 积分商品实体类
 * 
 * @author tenglong
 * @date 2018年11月29日
 */
@Entity
@Table(name = "integral_goods")
@Setter
@Getter
public class IntegralGoods {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 介绍
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 总量
	 */
	@Column(name = "total")
	private Integer total;

	/**
	 * 当前量
	 */
	@Column(name = "current_total")
	private Integer currentTotal;

	/**
	 * 金额
	 */
	@Column(name = "money")
	private Double money;

	/**
	 * 积分
	 */
	@Column(name = "integral")
	private Integer integral;

	/**
	 * 折扣
	 */
	@Column(name = "discount")
	private Double discount;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private byte status;

	/**
	 * 是否有效 0 有效 ，1 无效
	 */
	@Column(name = "is_deleted")
	private byte isDeleted;

	/**
	 * 图片
	 */
	@Column(name = "images")
	private String images;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}
