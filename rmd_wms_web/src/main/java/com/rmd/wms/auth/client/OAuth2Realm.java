package com.rmd.wms.auth.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.rmd.bms.entity.User;
import com.rmd.bms.service.MenuOperationService;

public class OAuth2Realm extends AuthorizingRealm {

	@Autowired
	private MenuOperationService menuOperationService;
	
	private String clientId;
	private String accessTokenUrl;
	private String clientSecret;
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}

	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	/**
	 * 表示此Realm只支持OAuth2Token类型
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof OAuth2Token;
	}

	/**
	 * 权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Subject subject = SecurityUtils.getSubject(); 
	    User user = (User) subject.getPrincipal();
	    List<String> list = menuOperationService.selectOperationCodeList(user.getId(), user.getSystemId());
	    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	    for(String p : list){
	        authorizationInfo.addStringPermission(p);
	    }
	    return authorizationInfo;
	}

	/**
	 * 登录
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		OAuth2Token oAuth2Token = (OAuth2Token) token;
		String code = oAuth2Token.getAuthCode();
		User user = extractUsername(code);
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, code, getName());
		return authenticationInfo;
	}

	private User extractUsername(String code) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("clientId", clientId);
		param.put("clientSecret", clientSecret);
		param.put("code", code);
		param.put("date", String.valueOf(System.currentTimeMillis()));
		String body = "";
		try{
			body = HttpClientUtil.post(accessTokenUrl, param);
		}catch(Exception e){
			throw new AuthenticationException("请求超时");
		}
		JSONObject json = JSONObject.parseObject(body);
		if(json.containsKey("error")){
			throw new AuthenticationException(json.getString("error"));
		}
		if(json.containsKey("user")){
			User user = json.getObject("user", User.class);
			return user;
		}
		throw new AuthenticationException("登录出错，请联系管理员");
	}
}
