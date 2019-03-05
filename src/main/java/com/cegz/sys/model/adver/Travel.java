package com.cegz.sys.model.adver;

import java.io.Serializable;
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
 * 车辆移动实体类
 * 
 * @author tenglong
 * @date 2018年11月15日
 */
@Entity
@Table(name = "travel")
@Getter
@Setter
public class Travel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 驾驶里程，单位米
	 */
	@Column(name = "drive_length", length = 255)
	private Double driveLength;

	/**
	 * 驾驶时间，单位秒
	 */
	@Column(name = "drive_time", length = 255)
	private Double driveTime;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time", length = 255)
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Column(name = "end_time", length = 255)
	private Date endTime;

	/**
	 * 开始位置 格式：经度，纬度
	 */
	@Column(name = "start_position", length = 1000)
	private String startPosition;

	/**
	 * 结束位置 格式： 经度、纬度
	 */
	@Column(name = "end_position", length = 10)
	private String endPosition;

	/**
	 * 是否有效，0 有效，1 无效
	 */
	@Column(name = "is_deleted", length = 10)
	private byte isDeleted;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time", length = 50)
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time", length = 50)
	private Date updateTime;

	/**
	 * 设备号
	 */
	@Column(name = "imei", length = 1000)
	private String imei;

}
