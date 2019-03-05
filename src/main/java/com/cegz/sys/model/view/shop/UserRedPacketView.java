package com.cegz.sys.model.view.shop;

/**
 * 用户红包视图
 * 
 * @author tenglong
 * @date 2018年9月5日
 */
public class UserRedPacketView {
	private Long id;
	/**
	 * 微信昵称
	 */
	private String wxName;

	/**
	 * 领取时间
	 */
	private String createTime;

	/**
	 * 领取金额
	 */
	private Double money;

	/**
	 * 红包类型，1 企业红包，2 手动红包
	 */
	private int type;

	/**
	 * 财务转账状态， 0 未完成，1 已经完成
	 */
	private int status;

	/**
	 * 提现状态，0 提现中，1 成功，2 失败
	 */
	private int wcStatus;

	/**
	 * 转账账号（支付宝账号）
	 */
	private String payAccount;

	/**
	 * 广告名称
	 */
	private String advertisementTitle;

	/**
	 * 对应车牌
	 */
	private String plateNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWxName() {
		return wxName;
	}

	public void setWxName(String wxName) {
		this.wxName = wxName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAdvertisementTitle() {
		return advertisementTitle;
	}

	public void setAdvertisementTitle(String advertisementTitle) {
		this.advertisementTitle = advertisementTitle;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public int getWcStatus() {
		return wcStatus;
	}

	public void setWcStatus(int wcStatus) {
		this.wcStatus = wcStatus;
	}

}
