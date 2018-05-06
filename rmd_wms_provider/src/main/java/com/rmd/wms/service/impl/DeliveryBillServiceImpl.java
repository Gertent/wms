package com.rmd.wms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.rmd.commons.servutils.Notification;
import com.rmd.oms.constant.OrderStatus;
import com.rmd.oms.constant.OrderType;
import com.rmd.oms.entity.vo.OperateUserVo;
import com.rmd.oms.service.OrderBaseService;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.app.DeliveryBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.vo.UpdateOrderStatusParam;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.DeliveryBillMapper;
import com.rmd.wms.dao.LogisticsCompanyMapper;
import com.rmd.wms.dao.MessageQueueMapper;
import com.rmd.wms.dao.StockOutBillMapper;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.DeliveryBillService;
import com.rmd.wms.service.DictionaryService;
import com.rmd.wms.service.wms2fms.SalesSlipService;
import com.rmd.wms.util.AsyncExcutor;
import com.rmd.wms.util.WmsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import yao.util.collection.CollectionUtil;

import java.util.*;

/**
 * Created by liu on 2017/2/25.
 */
@Service("deliveryBillService")
public class DeliveryBillServiceImpl extends BaseService implements DeliveryBillService {

    private Logger logger = Logger.getLogger(DeliveryBillServiceImpl.class);

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private OrderBaseService orderBaseService;
    @Autowired
    private SalesSlipService salesSlipService;

    @Override
    public List<LogisticsCompany> getLogisticsCompanyList(LogisticsCompany record) {
        if (record == null) record = new LogisticsCompany();
        record.setStatus(Constant.TYPE_STATUS_YES);
        return this.getMapper(LogisticsCompanyMapper.class).selectByCriteria(record);
    }

