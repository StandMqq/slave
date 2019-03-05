package com.cegz.sys.model.adver;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
/**
 * 车主收益记录
 * 
 * @author lijiaxin
 * @date 2018年12月20日
 */
@Entity
@Table(name = "contact_income_record")
@Setter
@Getter
public class ContactIncomeRecord implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**
	 * 收益金额
	 */
	@Column(name = "money")
	private BigDecimal money;
	
	/**
	 * 分配比例
	 */
	@Column(name = "discount")
	private BigDecimal discount;
	
	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted")
	private Byte isDeleted;
	
	/**
	 * 类型，1 广告分红，2 地图红包分红
	 */
	@Column(name = "type")
	private Byte type;
	
	/**
	 * 广告外键
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	
	/**
	 * 车辆外键
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "driving_registration_id")
	private DrivingRegistration drivingRegistration;
	
	/**
	 * 创建时间
	 */
	@Column(name = "create_time", nullable = false, length = 50)
	private Date createTime;
	
}
