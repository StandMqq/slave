package com.cegz.sys.model.view.adver;

/**
 * 用户视图
 *
 * @author tenglong
 * @date 2018年8月15日
 */
public class UsersView {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 是否为管理员
	 */
	private Boolean admin;

	/**
	 * 是否为黑名单
	 */
	private Boolean black;

	/**
	 * 是否设置了二次密码（管理员密码）
	 */
	private Boolean passwordSalt;

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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getBlack() {
		return black;
	}

	public void setBlack(Boolean black) {
		this.black = black;
	}

	public Boolean getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(Boolean passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

}
