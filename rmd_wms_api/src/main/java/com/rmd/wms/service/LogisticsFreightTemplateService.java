package com.rmd.wms.service;

import com.rmd.wms.bean.LogisticsFreightTemplate;
import com.rmd.wms.bean.vo.LogisticsFreightTemplateVo;

import java.util.List;
import java.util.Map;
/**
 * 运费模板服务
 * @author : wangyf
 * @Date : 2017/6/21
 */
public interface LogisticsFreightTemplateService {
    /**
     *  通过主键删除
     * @param id
     * @return
     * */
    int deleteByPrimaryKey(Integer id);
    /**
     * 通过条件删除
     * @param record
     * @return
     * */
    int deleteByCriteria(LogisticsFreightTemplate record);
    /**
     * 插入运费模板
     * @param record
     * @return
     * */
    LogisticsFreightTemplate insert(LogisticsFreightTemplate record);
    /**
     * 插入运费模板
     * @param record
     * @return
     * */
    LogisticsFreightTemplate insertSelective(LogisticsFreightTemplate record);
    /**
     * 通过主键查询运费模板
     * @param id
     * @return
     * */
    LogisticsFreightTemplate selectByPrimaryKey(Integer id);
    /**
     * 通过查询条件获取运费模板列表
     * @param map
     * @return
     * */
    List<LogisticsFreightTemplate> selectByCriteria(Map<String, Object> map);
    /**
     * 更新运费模板
     * @param record
     * @return
     * */
    int updateByPrimaryKeySelective(LogisticsFreightTemplate record);
    /**
     * 更新运费模板
     * @param record
     * @return
     * */
    int updateByPrimaryKey(LogisticsFreightTemplate record);

    /**
     * 更新运费模板及运费地区关系
     * @param logisticsId 承运商id
     * @param logisticsFreightTemplateList
     * */
    void updateLogisticsFreightTemplateBatch(Integer logisticsId,List<LogisticsFreightTemplateVo> logisticsFreightTemplateList);
}