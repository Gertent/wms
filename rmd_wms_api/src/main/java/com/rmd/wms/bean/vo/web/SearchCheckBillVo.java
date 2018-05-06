package com.rmd.wms.bean.vo.web;

import com.rmd.wms.bean.CheckBill;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询盘点单条件
 * @author : liu
 * @Date : 2017/4/18
 */
public class SearchCheckBillVo extends CheckBill implements Serializable {

    private static final long serialVersionUID = 3281944356023556579L;

    private Date starTime;
    private Date endTime;
    private String checkUserNames;

    public Date getStarTime() {
        return starTime;
    }

    public void setStarTime(Date starTime) {
        this.starTime = starTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCheckUserNames() {
        return checkUserNames;
    }

    public void setCheckUserNames(String checkUserNames) {
        this.checkUserNames = checkUserNames;
    }
}
