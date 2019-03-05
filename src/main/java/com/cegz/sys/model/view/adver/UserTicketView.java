package com.cegz.sys.model.view.adver;

import java.math.BigDecimal;

/**
 * 用户和劵的关系视图
 * 
 * @author tenglong
 * @date 2018年9月12日
 */
public class UserTicketView {
	private Long id;
	/**
	 * 领取人手机号
	 */
	private String phone;

	/**
	 * 领取金额
	 */
	private BigDecimal money;

	/**
	 * 领取时间
	 */
	private String receiveTime;

	/**
	 * 消费时间
	 */
	private String consumeTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

}
