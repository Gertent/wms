package com.rmd.wms.auth.client;

import org.apache.shiro.authc.AuthenticationToken;

public class OAuth2Token implements AuthenticationToken{

	private static final long serialVersionUID = 5411368225790236378L;
	
	private String authCode;
    private String principal;
    
	public OAuth2Token(String code) {
		this.authCode = code;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public Object getCredentials() {
		return getAuthCode();
	}

}
