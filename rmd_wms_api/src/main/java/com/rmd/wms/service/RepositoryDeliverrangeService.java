package com.rmd.wms.service;

import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.WareDeliverRange;
import com.rmd.wms.bean.Warehouse;

import java.util.List;

/**
 * Created by liu on 2017/3/17.
 */
public interface RepositoryDeliverrangeService {

    /**
     * 通过条件查询配送范围信息
     * @param record
     * @return
     */
    List<WareDeliverRange> selectByCriteria(WareDeliverRange record);

    /**
     * 通过省份编码获取仓库信息
     * @param provCode
     * @return
     */
    Notification<Warehouse> selectWareByProvCode(String provCode);
}
