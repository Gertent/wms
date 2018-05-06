package com.rmd.wms.service.impl;

import com.rmd.wms.bean.LogisticsFreightCity;
import com.rmd.wms.bean.LogisticsFreightTemplate;
import com.rmd.wms.bean.vo.LogisticsFreightTemplateVo;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.LogisticsFreightTemplateMapper;
import com.rmd.wms.enums.YesAdNoFlag;
import com.rmd.wms.service.LogisticsFreightCityService;
import com.rmd.wms.service.LogisticsFreightTemplateService;
import com.rmd.wms.util.WmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wyf on 2017/6/21.
 */
@Service("logisticsFreightTemplateService")
public class LogisticsFreightTemplateServiceImpl extends BaseService implements LogisticsFreightTemplateService {

    @Autowired
    private LogisticsFreightCityService logisticsFreightCityService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getMapper(LogisticsFreightTemplateMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByCriteria(LogisticsFreightTemplate record){
        return this.getMapper(LogisticsFreightTemplateMapper.class).deleteByCriteria(record);
    }

    @Override
    public LogisticsFreightTemplate insert(LogisticsFreightTemplate record) {
        this.getMapper(LogisticsFreightTemplateMapper.class).insert(record);
        return record;
    }

    @Override
    public LogisticsFreightTemplate insertSelective(LogisticsFreightTemplate record) {
        this.getMapper(LogisticsFreightTemplateMapper.class).insertSelective(record);
        return record;
    }

    @Override
    public LogisticsFreightTemplate selectByPrimaryKey(Integer id) {
        return this.getMapper(LogisticsFreightTemplateMapper.class).selectByPrimaryKey(id);
    }

    @Override
    public List<LogisticsFreightTemplate> selectByCriteria(Map<String, Object> map) {
        return this.getMapper(LogisticsFreightTemplateMapper.class).selectByCriteria(map);
    }

    @Override
    public int updateByPrimaryKeySelective(LogisticsFreightTemplate record) {
        return this.getMapper(LogisticsFreightTemplateMapper.class).updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(LogisticsFreightTemplate record) {
        return this.getMapper(LogisticsFreightTemplateMapper.class).updateByPrimaryKey(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLogisticsFreightTemplateBatch(Integer logisticsId,List<LogisticsFreightTemplateVo> logisticsFreightTemplateList){
        //删除该承运商下运费模板地区关系
        LogisticsFreightCity logisticsFreightCity=new LogisticsFreightCity();
        logisticsFreightCity.setLogisticsId(logisticsId);
        logisticsFreightCityService.deleteByCriteria(logisticsFreightCity);
        //删除该承运商下运费模板
        LogisticsFreightTemplate logisticsFreightTemplate=new LogisticsFreightTemplate();
        logisticsFreightTemplate.setLogisticsId(logisticsId);
        this.getMapper(LogisticsFreightTemplateMapper.class).deleteByCriteria(logisticsFreightTemplate);
        //重新新增运费模板
        List<LogisticsFreightCity> logisticsFreightCityList=null;//运费模板城市关系
        List<LogisticsFreightCity> newLogisticsFreightCityList=new ArrayList<>();//运费模板城市关系
        LogisticsFreightCity newLogisticsFreightCity=null;
        for(LogisticsFreightTemplateVo logisticsFreightTemplateVo:logisticsFreightTemplateList){
            logisticsFreightTemplate=new LogisticsFreightTemplate();
            WmsUtil.copyPropertiesIgnoreNull(logisticsFreightTemplateVo,logisticsFreightTemplate);
            logisticsFreightTemplate.setLogisticsId(logisticsId);
            this.getMapper(LogisticsFreightTemplateMapper.class).insertSelective(logisticsFreightTemplate);
            //添加运费模板地区映射
            if(YesAdNoFlag.A000.getValue()==logisticsFreightTemplate.getFreightType()) {
                logisticsFreightCityList = logisticsFreightTemplateVo.getLogisticsFreightCityList();
                for (LogisticsFreightCity logisticsFreightCity1 : logisticsFreightCityList) {
                    newLogisticsFreightCity = new LogisticsFreightCity();
                    WmsUtil.copyPropertiesIgnoreNull(logisticsFreightCity1, newLogisticsFreightCity);
                    newLogisticsFreightCity.setLogisticsId(logisticsId);
                    newLogisticsFreightCity.setTemplateId(logisticsFreightTemplate.getId());
                    newLogisticsFreightCityList.add(newLogisticsFreightCity);
                }
            }
            logisticsFreightCityService.insertBatch(newLogisticsFreightCityList);
        }
    }
}
