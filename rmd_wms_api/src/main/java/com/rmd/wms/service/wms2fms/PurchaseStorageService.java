package com.rmd.wms.service.wms2fms;

import com.rmd.wms.exception.WMSException;

/**
 * 采购入库服务
 * @author : liu
 * @Date : 2017/4/20
 */
public interface PurchaseStorageService {

    /**
     * 推送采购入库数据
     * @param billNo
     * @throws WMSException
     */
    void pushPurchaseInStock(String billNo) throws WMSException;

}
