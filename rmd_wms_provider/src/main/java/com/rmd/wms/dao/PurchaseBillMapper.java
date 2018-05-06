package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.PurchaseBill;

public interface PurchaseBillMapper {
	
	List<PurchaseBill> selectAllByWhere(Map<String, Object> parmaMap);
	
    int deleteByPrimaryKey(Integer id);

    int insert(PurchaseBill record);

    int insertSelective(PurchaseBill record);

    PurchaseBill selectByPrimaryKey(Integer id);

    PurchaseBill selectByPurchaseNo(String purchaseNo);

    int updateByPrimaryKeySelective(PurchaseBill record);

    int updateByPurchaseNoSelective(PurchaseBill record);

    int updateByPrimaryKey(PurchaseBill record);
}