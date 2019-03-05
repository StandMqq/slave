package com.cegz.sys.model.view.shop;

/**
 * 积分商品视图
 * 
 * @author tenglong
 * @date 2018年11月29日
 */
public class IntegralGoodsView {
	private Long id;
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 介绍
	 */
	private String remark;

	/**
	 * 总量
	 */
	private Integer total;

	/**
	 * 当前量
	 */
	private Integer currentTotal;

	/**
	 * 金额
	 */
	private Double money;

	/**
	 * 积分
	 */
	private Integer integral;

	/**
	 * 折扣
	 */
	private Double discount;

	/**
	 * 状态
	 */
	private byte status;

	/**
	 * 是否有效 0 有效 ，1 无效
	 */
	private byte isDeleted;

	/**
	 * 图片
	 */
	private String images;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 修改时间
	 */
	private String updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getCurrentTotal() {
		return currentTotal;
	}

	public void setCurrentTotal(Integer currentTotal) {
		this.currentTotal = currentTotal;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
