package com.cegz.sys.service.adver.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.ResourceRepository;
import com.cegz.sys.model.adver.Resource;
import com.cegz.sys.service.adver.ResourceService;

/**
 * 订单后台服务
 * 
 * @author tenglong
 * @date 2018年10月29日
 */
@Service("resourceService")
@Transactional
public class ResourceServiceImpl implements ResourceService {
	@Autowired
	private ResourceRepository resourceRepository;

	@Override
	public List<Resource> getResourceListByMenuId(Long menuId) {
		return resourceRepository.getResourceListByMenuId(menuId);
	}

}
