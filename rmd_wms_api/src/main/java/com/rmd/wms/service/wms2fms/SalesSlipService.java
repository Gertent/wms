package com.rmd.wms.service.wms2fms;

import com.rmd.wms.exception.WMSException;

/**
 * 订单相关服务
 * @author : liu
 * @Date : 2017/4/20
 */
public interface SalesSlipService {

    /**
     * 推送入库的服务单
     * @param billNo
     * @throws WMSException
     */
    void pushSalesSlipIn(String billNo) throws WMSException;

    /**
     * 推送出库的服务或订单
     * @param billNo
     * @throws WMSException
     */
    void pushSalesSlipOut(String billNo) throws WMSException;

}
