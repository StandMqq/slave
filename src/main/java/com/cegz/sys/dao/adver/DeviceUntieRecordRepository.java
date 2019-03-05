package com.cegz.sys.dao.adver;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cegz.sys.model.adver.DeviceUntieRecord;

/**
 * 设备解绑记录持久化接口
 * 
 * @author tenglong
 * @date 2018年11月21日
 */
public interface DeviceUntieRecordRepository extends JpaRepository<DeviceUntieRecord, Long> {

}
