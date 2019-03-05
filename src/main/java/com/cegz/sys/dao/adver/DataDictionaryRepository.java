package com.cegz.sys.dao.adver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cegz.sys.model.adver.DataDictionary;

/**
 * 数据字典持久化接口
 * 
 * @author tenglong
 * @date 2018年8月14日
 */
public interface DataDictionaryRepository extends JpaRepository<DataDictionary, Long> {

	/**
	 * 通过code获取数据字典数据
	 * 
	 * @param code
	 * @return
	 */
	@Query(value = "select * from data_dictionary dd where 1=1 and dd.code = ?1 order by dd.sort", nativeQuery = true)
	List<DataDictionary> getDataDictionaryByCode(String code);
}
