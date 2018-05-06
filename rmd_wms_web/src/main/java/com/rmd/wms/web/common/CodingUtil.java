package com.rmd.wms.web.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CodingUtil {
	
	public static void main(String[] args) throws ParseException, InterruptedException {
		for(int i = 0; i < 100; i++) {
			Thread.sleep(1000);
			String test = CodingUtil.orderCodingBuilder("1");
			String test2 = CodingUtil.recognitionCodingBuilder();
		}
	}
	
	/**
	 * @author zhanghaiyang
	 * @param firstIndex
	 * @return String
	 * @throws ParseException
	 * 按照规则生成订单编码
	 */
	public static String orderCodingBuilder(String firstIndex) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String str = sdf.format(new Date());
	
		String str2 = sdf2.format(new Date()).substring(0, 11) + "00:00:00";
		Date begin = sdf2.parse(str2);
		Date end = new Date();
		long result = end.getTime() - begin.getTime();
		str += String.valueOf(result / 1000);
		str += String.format("%02d", (int) (1+Math.random()*(99)));
		/*long day=result/(24*60*60*1000);
		long hour=(result/(60*60*1000)-day*24);
		long min=((result/(60*1000))-day*24*60-hour*60);
		long s=(result/1000-day*24*60*60-hour*60*60-min*60);*/
		StringBuilder sb = new StringBuilder(firstIndex);
		return sb.append(str).toString();
	}
	
	/**
	 * @author zhanghaiyang
	 * @return string
	 * 按照规则生成公司转账识别码
	 */
	public static String recognitionCodingBuilder() {
		String str = "H" + String.format("%04d" , (int) (1 + Math.random() * (9999)));
		return str;
	}
	
	/**
	 * 随机生成6位数字
	 */
	public static String sixNumber() {
		int[] array = {0,1,2,3,4,5,6,7,8,9};
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
		    int index = rand.nextInt(i);
		    int tmp = array[index];
		    array[index] = array[i - 1];
		    array[i - 1] = tmp;
		}
		int result = 0;
		for(int i = 0; i < 6; i++) {
		    result = result * 10 + array[i];
		}
		int yyy = rand.nextInt(900000) + 100000;
		return String.valueOf(yyy);
	}
	
	//先留着
	public static void getRandomPwd() {
		  Random rd = new Random();
		  String n="";
		  int getNum;
		  do {
		   getNum = Math.abs(rd.nextInt())%10 + 48;//产生数字0-9的随机数
		   //getNum = Math.abs(rd.nextInt())%26 + 97;//产生字母a-z的随机数
		   char num1 = (char)getNum;
		   String dn = Character.toString(num1);
		   n += dn;
		  } while (n.length()<6);
	}
	
	/**
	 * @Description 判断字符串是否为空字符或NULL
	 * @author lc
	 * @param string
	 * @return boolean   
	 * @date 2016年8月3日 下午3:01:33
	 */
	public static boolean IsNullOrEmpty(String string)
	{
		return string == null || string.equals("");
	}
}
