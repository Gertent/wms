package com.rmd.wms.common.service;

import com.rmd.wms.exception.WMSException;

/**
 * 出库公共服务
 * Created by liu on 2017/3/16.
 */
public interface StockOutService {

    /**
     * 对商品锁库
     * @param orderNo
     * @param ginfoId
     * @param goodsCode
     * @param lockedNum
     * @param wareId
     * @return boolean
     */
    boolean lockedOneSku(String orderNo, Integer ginfoId, String goodsCode, Integer lockedNum, Integer wareId) throws WMSException;

    /**
     * 待拣货时对商品解锁库存
     * @param orderNo
     * @param wareId
     */
    void unlockedAllLockSku(String orderNo, Integer wareId) throws WMSException;
}
