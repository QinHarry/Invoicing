package com.halen.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.halen.entity.Menu;
import com.halen.entity.Role;
import com.halen.entity.User;
import com.halen.repository.MenuRepository;
import com.halen.repository.RoleRepository;
import com.halen.repository.UserRepository;

/**
 * 自定义Realm
 * @author Administrator
 *
 */
public class MyRealm extends AuthorizingRealm{

	@Resource
	private UserRepository userRepository;
	
	@Resource
	private RoleRepository roleRepository;
	
	@Resource
	private MenuRepository menuRepository;
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName=(String) SecurityUtils.getSubject().getPrincipal();
		User user=userRepository.findByUserName(userName);
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		List<Role> roleList=roleRepository.findByUserId(user.getId());
		Set<String> roles=new HashSet<String>();
		for(Role role:roleList){
			roles.add(role.getName());
			List<Menu> menuList=menuRepository.findByRoleId(role.getId());
			for(Menu menu:menuList){
				info.addStringPermission(menu.getName());				
			}
		}
		info.setRoles(roles);
		return info;
	}

	/**
	 * 身份权限认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username=(String) token.getPrincipal();
		User user=userRepository.findByUserName(username);
		if(user!=null){
			AuthenticationInfo authcInfo=new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),"xxx");
			return authcInfo;
		}else{
			return null;			
		}
	}

}
