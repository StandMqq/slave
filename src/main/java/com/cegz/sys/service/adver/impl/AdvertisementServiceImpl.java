package com.cegz.sys.service.adver.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cegz.sys.dao.adver.AdvertisementRepository;
import com.cegz.sys.dao.adver.AdvertisementTypeRepository;
import com.cegz.sys.dao.adver.DriveRegistrationRepository;
import com.cegz.sys.dao.adver.PriceRepository;
import com.cegz.sys.dao.adver.PublishAdverRecordRepository;
import com.cegz.sys.model.adver.Advertisement;
import com.cegz.sys.model.adver.AdvertisementType;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.model.adver.Price;
import com.cegz.sys.model.view.adver.AdvertisementView;
import com.cegz.sys.service.adver.AdvertisementService;

/**
 * 广告后台服务
 * 
 * @author tenglong
 * @date 2018年8月6日
 */
@Service("advertisementService")
@Transactional
public class AdvertisementServiceImpl implements AdvertisementService {
	@Autowired
	private AdvertisementRepository advertisementRepository;

	@Autowired
	private PriceRepository priceRepository;

	@Autowired
	private AdvertisementTypeRepository typeRepository;

	@Autowired
	private DriveRegistrationRepository driveRegistrationRepository;

	@Autowired
	private PublishAdverRecordRepository publishAdverRecordRepository;

	@Override
	public int save(Advertisement advertiser) {
		advertisementRepository.save(advertiser);
		return 1;
	}

	@Override
	public Long queryAdvertisementCount(Integer status, String name, String advertiserName, String advertiserPhone,
			String typeNo) {
		return advertisementRepository.queryAdvertisementCount(status, name, advertiserName, advertiserPhone, typeNo);
	}

	@Override
	public List<Advertisement> getAdvertisementList(Integer curPage, Integer pageSize, Integer status, String name,
			String advertiserName, String advertiserPhone, String typeNo) {
		return advertisementRepository.queryAdvertisementByLimit(curPage, pageSize, status, name, advertiserName,
				advertiserPhone, typeNo);
	}

	@Override
	public Optional<Advertisement> getAdvertisementById(Long id) {
		return advertisementRepository.findById(id);
	}

	@Override
	public Advertisement getAdvertisementByIdV2(Long id) {
		return advertisementRepository.selectById(id);
	}

	@Override
	public Long queryAdvertisementCountByAdvertiserId(Long advertiserId, Integer status) {
		return advertisementRepository.queryAdvertisementCountByAdvertiserId(advertiserId, status);
	}

	@Override
	public List<AdvertisementView> getAdvertisementsByAdvertiserId(Long advertiserId) {
		List<AdvertisementView> result = new ArrayList<>();
		List<Advertisement> advertisements = advertisementRepository.findAllByAdvertiserId(advertiserId);
		if (advertisements != null && advertisements.size() > 0) {
			for (Advertisement advertisement : advertisements) {
				AdvertisementView view = new AdvertisementView();
				view.setId(advertisement.getId());
				view.setStatus(advertisement.getStatus());
				view.setReason(advertisement.getReason());
				view.setTitle(advertisement.getTitle());
				view.setTitleImgUrl(advertisement.getTitlePicUrl());
				view.setContentImages(advertisement.getContent());
				view.setContentImages(advertisement.getContentPicUrl());
				result.add(view);
			}
		}
		return result;
	}

	@Override
	public List<AdvertisementView> getAdvertisementsByAdvertiserIdLimit(Long advertiserId, Integer status,
			Integer curPage, Integer pageSize) {
		List<AdvertisementView> result = new ArrayList<>();
		List<Advertisement> advertisements = advertisementRepository.findAllByAdvertiserIdLimit(advertiserId, status,
				curPage, pageSize);
		if (advertisements != null && advertisements.size() > 0) {
			for (Advertisement advertisement : advertisements) {
				AdvertisementView view = new AdvertisementView();
				view.setId(advertisement.getId());
				view.setStatus(advertisement.getStatus());
				view.setReason(advertisement.getReason());
				view.setTitle(advertisement.getTitle());
				view.setTitleImgUrl(advertisement.getTitlePicUrl());
				view.setContentImages(advertisement.getContent());
				view.setContentImages(advertisement.getContentPicUrl());
				result.add(view);
			}
		}
		return result;
	}

	@Override
	public List<Price> listPrice() {
		return priceRepository.findAll();
	}

	@Override
	public Optional<AdvertisementType> getAdverTypeById(Long id) {
		return typeRepository.findById(id);
	}

	@Override
	public Optional<Price> getPriceById(Long id) {
		return priceRepository.findById(id);
	}

	@Override
	public Long queryCarCountByAdvertisementId(Long advertiserId, String plateNumber) {
		return driveRegistrationRepository.queryCarCountByAdvertisementId(advertiserId, plateNumber);
	}

	@Override
	public List<DrivingRegistration> getCarListByAdvertisementId(Integer curPage, Integer pageSize, Long id,
			String plateNumber) {
		return driveRegistrationRepository.getCarListByAdvertisementId(curPage, pageSize, id, plateNumber);
	}

	@Override
	public int advertisementStatusExamine(Long id, Integer status, String reason, Date updateTime) {
		return advertisementRepository.advertisementStatusExamine(id, status, reason, updateTime);
	}

	@Override
	public Long queryAdvertisementCountByCarId(Long id, String startTime, String endTime, String status, String typeNo,
			String name) {
		return advertisementRepository.queryAdvertisementCountByCarId(id, startTime, endTime, status, typeNo, name);
	}

	@Override
	public List<Advertisement> queryAdvertisementListByCarId(Integer startPage, Integer pageSize, Long id,
			String startTime, String endTime, String status, String typeNo, String name) {
		return advertisementRepository.queryAdvertisementListByCarId(startPage, pageSize, id, startTime, endTime,
				status, typeNo, name);
	}

	@Override
	public int editPublishRecord(Long advertisementId, Long orderId, Long updateUserId) {
		// 修改广告发布信息 : advertisementId, orderId, isDeleted, updateTime, updateUserId
		publishAdverRecordRepository.editPublishRecord(advertisementId, orderId, 1, new Date(), updateUserId);
		return 1;
	}
//
//	@Override
//	public Long getAdvertisementCountByCarId(Long id, String startTime, String endTime) {
//		return advertisementRepository.getAdvertisementCountByCarId(id, startTime, endTime);
//	}
//
//	@Override
//	public List<Advertisement> getAdvertisementListByCarId(Integer curPage, Integer pageSize, Long id, String startTime,
//			String endTime) {
//		return advertisementRepository.getAdvertisementListByCarId(curPage, pageSize, id, startTime, endTime);
//	}

}
