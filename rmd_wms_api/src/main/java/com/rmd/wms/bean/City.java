package com.rmd.wms.bean;

import java.io.Serializable;

public class City implements Serializable {
    private Integer id;

    private Integer cCode;

    private String city;

    private Integer fatherCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getcCode() {
        return cCode;
    }

    public void setcCode(Integer cCode) {
        this.cCode = cCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getFatherCode() {
        return fatherCode;
    }

    public void setFatherCode(Integer fatherCode) {
        this.fatherCode = fatherCode;
    }
}