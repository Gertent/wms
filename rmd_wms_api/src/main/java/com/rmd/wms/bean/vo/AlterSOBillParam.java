package com.rmd.wms.bean.vo;

import java.io.Serializable;

/**
 * Created by liu on 2017/3/16.
 */
public class AlterSOBillParam implements Serializable {

    private String orderNo;//订单号
    private Integer orderType;//订单号
    private Integer status;// 订单状态
    private Integer userId;
    private String userName;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
