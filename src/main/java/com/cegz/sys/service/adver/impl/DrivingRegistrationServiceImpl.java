package com.cegz.sys.service.adver.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bouncycastle.jce.provider.JCEMac.MD2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cegz.sys.dao.adver.DriveRegistrationRepository;
import com.cegz.sys.model.adver.DrivingRegistration;
import com.cegz.sys.service.adver.DrivingRegistrationService;

/**
 * 行驶证后台服务
 * 
 * @author tenglong
 * @date 2018年7月31日
 */
@Service("drivingRegistrationService")
@Transactional
public class DrivingRegistrationServiceImpl implements DrivingRegistrationService {
	@Autowired
	private DriveRegistrationRepository driveRegistrationRepository;

	@Override
	public Optional<DrivingRegistration> getDrivingRegistrationById(Long id) {
		return driveRegistrationRepository.findById(id);
	}

	@Override
	public int save(DrivingRegistration drivingRegistration) {
		driveRegistrationRepository.save(drivingRegistration);
		return 1;
	}

	@Override
	public int countCarByStatus(Byte status, String plateNumber, String model) {
		return driveRegistrationRepository.contByStatus(status, plateNumber, model);
	}

	@Override
	public List<DrivingRegistration> listPageCar(int startPos, int size, Byte status, String plateNumber,
			String model) {
		return driveRegistrationRepository.listDriveByPage(startPos, size, status, plateNumber, model);
	}

	@Override
	public Optional<DrivingRegistration> getById(Long id) {
		return driveRegistrationRepository.findById(id);
	}

	@Override
	public DrivingRegistration getDrivingRegistrationByPlateNumber(String plateNumber) {
		return driveRegistrationRepository.getDrivingRegistrationByPlateNumber(plateNumber);
	}

	@Override
	public int carsStatusExamine(Long id, Integer status, String reason, Date updateTime, String carNumber,
			String model, Date birthDate) {
		return driveRegistrationRepository.carsStatusExamine(id, status, reason, updateTime, carNumber, model,
				birthDate);
	}

	@Override
	public List<DrivingRegistration> getCarListBySponsorId(Long id, String name, String phone, Integer curPage,
			Integer pageSize) {
		return driveRegistrationRepository.getCarListBySponsorId(id, name, phone, curPage, pageSize);
	}

	@Override
	public Long getCarCountBySponsorId(Long id, String name, String phone) {
		return driveRegistrationRepository.getCarCountBySponsorId(id, name, phone);
	}

	@Override
	public DrivingRegistration queryPlateNumberByDeviceImei(String imei) {
		return driveRegistrationRepository.queryPlateNumberByDeviceImei(imei);
	}

	@Override
	public Long countRegistrationByContactId(Long id) {
		return driveRegistrationRepository.countRegistrationByContactId(id);
	}

	@Override
	public List<DrivingRegistration> listRegistrationByContactId(Integer startPage, Integer pageSize, Long id) {
		return driveRegistrationRepository.listRegistrationByContactId(startPage,pageSize,id);
	}

	@Override
	public List<DrivingRegistration> getDrivingRegistrationByContactId(Long id) {
		return driveRegistrationRepository.getDrivingRegistrationByContactId(id);
	}

	@Override
	public List<String> queryPlateNumberByContactsId(Long contactsId) {
		return driveRegistrationRepository.queryPlateNumberByContactsId(contactsId);
	}

	@Override
	public DrivingRegistration queryDrivingRegistrationByPlateNumber(String plateNumber) {
		return driveRegistrationRepository.queryDrivingRegistrationByPlateNumber(plateNumber);
	}

	@Override
	public DrivingRegistration findContactsIdByPlateNumber(String plateNumber) {
		return driveRegistrationRepository.findContactsIdByPlateNumber(plateNumber);
	}

	@Override
	public List<DrivingRegistration> queryDrivingRegistrationListByPlateNumber(Integer startPage, Integer pageSize, String plateNumber) {
		return driveRegistrationRepository.queryDrivingRegistrationListByPlateNumber(startPage, pageSize, plateNumber);
	}

	@Override
	public Long queryCountByPlateNumber(String plateNumber) {
		return driveRegistrationRepository.queryCountByPlateNumber(plateNumber);
	}


}
