package com.cegz.sys.model.view.shop;

/**
 * 商户视图
 * 
 * @author tenglong
 * @date 2018年9月18日
 */
public class BusinessView {
	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 区域地址
	 */
	private String address;

	/**
	 * 详细地址
	 */
	private String addressDetail;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	private byte isDeleted;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 商店编号
	 */
	private String businessNo;

	/**
	 * 经度
	 */
	private String lng;

	/**
	 * 纬度
	 */
	private String lat;

	/**
	 * 商户图片，多个图片使用逗号分割
	 */
	private String pictureUrls;

	/**
	 * 商户关联的劵
	 */
	private String ticketTag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getPictureUrls() {
		return pictureUrls;
	}

	public void setPictureUrls(String pictureUrls) {
		this.pictureUrls = pictureUrls;
	}

	public String getTicketTag() {
		return ticketTag;
	}

	public void setTicketTag(String ticketTag) {
		this.ticketTag = ticketTag;
	}

}
