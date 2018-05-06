package com.rmd.wms.app.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DomainSetting {

	@Deprecated
	public static void setDomain(HttpServletResponse response){
		//跨域设置
		response.addHeader("Access-Control-Allow-Origin", "http://192.168.0.172");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");  
		response.addHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
	}

	public static void setDomain(HttpServletRequest request, HttpServletResponse response) {
		//跨域设置
		response.addHeader("Access-Control-Allow-Origin", getDomain(request.getHeader("Referer")));
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");  
		response.addHeader("Access-Control-Allow-Headers", "Origin, accept, token, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");

	}

	public static void setDomainUsedOrigin(HttpServletRequest request, HttpServletResponse response) {
		//跨域设置
		response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "Origin, accept, token, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
	}

	private static String getDomain(String url){
//		if (!StringUtil.isTrimEmpty(url)){
//			if (!url.startsWith("http://")){
//				url = "http://"+url;
//			}
//			int endIndex = url.indexOf("/", 7);
//			if (endIndex < 0){
//				return url;
//			}
//			return url.substring(0, endIndex);
//		}
		return "*";
	}
}
