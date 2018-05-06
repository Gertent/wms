package com.rmd.wms.service;

import com.rmd.wms.bean.OrderLogisticsInfo;

/**
 * 
* @ClassName: OrderLogisticsInfoService 
* @Description: TODO(订单收货人信息) 
* @author ZXLEI
* @date Feb 22, 2017 6:50:42 PM 
*
 */
public interface OrderLogisticsInfoService {

    int deleteByPrimaryKey(Integer id);

    int insert(OrderLogisticsInfo record);

    int insertSelective(OrderLogisticsInfo record);

    OrderLogisticsInfo selectByPrimaryKey(Integer id);
    
    OrderLogisticsInfo selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(OrderLogisticsInfo record);

    int updateByPrimaryKey(OrderLogisticsInfo record);
    
}
