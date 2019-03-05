package com.cegz.sys.model.view.adver;


/**
 * 广告预约视图
 *
 * @author maqianqian
 * @date 2019年02月15日
 */
public class SubscribeDesignView {
    private Long id;

    /**
     * 申请人姓名
     */
    private String name;

    /**
     * 申请人电话
     */
    private String createUserPhone;

    /**
     * 申请处理人电话
     */
    private String updateUserPhone;

    /**
     * 申请时间
     */
    private String createTime;

    /**
     * 申请处理时间
     */
    private String updateTime;

    /**
     * 广告类型，1 小平面广告，2 大平面广告，3 地图广告
     */
    private String type;

    /**
     * 审核状态 ，0 处理中，1完成，2 失败
     */
    private int status;

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

    public String getCreateUserPhone() {
        return createUserPhone;
    }

    public void setCreateUserPhone(String createUserPhone) {
        this.createUserPhone = createUserPhone;
    }

    public String getUpdateUserPhone() {
        return updateUserPhone;
    }

    public void setUpdateUserPhone(String updateUserPhone) {
        this.updateUserPhone = updateUserPhone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
