package com.rmd.wms.service;

import com.rmd.wms.bean.DeliveryBill;
import com.rmd.wms.bean.LogisticsCompany;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.app.DeliveryBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: DeliveryBillService 
* @Description: TODO(发货单) 
* @author ZXLEI
* @date Feb 24, 2017 10:46:53 AM 
*
 */
public interface DeliveryBillService {

	int deleteByPrimaryKey(Integer id);

    int insert(DeliveryBill record);

    /**
     * 准备交接
     * @param param
     * @return
     */
    DeliveryBillInfo goDelivery(DeliveryBill param, Warehouse warehouse) throws InvocationTargetException, IllegalAccessException;

    int insertSelective(DeliveryBill record);

    DeliveryBill selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeliveryBill record);

    int updateByPrimaryKey(DeliveryBill record);
    
    List<DeliveryBill> ListDeliveryBills(Map<String, Object> parmaMap);
    
    PageBean<DeliveryBill> ListDeliveryBills(Integer page,Integer rows,Map<String, Object> parmaMap);

    /**
     * 交接
     * @param param
     * @return
     */
    ServerStatus delivery(DeliveryBillInfo param);
    
    List<LogisticsCompany> getLogisticsCompanyList(LogisticsCompany record);

}
