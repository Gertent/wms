package com.rmd.wms.service;

import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.Stock;
import com.rmd.wms.bean.vo.StockInfoVo;
import com.rmd.wms.bean.vo.web.PageBean;

import java.util.List;
import java.util.Map;

public interface StockService {

    int deleteByPrimaryKey(Integer id);

    int insert(Stock record);

    int insertSelective(Stock record);

    Stock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Stock record);

    int updateByPrimaryKey(Stock record);

    /**
     * oms获取单商品库存数量
     * @param goodsCode
     * @param wareId
     * @return
     */
    Notification<Integer> getSkuStockNum(String goodsCode, Integer wareId);

    /**
     * 通过条件查询库存信息
     * @param param
     * @return
     */
    Notification<List<StockInfoVo>> getWareStockByCriteria(StockInfoVo param);
    
    /**
     * 查询库存信息并分页
     * @param param
     * @return
     * */
    PageBean<StockInfoVo> getStockInfoVoByParm(int page, int rows, Map<String, Object> param);

}
