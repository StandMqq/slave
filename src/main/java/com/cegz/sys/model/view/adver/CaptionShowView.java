package com.cegz.sys.model.view.adver;


/**
 * 字幕视图
 *
 * @author maqianqian
 * @date 2019年02月21日
 */
public class CaptionShowView {

    private Long id;

    /**
     * 字幕内容
     */
    private String content;

    /**
     * 数据是否有效， 0 有效， 1 无效
     */
    private Byte isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }
}
