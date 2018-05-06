package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.CheckBill;

import java.util.List;
import java.util.Map;

public interface CheckBillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CheckBill record);

    int insertSelective(CheckBill record);

    CheckBill selectByPrimaryKey(Integer id);

    CheckBill selectByCheckNo(String checkNo);

    List<CheckBill> selectByCriteriaAndPage(Map<String, Object> param);

    int updateByPrimaryKeySelective(CheckBill record);

    int updateByPrimaryKey(CheckBill record);
    
    List<CheckBill> selectByCriteria(Map<String, Object> map);
}