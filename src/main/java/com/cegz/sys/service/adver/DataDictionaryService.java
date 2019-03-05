package com.cegz.sys.service.adver;

import java.util.List;

import com.cegz.sys.model.adver.DataDictionary;

/**
 * 数据字典服务
 * 
 * @author tenglong
 * @date 2018年8月14日
 */
public interface DataDictionaryService {
	/**
	 * 通过code查询数据
	 * 
	 * @param String code
	 * @return
	 * @author tenglong
	 * @date 2018年8月14日
	 */
	List<DataDictionary> getDataDictionaryByCode(String code);

}
