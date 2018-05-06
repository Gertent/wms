package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.StockOutInfo;
import com.rmd.wms.bean.vo.web.StockOutGoodsInfo;
/**
 * 
* @ClassName: StockOutInfoService 
* @Description: TODO(出库点商品详情) 
* @author ZXLEI
* @date Feb 23, 2017 10:59:34 AM 
*
 */
public interface StockOutInfoService {

    int deleteByPrimaryKey(Integer id);

    int insert(StockOutInfo record);

    int insertSelective(StockOutInfo record);

    StockOutInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockOutInfo record);

    int updateByPrimaryKey(StockOutInfo record);
    
    List<StockOutInfo> ListStockOutInfos(Map<String, Object> parmaMap);
    
    List<StockOutGoodsInfo> ListStockOutGoodsInfo(Map<String, Object> parmaMap);
}
