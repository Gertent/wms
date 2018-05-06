package com.rmd.wms.service.impl;

import com.rmd.wms.bean.LogisticsFreightCity;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.LogisticsFreightCityMapper;
import com.rmd.wms.service.LogisticsFreightCityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wyf on 2017/6/21.
 */
@Service("logisticsFreightCityService")
public class LogisticsFreightCityServiceImpl extends BaseService implements LogisticsFreightCityService {
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getMapper(LogisticsFreightCityMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByCriteria(LogisticsFreightCity record){
        return this.getMapper(LogisticsFreightCityMapper.class).deleteByCriteria(record);
    }

    @Override
    public int insert(LogisticsFreightCity record) {
        return this.getMapper(LogisticsFreightCityMapper.class).insert(record);
    }

    @Override
    public int insertSelective(LogisticsFreightCity record) {
        return this.getMapper(LogisticsFreightCityMapper.class).insertSelective(record);
    }

    @Override
    public LogisticsFreightCity selectByPrimaryKey(Integer id) {
        return this.getMapper(LogisticsFreightCityMapper.class).selectByPrimaryKey(id);
    }

    @Override
    public List<LogisticsFreightCity> selectByCriteria(Map<String, Object> map) {
        return this.getMapper(LogisticsFreightCityMapper.class).selectByCriteria(map);
    }

    @Override
    public int updateByPrimaryKeySelective(LogisticsFreightCity record) {
        return this.getMapper(LogisticsFreightCityMapper.class).updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(LogisticsFreightCity record) {
        return this.getMapper(LogisticsFreightCityMapper.class).updateByPrimaryKey(record);
    }

    @Override
    public int insertBatch(List<LogisticsFreightCity> list) {
        return this.getMapper(LogisticsFreightCityMapper.class).insertBatch(list);
    }
}
