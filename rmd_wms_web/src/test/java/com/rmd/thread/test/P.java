package com.rmd.thread.test;

import com.github.pagehelper.StringUtil;
import com.sun.beans.decoder.ValueObject;

/**
 * Created by wyf on 2017/7/25.
 */
public class P {
    private String lock;
    public P(String lock){
        super();
        this.lock=lock;
    }
    public void setValue(){
        try{
            synchronized (lock){
                if(!StringUtil.isEmpty(lock)){
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
