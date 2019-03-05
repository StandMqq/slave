package com.cegz.sys.model.shop;

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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 红包
 * 
 * @author lijiaxin
 * @date 2018年10月10日
 */
@Entity
@Table(name = "red_packet")
@Setter
@Getter
public class RedPacket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 编号
	 */
	@Column(name = "number")
	private String number;

	/**
	 * 总数
	 */
	@Column(name = "total")
	private Integer total;

	/**
	 * 剩余红包数
	 */
	@Column(name = "current_total")
	private Integer currentTotal;

	/**
	 * 红包金额
	 */
	@Column(name = "money")
	private BigDecimal money;

	/**
	 * 红包状态，0 有效，1 已经领取完
	 */
	@Column(name = "status")
	private byte status;

	/**
	 * 创建者
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUser;

	/**
	 * 修改者
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "update_user_id")
	private Users updateUser;

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

	/**
	 * 广告方用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 广告方广告id
	 */
	@Column(name = "adver_id")
	private Long adverId;

	/**
	 * 红包类型 1 企业红包，2 手动红包
	 */
	@Column(name = "type")
	private byte type;
	
	/**
	 * 开始时间
	 */
	@Column(name = "start_time")
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	@Column(name = "end_time")
	private Date endTime;
	
	/**
	 * 天数
	 */
	@Column(name = "days")
	private Integer days;
	
	/**
	 * 剩余金额
	 */
	@Column(name = "current_money")
	private BigDecimal currentMoney;
	
	/**
	 * 经度
	 */
	@Column(name = "lng")
	private String lng;
	
	/**
	 * 纬度
	 */
	@Column(name = "lat")
	private String lat;
	
	/**
	 * 地址
	 */
	@Column(name = "address")
	private String address;

}
