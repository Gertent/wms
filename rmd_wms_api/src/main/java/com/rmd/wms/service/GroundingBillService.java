package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.GroundingBill;
import com.rmd.wms.bean.vo.web.PageBean;

/**
 * @author ZXLEI
 * @ClassName: GroundingBillService
 * @Description: TODO(上架单)
 * @date Feb 22, 2017 11:36:31 AM
 */
public interface GroundingBillService {

    int deleteByPrimaryKey(Integer id);

    int insert(GroundingBill record);

    int insertSelective(GroundingBill record);

    GroundingBill selectByPrimaryKey(Integer id);

    GroundingBill selectByInStockNo(String inStockNo);

    int updateByPrimaryKeySelective(GroundingBill record);

    int updateByPrimaryKey(GroundingBill record);

    List<GroundingBill> ListGroundingBills(Map<String, Object> parmaMap);
    
    PageBean<GroundingBill> ListGroundingBills(Integer page,Integer rows,Map<String, Object> parmaMap);

    GroundingBill selectByGroundingNo(String groundingNo);
    
    int insertBatch( List<GroundingBill> list);
    /**
     * 获取上架单及商品信息
     * @param parmaMap
     * @return
     * */
    List<Map<String,Object>> listAllByParmMap(Map<String,Object> parmaMap);
}
