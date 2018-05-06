package com.rmd.wms.bean.vo;

import com.rmd.wms.bean.CheckBill;
import com.rmd.wms.bean.CheckInfo;
import com.rmd.wms.bean.CheckUser;

import java.io.Serializable;
import java.util.List;

/**
 * 盘点单及详情
 * @author : liu
 * @Date : 2017/4/12
 */
public class CheckInfos extends CheckBill implements Serializable {

    private static final long serialVersionUID = -6113274712895969702L;

    private Integer interrupt;
    private Integer regetTask;
    private List<CheckInfo> infos;
    private List<CheckUser> users;

    public Integer getInterrupt() {
        return interrupt;
    }

    public void setInterrupt(Integer interrupt) {
        this.interrupt = interrupt;
    }

    public Integer getRegetTask() {
        return regetTask;
    }

    public void setRegetTask(Integer regetTask) {
        this.regetTask = regetTask;
    }

    public List<CheckInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<CheckInfo> infos) {
        this.infos = infos;
    }

    public List<CheckUser> getUsers() {
        return users;
    }

    public void setUsers(List<CheckUser> users) {
        this.users = users;
    }
}
