package com.cegz.sys.model.view.adver;

/**
 * 角色视图
 *
 * @author tenglong
 * @date 2018年10月30日
 */
public class RoleView {
	private Long id;

	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 角色码
	 */
	private String code;

	/**
	 * 序号
	 */
	private Integer sort;

	/**
	 * 类型
	 */
	private Integer type;

	/**
	 * 描述
	 */
	private String remark;

	/**
	 * 数据是否有效，0 有效，1无效
	 */
	private byte isDeleted;

	/**
	 * 是否选中
	 */
	private boolean isSelected;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
