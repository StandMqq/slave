/**
 * 
 */
package com.cegz.sys.model.view.adver;

/**
 * 保荐方名下详情视图
 * @author yucheng
 * @date 2018年11月27号
 */
public class ContactsView {
	
	private Long id;
	
	/**
	 * 车主姓名
	 */
	private String name;
	
	/**
	 * 车主电话
	 */
	private String phone;
	
	/**
	 * 车牌号
	 */
	private String plateNumber;
	
	/**
	 * 图片广告数
	 */
	private int total = 0;
	
	/**
	 * 文字广告数
	 */
	private int scripttotal = 0;

	/**
	 * 搭载广告数量
	 */
	private String carryAdvertisementNum;
	/**
	 * 加入时间
	 */
	private String createTime;
	
	/**
	 * 开机时长
	 */
	private Double openTimeLength;
	
	/**
	 * 总开机时长
	 */
	private Double totalOpenTimeLength;

	/**
	 * 在线天数
	 */
	private int onLineDay;
	
	
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

	
	public Double getTotalOpenTimeLength() {
		return totalOpenTimeLength;
	}

	public void setTotalOpenTimeLength(Double totalOpenTimeLength) {
		this.totalOpenTimeLength = totalOpenTimeLength;
	}

	public int getOnLineDay() {
		return onLineDay;
	}

	public void setOnLineDay(int onLineDay) {
		this.onLineDay = onLineDay;
	}

	public Double getOpenTimeLength() {
		return openTimeLength;
	}

	public void setOpenTimeLength(Double openTimeLength) {
		this.openTimeLength = openTimeLength;
	}

	public String getPhone() {
		return phone;
	}

	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getCarryAdvertisementNum() {
		return carryAdvertisementNum;
	}

	public void setCarryAdvertisementNum(String string) {
		this.carryAdvertisementNum = string;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	
	public int getTotal() {
		return total;
	}

	
	public void setTotal(int total) {
		this.total = total;
	}



	
	public String getCreateTime() {
		return createTime;
	}

	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	
	public int getScripttotal() {
		return scripttotal;
	}

	
	public void setScripttotal(int scripttotal) {
		this.scripttotal = scripttotal;
	}

	
	
	
}
