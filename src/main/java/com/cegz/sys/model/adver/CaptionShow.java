package com.cegz.sys.model.adver;


import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

/**
 * 字幕实体类
 *
 * @author maqianqian
 * @date 2019年02月21日
 */

@Entity
@Table(name = "caption_show")
@Data
@Proxy(lazy = false)
public class CaptionShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 字幕内容
     */
    @Column(name = "content", length = 255)
    private String content;

    /**
     * 数据是否有效， 0 有效， 1 无效
     */
    @Column(name = "is_deleted", length = 4)
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "create_time", length = 50)
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time", length = 50)
    private Date updateTime;

    /**
     * 创建者外键
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    private Users createUserId;

    /**
     * 修改者外键
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "update_user_id")
    private Users updateUserId;

}
