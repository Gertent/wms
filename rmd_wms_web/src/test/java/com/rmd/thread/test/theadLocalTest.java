package com.rmd.thread.test;

/**
 * Created by wyf on 2017/7/26.
 */
public class theadLocalTest extends ThreadLocal {

    @Override
    protected Object initialValue(){
        return "我是默认值，第一次不在为null";
    }
}
