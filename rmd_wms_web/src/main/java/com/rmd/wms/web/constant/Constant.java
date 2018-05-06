package com.rmd.wms.web.constant;


/**
 * 系统常量定义
 * @author YYL
 *
 */
public class Constant {
	
	public static final String SESSION_CURRENT_USER_KEY="CURRENTUSER";//用户session id
	
	//公司信息
	public static final String COMPANY_NAME = "雷蒙德国际电子商务有限公司";
	public static final String COMPANY_PHONE = "400-0000-0000";
	public static final String COMPANY_EMAIL = "@raymondgroups.com";
	public static final String COMPANY_WEBSITE = "http://www.raymondgroups.com0";
	
	/**存储sesison_user 的 key  {@value}**/
    public static final String SESSION_USER="s_user";
    public static final String SESSION_SHIROUSER="shiro_user";
    
    public static final int  PAGESIZE=3;
    
    // 0-停用 ,1-启用, 2-删除
    public static final Integer STATUS_ZERO =0;
    public static final Integer STATUS_ONE =1;
    public static final Integer STATUS_TWO =2;
    
    // 0-不可卖 ,1-可卖, 
    public static final Integer TYPE_ZERO =0;
    public static final Integer TYPE_ONE =1;

    /**
     * 当前仓库
     */
    public static final String CURRENT_WARE = "currentWare";
    
    //出库单列表条件标识
    public static final String OUTSTOCK_LISTWHERE = "outstockListWhere";

    //运费单列表查询条件
    public static final String LOGIS_FREI_BILLS_PARAM = "LOGIS_FREI_BILLS_PARAM";

    //运费单列表导出文件名称
    public static final String LF_BILL_EXPORT_FILE_NAME = "运费明细单";

    //条件查询标识
    public static final String SEARCH_FLAG = "1";



}
