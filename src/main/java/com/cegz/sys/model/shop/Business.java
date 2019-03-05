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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 商户实体
 * 
 * @author lijiaxin
 * @date 2018年8月1日
 */
@Entity
@Table(name = "business")
@Setter
@Getter
public class Business {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 商户名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 商户编号
	 */
	@Column(name = "business_no")
	private String businessNo;

	/**
	 * 区域地址
	 */
	@Column(name = "address")
	private String address;

	/**
	 * 详细地址
	 */
	@Column(name = "address_detail")
	private String addressDetail;

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
	 * 用户id
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users users;

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
	 * 手机号
	 */
	@Column(name = "phone")
	private String phone;

	/**
	 * 商户图片，多个图片使用逗号分割
	 */
	@Column(name = "picture_urls")
	private String pictureUrls;

}
