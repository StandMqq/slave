package com.cegz.sys.controller.adver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cegz.sys.config.pojo.ServerAck;
import com.cegz.sys.model.adver.Menu;
import com.cegz.sys.model.adver.Role;
import com.cegz.sys.model.adver.RoleMenu;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.adver.UsersRole;
import com.cegz.sys.model.view.adver.MenuView;
import com.cegz.sys.model.view.adver.RoleView;
import com.cegz.sys.redis.RedisUtil;
import com.cegz.sys.service.adver.AuthorityService;
import com.cegz.sys.util.ResultData;
import com.cegz.sys.util.StringUtil;
import com.cegz.sys.util.TokenUtil;
import com.cegz.sys.util.URLConnectionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 权限控制类
 *
 * @author lijiaxin
 * @date 2018年7月26日
 */
@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

	@Autowired
	private ServerAck serverAck;

	@Autowired
	private AuthorityService authorityService;

	/**
	 * 版本号
	 */
	@Value("${server.version}")
	private String serverVersion;

	@Value("${oss.access-key}")
	private String accessKey;

	@Value("${oss.secret-key}")
	private String secretKey;

	@Value("${oss.all-bucket}")
	private String bucket;

	@Value("${message.url}")
	private String messageUrl;

	@Value("${message.account}")
	private String account;

	@Value("${message.pwd}")
	private String pwd;

	@Value("${message.status}")
	private Boolean needStatus;

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

	@PostMapping("getUpToken")
	public ResultData getOssUpToken(HttpServletRequest request, String version) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		try {
			String uptoken = TokenUtil.getUpToken(accessKey, secretKey, bucket);
			return serverAck.getSuccess().setData(uptoken);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 获取code
	 * 
	 * @param phone
	 * @param version
	 * @return
	 * @author Administrator
	 * @date 2018年7月31日
	 */
	@PostMapping("getCode")
	public ResultData getPhoneCode(String phone, String version) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (StringUtil.isEmpty(phone)) {
			return serverAck.getParamError().setMessage("手机号不能为空");
		}
		if (!StringUtil.isMobile(phone)) {
			return serverAck.getParamError().setMessage("手机号格式有误");
		}
		try {
			// 获取随机验证码
			String vaildCode = (String) redisUtil.get(phone);
			if (StringUtil.isEmpty(vaildCode)) {
				Random random = new Random();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < 6; i++) {
					int part = random.nextInt(10);
					sb.append(part);
				}
				vaildCode = sb.toString();
			}
			Map<String, Object> paramMap = new HashMap<>(16);
			paramMap.put("account", account);
			paramMap.put("pswd", pwd);
			paramMap.put("mobile", phone);
			paramMap.put("needstatus", needStatus);
			paramMap.put("msg", "您的验证码是" + vaildCode + ",在10分钟内有效。非本人操作请忽略本短信");
			String ret = URLConnectionUtil.doGet(messageUrl, paramMap);
			redisUtil.set(phone, vaildCode, 6000);
			log.info("{}发送短信{},結果{}", phone, vaildCode, ret);
			return serverAck.getSuccess();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过用户id获取该用户对应角色
	 * 
	 * @param userId ： 用户id
	 * @return
	 * @author tenglong
	 * @date 2018年10月30日
	 */

	@RequiresPermissions(value = { "auth/getRolesByUserId" })
	@PostMapping("getRolesByUserId")
	public ResultData getRolesByUserId(Long userId, HttpServletRequest request, String version) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			List<RoleView> resultList = new ArrayList<>();
			// 所有角色列表
			List<Role> roleList = authorityService.getRoleList();
			// 用户对应的角色列表
			List<UsersRole> usersRoles = authorityService.getUsersRoleListByUserId(userId);
			if (roleList != null && roleList.size() > 0) {
				for (int i = 0; i < roleList.size(); i++) {
					RoleView view = new RoleView();
					Role role = roleList.get(i);
					view.setId(role.getId());
					view.setIsDeleted(role.getIsDeleted());
					view.setCode(role.getCode());
					view.setName(role.getName());
					view.setRemark(role.getRemark());
					view.setSort(role.getSort());
					view.setType(role.getType());

					// 判定角色菜单是否选中
					if (usersRoles != null && usersRoles.size() > 0) {
						for (UsersRole usersRole : usersRoles) {
							if (usersRole != null && usersRole.getRoleId() != null
									&& usersRole.getRoleId().getId() == role.getId()) {
								view.setIsSelected(true);
								break;
							} else {
								view.setIsSelected(false);
							}
						}
					}
					resultList.add(view);
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("resultList", resultList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 设置用户角色
	 * 
	 * @param userId  ： 用户id
	 * @param roleIds ： 角色id串
	 * @return
	 * @author tenglong
	 * @date 2018年10月30日
	 */

	@RequiresPermissions(value = { "auth/setUserRoles" })
	@PostMapping("setUserRoles")
	public ResultData setUserRoles(HttpServletRequest request, String version, Long userId, String roleIds) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		String tokens = request.getHeader("token");
		if (StringUtil.isEmpty(tokens)) {
			return serverAck.getLoginTimeOutError();
		}
		Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		if (userId == null) {
			return serverAck.getParamError().setMessage("用户id不能为空");
		}
		try {
			// 第一步：删除用户已有角色 user.getId()：操作人id
			authorityService.deleteRolesByUserId(userId, user.getId());
			// 第二步：重新设置对应角色
			String[] roleIdArr = roleIds.split(",");
			if (roleIdArr.length > 0) {
				for (String roleId : roleIdArr) {
					if (StringUtils.isNotEmpty(roleId)) {
						// user.getId()：操作人id
						authorityService.insertUsersRole(userId, Long.valueOf(roleId), user.getId());
					}
				}
			}
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 查询角色列表
	 * 
	 * @return
	 * @author tenglong
	 * @date 2018年10月31日
	 */

	@RequiresPermissions(value = { "auth/getRoleList" })
	@PostMapping("getRoleList")
	public ResultData getRoleList(HttpServletRequest request, String version, Integer curPage, Integer pageSize) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		try {
			int count = authorityService.getRoleCount();
			if (count == 0) {
				return serverAck.getEmptyData();
			}
			List<RoleView> resultList = new ArrayList<>();
			List<Role> roleList = authorityService.getRoleList();
			if (roleList != null && roleList.size() > 0) {
				for (int i = 0; i < roleList.size(); i++) {
					RoleView view = new RoleView();
					Role role = roleList.get(i);
					view.setId(role.getId());
					view.setIsDeleted(role.getIsDeleted());
					view.setCode(role.getCode());
					view.setName(role.getName());
					view.setRemark(role.getRemark());
					view.setSort(role.getSort());
					view.setType(role.getType());
					resultList.add(view);
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("totalCount", count);
			map.put("list", resultList);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 通过角色id获取角色对应菜单
	 * 
	 * @param roleId ： 角色id
	 * @return
	 * @author tenglong
	 * @date 2018年10月31日
	 */

	@RequiresPermissions(value = { "auth/getMenuByRoleId" })
	@PostMapping("getMenuByRoleId")
	public ResultData getMenuByRoleId(Long roleId, HttpServletRequest request, String version) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		if (checkToken(request) != null) {
			return checkToken(request);
		}
		if (roleId == null) {
			return serverAck.getParamError().setMessage("角色id不能为空");
		}
		try {
			List<MenuView> resultList = new ArrayList<>();
			// 所有菜单列表
			List<Menu> menuList = authorityService.getMenuList();
			// 角色对应的菜单列表
			List<RoleMenu> roleMenus = authorityService.getRoleMenuListByRoleId(roleId);
			if (menuList != null && menuList.size() > 0) {
				for (int i = 0; i < menuList.size(); i++) {
					MenuView view = new MenuView();
					Menu menu = menuList.get(i);
					view.setId(menu.getId());
					view.setIsDeleted(menu.getIsDeleted());
					view.setName(menu.getName());
					view.setRemark(menu.getRemark());
					view.setUrl(menu.getUrl());
					if (menu.getPid() != null) {
						view.setPid(menu.getPid().getId());
					}
					// 判定角色菜单是否选中
					if (roleMenus != null && roleMenus.size() > 0) {
						for (RoleMenu roleMenu : roleMenus) {
							if (roleMenu != null && roleMenu.getMenuId() != null
									&& roleMenu.getMenuId().getId() == menu.getId()) {
								view.setIsSelected(true);
								break;
							} else {
								view.setIsSelected(false);
							}
						}
					}
					resultList.add(view);
				}
			}

			// 组装数据
			List<Map<String, Object>> list = new ArrayList<>();
			if (resultList != null && resultList.size() > 0) {
				for (MenuView menuView : resultList) {
					if (menuView.getPid() == null) {
						Map<String, Object> menuMap = new HashMap<>();
						menuMap.put("id", menuView.getId());
						menuMap.put("name", menuView.getName());
						menuMap.put("isSelected", menuView.getIsSelected());
						menuMap.put("url", menuView.getUrl());
						List<Map<String, Object>> childrenMenuList = new ArrayList<>();
						for (MenuView childrenMenuView : resultList) {
							if (childrenMenuView != null && childrenMenuView.getPid() == menuView.getId()) {
								Map<String, Object> childrenMenuMap = new HashMap<>();
								childrenMenuMap.put("id", childrenMenuView.getId());
								childrenMenuMap.put("name", childrenMenuView.getName());
								childrenMenuMap.put("isSelected", childrenMenuView.getIsSelected());
								childrenMenuList.add(childrenMenuMap);
							}
						}
						menuMap.put("childrenMenu", childrenMenuList);
						list.add(menuMap);
					}
				}
			}
			Object resource = JSONObject.toJSON(list);
			String jsonString = JSONObject.toJSONString(resource);
			Map<String, Object> map = new HashMap<>();
			map.put("resultList", jsonString);
			return serverAck.getSuccess().setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

	/**
	 * 设置角色菜单
	 * 
	 * @param userId  ： 用户id
	 * @param roleIds ： 角色id串
	 * @return
	 * @author tenglong
	 * @date 2018年10月30日
	 */

	@RequiresPermissions(value = { "auth/setRoleMenus" })
	@PostMapping("setRoleMenus")
	public ResultData setRoleMenus(HttpServletRequest request, String version, Long roleId, String menuIds) {
		if (checkVersion(version) != null) {
			return checkVersion(version);
		}
		String tokens = request.getHeader("token");
		if (StringUtil.isEmpty(tokens)) {
			return serverAck.getLoginTimeOutError();
		}
		Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(tokens));
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			return serverAck.getParamError().setMessage("token无效");
		}
		if (roleId == null) {
			return serverAck.getParamError().setMessage("角色id不能为空");
		}
		try {
			// 第一步：删除用户已有角色 user.getId()：操作人id
			authorityService.deleteMenusByRoleId(roleId, user.getId());
			// 第二步：重新设置对应角色
			String[] menuIdArr = menuIds.split(",");
			if (menuIdArr.length > 0) {
				for (String menuId : menuIdArr) {
					if (StringUtils.isNotEmpty(menuId)) {
						// user.getId()：操作人id
						authorityService.insertRoleMenu(roleId, Long.valueOf(menuId), user.getId());
					}
				}
			}
			return serverAck.getSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return serverAck.getServerError();
		}
	}

}
