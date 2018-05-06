package com.rmd.wms.app.common;

import com.alibaba.fastjson.JSONObject;
import com.rmd.bms.entity.User;
import com.rmd.bms.service.BmsApiService;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.Notifications;
import com.rmd.wms.app.model.CurrentUser;
import com.rmd.wms.app.util.AESCipher;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.constant.ConstantEncryption;
import com.rmd.wms.service.WarehouseService;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import yao.util.web.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * 用户登录状态切面
 */
@Component
@Aspect
public class LoginAspect {

	private Logger logger = Logger.getLogger(LoginAspect.class);

	@Autowired
	private BmsApiService bmsApiService;
	@Autowired
	private WarehouseService warehouseService;

	private Method getTargetMethod(Object target, Class<? extends Annotation> object) {
		Method[] mt = target.getClass().getMethods();
		for (int i = 0; i < mt.length; i++) {
			if (mt[i].getName().equals("setCurrentUser")) {
				return mt[i];
			}
		}
		return null;
	}

	private void setCurrentUser(Object target, CurrentUser user) {
		Method med = this.getTargetMethod(target, CurrentUserAnno.class);
		if (med != null) {
			try {
				med.invoke(target, user);
			} catch (Exception ex) {
				java.util.logging.Logger.getLogger(LoginAspect.class.getName()).log(Level.SEVERE, null, ex);
				throw (new RuntimeException("setCurrentUser Error "));
			}
		}
	}

	@Around("@annotation(com.rmd.wms.app.common.LoginRequired)")
	public Object mobileLoginCheck(ProceedingJoinPoint jp) throws Exception {
		Object[] arg = jp.getArgs();
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		JSONObject json = new JSONObject();
		int userId = 0;
		int wareId = 0;
		Cookie[] cookies = request.getCookies();
		String tokenStr = CookieUtil.getCookieValue("token", cookies);
		if (tokenStr == null) {
			json.put("status", "-1");
			json.put("massage", "token过期，请重新登录");
			return json;
		}
		AESCipher aes = new AESCipher(ConstantEncryption.APP_RANDOM);
		try {
			String decrypt = aes.decrypt(tokenStr);
			if (decrypt == null || decrypt.split(",") == null) {
				json.put("status", "-2");
				json.put("massage", "token校验失败，请重新登录");
				return json;
			}
			String[] strings = decrypt.split(",");
			userId = Integer.parseInt(strings[0]);
			wareId = Integer.parseInt(strings[1]);
		} catch (Exception e){
			logger.error(e);
		}
		Notification<User> noti = bmsApiService.selectUserByUserid(userId);
		if (noti == null) {
			json.put("status", "-3");
			json.put("massage", "用户服务异常");
			return json;
		}
		if (Notifications.OK.getNotifCode() != noti.getNotifCode()) {
			json.put("status", "-4");
			json.put("massage", noti.getNotifInfo());
			return json;
		}
		User user = noti.getResponseData();
		if (user == null || user.getStatus() != Constant.UserStatus.STATUS_YES) {
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
			}
			json.put("status", "-5");
			json.put("massage", "用户不存在或已停用，请重新登录");
			return json;
		}
		Warehouse warehouse = warehouseService.selectByPrimaryKey(wareId);
		if (warehouse == null || Constant.TYPE_STATUS_NO == warehouse.getStatus()) {
			json.put("status", "-6");
			json.put("massage", "仓储不存在或已停用");
			return json;
		}
		CurrentUser u = new CurrentUser();
		u.setUser(user);
		u.setWarehouse(warehouse);
		this.setCurrentUser(jp.getThis(), u);
		try {
			return jp.proceed(arg);
		} catch (Throwable ex) {
			logger.info("Login Aspect mobileLoginCheck Error!", ex);
			throw (new RuntimeException(ex.getMessage()));
		}
	}
}
