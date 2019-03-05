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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

// 积分商品兑换
@Entity
@Table(name = "integral_goods_record")
@Setter
@Getter
public class IntegralGoodsRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 电话号码
	 */
	@Column(name = "phone")
	private String phone;

	/**
	 * 描述
	 */
	@Column(name = "remark")
	private String remark;
	
	/**
	 * 状态，0 处理中，1 完成，2 失败
	 */
	@Column(name = "status")
	private Byte status;
	
	/**
	 * 是否有效，0 有效，1 无效
	 */
	@Column(name = "is_deleted")
	private Byte isDeleted;
	
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
   
	/**
	 * 用户id
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users users;
	
	/**
	 * 商品信息
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_id")
	private IntegralGoods goods;
	
	/**
	 * 用户id
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "record_id")
	private IntegralRecord record;
	

   
}
