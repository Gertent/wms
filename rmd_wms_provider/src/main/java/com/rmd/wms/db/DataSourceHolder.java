package com.rmd.wms.db;

/**
 * 动态访问数据库
 */
public class DataSourceHolder {
    private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

    public static String getDataSource() {
        return (String) dataSources.get();
    }

    public static void setDataSource(String customerType) {
        dataSources.set(customerType);
    }

    public static void clearDataSource() {
        dataSources.remove();
    }
}
