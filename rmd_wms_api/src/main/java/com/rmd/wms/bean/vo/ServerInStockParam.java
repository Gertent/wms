package com.rmd.wms.bean.vo;

import com.rmd.wms.bean.OrderLogisticsInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 服务单入库参数
 *
 * @author : liu
 * Date : 2017/3/25
 */
public class ServerInStockParam implements Serializable {

    private static final long serialVersionUID = 3218169807038519104L;

    private String orderNo;
    private String serverNo;
    private Integer agree;
    private Integer wareId;
    private String wareName;
    private Integer userId;
    private String userName;
    private String remark;
    private Date orderdate;

    private List<InStockInfo> params;
    private OrderLogisticsInfo orderLogiInfo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getServerNo() {
        return serverNo;
    }

    public void setServerNo(String serverNo) {
        this.serverNo = serverNo;
    }

    public Integer getAgree() {
        return agree;
    }

    public void setAgree(Integer agree) {
        this.agree = agree;
    }

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public List<InStockInfo> getParams() {
        return params;
    }

    public void setParams(List<InStockInfo> params) {
        this.params = params;
    }

    public OrderLogisticsInfo getOrderLogiInfo() {
        return orderLogiInfo;
    }

    public void setOrderLogiInfo(OrderLogisticsInfo orderLogiInfo) {
        this.orderLogiInfo = orderLogiInfo;
    }
}
