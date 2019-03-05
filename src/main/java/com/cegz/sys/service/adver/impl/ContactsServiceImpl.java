package com.cegz.sys.service.adver.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cegz.sys.dao.adver.ContactsRepository;
import com.cegz.sys.dao.adver.DriveLicenseRepository;
import com.cegz.sys.dao.adver.DriveRegistrationRepository;
import com.cegz.sys.dao.adver.IdCardRepository;
import com.cegz.sys.model.adver.Contacts;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.service.adver.ContactsService;

/**
 * 联系人系列服务
 * 
 * @author tenglong
 * @date 2018年7月31日
 */
@Service("contactsService")
@Transactional
public class ContactsServiceImpl implements ContactsService {
	@Autowired
	private ContactsRepository contactsRepository;
	@Autowired
	private DriveRegistrationRepository drivingRegistrationRepository;
	@Autowired
	private IdCardRepository idCardRepository;
	@Autowired
	private DriveLicenseRepository driveLicenseRepository;

	@Override
	public List<Contacts> listContactsExamine(int startPos, int size, int status, String name, String phone) {
		return contactsRepository.getContactsExamineList(startPos, size, status, name, phone);
	}

	@Override
	public int countContractsExamine(int status, String name, String phone) {
		return contactsRepository.countByStatus(status, name, phone);
	}

	@Override
	public Optional<Contacts> getContactById(Long id) {
		return contactsRepository.findById(id);
	}

	@Override
	public int insertData(Contacts contacts) {
		contactsRepository.save(contacts);
		return 1;
	}

	@Override
	public Contacts getContactsByUsersId(Long usersId) {
		return contactsRepository.getContactsByUsersId(usersId);
	}

	@Override
	public Optional<DrivingRegistration> getRegistrationById(Long id) {
		return drivingRegistrationRepository.findById(id);
	}

	@Override
	public int insertContractDriveRegist(DrivingRegistration dr) {
		drivingRegistrationRepository.save(dr);
		return 1;
	}

	@Override
	public int carOwnerStatusExamine(Long id, byte status, String reason, Date updateTime, String name, String phone) {
		return contactsRepository.carOwnerStatusExamine(id, status, reason, updateTime, name, phone);
	}

	@Override
	public int idCardStatusExamine(Long id, byte status, String reason, Date updateTime) {
		return idCardRepository.idCardStatusExamine(id, status, reason, updateTime);
	}

	@Override
	public int drivingLicenseStatusExamine(Long id, byte status, String reason, Date updateTime) {
		return driveLicenseRepository.drivingLicenseStatusExamine(id, status, reason, updateTime);
	}

	@Override
	public List<Contacts> queryContactsList(int i, int j, String phoneInput, String nameInput) {
		return contactsRepository.queryContactsList(i , j ,phoneInput,nameInput);
	}

	@Override
	public Long queryAuthorityCountByConditions(String name, String phone) {
		return contactsRepository.queryAuthorityCountByConditions(name, phone);
	}

	@Override
	public List<Contacts> getContactsListByNameOrPhone(Integer startPage, Integer pageSize, String name, String phone) {
		return contactsRepository.getContactsListByNameOrPhone(startPage, pageSize, name, phone);
	}

	@Override
	public Contacts getOneByContactId(Long contactId) {
		return contactsRepository.getOneByContactId(contactId);
	}

	@Override
	public Long getContactsIdByid(Long id) {
		return contactsRepository.getContactsIdByid(id);
	}
}
