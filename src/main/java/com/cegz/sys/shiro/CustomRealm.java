package com.cegz.sys.shiro;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.cegz.sys.model.adver.RoleMenu;
import com.cegz.sys.model.adver.Users;
import com.cegz.sys.model.adver.UsersRole;
import com.cegz.sys.service.adver.AccountService;
import com.cegz.sys.service.adver.ResourceService;

/**
 * 权限控制类
 * 
 * @author lijiaxin
 * @date 2018年10月19日
 */
public class CustomRealm extends AuthorizingRealm {

	@Resource
	private AccountService accountService;

	@Resource
	private ResourceService resourceService;

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("进入授权");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Users users = (Users) principals.getPrimaryPrincipal();
		// 给登录用户设置角色权限
		if (users.getListRole() != null) {
			Iterator<UsersRole> iterator = users.getListRole().iterator();
			if (iterator != null) {
				while (iterator.hasNext()) {
					UsersRole usersRole = (UsersRole) iterator.next();
					authorizationInfo.addRole(usersRole.getRoleId().getCode());
					Set<RoleMenu> listRoleMenu = usersRole.getRoleId().getListRoleMenu();
					Iterator<RoleMenu> iterator2 = listRoleMenu.iterator();
					if (listRoleMenu != null) {
						while (iterator2.hasNext()) {
							RoleMenu roleMenu = iterator2.next();
							if (roleMenu != null && roleMenu.getMenuId() != null) {
								Long menuId = roleMenu.getMenuId().getId();
								List<com.cegz.sys.model.adver.Resource> resourceList = resourceService
										.getResourceListByMenuId(menuId);
								for (com.cegz.sys.model.adver.Resource resource : resourceList) {
									if (resource != null) {
										authorizationInfo.addStringPermission(resource.getUrl());
									}
								}
							}
						}
					}
				}
			}
		}
		return authorizationInfo;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 获取用户的输入的账号.
		String userName = (String) token.getPrincipal();
		// 获取用户信息
		Users userInfo = accountService.getUserByName(userName);
		if (userInfo == null) {
			return null;
		}
		// 账户冻结
		if (userInfo.getIsDeleted() == 1) {
			throw new LockedAccountException();
		}
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userInfo, userInfo.getPasswordSalt(),
				ByteSource.Util.bytes(userInfo.getUserName()), getName());
		return authenticationInfo;
	}

}
