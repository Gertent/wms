package com.rmd.wms.service.wms2scm;

import com.rmd.wms.exception.WMSException;

/**
 * 采购上架相关服务
 * @author : liu
 * @Date : 2017/4/20
 */
public interface PurGroundingService {

    /**
     * 推送上架的采购单信息
     * @param inStockNo
     * @throws WMSException
     */
    void pushPurGrounding(String inStockNo) throws WMSException;


}
