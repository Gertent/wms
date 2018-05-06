package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.vo.LocationGoodsBindVo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.bean.vo.web.SearchBindsParam;

public interface LocationGoodsBindService {
    
    int deleteByPrimaryKey(Integer id);

    int insert(LocationGoodsBind record);

    int insertSelective(LocationGoodsBind record);

    LocationGoodsBind selectByPrimaryKey(Integer id);

    LocationGoodsBind selectUnionPrimaryKey(LocationGoodsBind record);

    List<LocationGoodsBind> selectByCriteria(LocationGoodsBind record);

    List<LocationGoodsBindVo> selectByParmMap(Map<String,Object> map);

    List<LocationGoodsBind> searchBindsByCreateCheck(SearchBindsParam param);

    int updateByPrimaryKeySelective(LocationGoodsBind record);

    int updateByPrimaryKey(LocationGoodsBind record);

	PageBean<LocationGoodsBind> listLocationGoodsBind(Integer page,
			Integer rows, LocationGoodsBind locationGoodsBind);

    PageBean<LocationGoodsBindVo> selectByParmMapAdPage(Integer page,
                                                        Integer rows, Map<String,Object> map);


}
