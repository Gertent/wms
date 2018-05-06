package com.rmd.wms.common.service;

import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.LocationGoodsBindIn;
import com.rmd.wms.bean.PurchaseInInfo;
import com.rmd.wms.exception.WMSException;

import java.util.List;

/**
 * 入库公共服务
 * Created by liu on 2017/3/13.
 */
public interface InStockService {

    /**
     * 上架修改库存信息
     *
     * @param bindIn
     * @param wareId
     */
    void putawayAlterStock(LocationGoodsBindIn bindIn, Integer wareId) throws WMSException;

    /**
     * 生成入库单
     * @param inStockBill
     * @param inInfosParam
     * @return inStockNo
     */
    String generateInStockBill(InStockBill inStockBill, List<PurchaseInInfo> inInfosParam) throws WMSException;


}
