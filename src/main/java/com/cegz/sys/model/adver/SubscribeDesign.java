package com.cegz.sys.model.adver;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

/**
 * 广告预约实体类
 *
 * @author maqianqian
 * @date 2019年02月15日
 */
@Entity
@Table(name = "subscribe_design")
@Data
@Proxy(lazy = false)
public class SubscribeDesign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 申请人姓名
     */
    @Column(name = "name", length = 255)
    private String name;

    /**
     * 申请人电话
     */
    @Column(name = "phone", length = 255)
    private String phone;

    /**
     * 广告类型，1 小平面广告，2 大平面广告，3 地图广告
     */
    @Column(name = "type", length = 4)
    private Byte type;

    /**
     * 数据是否有效，0 有效，1 无效
     */
    @Column(name = "is_deleted", length = 4)
    private Byte isDeleted;

    /**
     * 审核状态，0 处理中，1 完成，2 失败
     */
    @Column(name = "status", length = 4)
    private Byte status;


    /**
     * 申请时间
     */
    @Column(name = "create_time", length = 50)
    private Date createTime;

    /**
     * 审核时间
     */
    @Column(name = "update_time", length = 50)
    private Date updateTime;

    /**
     * 创建者外键
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    private Users createUserId;

    /**
     * 修改者外键
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_user_id")
    private Users updateUserId;


}
