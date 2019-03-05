package com.cegz.sys.model.view.adver;

/**
 * 发票视图
 *
 * @author tenglong
 * @date 2018年11月20日
 */
public class InvoiceView {
	private Long id;
	/**
	 * 发票抬头类型 1 企业，2 个人
	 */
	private Byte type;

	/**
	 * 抬头名称
	 */
	private String company;

	/**
	 * 税号
	 */
	private String number;

	/**
	 * 发票内容
	 */
	private String remark;

	/**
	 * 发票金额
	 */
	private Double money;

	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 是否有效，0 有效，1 无效；
	 */
	private Byte isDeleted;

	/**
	 * 创建人
	 */
	private String createPerson;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 税费
	 */
	private Double taxation;

	/**
	 * 价税合计
	 */
	private Double priceTaxationSum;

	/**
	 * 发票图片
	 */
	private String imgUrl;

	/**
	 * 状态，0 未完成，1 处理中，2 完成，3 失败
	 */
	private Byte status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Double getTaxation() {
		return taxation;
	}

	public void setTaxation(Double taxation) {
		this.taxation = taxation;
	}

	public Double getPriceTaxationSum() {
		return priceTaxationSum;
	}

	public void setPriceTaxationSum(Double priceTaxationSum) {
		this.priceTaxationSum = priceTaxationSum;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

}
