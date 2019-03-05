package com.cegz.sys.model.view.adver;

import java.math.BigDecimal;

import javax.persistence.Column;

/**
 * 广告视图
 *
 * @author lijiaxin
 * @date 2018年7月25日
 */
public class AdvertisementView {
	private Long id;
	/**
	 * 标题
	 */
	private String title;

	/**
	 * 缩略图地址
	 */
	private String titleImgUrl;
	
	/**
	 * 广告logo地址
	 */
	private String iconImgUrl;
	/**
	 * 经度
	 */
	private String lng;
	
	/**
	 * 纬度
	 */

	private String lat;
	/**
	 * 内容
	 */
	private String content;

	/**
	 * 内容图片地址集合，用逗号分隔
	 */
	private String contentImages;

	/**
	 * 订单状态 0 审核中，1 进行中，2 完成，3 失败
	 */
	private int status;

	/**
	 * 原因
	 */
	private String reason;

	/**
	 * 车辆类型 1 网约车，2 教练车 , 3 网约车和教练车
	 */
	private int carType;

	/**
	 * 广告类型 001：网约车图片广告，002：网约车文字广告，003：教练车图片广告，007:地图红包广告
	 */
	private String advertisementType;

	/**
	 * 广告类型名称
	 */
	private String advertisementTypeName;

	/**
	 * 广告主名称
	 */
	private String advertiserName;

	/**
	 * 广告主电话
	 */
	private String advertiserPhone;

	/**
	 * 创建人电话
	 */
	private String createPhone;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 广告发布开始时间
	 */
	private String publishStartTime;

	/**
	 * 广告发布结束时间
	 */
	private String publishEndTime;

	/**
	 * 广告发布周期
	 */
	private int publishDay = 0;

	/**
	 * 广告点击数
	 */
	private Integer clickNum;

	/**
	 * 车牌号码
	 */
	private String plateNumber;
	
	/**
	 * 车牌号码
	 */
	private String income;

	/**
	 * 套餐id
	 */
	private Long priceId;

	/**
	 * 套餐名称
	 */
	private String priceName;

	/**
	 * 套餐价格
	 */
	private Double price;

	/**
	 * 付费区分
	 */
	private String payDistinguish;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public int getPublishDay() {
		return publishDay;
	}

	public void setPublishDay(int publishDay) {
		this.publishDay = publishDay;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleImgUrl() {
		return titleImgUrl;
	}

	public void setTitleImgUrl(String titleImgUrl) {
		this.titleImgUrl = titleImgUrl;
	}

	public String getIconImgUrl() {
		return iconImgUrl;
	}

	public void setIconImgUrl(String iconImgUrl) {
		this.iconImgUrl = iconImgUrl;
	}


	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getContentImages() {
		return contentImages;
	}

	public void setContentImages(String contentImages) {
		this.contentImages = contentImages;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	public String getAdvertisementType() {
		return advertisementType;
	}

	public void setAdvertisementType(String advertisementType) {
		this.advertisementType = advertisementType;
	}

	public String getAdvertisementTypeName() {
		return advertisementTypeName;
	}

	public void setAdvertisementTypeName(String advertisementTypeName) {
		this.advertisementTypeName = advertisementTypeName;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAdvertiserPhone() {
		return advertiserPhone;
	}

	public void setAdvertiserPhone(String advertiserPhone) {
		this.advertiserPhone = advertiserPhone;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public Long getPriceId() {
		return priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCreatePhone() {
		return createPhone;
	}

	public void setCreatePhone(String createPhone) {
		this.createPhone = createPhone;
	}

	public String getPublishStartTime() {
		return publishStartTime;
	}

	public void setPublishStartTime(String publishStartTime) {
		this.publishStartTime = publishStartTime;
	}

	public String getPublishEndTime() {
		return publishEndTime;
	}

	public void setPublishEndTime(String publishEndTime) {
		this.publishEndTime = publishEndTime;
	}

	public String getPayDistinguish() {
		return payDistinguish;
	}

	public void setPayDistinguish(String payDistinguish) {
		this.payDistinguish = payDistinguish;
	}

}
