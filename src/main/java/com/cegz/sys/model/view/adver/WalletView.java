package com.cegz.sys.model.view.adver;

/**
 * 钱包视图
 *
 * @author lijiaxin
 * @date 2018年7月26日
 */
public class WalletView {
	private Long id;
	/**
	 * 编号
	 */
	private String walletNo;

	/**
	 * 金额
	 */
	private double money;

	/**
	 * 描述
	 */
	private Integer remark;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	private byte isDeleted;

	/**
	 * 账号
	 */
	private String accountNum;

	/**
	 * 充值金额
	 */
	private double rechargeMoney;

	/**
	 * 充值次数
	 */
	private Long rechargeCount;

	/**
	 * 修改时间（最后一次充值的时间）
	 */
	private String updateTime;

	/**
	 * 被充值人id
	 */
	private Long createUserId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWalletNo() {
		return walletNo;
	}

	public void setWalletNo(String walletNo) {
		this.walletNo = walletNo;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Integer getRemark() {
		return remark;
	}

	public void setRemark(Integer remark) {
		this.remark = remark;
	}

	public byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public double getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(double rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	public Long getRechargeCount() {
		return rechargeCount;
	}

	public void setRechargeCount(Long rechargeCount) {
		this.rechargeCount = rechargeCount;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

}
