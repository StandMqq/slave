package com.cegz.sys.model.view.shop;

/**
 * 券视图
 * 
 * @author tenglong
 * @date 2018年9月5日
 */
public class TicketTypeView {
	private Long id;
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 编号
	 */
	private String remark;

	/**
	 * 编号
	 */
	private String ticketTypeNo;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	private byte isDeleted;

	/**
	 * 创建时间
	 */
	private String createTime;

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

	public byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTicketTypeNo() {
		return ticketTypeNo;
	}

	public void setTicketTypeNo(String ticketTypeNo) {
		this.ticketTypeNo = ticketTypeNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
