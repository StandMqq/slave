package com.cegz.sys.shiro;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;

import com.cegz.sys.util.StringUtil;

/**
 * 获取session
 * 
 * @author lijiaxin
 * @date 2018年10月19日
 */
public class PermissionUtils {
	private static ThreadLocal<String> sessionToken = new ThreadLocal<String>();

	public static boolean isLogin(HttpServletRequest request) {
		String token = sessionToken(request);
		if (StringUtil.isEmpty(token))
			return false;
		/**
		 * 使用token检查是否存在登录session
		 */
		// Session session = SecurityUtils.getSecurityManager().getSession(new
		// WebSessionKey(token, request, response));
		Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(token));
		if (session != null) {
			session.touch();
			sessionToken.set(token);
			return true;
		}
		return false;
	}

	private static String sessionToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}
}
