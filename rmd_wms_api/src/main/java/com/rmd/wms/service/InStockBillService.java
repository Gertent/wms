package com.rmd.wms.service;

import com.rmd.wms.bean.GroundingBill;
import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.vo.ServerInStockParam;
import com.rmd.wms.bean.vo.app.InStockBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.exception.WMSException;

import java.util.List;
import java.util.Map;

/**
 * @author ZXLEI
 * @ClassName: InStockBillService
 * @Description: TODO(入库单)
 * @date Feb 22, 2017 11:37:09 AM
 */
public interface InStockBillService {

    List<InStockBill> ListInStockBills(Map<String, Object> parmaMap);
    
    PageBean<InStockBill> ListInStockBills(Integer page,Integer rows,Map<String, Object> parmaMap);

    int deleteByPrimaryKey(Integer id);

    int insert(InStockBill record);

    int insertSelective(InStockBill record);

    InStockBill selectByPrimaryKey(Integer id);

    InStockBill selectByInStockNo(String inStockNo);

    int updateByPrimaryKeySelective(InStockBill record);

    int updateByPrimaryKey(InStockBill record);

    /**
     * 领取上架单任务
     * @param groundingBill
     * @return
     */
    InStockBillInfo getInBillTask(GroundingBill groundingBill);

    /**
     * 上架
     * @param inStockBillInfo
     * @return
     */
    ServerStatus putaway(InStockBillInfo inStockBillInfo) throws WMSException;

    /**
     * 售后入库处理
     * @param param
     * @return
     * @throws WMSException
     */
    ServerStatus serverInStock(ServerInStockParam param) throws WMSException;

    /**
     * 售后入库，批量添加入库单，入库单详情，上架单
     * @param map
     * @return
     */
    @Deprecated
    int insertBatch(Map<String , Object> map);

    /**
     * 获取入库单及详情信息
     * @param parmaMap
     * @return
     * */
    List<Map<String,Object>> selectAllByParmMap(Map<String,Object> parmaMap);

}
