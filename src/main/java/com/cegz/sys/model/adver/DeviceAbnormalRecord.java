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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 设备异常记录实体类
 * @author yucheng
 * @date 2018年12月20日
 */
@Entity
@Table(name = "device_abnormal_record")
@Getter
@Setter
public class DeviceAbnormalRecord implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 在线时间
	 */
	@Column(name = "on_line_time", length = 100)
	private Date onLineTime;
	
	/**
	 *到目前不在线的 离线时间
	 */
	@Column(name = "off_line_time", length = 100)
	private Date offLineTime;
	
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
	 * 处理异常设备时间
	 */
	@Column(name = "contact_time", length = 100)
	private Date contactTime;
	
	/**
	 * 数据状态 0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 100)
	private Byte isDeleted;
	
	/**
	 * 设备号
	 */
	@Column(name = "imei", length = 20)
	private String imei;
	
	/**
	 * 处理状态：0：未处理；1：已处理
	 */
	@Column(name = "status", length = 20)
	private Byte status;
	
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
}
