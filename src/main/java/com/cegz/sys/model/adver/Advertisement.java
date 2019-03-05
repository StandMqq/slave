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

import lombok.Data;

/**
 * 广告实体类
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Entity
@Table(name = "advertisement")
@Data
public class Advertisement implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 标题
	 */
	@Column(name = "title", length = 255)
	private String title;

	/**
	 * 标题图片
	 */
	@Column(name = "title_pic_url", length = 255)
	private String titlePicUrl;

	/**
	 * logo图片
	 */
	@Column(name = "icon_url", length = 255)
	private String iconUrl;

	/**
	 * 内容图片
	 */
	@Column(name = "content_pic_url", length = 255)
	private String contentPicUrl;

	/**
	 * 内容
	 */
	@Column(name = "content", length = 1000)
	private String content;

	/**
	 * 审核状态 0 审核中，1 完成， 2 失败
	 */
	@Column(name = "status", length = 10)
	private int status;

	/**
	 * 审核失败原因
	 */
	@Column(name = "reason", length = 255)
	private String reason;

	/**
	 * 广告类型
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "advertisement_type_id")
	private AdvertisementType advertisementType;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 10)
	private byte isDeleted;

	/**
	 * 创建用户id
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
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

	/**
	 * 广告方
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "advertiser_id")
	private Advertiser advertiser;

	/**
	 * 套餐
	 */
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "price_id")
	private Price price;

}
