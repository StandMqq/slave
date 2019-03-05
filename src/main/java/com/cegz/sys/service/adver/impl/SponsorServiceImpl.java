package com.cegz.sys.service.adver.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.SponsorRepository;
import com.cegz.sys.model.adver.Sponsor;
import com.cegz.sys.service.adver.SponsorService;

/**
 * 保荐方后台服务
 * 
 * @author tenglong
 * @date 2018年7月30日
 */
@Service("sponsorService")
public class SponsorServiceImpl implements SponsorService {
	@Autowired
	private SponsorRepository sponsorRepository;

	@Override
	public List<Sponsor> listSponsorExamine(Integer curPage, Integer pageSize, Integer status, String name,
			String phone) {
		return sponsorRepository.getSponsorExamineList(curPage, pageSize, status, name, phone);
	}

	@Override
	public Long querySponsorExamineTotalCount(Integer status, String name, String phone) {
		return sponsorRepository.querySponsorExamineTotalCount(status, name, phone);
	}

	@Override
	public Optional<Sponsor> getSponSorById(Long id) {
		return sponsorRepository.findById((Long) id);
	}

	@Override
	public int save(Sponsor sponsor) {
		sponsorRepository.save(sponsor);
		return 1;
	}

	@Override
	public List<Sponsor> getSponsorList() {
		Sponsor parm = new Sponsor();
		parm.setIsDeleted((byte) 0);
		parm.setStatus((byte) 1);
		Example<Sponsor> example = Example.of(parm);
		return sponsorRepository.findAll(example);
	}

	@Override
	public int statusExamine(Long id, byte status, String reason, Date updateTime, String city, String company,
			String companyLittle, String businessNumber, String name, String phone, String email, String address,
			String addressDetail) {
		return sponsorRepository.statusExamine(id, status, reason, updateTime, city, company, companyLittle,
				businessNumber, name, phone, email, address, addressDetail);
	}

	@Override
	public boolean checkCompanyNameExist(String company) {
		int checkCompanyNameExist = sponsorRepository.checkCompanyNameExist(company);
		if (checkCompanyNameExist > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkCompanyLittleNameExist(String companyLittle) {
		int checkCompanyLittleNameExist = sponsorRepository.checkCompanyLittleNameExist(companyLittle);
		if (checkCompanyLittleNameExist > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Long querySponsorDetailListCountId(Long id, String name, String phone) {
//		return sponsorRepository.querysponsorTotalCount(id, name, phone);
		return 10L;
	}
//
//	@Override
//	public List<Sponsor> querySponsorList(Integer startPage, Integer pageSize, Long id, String name, String phone) {
//
//		return null;
//	}
}
