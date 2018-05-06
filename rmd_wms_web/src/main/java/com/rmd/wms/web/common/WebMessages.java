package com.rmd.wms.web.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/** 
 * @ClassName:	WebMessages 
 * @Description:TODO(这里用一句话描述这个类的作用) 
 * @author:	hanguoshuai
 * @date:	2015年11月3日 上午11:32:32 
 *  
 */
public class WebMessages {
	
	private MessageSource messageSource;
	private Locale locale;
	private Map<String,String> messages;
	
	/**
	 * 通过HttpServletRequest创建WebMessages
	 * 
	 * @param request
	 *            从request中获得MessageSource和Locale，如果存在的话。
	 */
	public WebMessages(HttpServletRequest request) {
		WebApplicationContext webApplicationContext = RequestContextUtils
				.getWebApplicationContext(request);
		if (webApplicationContext != null) {
			LocaleResolver localeResolver = RequestContextUtils
					.getLocaleResolver(request);
			Locale locale;
			if (localeResolver != null) {
				locale = localeResolver.resolveLocale(request);
				this.messageSource = webApplicationContext;
				this.locale = locale;
			}
		}
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * 获得本地化信息
	 * 
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * 设置本地化信息
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	/**
	 * 是否存在信息
	 * 
	 * @return
	 */
	public boolean hasMessages() {
		return messages != null && messages.size() > 0;
	}

	/**
	 * 信息数量
	 * 
	 * @return
	 */
	public int getCount() {
		return messages == null ? 0 : messages.size();
	}
	/**
	 * 信息列表
	 * 
	 * @return
	 */
	
	public Map<String,String> getMessagesMap(){
		if (messages == null) {
			messages = new HashMap<String,String>();
		}
		return messages;
		
	}
	
	
	/**
	 * 添加信息代码
	 * 
	 * @param code
	 *            信息代码
	 * @param args
	 *            信息参数
	 * @see org.springframework.context.MessageSource#getMessage
	 */
	public void addMessageCode(WebMessageCode code, Object... args) {
		getMessagesMap().put(code.getCode(),getMessage(code, args));
	}

	/**
	 * 添加信息代码
	 * 
	 * @param code
	 *            信息代码
	 * @see org.springframework.context.MessageSource#getMessage
	 */
	public void addMessageCode(WebMessageCode code) {
		getMessagesMap().put("code", code.getCode());
		getMessagesMap().put("desc",getMessage(code));
		getMessagesMap().put(code.getCode(),getMessage(code));
	}
	public void addMessageCodeSend(WebMessageCode code,String id) {
		getMessagesMap().put("code", code.getCode());
		getMessagesMap().put("desc",getMessage(code));
		getMessagesMap().put(code.getCode(),getMessage(code));
		getMessagesMap().put("id",id);
	}
	
	public String getMessage(WebMessageCode code, Object... args) {
		if (messageSource == null) {
			throw new IllegalStateException("MessageSource cannot be null.");
		}
		return messageSource.getMessage(code.getName(), args, locale);
	}
}
