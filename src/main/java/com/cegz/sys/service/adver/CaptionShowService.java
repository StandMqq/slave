package com.cegz.sys.service.adver;

import com.cegz.sys.model.adver.CaptionShow;

/**
 * 字幕秀服务
 */
public interface CaptionShowService {

    /**
     * 根据状态获取有效的字幕内容
     * @param isDeleted
     * @return
     */
    CaptionShow getCaptionShowContentByIsDeleted(Byte isDeleted);

    /**
     *  根据id修改字幕内容
     * @param id
     * @param updateContent
     */
    void updateCaptionShowContentById(Long id, String updateContent, String updateTime);

    /**
     * 根据id查询字幕
     * @param id
     * @return
     */
    CaptionShow getCaptionShowContentById(Long id);

    /**
     * id为空时，新增字幕
     * @param createUserId
     * @param isDeleted
     * @param createTime
     * @param updateContent
     */
    void addCaptionShowContent(Long createUserId, Byte isDeleted, String createTime, String updateContent);
}
