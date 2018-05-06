package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.DeliveryRange;
import com.rmd.wms.bean.LogisticsCompany;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.LogisticsFreightTemplateVo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;

public interface LogisticsCompanyService {

    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsCompany record);

    int insertSelective(LogisticsCompany record);

    LogisticsCompany selectByPrimaryKey(Integer id);

    List<LogisticsCompany> selectByCriteria(LogisticsCompany record);

    int updateByPrimaryKeySelective(LogisticsCompany record);

    int updateByPrimaryKey(LogisticsCompany record);
    
    PageBean<LogisticsCompany> ListLogisticsCompanys(int page,int rows,Map<String, Object> parmaMap);

    /**
     * 通过省份编码和重量获取物流公司
     * @param provCode
     * @param parcelWeight (统一单位千克)
     * @return
     */
    Notification<LogisticsCompany> selectByProvCodeAndWeight(String provCode, Double parcelWeight);

    /**
     * 查询所有可用物流公司
     * @return
     */
    Notification<List<LogisticsCompany>> selectAllLogisComp();


    /**
     * 新增承运商及运费模板
     * @param logisticsCompany 承运商
     * @param deliveryRangeList 承运商配送范围
     * @param logisticsFreightList 运费模板
     * @return
     * */
    ServerStatus addLogisticsCompanyAdFreight(LogisticsCompany logisticsCompany, List<DeliveryRange> deliveryRangeList, List<LogisticsFreightTemplateVo> logisticsFreightList);

    /**
     * 修改承运商
     * @param logisticsCompany 承运商
     * @param deliveryRangeList 承运商配送范围
     * @return
     * */
    ServerStatus updateLogisticsCompany(LogisticsCompany logisticsCompany, List<DeliveryRange> deliveryRangeList);

    /**
     * 修改承运商运费模板
     * @param logisticsId 承运商id
     * @param logisticsFreightList
     * */
    ServerStatus updateFreightTemplate(Integer logisticsId, List<LogisticsFreightTemplateVo> logisticsFreightList);
}
