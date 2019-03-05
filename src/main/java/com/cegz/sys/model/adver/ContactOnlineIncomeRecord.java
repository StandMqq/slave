package com.cegz.sys.model.adver;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * 车主在线收益
 * 
 * @author lijiaxin
 * @date 2018年12月22日
 */
@Entity
@Table(name = "contact_online_income_record")
@Setter
@Getter
public class ContactOnlineIncomeRecord implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;
	
	/**
	 * 在线时间，s
	 */
	@Column(name = "online_time")
	private Long onlineTime;
	
	/**
	 * 统计开始时间
	 */
	@Column(name = "start_time")
	private Date startTime;
	
	/**
	 * 统计结束时间
	 */
	@Column(name = "end_time")
	private Date endTime;
	
	/**
	 * 收益金额
	 */
	@Column(name = "money")
	private BigDecimal money;
	
	/**
	 * 等级，0 无级，1 初级，2 中级，3 高级
	 */
	@Column(name = "grade")
	private Byte grade;
	
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
	 * 修改用户ID
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "driving_registration_id")
	private DrivingRegistration drivingRegistration;
	
	
}
