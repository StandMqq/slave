package com.cegz.sys.model.view.adver;

/**
 * 菜单视图
 *
 * @author tenglong
 * @date 2018年10月31日
 */
public class MenuView {
	private Long id;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单
	 */
	private String url;

	/**
	 * 描述
	 */
	private String remark;

	/**
	 * 数据是否有效，0 有效，1无效
	 */
	private byte isDeleted;

	/**
	 * 父级id
	 */
	private Long pid;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
