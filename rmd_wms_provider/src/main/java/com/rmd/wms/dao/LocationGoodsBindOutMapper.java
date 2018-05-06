package com.rmd.wms.dao;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.LocationGoodsBindOut;

public interface LocationGoodsBindOutMapper {
	
    int deleteByPrimaryKey(Integer id);

    /**
     * 批量删除订单的缺货信息
     * @param parmaMap
     * @return
     */
    int deleteBatchOrderStockOut(Map<String, Object> parmaMap);

    int insert(LocationGoodsBindOut record);

    int insertBatch(List<LocationGoodsBindOut> list);

    int insertSelective(LocationGoodsBindOut record);

    LocationGoodsBindOut selectByPrimaryKey(Integer id);
    /**
     * 批量修改已拣数量
     * @param list
     * @return
     */
    int updateBatchByPrimaryKey(List<LocationGoodsBindOut> list);

    /**
     * 通过订单号修改出库记录
     * @param record
     * @return
     */
    int updateByOrderNo(LocationGoodsBindOut record);

    int updateByPrimaryKeySelective(LocationGoodsBindOut record);

    int updateByPrimaryKey(LocationGoodsBindOut record);

    List<LocationGoodsBindOut> selectByOrderNoCode(Map<String, Object> parmaMap);

    /**
     * 查询有效的拣货库位通过出库单号
     * @param record
     * @return
     */
    List<LocationGoodsBindOut> selectValidOutByOrderNo(LocationGoodsBindOut record);

    /**
     * 查询有效的拣货库位通过详情id
     * @param list
     * @return
     */
    List<LocationGoodsBindOut> selectValidOutByGinfoIds(List<Integer> list);

    
}