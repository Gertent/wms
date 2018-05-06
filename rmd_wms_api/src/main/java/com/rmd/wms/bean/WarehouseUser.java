package com.rmd.wms.bean;

import java.io.Serializable;

public class WarehouseUser implements Serializable {
	private static final long serialVersionUID = -747268517591512716L;

	private Integer id;

    private Integer wareId;

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}