package com.insigma.shiro.realm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.StringUtil;
import com.insigma.common.util.EhCacheUtil;
import com.insigma.http.HttpRequestUtils;
import com.insigma.mvc.model.SPermission;
import com.insigma.mvc.model.SRole;
import com.insigma.mvc.model.SUser;
import com.insigma.shiro.cache.RedisCache;
import com.insigma.shiro.token.CustomUsernamePasswordToken;

public class MyShiroRealm extends AuthorizingRealm {

	
	private String API_BASE_URL="http://127.0.0.1:8091/myapi";

	@Autowired
	private RedisCache<String, Set<String>> redisCache;

	/**
	 * 认证
	 */
	public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		CustomUsernamePasswordToken token = (CustomUsernamePasswordToken) authcToken;
		try {
			// 调用接口
			String url = API_BASE_URL + "/getUserAndGroupInfo/"+ token.getUsername();
			JSONObject jsonobject = HttpRequestUtils.httpGet(url);
			SUser suser = (SUser) JSONObject.toBean(jsonobject.getJSONObject("obj"), SUser.class);
	
			if (suser == null) {
				throw new UnknownAccountException();// 没找到帐号
			}
	
			if (suser.getEnabled().equals("0")) {
				throw new LockedAccountException(); // 帐号锁定
			}
	
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(suser.getUsername(), suser.getPassword(), getName()); // realm // name
			setSession(SysUserUtil.SHIRO_CURRENT_USER_INFO, suser);
			SysUserUtil.setCurrentUser(suser);
			// 用户权限
			
			url = API_BASE_URL + "/findPermissionStr/" + token.getUsername();
			 jsonobject = HttpRequestUtils.httpGet(url);
			List<SPermission> permlist = JSONArray.toList(jsonobject.getJSONArray("obj"),SPermission.class);
			EhCacheUtil
					.getManager()
					.getCache("webcache")
					.put(new Element(SysUserUtil.SHIRO_CURRENT_PERM_LIST_INFO+ "_" + suser.getUsername(), SysUserUtil.filterPersmList(permlist)));
			
			// 清理缓存
			clearCachedAuthorizationInfo(authenticationInfo.getPrincipals());
			return authenticationInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 授权
	 */
	public AuthorizationInfo doGetAuthorizationInfo( PrincipalCollection principals) {
		String loginname = (String) principals.getPrimaryPrincipal();
		try {
			if (StringUtil.isNotEmpty(loginname)) {
				SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
				// 用户角色

				String url = API_BASE_URL + "/findRolesStr/" + loginname;
				JSONObject jsonobject = HttpRequestUtils.httpGet(url);
				List<SRole> rolelist = JSONArray.toList(jsonobject.getJSONArray("obj"),SRole.class);
				if (rolelist != null) {
					Set<String> roleset = new HashSet<String>();
					Iterator iterator_role = rolelist.iterator();
					while (iterator_role.hasNext()) {
						SRole srole = (SRole) iterator_role.next();
						roleset.add(srole.getCode());
					}
					authenticationInfo.setRoles(roleset);
				}

				// 用户权限
				url = API_BASE_URL + "/findPermissionStr/" + loginname;
				 jsonobject = HttpRequestUtils.httpGet(url);
				List<SPermission> permlist = JSONArray.toList(jsonobject.getJSONArray("obj"),SPermission.class);

				EhCacheUtil
						.getManager()
						.getCache("webcache")
						.put(new Element(
								SysUserUtil.SHIRO_CURRENT_PERM_LIST_INFO + "_"+ principals.getPrimaryPrincipal(),
								SysUserUtil.filterPersmList(permlist)));
				if (permlist != null) {
					Set<String> set = new HashSet<String>();
					Iterator iterator = permlist.iterator();
					while (iterator.hasNext()) {
						SPermission spermission = (SPermission) iterator.next();
						set.add(spermission.getCode());
					}
					authenticationInfo.setStringPermissions(set);
				}
				return authenticationInfo;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 清理缓存
	 * 
	 * @param principal
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		System.out.println("更新用户授权信息缓存");
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		super.clearCachedAuthorizationInfo(principals);
		super.clearCache(principals);
		super.clearCachedAuthenticationInfo(principals);
	}

	/**
	 * 清理缓存 redis
	 * 
	 * @param principal
	 */
	public void clearCachedAuthorizationInfo_rediscache(String principal) {
		System.out.println("更新用户授权信息缓存");
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		super.clearCachedAuthorizationInfo(principals);
		super.clearCache(principals);
		super.clearCachedAuthenticationInfo(principals);
		redisCache.remove(Constants.getUserPermissionCacheKey(principal));
		redisCache.remove(Constants.getUserRolesCacheKey(principal));
	}

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用
	 * 
	 * @see 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
	 */
	private void setSession(Object key, Object value) {
		Subject subject = SecurityUtils.getSubject();
		if (null != subject) {
			Session session = subject.getSession();
			if (null != session) {
				session.setAttribute(key, value);
			}
		}
	}

}
