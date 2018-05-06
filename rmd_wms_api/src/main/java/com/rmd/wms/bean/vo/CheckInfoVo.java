package com.rmd.wms.bean.vo;

import com.rmd.wms.bean.CheckInfo;

import java.io.Serializable;

/**
 * Created by win7 on 2017/5/4.
 */
public class CheckInfoVo extends CheckInfo implements Serializable {
    //盘点人id ,可多个
    private Integer checkUserId;
    //盘点人名称,可多个
    private String checkUserName;

    public  Integer getCheckUserId() {
        return checkUserId;
    }
    public void    setCheckUserId(Integer checkUserId) {
        this.checkUserId = checkUserId;
    }
    public String  getCheckUserName() {
        return checkUserName;
    }
    public void    setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }

}
