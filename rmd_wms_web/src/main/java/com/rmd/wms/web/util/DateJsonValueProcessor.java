package com.rmd.wms.web.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
/**
 * @Json日期格式转换
 * @author ZXL
 *
 */
public class DateJsonValueProcessor implements JsonValueProcessor {

	public static final String Default_DATE_PATTERN = "yyyy-MM-dd";

	private DateFormat dateFormat;
	private String type = "";
	
	/**
	 * @构造方法
	 * @param datePattern
	 * @param type (Date,Timestamp)
	 */
	public DateJsonValueProcessor(String datePattern, String type) {
		try {
			dateFormat = new SimpleDateFormat(datePattern);
			this.type=type;
		} catch (Exception e) {
			dateFormat = new SimpleDateFormat(Default_DATE_PATTERN);
		}
	}

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		if (value == null) {
			return "";
		} else {
			if(type.equals("Date")){
				return dateFormat.format((Date) value);
			}else{
				return dateFormat.format((Timestamp) value);
			}
			
		}
	}
}
