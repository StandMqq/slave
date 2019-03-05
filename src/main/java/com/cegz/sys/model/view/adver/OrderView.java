package com.cegz.sys.model.view.adver;

/**
 * 订单视图
 *
 * @author tenglong
 * @date 2018年7月31日
 */
public class OrderView {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 车辆数
	 */
	private Integer carSum;

	/**
	 * 实际金额
	 */
	private Double realMoney;

	/**
	 * 总金额
	 */
	private Double totalMoney;

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

	public Integer getCarSum() {
		return carSum;
	}

	public void setCarSum(Integer carSum) {
		this.carSum = carSum;
	}

	public Double getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(Double realMoney) {
		this.realMoney = realMoney;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
