package com.rmd.wms.bean.vo.web;

import com.rmd.wms.bean.LogisticsFreightBill;

import java.io.Serializable;

/**
 * 物流运费单vo
 * @author : liu
 * @Date : 2017/6/23
 */
public class LogisFreiBillVo extends LogisticsFreightBill implements Serializable{

    private String starTime;
    private String endTime;

    public String getStarTime() {
        return starTime;
    }

    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
