package com.cegz.sys.service.adver.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cegz.sys.dao.adver.UsersRepository;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.service.adver.AccountService;

/**
 * 账号服务
 * 
 * @author lijiaxin
 * @date 2018年7月19日
 */
@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private UsersRepository usersRepository;

	@Override
	public int regist(Users user) {
		usersRepository.save(user);
		return 1;
	}

	@Override
	public Users login(String userName, String password) {
		return usersRepository.findByUserNameAndPasswordSalt(userName, password);
	}

	@Override
	public Users getUserByName(String userName) {
		return usersRepository.findByUserName(userName);
	}

	@Override
	public Optional<Users> getUserById(Long id) {
		return usersRepository.findById(id);
	}

	@Override
	public Long getUsersCount(String userName, String phone) {
		return usersRepository.getUsersCount(userName, phone);
	}

	@Override
	public List<Users> getUsersList(Integer curPage, Integer pageSize, String userName, String phone) {
		return usersRepository.getUsersList(curPage, pageSize, userName, phone);
	}

}
