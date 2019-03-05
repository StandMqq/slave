package com.cegz.sys.model.view.adver;

/**
 * 设备视图
 *
 * @author tenglong
 * @date 2018年8月15日
 */
public class DeviceView {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 设备号
	 */
	private String imei;

	/**
	 * 车牌号
	 */
	private String carNumber;

	/**
	 * 操作时间
	 */
	private String updateTime;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 所属保荐方名称
	 */
	private String sponsorName;

	/**
	 * 车队长姓名
	 */
	private String carCaptainName;
	
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
	 * 物联网卡号
	 */
	private String number;

	/**
	 * 设备状态 0 正常 1 异常
	 */
	private int status;
	

	/**
	 * 图片广告总数
	 */
	private int total;

	/**
	 * 文字广告总数
	 */
	private int scriptTotal;

	/**
	 * 绑定时间
	 */
	private String bindTime;

	/**
	 * 绑定状态
	 */
	private String drivingRegistration;
	
	/**
	 * 在线时间
	 */
	private String onLineTime;
	
	/**
	 * 异常设备处理时间
	 */
	private String contactTime;
	
	/**
	 *到目前不在线的 离线时间
	 */
	private String offLineTime;
	
	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 姓名
	 */
	private String name;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	/**
	 * @return the carCaptainName
	 */
	public String getCarCaptainName() {
		return carCaptainName;
	}

	/**
	 * @param carCaptainName the carCaptainName to set
	 */
	public void setCarCaptainName(String carCaptainName) {
		this.carCaptainName = carCaptainName;
	}

	/**
	 * @return the onLineTime
	 */
	public String getOnLineTime() {
		return onLineTime;
	}

	/**
	 * @param onLineTime the onLineTime to set
	 */
	public void setOnLineTime(String onLineTime) {
		this.onLineTime = onLineTime;
	}

	/**
	 * @return the contactTime
	 */
	public String getContactTime() {
		return contactTime;
	}

	/**
	 * @param contactTime the contactTime to set
	 */
	public void setContactTime(String contactTime) {
		this.contactTime = contactTime;
	}

	/**
	 * @return the offLineTime
	 */
	public String getOffLineTime() {
		return offLineTime;
	}

	/**
	 * @param offLineTime the offLineTime to set
	 */
	public void setOffLineTime(String offLineTime) {
		this.offLineTime = offLineTime;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getScriptTotal() {
		return scriptTotal;
	}

	public void setScriptTotal(int scriptTotal) {
		this.scriptTotal = scriptTotal;
	}

	public String getBindTime() {
		return bindTime;
	}

	public void setBindTime(String bindTime) {
		this.bindTime = bindTime;
	}

	public String getDrivingRegistration() {
		return drivingRegistration;
	}

	public void setDrivingRegistration(String drivingRegistration) {
		this.drivingRegistration = drivingRegistration;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
