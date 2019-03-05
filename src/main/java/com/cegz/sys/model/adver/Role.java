package com.cegz.sys.model.adver;
/**
 * 角色实体类
 * @author tenglong
 * @date 2018年10月18日
 */

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Setter
@Getter
public class Role implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 角色名称
	 */
	@Column(name = "name", length = 255)
	private String name;

	/**
	 * 角色码
	 */
	@Column(name = "code", length = 255)
	private String code;

	/**
	 * 序号
	 */
	@Column(name = "sort", length = 11)
	private Integer sort;

	/**
	 * 类型
	 */
	@Column(name = "type", nullable = true, length = 11)
	private Integer type;

	/**
	 * 描述
	 */
	@Column(name = "remark", nullable = true, length = 255)
	private String remark;

	/**
	 * 数据是否有效，0 有效，1无效
	 */
	@Column(name = "is_deleted", length = 4)
	private byte isDeleted;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time", length = 100)
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time", length = 100)
	private Date updateTime;

	/**
	 * 创建用户
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users createUserId;

	/**
	 * 修改用户
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "update_user_id")
	private Users updateUserId;

	/**
	 * 菜单列表
	 */
	@OneToMany(mappedBy = "roleId", fetch = FetchType.EAGER)
	private Set<RoleMenu> listRoleMenu;

}
