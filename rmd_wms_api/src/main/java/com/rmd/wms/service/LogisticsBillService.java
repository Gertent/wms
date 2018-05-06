package com.rmd.wms.service;

import com.rmd.wms.bean.LogisticsBill;

/**
 * 
* @ClassName: LogisticsBillService 
* @Description: TODO(订单物流信息) 
* @author ZXLEI
* @date Feb 22, 2017 6:56:26 PM 
*
 */
public interface LogisticsBillService {

    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsBill record);

    int insertSelective(LogisticsBill record);

    LogisticsBill selectByPrimaryKey(Integer id);
    
    LogisticsBill selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(LogisticsBill record);

    int updateByPrimaryKey(LogisticsBill record);
}
