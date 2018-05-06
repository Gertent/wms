package com.rmd.wms.service.wms2fms;

import com.rmd.wms.bean.po.InventoryRequestMqVo;
import com.rmd.wms.exception.WMSException;

import java.util.List;

/**
 * 提报对接接口
 * @author : liu
 * @Date : 2017/4/26
 */
public interface InventoryRequestService {

    /**
     * 推送给fms的提报信息
     * @param mqVoList
     * @throws WMSException
     */
    boolean pushInventoryReport(List<InventoryRequestMqVo> mqVoList) throws WMSException;

}
