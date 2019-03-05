package com.cegz.sys.dao.adver;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cegz.sys.model.adver.AdvertisementType;

/**
 * 广告类型持久化接口
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
public interface AdvertisementTypeRepository extends JpaRepository<AdvertisementType, Long> {

}
