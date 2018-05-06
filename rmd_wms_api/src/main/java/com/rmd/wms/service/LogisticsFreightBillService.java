package com.rmd.wms.service;

import com.rmd.wms.bean.LogisticsFreightBill;
import com.rmd.wms.bean.vo.web.LogisFreiBillVo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.exception.WMSException;

import java.math.BigDecimal;
import java.util.List;

/**
 * 物流运费单服务
 * @author : liu
 * @Date : 2017/6/22
 */
public interface LogisticsFreightBillService {

    /**
     * 通过发货单id生成物流运费单
     * @param deliveryIds
     */
    void generateFreightBill(List<Integer> deliveryIds) throws WMSException;

    /**
     * 查询运费单列表
     * @param page
     * @param rows
     * @param param
     * @return
     */
    PageBean<LogisticsFreightBill> getLFBillsByPage(int page, int rows, LogisFreiBillVo param);

    /**
     * 通过条件查询运费单列表
     * @param param
     * @return
     */
    List<LogisticsFreightBill> getLFBillsByCriteria(LogisFreiBillVo param);


    int updateByPrimaryKeySelective(LogisticsFreightBill record);

    LogisticsFreightBill selectByPrimaryKey(Integer id);

    BigDecimal getFreightPrice(Integer logisComId, String cityCode, Double parcelWeight, Integer wareId);
}
