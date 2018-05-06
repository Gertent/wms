package com.rmd.wms.bean.vo.web;

import java.io.Serializable;
import java.util.List;

/**
 * @author : liu
 * @Date : 2017/4/14
 */
public class SearchBindsParam implements Serializable{

    private static final long serialVersionUID = -8315798559320860213L;

    private Integer wareId;
    private String goodsName;
    private String spec;
    private String locationNo;
    private String areaIds;
    private List<Integer> areaIdList;
    private List<String> goodsCodeList;

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public List<Integer> getAreaIdList() {
        return areaIdList;
    }

    public void setAreaIdList(List<Integer> areaIdList) {
        this.areaIdList = areaIdList;
    }

    public List<String> getGoodsCodeList() {
        return goodsCodeList;
    }

    public void setGoodsCodeList(List<String> goodsCodeList) {
        this.goodsCodeList = goodsCodeList;
    }
}
