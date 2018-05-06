package com.rmd.wms.service;

import com.rmd.wms.bean.PurchaseBill;
import com.rmd.wms.bean.vo.app.PurchaseBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.exception.WMSException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * @author ZXLEI
 * @ClassName: PurchaseBillService
 * @Description: TODO(采购入库表)
 * @date Feb 20, 2017 1:25:53 PM
 */
public interface PurchaseBillService {

    List<PurchaseBill> ListPurchaseBills(Map<String, Object> parmaMap);

    PageBean<PurchaseBill> ListPurchaseBills(Integer page, Integer rows, Map<String, Object> parmaMap);

    int deleteByPrimaryKey(Integer id);

    int insert(PurchaseBill record);

    int insertSelective(PurchaseBill record);

    PurchaseBill selectByPrimaryKey(Integer id);

    PurchaseBill selectByPurchaseNo(String purchaseNo);

    int updateByPrimaryKeySelective(PurchaseBill record);

    int updateByPrimaryKey(PurchaseBill record);

    /**
     * 领取采购单任务
     *
     * @param purchaseBill
     * @return
     */
    PurchaseBillInfo getPurBillTask(PurchaseBill purchaseBill) throws InvocationTargetException, IllegalAccessException;

    /**
     * 采购入库
     *
     * @param pinfo
     * @return
     */
    ServerStatus purInStock(PurchaseBillInfo pinfo) throws WMSException;

}
