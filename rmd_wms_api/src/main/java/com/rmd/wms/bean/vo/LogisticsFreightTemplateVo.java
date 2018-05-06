package com.rmd.wms.bean.vo;

import com.rmd.wms.bean.LogisticsFreightCity;
import com.rmd.wms.bean.LogisticsFreightTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyf on 2017/6/22.
 */
public class LogisticsFreightTemplateVo extends LogisticsFreightTemplate implements Serializable {

    private String freightRange;//运费模板城市关系

    private List<LogisticsFreightCity> logisticsFreightCityList=new ArrayList<LogisticsFreightCity>();//运费模板城市关系

    public String getFreightRange() {
        return freightRange;
    }

    public void setFreightRange(String freightRange) {
        this.freightRange = freightRange;
    }

    public List<LogisticsFreightCity> getLogisticsFreightCityList() {
        return logisticsFreightCityList;
    }

    public void setLogisticsFreightCityList(List<LogisticsFreightCity> logisticsFreightCityList) {
        this.logisticsFreightCityList = logisticsFreightCityList;
    }
}
