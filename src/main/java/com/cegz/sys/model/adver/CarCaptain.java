/**
 * 
 */
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
/**
 * 车队长实体类
 * @author yucheng
 * @date 2018年12月03日
 */
@Entity
@Table(name = "car_captain")
@Setter
@Getter
public class CarCaptain implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 车队长姓名
	 */
	@Column(name = "name", length = 200)
	private String name;
	
	/**
	 * 车队长电话
	 */
	@Column(name = "phone",length = 20)
	private String phone;
	
	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 10)
	private byte isDeleted;

	/**
	 * 创建用户id
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改用户ID
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

}
