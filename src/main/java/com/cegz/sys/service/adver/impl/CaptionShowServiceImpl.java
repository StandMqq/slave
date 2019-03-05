package com.cegz.sys.service.adver.impl;

import com.cegz.sys.dao.adver.CaptionShowRepository;
import com.cegz.sys.model.adver.CaptionShow;
import com.cegz.sys.service.adver.CaptionShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * 字幕秀服务
 */
@Service("captionShowService")
@Transactional
public class CaptionShowServiceImpl implements CaptionShowService {

    @Autowired
    private CaptionShowRepository captionShowRepository;



    @Override
    public CaptionShow getCaptionShowContentByIsDeleted(Byte isDeleted) {
        return captionShowRepository.getCaptionShowContentByIsDeleted(isDeleted);
    }

    @Override
    public void updateCaptionShowContentById(Long id, String updateContent, String updateTime) {
        captionShowRepository.updateCaptionShowContentById(id, updateContent, updateTime);
    }

    @Override
    public CaptionShow getCaptionShowContentById(Long id) {
        return captionShowRepository.getOne(id);
    }

    @Override
    public void addCaptionShowContent(Long createUserId, Byte isDeleted, String createTime, String updateContent) {
        captionShowRepository.addCaptionShowContent(createUserId, isDeleted, createTime, updateContent);
    }
}
