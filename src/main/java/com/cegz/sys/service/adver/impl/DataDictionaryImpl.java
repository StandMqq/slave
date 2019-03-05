package com.cegz.sys.service.adver.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.DataDictionaryRepository;
import com.cegz.sys.model.adver.DataDictionary;
import com.cegz.sys.service.adver.DataDictionaryService;

/**
 * 数据字典服务
 * 
 * @author tenglong
 * @date 2018年8月14日
 */
@Service("dataDictionaryService")
public class DataDictionaryImpl implements DataDictionaryService {

	@Autowired
	private DataDictionaryRepository dataDictionaryRepository;

	@Override
	public List<DataDictionary> getDataDictionaryByCode(String code) {
		return dataDictionaryRepository.getDataDictionaryByCode(code);
	}

}
