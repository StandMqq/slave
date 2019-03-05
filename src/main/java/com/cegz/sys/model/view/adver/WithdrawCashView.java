package com.cegz.sys.model.view.adver;

/**
 * 提现视图
 *
 * @author tenglong
 * @date 2018年11月5日
 */
public class WithdrawCashView {
	private Long id;
	/**
	 * 提现类型，1 支付宝，2 银行卡
	 */
	private int type;

	/**
	 * 银行卡姓名
	 */
	private String bankCardName;

	/**
	 * 开户行
	 */
	private String openingBank;

	/**
	 * 银行卡卡号
	 */
	private String bankNum;

	/**
	 * 支付宝姓名
	 */
	private String alipayName;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankCardName() {
		return bankCardName;
	}

	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}

	public String getOpeningBank() {
		return openingBank;
	}

	public void setOpeningBank(String openingBank) {
		this.openingBank = openingBank;
	}

	public String getBankNum() {
		return bankNum;
	}

	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}

	public String getAlipayName() {
		return alipayName;
	}

	public void setAlipayName(String alipayName) {
		this.alipayName = alipayName;
	}

	public String getAlipayCount() {
		return alipayCount;
	}

	public void setAlipayCount(String alipayCount) {
		this.alipayCount = alipayCount;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
