package com.cegz.sys.model.view.adver;

/**
 * 黑名单视图
 *
 * @author tenglong
 * @date 2018年8月21日
 */
public class AuthorityView {
	private Long id;
	/**
	 * 权限编号
	 */
	private String authNo;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 名单等级 0 黑名单，1 黄名单，2 白名单，3 管理员
	 */
	private Integer grade;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	private byte isDeleted;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 修改时间
	 */
	private String updateTime;

	/**
	 * 被操作人电话
	 */
	private String beiOperatorPhone;

	/**
	 * 操作人电话
	 */
	private String operatorPhone;

	/**
	 * 加入黑名单原因描述
	 */
	private String remark;

	/**
	 * 车牌号
	 */
	private String plateNumber;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthNo() {
		return authNo;
	}

	public void setAuthNo(String authNo) {
		this.authNo = authNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
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

	public String getBeiOperatorPhone() {
		return beiOperatorPhone;
	}

	public void setBeiOperatorPhone(String beiOperatorPhone) {
		this.beiOperatorPhone = beiOperatorPhone;
	}

	public String getOperatorPhone() {
		return operatorPhone;
	}

	public void setOperatorPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
}
