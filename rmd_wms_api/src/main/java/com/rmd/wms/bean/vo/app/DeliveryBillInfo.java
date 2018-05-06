package com.rmd.wms.bean.vo.app;

import com.rmd.wms.bean.DeliveryBill;
import com.rmd.wms.bean.StockOutBill;

import java.io.Serializable;
import java.util.List;

/**
 * 承运交接vo类
 * @author : liu
 * Date : 2017/2/25
 */
public class DeliveryBillInfo extends DeliveryBill implements Serializable {

    private Integer userId;

    private String userName;

    private List<StockOutBill> soBillList;

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

    public List<StockOutBill> getSoBillList() {
        return soBillList;
    }

    public void setSoBillList(List<StockOutBill> soBillList) {
        this.soBillList = soBillList;
    }
}
