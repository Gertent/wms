package com.rmd.wms.common.service;

import com.rmd.wms.bean.MessageQueue;

import java.util.List;

/**
 * 任务处理服务
 * @author : liu
 * Date : 2017/3/24
 */
public interface TaskService {

    /**
     * 重新锁库任务
     */
    void relockedStock();

    /**
     * 修改订单状态
     */
    void alterOrderStatus();

    /**
     * 修改订单状态的异常处理
     */
    List<MessageQueue> getAOSErrorData();

    /**
     * 未锁成功订单重新锁库存
     */
    void errorOrderRelock();

    /**
     * 处理拣货中取消的订单
     */
    void doPickingCancelBill();

    int updateMQByPrimaryKeySelective(MessageQueue record);

}
