package com.rmd.wms.service;

import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.StockOutBill;
import com.rmd.wms.bean.vo.AlterSOBillParam;
import com.rmd.wms.bean.vo.OrderInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.app.StockOutBillInfo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.exception.WMSException;

import java.util.List;
import java.util.Map;


/**
 * @author ZXLEI
 * @ClassName: StockOutBillService
 * @Description: TODO(出库单)
 * @date Feb 22, 2017 11:46:51 AM
 */
public interface StockOutBillService {

    int deleteByPrimaryKey(Integer id);

    int insert(StockOutBill record);

    int insertSelective(StockOutBill record);

    StockOutBill selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockOutBill record);

    int updateByPrimaryKey(StockOutBill record);

    List<StockOutBill> ListStockOutBills(Map<String, Object> parmaMap);

    PageBean<StockOutBill> ListStockOutBills(int page, int rows, Map<String, Object> parmaMap);

    StockOutBill selectByOrderNo(String orderNo);

    /**
     * 查询拣货单简单数据
     *
     * @param orderNo
     * @return
     */
    StockOutBillInfo selectSobillInfoByOrderNo(String orderNo);

    /**
     * 获取拣货任务
     *
     * @param param
     * @param userId
     * @param userName
     * @return
     */
    StockOutBillInfo getPinkingTask(StockOutBill param, Integer userId, String userName, int orderStatus);

    /**
     * 拣货
     *
     * @param param
     * @return
     */
    ServerStatus doPicking(StockOutBillInfo param) throws WMSException;

    /**
     * 获取打包复检任务
     *
     * @param param
     * @return
     */
    StockOutBillInfo getRecheckTask(StockOutBill param);

    /**
     * 打包复检
     *
     * @param param
     * @return
     */
    ServerStatus doRechecking(StockOutBill param);

    /**
     * 添加出库单数据
     *
     * @param param
     * @return
     * @throws WMSException
     */
    Notification<Object> insertStockOutBillInfo(OrderInfo param) throws WMSException;

    /**
     * 订单的锁库
     *
     * @param orderNo
     * @return
     * @throws WMSException
     */
    Notification<Object> lockedStockOfOrder(String orderNo) throws WMSException;

    /**
     * oms下单锁库
     *
     * @param param
     * @return
     */
    Notification<Object> orderLock(OrderInfo param);

    /**
     * oms修改订单（取消，冻结和解冻）
     *
     * @param param
     * @return
     */
    Notification<Object> alterStockOutBill(AlterSOBillParam param);

    /**
     * 获取出库单及商品信息
     * @param parmaMap
     * @return
     * */
    List<Map<String,Object>> listAllByParmMap(Map<String,Object> parmaMap);

}
