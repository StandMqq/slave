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
 * 价格实体类
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Entity
@Table(name = "price")
@Getter
@Setter
public class Price implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 名称
	 */
	@Column(name = "name", length = 255)
	private String name;

	/**
	 * 价格编号
	 */
	@Column(name = "price_no", length = 255)
	private String priceNo;

	/**
	 * 价格
	 */
	@Column(name = "price", length = 255)
	private Double price;

	/**
	 * 折扣
	 */
	@Column(name = "discount", length = 255)
	private Double discount;

	/**
	 * 描述
	 */
	@Column(name = "remark", length = 255)
	private String remark;

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

}
