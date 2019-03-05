package com.cegz.sys.model.view.shop;

import java.math.BigDecimal;

/**
 * 券视图
 * 
 * @author tenglong
 * @date 2018年9月5日
 */
public class TicketView {
	private Long id;
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 编号
	 */
	private String ticketNo;

	/**
	 * 总记录数
	 */
	private Integer total;

	/**
	 * 领取数
	 */
	private Integer gainCount;

	/**
	 * 有效天数
	 */
	private Integer days;

	/**
	 * 简介
	 */
	private String remark;

	/**
	 * 开始时间
	 */
	private String startTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	/**
	 * 券类型id
	 */
	private Long typeId;

	/**
	 * 券类型名
	 */
	private String typeName;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	private byte isDeleted;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 使用时间
	 */
	private String updateTime;

	/**
	 * 金额
	 */
	private BigDecimal money;

	/**
	 * 联系人
	 */
	private String contacts;

	/**
	 * 图片地址
	 */
	private String pictureUrl;

	/**
	 * 商户id
	 */
	private Long businessId;

	/**
	 * 状态
	 */
	private byte status;

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

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getGainCount() {
		return gainCount;
	}

	public void setGainCount(Integer gainCount) {
		this.gainCount = gainCount;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}
