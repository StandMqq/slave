package com.cegz.sys.service.adver.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.TravelRepository;
import com.cegz.sys.model.adver.Travel;
import com.cegz.sys.service.adver.TravelService;

/**
 * 车辆移动后台服务
 * 
 * @author tenglong
 * @date 2018年11月15日
 */
@Service("travelService")
public class TravelServiceImpl implements TravelService {
	@Autowired
	private TravelRepository travelRepository;

	@Override
	public List<Travel> queryOpenTimeLength(String imeis, String createTime) {
		return travelRepository.queryOpenTimeLength(imeis, createTime);
	}

	@Override
	public List<String> queryOnLineDay(String imeis) {
		return travelRepository.queryOnLineDay(imeis);
	}

	
	@Override
	public List<Travel> queryOpenTimeLength(String imei) {
		return travelRepository.queryOpenTimeLength(imei);
	}

	@Override
	public List<Travel> getLastWeekOpenTimeLengths(String imei, String currentDate) {
		return travelRepository.getLastWeekOpenTimeLengths(imei, currentDate);
	}
}
