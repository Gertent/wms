package com.rmd.wms.dao;

import com.rmd.wms.bean.LocationGoodsBind;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.vo.LocationGoodsBindVo;
import com.rmd.wms.bean.vo.web.SearchBindsParam;
import org.apache.ibatis.annotations.Param;

public interface LocationGoodsBindMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LocationGoodsBind record);

    int insertSelective(LocationGoodsBind record);

    LocationGoodsBind selectByPrimaryKey(Integer id);

    LocationGoodsBind selectUnionPrimaryKey(LocationGoodsBind record);

    List<LocationGoodsBind> selectByCriteria(LocationGoodsBind record);

    List<LocationGoodsBind> selectUnBindLocations(LocationGoodsBind record);

    List<LocationGoodsBindVo> selectByParmMap(Map<String,Object> map);

    List<LocationGoodsBind> searchBindsByCreateCheck(SearchBindsParam param);

    List<LocationGoodsBind> selectByIds(List<Integer> list);

    int updateByPrimaryKeySelective(LocationGoodsBind record);

    int updateBatchByPrimaryKey(List<LocationGoodsBind> list);

    int updateByPrimaryKey(LocationGoodsBind record);

	int selectCountByLocationNum(LocationGoodsBind locationGoodsBind);

}