package com.cegz.sys.model.view.adver;

/**
 * 车辆视图
 *
 * @author lijiaxin
 * @date 2018年7月26日
 */
public class PriceView {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 编号
	 */
	private String priceNo;

	/**
	 * 套餐名
	 */
	private String priceName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPriceNo() {
		return priceNo;
	}

	public void setPriceNo(String priceNo) {
		this.priceNo = priceNo;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

}
