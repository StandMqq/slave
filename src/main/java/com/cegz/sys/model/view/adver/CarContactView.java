package com.cegz.sys.model.view.adver;

/**
 * 车主视图
 * 
 * @author lijiaxin
 * @date 2018年8月7日
 */
public class CarContactView {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 时间
	 */
	private String time;

	/**
	 * 是否绑定设备
	 */
	private String bindDevice;

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
	
	/**
	 * 设备号（临时字段）
	 */
	private String devicesNum;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBindDevice() {
		return bindDevice;
	}

	public void setBindDevice(String bindDevice) {
		this.bindDevice = bindDevice;
	}

	public Double getOpenTimeLength() {
		return openTimeLength;
	}

	public void setOpenTimeLength(Double openTimeLength) {
		this.openTimeLength = openTimeLength;
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

	public String getDevicesNum() {
		return devicesNum;
	}

	public void setDevicesNum(String devicesNum) {
		this.devicesNum = devicesNum;
	}

}
