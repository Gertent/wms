package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.LocationGoodsBindOut;

/**
 * 
* @ClassName: LocationGoodsBindOutService 
* @Description: TODO(出库商品货位绑定) 
* @author ZXLEI
* @date Feb 24, 2017 9:04:08 AM 
*
 */
public interface LocationGoodsBindOutService {

    int deleteByPrimaryKey(Integer id);

    int insert(LocationGoodsBindOut record);

    int insertSelective(LocationGoodsBindOut record);

    LocationGoodsBindOut selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LocationGoodsBindOut record);

    int updateByPrimaryKey(LocationGoodsBindOut record);

    List<LocationGoodsBindOut> selectByOrderNoCode(Map<String, Object> parmaMap);
    
    int insertBatch(List<LocationGoodsBindOut> list);
}
