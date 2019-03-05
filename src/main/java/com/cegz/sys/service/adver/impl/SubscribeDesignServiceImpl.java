package com.cegz.sys.service.adver.impl;

import com.cegz.sys.dao.adver.SubsecibeDesignRepository;
import com.cegz.sys.model.adver.SubscribeDesign;
import com.cegz.sys.service.adver.SubscribeDesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 广告申请预约服务
 *
 * @author maqianqian
 * @date 2019年02月15日
 */
@Service("subscribeDesignService")
@Transactional
public class SubscribeDesignServiceImpl implements SubscribeDesignService {

    @Autowired
    private SubsecibeDesignRepository subsecibeDesignRepository;

    @Override
    public Long countSubscribeDesignByStatus(Byte status) {
        return subsecibeDesignRepository.countSubscribeDesignByStatus(status);
    }

    @Override
    public List<SubscribeDesign> listSubscribeDesignByStatus(Byte status, int startPage, Integer pageSize) {
        return subsecibeDesignRepository.listSubscribeDesignByStatus(status, startPage, pageSize);
    }

    @Override
    public void updateSubscribeDesignStatusById(Long id, Byte status, Long updateUserId, String updateTime) {
        subsecibeDesignRepository.updateSubscribeDesignStatusById(id, status, updateUserId, updateTime);
    }

    @Override
    public String getUpDateUserPhone(Long updateUserId) {
        return subsecibeDesignRepository.getUpDateUserPhone(updateUserId);
    }
}
