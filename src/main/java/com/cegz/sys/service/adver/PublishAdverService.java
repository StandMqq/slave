package com.cegz.sys.service.adver;

import java.util.List;

import com.cegz.sys.model.adver.PublishAdverRecord;

/**
 * 发布广告服务
 * 
 * @author tenglong
 * @date 2018年12月4日
 */
public interface PublishAdverService {

	/**
	 * 通过设备id获取广告发布数据
	 * 
	 * @param deviceId 设备id
	 * @return
	 * @author tenglong
	 * @date 2018年12月4日
	 */
	List<PublishAdverRecord> listPublishAdverByDeviceId(Long deviceId);

}
