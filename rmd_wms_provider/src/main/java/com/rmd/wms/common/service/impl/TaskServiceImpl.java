package com.rmd.wms.common.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.Notifications;
import com.rmd.oms.service.OrderBaseService;
import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.MessageQueue;
import com.rmd.wms.bean.StockOutBill;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.InStockService;
import com.rmd.wms.common.vo.UpdateOrderStatusParam;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.MessageQueueMapper;
import com.rmd.wms.dao.StockOutBillMapper;
import com.rmd.wms.service.MovementService;
import com.rmd.wms.common.service.TaskService;
import com.rmd.wms.service.StockOutBillService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务处理实现
 * @author : liu
 * Date : 2017/3/24
 */
@Service("taskService")
public class TaskServiceImpl extends BaseService implements TaskService {

    private Logger logger = Logger.getLogger(TaskServiceImpl.class);

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private MovementService movementService;

    @Autowired
    private StockOutBillService stockOutBillService;

    @Autowired
    private InStockService inStockService;

    @Override
    public void relockedStock() {
        try {
            ServerStatus ss = movementService.relockedStock(null);
            logger.info(Constant.LINE + JSON.toJSONString(ss));
        } catch (Exception e) {
            logger.error(Constant.LINE + "任务处理重新锁库异常", e);
        }
    }

    @Override
    public void alterOrderStatus() {
        // 查询处理失败的任务
        MessageQueue mq = new MessageQueue(Constant.MessageQueueType.ALTER_ORDER_STATUS, null, Constant.MessageQueueState.FAILURE);
        List<MessageQueue> mqs = this.getMapper(MessageQueueMapper.class).selectByCriteria(mq);
        if (mqs == null || mqs.isEmpty()) {
            logger.info(Constant.LINE + "修改订单状态，没有待处理数据");
            return;
        }
        // 将处理失败的任务再次处理一下
        for (MessageQueue mqTemp : mqs) {
            UpdateOrderStatusParam uosParam = JSON.parseObject(mqTemp.getParam(), UpdateOrderStatusParam.class);
            try {
                Notification<Boolean> noti = orderBaseService.updateOrderStatus(uosParam.getVar1(), uosParam.getVar2(), uosParam.getVar3(), uosParam.getVar4());
                logger.debug(Constant.LINE + "orderBaseService.updateOrderStatus ：" + JSON.toJSONString(noti));
                if (noti != null && noti.getResponseData()) {
                    mqTemp.setState(Constant.MessageQueueState.SUCCESS);
                } else {
                    mqTemp.setState(Constant.MessageQueueState.REFAILURE);
                }
                this.getMapper(MessageQueueMapper.class).updateByPrimaryKeySelective(mqTemp);
                logger.info(Constant.LINE + "处理数据完成");
            } catch (Exception e) {
                mqTemp.setState(Constant.MessageQueueState.REFAILURE);
                this.getMapper(MessageQueueMapper.class).updateByPrimaryKeySelective(mqTemp);
                logger.error(Constant.LINE + "修改订单状态异常", e);
            }
        }
    }

    @Override
    public List<MessageQueue> getAOSErrorData() {
        // 获取所有修改订单状态，状态为重复失败的数据
        MessageQueue mq = new MessageQueue(Constant.MessageQueueType.ALTER_ORDER_STATUS, null, Constant.MessageQueueState.REFAILURE);
        return this.getMapper(MessageQueueMapper.class).selectByCriteria(mq);
    }

    @Override
    public void errorOrderRelock() {
        // 查询处理失败的任务
        MessageQueue mq = new MessageQueue(Constant.MessageQueueType.RELOCK, null, Constant.MessageQueueState.FAILURE);
        List<MessageQueue> mqs = this.getMapper(MessageQueueMapper.class).selectByCriteria(mq);
        if (mqs == null || mqs.isEmpty()) {
            logger.info(Constant.LINE + "锁库失败订单重新锁库，没有待处理数据");
            return;
        }
        // 将处理失败的任务再次处理一下
        for (MessageQueue mqTemp : mqs) {
            try {
                Notification<Object> noti = stockOutBillService.lockedStockOfOrder(mqTemp.getParam());
                if (noti != null && Notifications.OK.getNotifCode() == noti.getNotifCode()) {
                    mqTemp.setState(Constant.MessageQueueState.SUCCESS);
                } else {
                    mqTemp.setState(Constant.MessageQueueState.REFAILURE);
                }
                this.getMapper(MessageQueueMapper.class).updateByPrimaryKeySelective(mqTemp);
                logger.info(Constant.LINE + "处理数据完成");
            } catch (Exception e) {
                mqTemp.setState(Constant.MessageQueueState.REFAILURE);
                this.getMapper(MessageQueueMapper.class).updateByPrimaryKeySelective(mqTemp);
                logger.error(Constant.LINE + "修改订单状态异常", e);
            }
        }
    }

    @Override
    public void doPickingCancelBill() {
        // 查询处理拣货中的取消订单的任务
        MessageQueue mq = new MessageQueue(Constant.MessageQueueType.PICKING_CANTEL, null, Constant.MessageQueueState.WATTING);
        List<MessageQueue> mqs = this.getMapper(MessageQueueMapper.class).selectByCriteria(mq);
        if (mqs == null || mqs.isEmpty()) {
            logger.info(Constant.LINE + "拣货中取消订单，没有待处理数据");
            return;
        }
        // 处理任务
        for (MessageQueue mqTemp : mqs) {
            String param = mqTemp.getParam();
            InStockBill inStockBill = JSON.parseObject(param, InStockBill.class);
            StockOutBill outBill = this.getMapper(StockOutBillMapper.class).selectByOrderNo(mqTemp.getBusinessId());
            if (outBill == null || Constant.PickingStatus.PICKING == outBill.getPickingStatus()) {
                continue;
            }
            try {
                String billNo = inStockService.generateInStockBill(inStockBill, null);
                if (StringUtils.isBlank(billNo)) {
                    mqTemp.setState(Constant.MessageQueueState.FAILURE);
                } else {
                    mqTemp.setState(Constant.MessageQueueState.SUCCESS);
                }
                this.getMapper(MessageQueueMapper.class).updateByPrimaryKeySelective(mqTemp);
                logger.info(Constant.LINE + "处理数据完成");
            } catch (Exception e) {
                mqTemp.setState(Constant.MessageQueueState.FAILURE);
                this.getMapper(MessageQueueMapper.class).updateByPrimaryKeySelective(mqTemp);
                logger.error(Constant.LINE + "处理拣货中订单失败", e);
            }
        }


    }

    @Override
    public int updateMQByPrimaryKeySelective(MessageQueue record) {
        return this.getMapper(MessageQueueMapper.class).updateByPrimaryKeySelective(record);
    }
}
