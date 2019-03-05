package com.cegz.sys.model.adver;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 银行卡
 * 
 * @author tenglong
 * @date 2018年11月5日
 */
@Entity
@Table(name = "bank_card")
@Setter
@Getter
public class BankCard implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 姓名
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 开户行
	 */
	@Column(name = "bank")
	private String bank;

	/**
	 * 卡号
	 */
	@Column(name = "card_no")
	private String cardNo;

	/**
	 * 数据是否有效 0 有效，1无效
	 */
	@Column(name = "is_deleted")
	private Byte isDeleted;

	/**
	 * 用户信息
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id")
	private Users users;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}
