package com.rmd.wms.web.util;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonBinder {
	
	private static final Logger log=LoggerFactory.getLogger("JSONBinder");
	
	public static String toJson(Object obj){
		return JSON.toJSONString(obj);
	}
	
	public static <T> T fromJson(String json,Class<T> clazz){
		
		if(StringUtils.isEmpty(json)){
			return null;
		}
		try {
			return JSON.parseObject(json, clazz);
		} catch (Exception e) {
			log.error(" parse from json string error: ",e);
		}
		
		return null;
	}
	
	public static <T> List<T> getListFromJson(String json,Class<T> clazz){
		if(StringUtils.isEmpty(json)){
			return null;
		}
		try {
			return JSON.parseArray(json, clazz);
		} catch (Exception e) {
			log.error("parse list from json error ", e);
		}
		return null;
	}
	
	/**
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> Map<String,T> getMapFromJson(String json,Class<T> clazz){
		if(StringUtils.isEmpty(json)){
			return null;
		}
		try {
			return JSON.parseObject(json,new TypeReference<Map<String,T>>(){});
		} catch (Exception e) {
			log.error("parse map from json error ", e);
		}
		return null;
	}
	
	/**
	 * 转换日期 default format 'yyyy-MM-dd HH:mm:ss'
	 * @param date
	 * @return
	 */
	public static String parseDate(Date date){
		if(date==null){
			return null;
		}
		return JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
	 * 转换日期 
	 * @param date 
	 * @param format your self format , if null then 'yyyy-MM-dd HH:mm:ss'
	 * @return
	 */
	public static String parseDate(Date date, String format){
		if(date==null){
			return null;
		}
		if(StringUtils.isEmpty(format)){
			return parseDate(date);
		}
		return JSON.toJSONStringWithDateFormat(date, format ,SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**调整为不排序**/
	public static JSONObject getJsonObject(String json) {
		return JSON.parseObject(json,Feature.OrderedField); ///不排序
	}
	
	/**调整为不排序**/
	public static JSONObject getJsonObjectWithSort(String json) {
		return JSON.parseObject(json); ///不排序
	}
	
	public static JSONObject toJsonObject(Object obj) {
		return JSON.parseObject(toJson(obj));
	}
	
}
