package com.cegz.sys.service.adver;

import java.util.List;

import com.cegz.sys.model.adver.Resource;

/**
 * 资源后台服务
 * 
 * @author tenglong
 * @date 2018年10月29日
 */
public interface ResourceService {

	/**
	 * 通过菜单id获取List<Resource>
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年10月29日
	 */
	List<Resource> getResourceListByMenuId(Long menuId);
}
