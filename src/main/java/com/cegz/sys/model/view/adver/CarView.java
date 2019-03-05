package com.cegz.sys.model.view.adver;

/**
 * 车辆视图
 *
 * @author lijiaxin
 * @date 2018年7月26日
 */
public class CarView {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 车牌号
	 */
	private String carNumber;

	/**
	 * 状态 0 审核中，1 完成，2 失败
	 */
	private int Status;

	/**
	 * 车辆类型，1 网约车，2 教练车
	 */
	private int carType;

	/**
	 * 车主
	 */
	private String carOwner;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 所属保荐方名称
	 */
	private String sponsorName;

	/**
	 * 所属保荐方电话
	 */
	private String sponsorPhone;

	/**
	 * 所属保荐方邮件
	 */
	private String sponsorEmail;

	/**
	 * 所属保荐方地址
	 */
	private String sponsorAddress;

	/**
	 * 搭载广告数量
	 */
	private int carryAdvertisementNum;

	/**
	 * 出厂日期
	 */
	private String carBirthday;

	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getCarOwner() {
		return carOwner;
	}

	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getSponsorPhone() {
		return sponsorPhone;
	}

	public void setSponsorPhone(String sponsorPhone) {
		this.sponsorPhone = sponsorPhone;
	}

	public String getSponsorEmail() {
		return sponsorEmail;
	}

	public void setSponsorEmail(String sponsorEmail) {
		this.sponsorEmail = sponsorEmail;
	}

	public String getSponsorAddress() {
		return sponsorAddress;
	}

	public void setSponsorAddress(String sponsorAddress) {
		this.sponsorAddress = sponsorAddress;
	}

	public int getCarryAdvertisementNum() {
		return carryAdvertisementNum;
	}

	public void setCarryAdvertisementNum(int carryAdvertisementNum) {
		this.carryAdvertisementNum = carryAdvertisementNum;
	}

	public String getCarBirthday() {
		return carBirthday;
	}

	public void setCarBirthday(String carBirthday) {
		this.carBirthday = carBirthday;
	}

}
