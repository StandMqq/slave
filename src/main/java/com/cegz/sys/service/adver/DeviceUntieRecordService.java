package com.cegz.sys.service.adver;

import com.cegz.sys.model.adver.DeviceUntieRecord;

/**
 * 设备解绑记录服务
 * 
 * @author tenglong
 * @date 2018年8月15日
 */
public interface DeviceUntieRecordService {

	/**
	 * 保存设备解绑记录数据
	 * 
	 * @param device：设备解绑记录数据
	 * @return
	 * @author tenglong
	 * @date 2018年11月21日
	 */
	int save(DeviceUntieRecord deviceUntieRecord);
}
