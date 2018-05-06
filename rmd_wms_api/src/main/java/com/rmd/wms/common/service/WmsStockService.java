package com.rmd.wms.common.service;

import com.rmd.wms.exception.WMSException;

/**
 * 库存服务（包括仓库和货位）
 * @author : liu
 * Date : 2017/3/27
 */

public interface WmsStockService {

    /**
     * 修改货位库存
     * @param locationNo
     * @param goodsCode
     * @param wareId
     * @param areaId
     * @param saleType
     * @param alterRealNum
     * @param alterValidNum
     * @throws WMSException
     */
    void alterLocaStock(String locationNo, String goodsCode, Integer wareId, Integer areaId, Integer saleType, Integer alterRealNum, Integer alterValidNum) throws WMSException;

    /**
     * 修改仓库库存
     * @param goodsCode
     * @param wareId
     * @param saleType
     * @param alterRealNum
     * @param alterValidNum
     * @throws WMSException
     */
    void alterWareStock(String goodsCode, Integer wareId, Integer saleType, Integer alterRealNum, Integer alterValidNum) throws WMSException;

    /**
     * 修改所有库存
     * @param locationNo
     * @param goodsCode
     * @param wareId
     * @param areaId
     * @param saleType
     * @param alterRealNum
     * @param alterValidNum
     * @throws WMSException
     */
    void alterWmsStock(String locationNo, String goodsCode, Integer wareId, Integer areaId, Integer saleType, Integer alterRealNum, Integer alterValidNum) throws WMSException;

}
