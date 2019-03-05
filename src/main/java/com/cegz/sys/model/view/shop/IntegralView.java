package com.cegz.sys.model.view.shop;

/**
 * 积分视图
 *
 * @author tenglong
 * @date 2018年9月26日
 */
public class IntegralView {
	private Long id;
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 总积分
	 */
	private Integer totalNumber;
	
	/**
	 * 所需积分
	 */
	private Integer needNumber;
	
	/**
	 * 可用积分
	 */
	private Integer usableNumber;
	/**
	 * 微信名称
	 */
	private String wXName;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	
	/**
	 * 数据是否有效 0 有效，1无效
	 */
	private byte isDeleted;

	/**
	 * 修改时间
	 */
	private String updateTime;

	/**
	 * 创建时间
	 */
	private String createTime;

	public Integer getNeedNumber() {
		return needNumber;
	}

	public void setNeedNumber(Integer needNumber) {
		this.needNumber = needNumber;
	}

	public String getwXName() {
		return wXName;
	}

	public void setwXName(String wXName) {
		this.wXName = wXName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

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

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Integer getUsableNumber() {
		return usableNumber;
	}

	public void setUsableNumber(Integer usableNumber) {
		this.usableNumber = usableNumber;
	}

	public byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
