/**
 * 
 */
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
 * 地区区域表
 * @author yucheng
 * @date 2019年1月14号
 */
@Entity
@Table(name = "region")
@Getter
@Setter
public class Region implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 数据是否有效 0 有效，1无效
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
	 * 区域名称
	 */
	@Column(name = "title", length = 255)
	private String title;

	/**
	 * 区域英文名称
	 */
	@Column(name = "chtitle", length = 255)
	private String chtitle;

	/**
	 * pid
	 */
	@Column(name = "pid", length = 255)
	private String pid;


	/**
	 * pids
	 */
	@Column(name = "pids", length = 255)
	private String pids;


	/**
	 * 序号
	 */
	 @Column(name = "order_num", length = 255)
	 private Integer orderNum;

	 /**
	   *等级：1：省，自治区，直辖市 2：市 3：区，县 4：镇  
	  */
	  @Column(name = "grade", length = 255)
	  private Integer grade;
}
