package com.rmd.wms.web.common;

/** 
 * @ClassName:	WebMessageCode 
 * @Description:TODO(这里用一句话描述这个类的作用) 
 * @author:	hanguoshuai
 * @date:	2015年11月3日 上午11:33:30 
 *  
 */
public enum WebMessageCode {
	
	OPERATE_SUCESS("010000","global.success"),          //操作成功
	OPERATE_FAILED("011111","global.failed"), 			//操作失败
	LOGIN_SUCCESS("010001","global.login.success"),     //登录成功
	
	INVALID_CAPTCHA("020001","error.invalidCaptcha"),   //验证码错误
	EXCEPTION_CAPTCHA("020002","error.exceptionCaptcha"), //验证码无效
	LOGIN_FAILED("020003","global.login.failed"),       //登录失败
	LOGIN_UP_FAILED("020004","global.login.upfailed"),      //登录失败：用户名或密码错误
	
	INFO_EXIST("030001","global.info.exist");//该信息已存在
	
	
	private String code;  
	private String name;  
	
    // 构造方法   
    private  WebMessageCode(String code,String name) {  
    	this.code = code;  
        this.name = name;  
    }  
    
    
    public String getName(){
    	return this.name;
    }
    
    public String getCode(){
    	return this.code;
    }
}
