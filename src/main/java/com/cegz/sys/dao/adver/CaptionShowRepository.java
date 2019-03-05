package com.cegz.sys.dao.adver;

import com.cegz.sys.model.adver.CaptionShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * 字幕
 *
 * @author maqianqian
 * @date 2019年02月21日
 */
@Transactional
public interface CaptionShowRepository extends JpaRepository<CaptionShow,Long> {

    /**
     * 获取有效的字幕内容
     * @param isDeleted
     * @return
     */
    @Query(value = "select cs.* from caption_show cs where cs.is_deleted = ?1 ", nativeQuery = true)
    CaptionShow getCaptionShowContentByIsDeleted(Byte isDeleted);

    /**
     * 根据id修改字幕内容
     * @param id
     */
    @Modifying
    @Query(value = "update caption_show cs set cs.content = ?2, cs.update_time = ?3 where id = ?1 ", nativeQuery = true)
    void updateCaptionShowContentById(Long id, String updateContent, String updateTime);

    /**
     * 当id为空时，新增字幕
     */
    @Modifying
    @Query(value = "insert into caption_show (create_user_id, is_deleted, create_time, content) " +
            "values(?1, ?2, ?3 ,?4) ", nativeQuery = true)
    void addCaptionShowContent(Long createUserId, Byte isDeleted, String createTime, String updateContent);

}
