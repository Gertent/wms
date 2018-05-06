package com.rmd.wms.common.service;

import com.rmd.wms.bean.CheckInfo;
import com.rmd.wms.exception.WMSException;

import java.util.List;

/**
 * 仓库盘点公共逻辑
 * @author : liu
 * @Date : 2017/4/26
 */
public interface WmsCheckService {

    /**
     * 盘点锁库
     * @param lockedList
     * @throws WMSException
     */
    void checkLockedStock(List<CheckInfo> lockedList) throws WMSException;
}
