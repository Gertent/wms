package com.rmd.wms.auth.client;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

public class OAuth2AuthenticationFilter extends AuthenticatingFilter {

	// oauth2 code参数名
	private static final String AUTHC_CODE_PARAM = "code";
	// 登录失败以后跳转的URL
	private String failureUrl;

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String code = httpRequest.getParameter(AUTHC_CODE_PARAM);
		return new OAuth2Token(code);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated()) {
			if (StringUtils.isEmpty(request.getParameter(AUTHC_CODE_PARAM))) {
				// 如果用户没有身份验证，且没有auth code，则重定向到服务端授权
				saveRequestAndRedirectToLogin(request, response);
				return false;
			}
		}
		return executeLogin(request, response);
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		String successUrl = null;
        boolean contextRelative = true;
        SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase(AccessControlFilter.GET_METHOD)) {
            successUrl = savedRequest.getRequestUrl();
            contextRelative = false;
        }
        if (successUrl == null) {
            successUrl = getSuccessUrl();
        }
        if (successUrl == null) {
            throw new IllegalStateException("Success URL not available via saved request or via the " +
                    "successUrlFallback method parameter. One of these must be non-null for " +
                    "issueSuccessRedirect() to work.");
        }
        WebUtils.issueRedirect(request, response, successUrl, null, contextRelative);
		return false;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		try {
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher(failureUrl).forward(request, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return false;
	}

}
