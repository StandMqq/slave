package com.cegz.sys.service.adver.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cegz.sys.dao.adver.AdvertiserRepository;
import com.cegz.sys.model.adver.Advertiser;
import com.cegz.sys.model.view.adver.AdvertiserView;
import com.cegz.sys.service.adver.AdvertiserService;

/**
 * 广告主后台服务
 * 
 * @author tenglong
 * @date 2018年7月31日
 */
@Service("advertiserService")
@Transactional
public class AdvertiserServiceImpl implements AdvertiserService {
	@Autowired
	private AdvertiserRepository advertiserRepository;

	@Override
	public int save(Advertiser advertiser) {
		advertiserRepository.save(advertiser);
		return 1;
	}

	@Override
	public List<Advertiser> listAdvertiserExamine(Integer curPage, Integer pageSize, Integer status, String name,
			String phone, String registerPhone) {
		return advertiserRepository.getAdvertiserExamineList(curPage, pageSize, status, name, phone, registerPhone);
	}

	@Override
	public Long queryAdvertiserExamineTotalCount(Integer status, String name, String phone, String registerPhone) {
		return advertiserRepository.queryAdvertiserExamineTotalCount(status, name, phone, registerPhone);
	}

	@Override
	public AdvertiserView getAdvertiserDetailById(Long id) {
		AdvertiserView view = new AdvertiserView();
		Optional<Advertiser> advertiserOpt = advertiserRepository.findById(id);
		Advertiser advertiser = advertiserOpt.get();
		if (advertiser != null) {
			view.setId(advertiser.getId());
			view.setName(advertiser.getName());
			view.setPhone(advertiser.getPhone());
			view.setBusinessLicense(advertiser.getBusinessLicense());
			view.setPictureUrl(advertiser.getPictureUrl());
		}
		return view;
	}

	@Override
	public Long queryAdvertiserTotalCount() {
		Advertiser parm = new Advertiser();
		parm.setIsDeleted((byte) 0);
		parm.setStatus((byte) 1);
		Example<Advertiser> example = Example.of(parm);
		long count = advertiserRepository.count(example);
		return count;
	}

	@Override
	public Optional<Advertiser> getAdvertiserById(Long id) {
		return advertiserRepository.findById(id);
	}

	@Override
	public int advertiserStatusExamine(Long id, byte status, String reason, Date updateTime, String city,
			String company, String businessNumber, String name, String phone, String email, String address,
			String addressDetail) {
		return advertiserRepository.advertiserStatusExamine(id, status, reason, updateTime, city, company,
				businessNumber, name, phone, email, address, addressDetail);
	}

	@Override
	public boolean checkCompanyNameExist(String company) {
		int checkCompanyNameExist = advertiserRepository.checkCompanyNameExist(company);
		if (checkCompanyNameExist > 0) {
			return true;
		}
		return false;
	}
	@Override
	public Advertiser findAdvertiserByOrderId(Long id) {
		return advertiserRepository.findAdvertiserByOrderId(id);
	}


}
