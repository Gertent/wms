package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.Movement;
import com.rmd.wms.bean.MovementInfo;
import com.rmd.wms.bean.vo.app.MovementBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.MovementLocation;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.exception.WMSException;

/**
 * Created by liu on 2017/3/2.
 */
public interface MovementService {

    Movement selectByPrimaryKey(Integer id);

    /**
     * 拣货报损
     *
     * @param param
     * @return
     */
    ServerStatus pickingBreakage(MovementBillInfo param) throws WMSException;

    /**
     * 处理该仓库下的异常订单重新锁库
     * @param wareId
     * @return
     */
    ServerStatus relockedStock(Integer wareId) throws WMSException;

    /**
     * 校验普通报损和普通移库的货位商品和数量
     *
     * @param param
     * @param wareId
     * @return
     */
    ServerStatus verifyMovingAndBreakage(MovementInfo param, Integer wareId);

    /**
     * 普通报损和普通移库
     *
     * @param param
     * @return
     */
    ServerStatus movingAndBreakage(MovementBillInfo param) throws WMSException;

    /**
     * 查询移库任务列表
     *
     * @param movement
     * @return
     */
    List<Movement> getMovementList(Movement movement);

    /**
     * 领取移库任务
     * @param param
     * @return
     */
    MovementBillInfo getMovementTask(MovementBillInfo param) throws WMSException;

    /**
     * 移库入库
     * @param param
     * @return
     */
    ServerStatus movePutaway(MovementBillInfo param) throws WMSException;


    /**
     * 移库查询
     * @param parmaMap
     * @return
     */
    PageBean<MovementLocation> ListMovementBillInfo(int page, int rows, Map<String, Object> parmaMap);

    /**
     * 获取移库及商品信息
     * @param parmaMap
     * @return
     * */
    List<Map<String,Object>> listAllByParmMap(Map<String,Object> parmaMap);




}
