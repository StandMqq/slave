package com.cegz.sys.model.view.adver;

/**
 * 行驶证视图
 *
 * @author tenglong
 * @date 2018年7月31日
 */
public class DrivingRegistrationView {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 保荐方
	 */
	private String company;

	/**
	 * 车牌号
	 */
	private String plateNumber;

	/**
	 * 品牌
	 */
	private String model;

	/**
	 * 出厂日期
	 */
	private String carBirthday;

	/**
	 * 搭载广告数量
	 */
	private String carryAdvertisementNum;
	
	public String getCarryAdvertisementNum() {
		return carryAdvertisementNum;
	}

	public void setCarryAdvertisementNum(String carryAdvertisementNum) {
		this.carryAdvertisementNum = carryAdvertisementNum;
	}

	/**
	 * 使用性质
	 */
	private String useCharacter;

	/**
	 * 行驶证照片
	 */
	private String pictureUrl;

	/**
	 * 状态 0 审核中，1 完成，2 失败
	 */
	private int Status;

	/**
	 * 审核原因
	 */
	private String reason;

	/**
	 * 安装时间
	 */
	private String installTime;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 安装时间
	 */
	private String createTime;

	/**
	 * 注册时间
	 */
	private String registerDate;

	/**
	 * 设备在线情况
	 */
	private String deviceOnLineDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUseCharacter() {
		return useCharacter;
	}

	public void setUseCharacter(String useCharacter) {
		this.useCharacter = useCharacter;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCarBirthday() {
		return carBirthday;
	}

	public void setCarBirthday(String carBirthday) {
		this.carBirthday = carBirthday;
	}

	public String getInstallTime() {
		return installTime;
	}

	public void setInstallTime(String installTime) {
		this.installTime = installTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getDeviceOnLineDetail() {
		return deviceOnLineDetail;
	}

	public void setDeviceOnLineDetail(String deviceOnLineDetail) {
		this.deviceOnLineDetail = deviceOnLineDetail;
	}

}
