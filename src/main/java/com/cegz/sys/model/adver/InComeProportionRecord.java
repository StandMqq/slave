package com.cegz.sys.model.adver;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * 收益比例记录实体类
 * @author yucheng
 * @date 2018年12月17号
 */
@Entity
@Table(name = "income_proportion_record")
@Getter
@Setter
public class InComeProportionRecord implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
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
	 * 收益类型: 1,保荐方收益比例,2,车队长收益比例
	 */
	@Column(name = "type")
	private Byte type;
	
	/**
	 * 当月收益比例：按百分比计算
	 */
	@Column(name = "now_propertion")
	private BigDecimal nowPropertion;
	
	
	/**
	 * 下月收益比例：按百分比计算
	 */
	@Column(name = "set_propertion")
	private BigDecimal setPropertion;
	
	/**
	 * 车队长ID
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "carcaptain_id")
	private CarCaptain carCaptain; 
	
	/**
	 * 保荐方ID
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "sponsor_id")
	private Sponsor sponsor; 
	
	/**
	 * 数据状态 0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 100)
	private Byte isDeleted;
	
	
	
}
