package com.cegz.sys.service.adver.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.PublishAdverRecordRepository;
import com.cegz.sys.model.adver.PublishAdverRecord;
import com.cegz.sys.service.adver.PublishAdverService;

/**
 * 发布广告服务
 * 
 * @author tenglong
 * @date 2018年12月4日
 */
@Service("publishAdverService")
@Transactional
public class PublishAdverServiceImpl implements PublishAdverService {

	@Autowired
	private PublishAdverRecordRepository publishRepository;

	@Override
	public List<PublishAdverRecord> listPublishAdverByDeviceId(Long deviceId) {
		return publishRepository.listPublishAdverByDeviceId(deviceId);
	}

}
