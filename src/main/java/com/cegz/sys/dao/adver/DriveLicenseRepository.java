package com.cegz.sys.dao.adver;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.cegz.sys.model.adver.DrivingLicense;

/**
 * 驾驶证持久化接口
 * 
 * @author lijiaxin
 * @date 2018年7月24日
 */
@Transactional
public interface DriveLicenseRepository extends JpaRepository<DrivingLicense, Long> {

	/**
	 * 审核驾驶证数据
	 */
	@Modifying
	@Query(value = "update driving_license set status = ?2, reason = ?3, update_time = ?4 where id = ?1", nativeQuery = true)
	int drivingLicenseStatusExamine(Long id, byte status, String reason, Date updateTime);
}
