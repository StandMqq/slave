package com.cegz.sys.controller.adver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.adver.Role;
import com.cegz.sys.model.adver.RoleMenu;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.adver.UsersRole;
import com.cegz.sys.redis.RedisUtil;
import com.cegz.sys.service.adver.AccountService;
import com.cegz.sys.service.adver.AuthorityService;
import com.cegz.sys.util.Constant;
import com.cegz.sys.util.Md5Util;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import com.cegz.sys.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 账号后台控制类
 * 
 * @author lijiaxin
 * @date 2018年7月19日
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class AccountController {
	@Autowired
	private ServerAck serverAck;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthorityService authorityService;

	/**
	 * 版本号
	 */
	@Value("${server.version}")
	private String serverVersion;

	@Value("${accountant.phone}")
	private String accountant;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 校验version
	 * 
	 * @param version
	 * @return
	 */
	private ResultData checkVersion(String version) {
		if (StringUtil.isEmpty(version)) {
			return serverAck.getParamError().setMessage("版本号不能为空");
		}
		if (serverVersion != null && !serverVersion.equals(version)) {
			return serverAck.getParamError().setMessage("版本错误");
		}
		return null;
	}

	/**
	 * 校验token
	 * 
	 * @param request
	 * @return
	 */
	private ResultData checkToken(HttpServletRequest request) {
		String tokens = request.getHeader("token");
		if (StringUtil.isEmpty(tokens)) {
			return serverAck.getLoginTimeOutError();
		}
		Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		return null;
	}

	/**
	 * 管理员注册
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @author lijiaxin
	 * @date 2018年7月19日
	 */
	@PostMapping("regist")
	public ResultData regist(HttpServletRequest request, String userName, String password, String requrePassword,
			String version) {

		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (StringUtil.isEmpty(userName)) {
			return serverAck.getParamError().setMessage("账号不能为空");
		}
		if (!StringUtil.isMobile(userName)) {
			return serverAck.getParamError().setMessage("用户名必须为手机号");
		}
		if (StringUtil.isEmpty(password)) {
			return serverAck.getParamError().setMessage("密码不能为空");
		}
		if (password.length() < 6) {
			return serverAck.getParamError().setMessage("密码长度不能小于6位");
		}
		if (StringUtil.isEmpty(requrePassword)) {
			return serverAck.getParamError().setMessage("确认密码不能为空");
		}
		if (!password.equals(requrePassword)) {
			return serverAck.getParamError().setMessage("两次密码输入不一致");
		}

		try {
			userName = userName.trim();
			password = password.trim();
			Users userStatus = accountService.getUserByName(userName);

//			if (userStatus != null && userStatus.getPasswordSalt() != null) {
//				return serverAck.getParamError().setMessage("账号密码已经存在");
//			}
			// 数据处理
			// 二次密码加密
			ByteSource salt = ByteSource.Util.bytes(userName);
			String passwordSalt = new SimpleHash("md5", password, salt, 2).toHex();
			Users user = null;
			if (userStatus != null && userStatus.getId() != null) {
				user = userStatus;
				user.setPasswordSalt(passwordSalt);
				user.setUpdateTime(new Date());
			} else {
				user = new Users();
				user.setUserName(userName);
				String encodePassword = Md5Util.EncoderByMd5(password);
				user.setPassword(encodePassword);
				user.setPhone(userName);
				user.setPasswordSalt(passwordSalt);
				user.setCreateTime(new Date());
				// 生成uuid
				String uuid = TokenUtil.getUUID();
				user.setUuid(uuid);
			}
			// 数据存储
			int ret = accountService.regist(user);
			if (ret != 0) {
				return serverAck.getSuccess();
			}
			return serverAck.getFailure();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}

	}

	/**
	 * 登陆
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @author lijiaxin
	 * @date 2018年7月20日
	 */
	@PostMapping("login")
	public ResultData login(String userName, String password, String version) {
		if (StringUtil.isEmpty(version)) {
			return serverAck.getParamError().setMessage("版本号不能为空");
		}
		if (serverVersion != null && !serverVersion.equals(version)) {
			return serverAck.getParamError().setMessage("版本错误");
		}
		if (StringUtil.isEmpty(userName)) {
			return serverAck.getParamError().setMessage("账号不能为空");
		}
		if (StringUtil.isEmpty(password)) {
			return serverAck.getParamError().setMessage("密码不能为空");
		}
		try {
			ByteSource salt = ByteSource.Util.bytes(userName);
			String passwordSalt = new SimpleHash("md5", password, salt, 2).toHex();
			Users ret = accountService.login(userName.trim(), passwordSalt);
			if (ret == null || ret.getId() == null) {
				return serverAck.getFailure().setMessage("账号、密码错误或者当前账号不是管理员，请联系管理员");
			}
			// 设置session
			ret.setPasswordSalt(null);
			ret.setPassword(null);
			Subject sb = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
			sb.login(token);
			sb.getSession().setAttribute("user", ret);
			sb.isPermitted("TLONG");
			Map<String, Object> map = new HashMap<>(16);
			map.put("token", sb.getSession().getId());
			map.put("name", userName);
			int accontStatus = 0;
			if (userName.equals(accountant)) {
				accontStatus = 1;
			}
			map.put("status", accontStatus);

			// 给登录用户设置角色权限(左侧菜单栏) 通过sql
			List<RoleMenu> roleMenuList = new ArrayList<>();
			Long userId = ret.getId();
			// 通过用户id得到用户关联的角色List<role>
			List<UsersRole> usersRoles = authorityService.getUsersRoleListByUserId(userId);
			if (usersRoles != null && usersRoles.size() > 0) {
				for (UsersRole usersRole : usersRoles) {
					Role role = usersRole.getRoleId();
					if (role != null) {
						Long id = role.getId();
						List<RoleMenu> roleMenus = authorityService.getRoleOneMenuListByRoleId(id);
						if (roleMenus != null && roleMenus.size() > 0) {
							for (RoleMenu roleMenu : roleMenus) {
								roleMenuList.add(roleMenu);
							}
						}
					}
				}
			}
			List<Map<String, Object>> list = new ArrayList<>();
			if (roleMenuList != null && roleMenuList.size() > 0) {
				for (RoleMenu roleMenu : roleMenuList) {
					if (roleMenu.getMenuId().getPid() == null) {
						// 一级菜单
						Map<String, Object> menuMap = new HashMap<>();
						if (roleMenu != null) {
							Long id = roleMenu.getMenuId().getId();
							menuMap.put("id", id);
							String oneMenuName = roleMenu.getMenuId().getName();
							menuMap.put("name", oneMenuName);
							String remark = roleMenu.getMenuId().getRemark();
							menuMap.put("remark", remark);
							// 二级菜单
							List<Map<String, Object>> childrenMenuList = new ArrayList<>();
							List<RoleMenu> roleMenus = authorityService.getTwoMenuListByRoleIdAndPId(
									roleMenu.getRoleId().getId(), roleMenu.getMenuId().getId());
							if (roleMenus != null && roleMenus.size() > 0) {
								for (RoleMenu roleMenu2 : roleMenus) {
									Map<String, Object> childrenMenuMap = new HashMap<>();
									childrenMenuMap.put("id", roleMenu2.getMenuId().getId());
									childrenMenuMap.put("name", roleMenu2.getMenuId().getName());
									childrenMenuMap.put("url", roleMenu2.getMenuId().getUrl());
									childrenMenuList.add(childrenMenuMap);
								}
							}
							menuMap.put("children", childrenMenuList);
							list.add(menuMap);
						}
					}
				}
			}

			// 给登录用户设置角色权限(左侧菜单栏) 通过关联对象获取
//			List<Map<String,Object>> list = new ArrayList<>();
//			if (ret.getListRole() != null) {
//				Iterator<UsersRole> usersRoleIt = ret.getListRole().iterator();
//				if (usersRoleIt != null) {
//					while (usersRoleIt.hasNext()) {
//						UsersRole usersRole = (UsersRole) usersRoleIt.next();
//						if(usersRole != null) {
//							Set<RoleMenu> listRoleMenu = usersRole.getRoleId().getListRoleMenu();
//							if(listRoleMenu != null) {
//								Iterator<RoleMenu> roleMenuIt = listRoleMenu.iterator();
//								if (roleMenuIt != null) {
//									while (roleMenuIt.hasNext()) {
//										// 一级菜单
//										Map<String,Object> menuMap = new HashMap<>();
//										RoleMenu roleMenu = roleMenuIt.next();
//										if(roleMenu != null) {
//											Long id = roleMenu.getMenuId().getId();
//											menuMap.put("id", id);
//											String oneMenuName = roleMenu.getMenuId().getName();
//											menuMap.put("name", oneMenuName);
//											String remark = roleMenu.getMenuId().getRemark();
//											menuMap.put("remark", remark);
//											// 二级菜单
//											List<Menu> children = roleMenu.getMenuId().getChildren();
//											Iterator<Menu> childrenMenuIt = children.iterator();
//											List<Map<String, Object>> childrenMenuList = new ArrayList<>();
//											while (childrenMenuIt.hasNext()) {
//												Menu childrenMenu = childrenMenuIt.next();
//												Map<String, Object> childrenMenuMap = new HashMap<>();
//												childrenMenuMap.put("id", childrenMenu.getId());
//												childrenMenuMap.put("name", childrenMenu.getName());
//												childrenMenuMap.put("url", childrenMenu.getUrl());
//												childrenMenuList.add(childrenMenuMap);
//											}
//											menuMap.put("children", childrenMenuList);
//											list.add(menuMap);
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
			Object resource = JSONObject.toJSON(list);
			String jsonString = JSONObject.toJSONString(resource);
			map.put("resultResource", jsonString);
			return serverAck.getSuccess().setMessage("登陆成功").setData(map);
//			password = Md5Util.EncoderByMd5(password).trim();
//			Users ret = accountService.login(userName.trim(), password);
//			if (ret != null && ret.getId() != null) {
//				List<Authority> list = ret.getListAuth();
//				if (list == null || list.size() == 0) {
//					return serverAck.getParamError().setMessage("登录失败，该账号不是管理员");
//				} else {
//					boolean status = false;
//					for (int i = 0; i < list.size(); i++) {
//						int grade = list.get(i).getGrade();
//						if (grade == 3 && list.get(i).getIsDeleted() == 0) {
//							status = true;
//							break;
//						}
//
//					}
//					if (status) {
//						// 登陆成功
//						// 获取token
//						Subject sb = SecurityUtils.getSubject();
//						UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
//						sb.login(token);
//						System.out.println(sb.getSession().getId());
//						Map<String, Object> map = new HashMap<>(16);
//						map.put("token", token);
//						map.put("name", userName);
//						map.put("id", ret.getId());
//						int accontStatus = 0;
//						if (userName.equals(accountant)) {
//							accontStatus = 1;
//						}
//						map.put("status", accontStatus);
//						return serverAck.getSuccess().setMessage("登陆成功").setData(map);
//					} else {
//						return serverAck.getParamError().setMessage("登录失败，该账号不是管理员");
//					}
//				}
//
//			}
//			return serverAck.getFailure().setMessage("登陆失败，账号或密码有误");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 登出
	 *
	 * @param session
	 * @return
	 * @author tenglong
	 * @date 2018年8月3日
	 */
	@PostMapping("loginOut")
	public ResultData loginOut(String version, HttpSession session) {
		if (StringUtil.isEmpty(version)) {
			return serverAck.getParamError().setMessage("版本号不能为空");
		}
		if (serverVersion != null && !serverVersion.equals(version)) {
			return serverAck.getParamError().setMessage("版本错误");
		}
		try {
//			Object object = session.getAttribute(Constant.SESSION_KEY);
//			if(object == null) {
//				return serverAck.getEmptyData().setMessage("该账号已登出，请勿重复提交");
//			}
			// 清除 redis
			Subject sb = SecurityUtils.getSubject();
			sb.logout();
			session.removeAttribute(Constant.SESSION_KEY);
			return serverAck.getFailure().setMessage("登出成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param userName
	 * @param validateCode    验证码
	 * @param passWord
	 * @param confirmPassWord 确认密码
	 * @return
	 * @author tenglong
	 * @date 2018年8月3日
	 */
	@PostMapping("updatePassWord")
	public ResultData updatePassWord(String userName, String validateCode, String passWord, String confirmPassWord,
			String version, HttpSession session) {
		if (StringUtil.isEmpty(version)) {
			return serverAck.getParamError().setMessage("版本号不能为空");
		}
		if (serverVersion != null && !serverVersion.equals(version)) {
			return serverAck.getParamError().setMessage("版本错误");
		}
		if (StringUtil.isEmpty(userName)) {
			return serverAck.getParamError().setMessage("账号不能为空");
		}
		if (StringUtil.isEmpty(validateCode)) {
			return serverAck.getParamError().setMessage("验证码不能为空");
		}
		if (StringUtil.isEmpty(passWord)) {
			return serverAck.getParamError().setMessage("密码不能为空");
		}
		if (StringUtil.isEmpty(confirmPassWord)) {
			return serverAck.getParamError().setMessage("确认密码不能为空");
		}
		if (!passWord.equals(confirmPassWord)) {
			return serverAck.getParamError().setMessage("密码和确认密码不一致");
		}
		try {
			Users user = accountService.getUserByName(userName);
			if (user == null) {
				return serverAck.getEmptyData().setMessage("手机号未注册");
			}
			Object object = redisUtil.get(userName);
			if (object == null) {
				return serverAck.getEmptyData().setMessage("修改失败，请重新获取验证码");
			}
			if (!validateCode.equals(object)) {
				return serverAck.getFailure().setMessage("验证码错误");
			}

			// 二次密码加密
			ByteSource salt = ByteSource.Util.bytes(userName);
			String passwordSalt = new SimpleHash("md5", passWord, salt, 2).toHex();

//			passWord = Md5Util.EncoderByMd5(passWord).trim();
			user.setPasswordSalt(passwordSalt);
//			user.setPassword(passWord);
			int ret = accountService.regist(user);
			if (ret != 0) {
				session.removeAttribute(Constant.SESSION_KEY);
				return serverAck.getSuccess().setMessage("操作成功");
			}
			return serverAck.getFailure().setMessage("操作失败，账号异常，请联系管理员");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}
}
