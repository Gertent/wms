package com.rmd.wms.dao;

import com.rmd.wms.bean.LogisticsFreightBill;
import com.rmd.wms.bean.vo.web.LogisFreiBillVo;

import java.util.List;

public interface LogisticsFreightBillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsFreightBill record);

    int insertBatch(List<LogisticsFreightBill> list);

    int insertSelective(LogisticsFreightBill record);

    LogisticsFreightBill selectByPrimaryKey(Integer id);

    List<LogisticsFreightBill> selectByCriteria(LogisFreiBillVo param);

    int updateByPrimaryKeySelective(LogisticsFreightBill record);

    int updateByPrimaryKey(LogisticsFreightBill record);
}