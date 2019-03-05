package com.cegz.sys.model.view.sale;

/**
 * 銷售綁定视图
 * 
 * @author tenglong
 * @date 2018年10月18日
 */
public class SellBindUserView {
	private Long id;

	/**
	 * 账号
	 */
	private String phone;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 绑定人数
	 */
	private Long bindNum;

	/**
	 * 累计充值金额
	 */
	private Double rechargeMoneySum;

	/**
	 * 上次充值时间
	 */
	private String lastRechargeTime;

	/**
	 * 客户id
	 */
	private Long userId;

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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getBindNum() {
		return bindNum;
	}

	public void setBindNum(Long bindNum) {
		this.bindNum = bindNum;
	}

	public Double getRechargeMoneySum() {
		return rechargeMoneySum;
	}

	public void setRechargeMoneySum(Double rechargeMoneySum) {
		this.rechargeMoneySum = rechargeMoneySum;
	}

	public String getLastRechargeTime() {
		return lastRechargeTime;
	}

	public void setLastRechargeTime(String lastRechargeTime) {
		this.lastRechargeTime = lastRechargeTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
