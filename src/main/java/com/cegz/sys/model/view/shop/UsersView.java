package com.cegz.sys.model.view.shop;

/**
 * 用户视图
 *
 * @author tenglong
 * @date 2018年8月15日
 */
public class UsersView {
	private Long id;
	/**
	 * 账号名
	 */
	private String userName;

	/**
	 * 最后登录时间
	 */
	private String loginTime;

	/**
	 * 创建ip
	 */
	private String createIp;

	/**
	 * 登录ip
	 */
	private String loginIp;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 用户uuid
	 */
	private String uuid;

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

	/**
	 * 微信名称
	 */
	private String wxName;

	/**
	 * 领券数
	 */
	private Long pullNum;

	/**
	 * 使用数
	 */
	private Long useNum;

	/**
	 * 积分
	 */
	private int integral;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public String getWxName() {
		return wxName;
	}

	public void setWxName(String wxName) {
		this.wxName = wxName;
	}

	public Long getPullNum() {
		return pullNum;
	}

	public void setPullNum(Long pullNum) {
		this.pullNum = pullNum;
	}

	public Long getUseNum() {
		return useNum;
	}

	public void setUseNum(Long useNum) {
		this.useNum = useNum;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

}
