package com.rmd.wms.app.model;

import com.rmd.bms.entity.User;
import com.rmd.wms.bean.Warehouse;

import java.util.Date;

public class CurrentUser {

    private User user;
    private Date logintime;
    private String loginip;
    private Warehouse warehouse;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