    @Override
    public DeliveryBillInfo goDelivery(DeliveryBill param, Warehouse warehouse) {
        if (param == null || param.getLogisComId() == null) return null;
        // 查询该仓库下的所有待交接的出货单
        StockOutBill outBillParam = new StockOutBill(param.getLogisComId(), Constant.StockOutBillStatus.ENTER_THE_AWB, warehouse.getId());
        List<StockOutBill> stockOutBills = this.getMapper(StockOutBillMapper.class).selectWaitDelivery(outBillParam);
        if (stockOutBills == null || stockOutBills.size() < 1) return null;
        // 返回发货单数据
        DeliveryBillInfo info = new DeliveryBillInfo();
        info.setSoBillList(stockOutBills);
        // 如果该用户没有发货单，则新建发货单，否则，使用自己的发货单
        DeliveryBill dbillParam = new DeliveryBill(param.getDeliveryUser(), warehouse.getId(), null, param.getLogisComId());
        List<DeliveryBill> deliveryBills = this.getMapper(DeliveryBillMapper.class).selectByUser(dbillParam);
        if (deliveryBills != null && deliveryBills.size() > 0) {
            WmsUtil.copyPropertiesIgnoreNull(deliveryBills.get(0), info);
        } else {
            param.setWareId(warehouse.getId());
            param.setWareName(warehouse.getWareName());
            param.setDeliveryNo(dictionaryService.generateBillNo(Constant.BillNoPreFlag.FH, warehouse.getCode()));
            if (StringUtils.isBlank(param.getDeliveryNo())) {
                throw new WMSException("创建发货单失败");
            }
            Date now = new Date();
            param.setDeliveryStartTime(now);
            param.setCreateTime(now);
            this.getMapper(DeliveryBillMapper.class).insertSelective(param);
            WmsUtil.copyPropertiesIgnoreNull(param, info);
        }
        return info;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public ServerStatus delivery(DeliveryBillInfo param) {
        ServerStatus json = new ServerStatus();
        if (param == null || param.getId() == null || StringUtils.isBlank(param.getDeliveryNo()) || param.getSoBillList() == null) {
            json.setStatus("101");
            json.setMessage("参数错误");
            return json;
        }
        DeliveryBill deliveryBill = this.getMapper(DeliveryBillMapper.class).selectByPrimaryKey(param.getId());
        if (deliveryBill == null || !param.getWareId().equals(deliveryBill.getWareId()) || deliveryBill.getDeliveryEndTime() != null) {
            json.setStatus("102");
            json.setMessage("发货单不存在或不可重复交接");
            return json;
        }
        final String deliveryNo = param.getDeliveryNo();
        // 修改出库单中的状态
        Set<Integer> idSet = (Set<Integer>)CollectionUtil.toFieldSet(param.getSoBillList(), "id");
        List<Integer> list = new ArrayList<>(idSet);
        StockOutBill bill = new StockOutBill();
        bill.setStatus(Constant.StockOutBillStatus.SHIPPING);
        bill.setDeliveryNo(deliveryNo);
        this.getMapper(StockOutBillMapper.class).updateForDelivery(list, bill);
        // 修改发货单中的状态
        deliveryBill.setDeliveryEndTime(new Date());
        deliveryBill.setOrderSum(list.size());
        deliveryBill.setDodeliveryPrint(Constant.TYPE_STATUS_NO);
        this.getMapper(DeliveryBillMapper.class).updateByPrimaryKeySelective(deliveryBill);

        /*
         * 批量修改订单状态
         */
        for (StockOutBill soParamTemp : param.getSoBillList()) {
            Integer otv = Integer.valueOf(soParamTemp.getOrderNo().substring(0, 1));
            OrderType orderType = OrderType.getOrderType(otv);
            OperateUserVo operUser = new OperateUserVo();
            operUser.setUserId(param.getUserId());
            operUser.setUserName(param.getUserName());
            operUser.setRemark("wms修改订单状态");
            // 封装mq数据
            MessageQueue mq = new MessageQueue();
            mq.setBusinessType(Constant.MessageQueueType.ALTER_ORDER_STATUS);
            mq.setBusinessId(soParamTemp.getOrderNo());
            mq.setOperation(Constant.MessageQueueAOSOper.FOUR);
            Date now = new Date();
            mq.setCreatetime(now);
            mq.setUpdatetime(now);
            UpdateOrderStatusParam uosParam = new UpdateOrderStatusParam(orderType, soParamTemp.getOrderNo(), operUser, OrderStatus.ORDER_WAIT_RECEIVE);
            mq.setParam(JSON.toJSONString(uosParam));
            try {
                Notification<Boolean> noti = orderBaseService.updateOrderStatus(orderType, soParamTemp.getOrderNo(), operUser, OrderStatus.ORDER_WAIT_RECEIVE);
                // 放入任务队列
                if (noti != null && noti.getResponseData()) {
                    mq.setState(Constant.MessageQueueState.SUCCESS);
                } else {
                    mq.setState(Constant.MessageQueueState.FAILURE);
                }
                this.getMapper(MessageQueueMapper.class).insertSelective(mq);
            } catch (Exception e) {
                // 放入任务队列
                mq.setState(Constant.MessageQueueState.FAILURE);
                this.getMapper(MessageQueueMapper.class).insertSelective(mq);
                logger.error("修改订单状态异常", e);
            }
        }
        /*
         * 发货完成时，推送财务系统出库单信息
         */
        try {
            salesSlipService.pushSalesSlipOut(deliveryNo);
        } catch (Exception e) {
            logger.error(Constant.LINE + "推送出库信息到财务系统异常", e);
        }
        json.setStatus("200");
        json.setMessage("操作成功");
        return json;
    }

	@Override
	public int deleteByPrimaryKey(Integer id) {
		
		return this.getMapper(DeliveryBillMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(DeliveryBill record) {
		return this.getMapper(DeliveryBillMapper.class).insert(record);
	}

	@Override
	public int insertSelective(DeliveryBill record) {
		return this.getMapper(DeliveryBillMapper.class).insertSelective(record);
	}

	@Override
	public DeliveryBill selectByPrimaryKey(Integer id) {
		return this.getMapper(DeliveryBillMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(DeliveryBill record) {
		return this.getMapper(DeliveryBillMapper.class).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DeliveryBill record) {
		return this.getMapper(DeliveryBillMapper.class).updateByPrimaryKey(record);
	}

	@Override
	public List<DeliveryBill> ListDeliveryBills(Map<String, Object> parmaMap) {
		return this.getMapper(DeliveryBillMapper.class).selectAllByWhere(parmaMap);
	}

	@Override
	public PageBean<DeliveryBill> ListDeliveryBills(Integer page,
		Integer rows, Map<String, Object> parmaMap) {
	    PageHelper.startPage(page, rows);
	    return new PageBean<DeliveryBill>(this.getMapper(DeliveryBillMapper.class).selectAllByWhere(parmaMap));
	}
}
