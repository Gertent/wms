package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.StockOutBill;
import org.apache.ibatis.annotations.Param;

public interface StockOutBillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StockOutBill record);

    int insertSelective(StockOutBill record);

    StockOutBill selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockOutBill record);

    int updateByOrderNoSelective(StockOutBill record);

    int updateByPrimaryKey(StockOutBill record);

    int updateBatchByPrimaryKeySelective(List<StockOutBill> list);

    List<StockOutBill> selectAllByWhere(Map<String, Object> parmaMap);

    List<StockOutBill> selectWaitDelivery(StockOutBill record);

    StockOutBill selectByOrderNo(String  orderNo);

    int updateForDelivery(@Param("list") List<Integer> list, @Param("bill") StockOutBill bill);

    List<Map<String,Object>> selectAllByParmMap(Map<String,Object> parmaMap);
}