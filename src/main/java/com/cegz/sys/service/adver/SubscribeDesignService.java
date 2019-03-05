package com.cegz.sys.service.adver;


import com.cegz.sys.model.adver.SubscribeDesign;

import java.util.List;

/**
 * 广告预约申请服务
 *
 * @author maqianqian
 * @date 2019年02月15日
 */
public interface SubscribeDesignService {

    /**
     * 通审核状态获取广告预约总记录数
     * @param status
     * @return
     */
    Long countSubscribeDesignByStatus(Byte status);

    /**
     * 通过审核状态获取广告预约申请列表数据
     * @param status
     * @param startPage
     * @param pageSize
     * @return
     */
    List<SubscribeDesign> listSubscribeDesignByStatus(Byte status, int startPage, Integer pageSize);

    /**
     * 修改广告审核状态
     * @param id
     * @param status
     * @param updateUserId
     * @param updateTime
     */
    void updateSubscribeDesignStatusById(Long id, Byte status, Long updateUserId, String updateTime);

    /**
     * 获取审核者电话号码
     * @param updateUserId
     * @return
     */
    String getUpDateUserPhone(Long updateUserId);

}
