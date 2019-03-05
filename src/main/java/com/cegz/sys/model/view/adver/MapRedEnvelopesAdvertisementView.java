package com.cegz.sys.model.view.adver;

import java.math.BigDecimal;

/**
 * 地图红包视图
 * @author yucheng
 * @date 2018年12月12号
 */
public class MapRedEnvelopesAdvertisementView {

	private Long id;
	
	/**
	 * 广告标题
	 */
	private String name;
	
	/**
	 * 红包金额
	 */
	private String money;
	
	/**
	 * 剩余红包数
	 */
	private String currentTotal;
	
	/**
	 * 红包数
	 */
	private String total;
	
	/**
	 * 发布时间
	 */
	private String publishTime;
	
	/**
	 * 保荐方收益
	 */
	private Double sponsorMoney;
	
	/**
	 * 乘客领取总金额
	 */
	private String contactsMoney;
	
	/**
	 * 剩余金额
	 */
	private String balance;

	public Long getId() {
		return id;
	}

	public void setId(Long long1) {
		this.id = long1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	

	public String getCurrentTotal() {
		return currentTotal;
	}

	public void setCurrentTotal(String currentTotal) {
		this.currentTotal = currentTotal;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public Double getSponsorMoney() {
		return sponsorMoney;
	}

	public void setSponsorMoney(Double sponsorMoney) {
		this.sponsorMoney = sponsorMoney;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getContactsMoney() {
		return contactsMoney;
	}

	public void setContactsMoney(String contactsMoney) {
		this.contactsMoney = contactsMoney;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	
	
	
}
