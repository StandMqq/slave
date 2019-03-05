package com.cegz.sys.model.view.shop;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 提现视图
 *
 * @author tenglong
 * @date 2018年11月5日
 */
public class WithdrawCashView {
	private Long id;
	
	private Long contactId;
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 支付宝账号
	 */
	private String alipayCount;

	/**
	 * 提现状态，0 提现中，1 成功，2 失败
	 */
	private int status;

	/**
	 * 提现金额
	 */
	private Double putMoney;

	/**
	 * 提现时间
	 */
	private String createTime;

	/**
	 * 微信名称
	 */
	private String wxName;

	/**
	 * 操作时间
	 */
	private String updateTime;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 车主姓名
	 */
	private String contactName;
	
	/**
	 * 保荐方名称
	 */
	private String sponsorName;
	
	/**
	 * 车主收益比例
	 */
	private BigDecimal incomePropertion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public BigDecimal getIncomePropertion() {
		return incomePropertion;
	}

	public void setIncomePropertion(BigDecimal incomePropertion) {
		this.incomePropertion = incomePropertion;
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

	public String getAlipayCount() {
		return alipayCount;
	}

	public void setAlipayCount(String alipayCount) {
		this.alipayCount = alipayCount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Double getPutMoney() {
		return putMoney;
	}

	public void setPutMoney(Double putMoney) {
		this.putMoney = putMoney;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getWxName() {
		return wxName;
	}

	public void setWxName(String wxName) {
		this.wxName = wxName;
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
