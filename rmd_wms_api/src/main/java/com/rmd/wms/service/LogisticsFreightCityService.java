package com.rmd.wms.service;

import com.rmd.wms.bean.LogisticsFreightCity;

import java.util.List;
import java.util.Map;

/**
 * 运费模板城市关系表
 * @author : wangyf
 * @Date : 2017/6/21
 */
public interface LogisticsFreightCityService {
    /**
     *  通过主键删除对象
     * @param id
     * @return
     * */
    int deleteByPrimaryKey(Integer id);

    /**
     * 通过条件删除
     * @param record
     * @return
     * */
    int deleteByCriteria(LogisticsFreightCity record);
    /**
     *  插入对象
     * @param record
     * @return
     * */
    int insert(LogisticsFreightCity record);
    /**
     *  插入对象
     * @param record
     * @return
     * */
    int insertSelective(LogisticsFreightCity record);
    /**
     *  通过主键查询对象
     * @param id
     * @return
     * */
    LogisticsFreightCity selectByPrimaryKey(Integer id);
    /**
     * 通过条件查询对象列表
     * @param map
     * @return
     * */
    List<LogisticsFreightCity> selectByCriteria(Map<String, Object> map);
    /**
     * 更新对象
     * @param record
     * @return
     * */
    int updateByPrimaryKeySelective(LogisticsFreightCity record);
    /**
     * 更新对象
     * @param record
     * @return
     * */
    int updateByPrimaryKey(LogisticsFreightCity record);

    /**
     * 批量插入
     * @param list
     * */
    int insertBatch(List<LogisticsFreightCity> list);
}