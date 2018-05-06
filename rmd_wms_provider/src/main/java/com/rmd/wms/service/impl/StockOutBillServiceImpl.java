package com.rmd.wms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.NotificationBuilder;
import com.rmd.commons.servutils.Notifications;
import com.rmd.oms.constant.OrderStatus;
import com.rmd.oms.constant.OrderType;
import com.rmd.oms.entity.vo.OperateUserVo;
import com.rmd.oms.service.OrderBaseService;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.AlterSOBillParam;
import com.rmd.wms.bean.vo.OrderInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.app.StockOutBillInfo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.InStockService;
import com.rmd.wms.common.service.StockOutService;
import com.rmd.wms.common.service.WmsStockService;
import com.rmd.wms.common.vo.UpdateOrderStatusParam;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.*;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.StockOutBillService;
import com.rmd.wms.util.WmsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author ZXLEI
 * @ClassName: StockOutBillServiceImpl
 * @Description: TODO(出库单接口实现)
 * @date Feb 22, 2017 1:33:34 PM
 */
@Service("stockOutBillService")
public class StockOutBillServiceImpl extends BaseService implements StockOutBillService {

    private Logger logger = Logger.getLogger(StockOutBillServiceImpl.class);

    @Autowired
    private StockOutService stockOutService;
    @Autowired
    private InStockService inStockService;
    @Autowired
    private OrderBaseService orderBaseService;
    @Autowired
    private WmsStockService wmsStockService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getMapper(StockOutBillMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int insert(StockOutBill record) {
        return this.getMapper(StockOutBillMapper.class).insert(record);
    }

    @Override
    public int insertSelective(StockOutBill record) {
        return this.getMapper(StockOutBillMapper.class).insertSelective(record);
    }

    @Override
    public StockOutBill selectByPrimaryKey(Integer id) {
        return this.getMapper(StockOutBillMapper.class).selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(StockOutBill record) {

        return this.getMapper(StockOutBillMapper.class).updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(StockOutBill record) {

        return this.getMapper(StockOutBillMapper.class).updateByPrimaryKey(record);
    }

    @Override
    public List<StockOutBill> ListStockOutBills(Map<String, Object> parmaMap) {
        return this.getMapper(StockOutBillMapper.class).selectAllByWhere(parmaMap);
    }

    @Override
    public PageBean<StockOutBill> ListStockOutBills(int page, int rows, Map<String, Object> parmaMap) {
        PageHelper.startPage(page, rows);
        return new PageBean<StockOutBill>(this.getMapper(StockOutBillMapper.class).selectAllByWhere(parmaMap));
    }

    @Override
    public StockOutBill selectByOrderNo(String orderNo) {

        return this.getMapper(StockOutBillMapper.class).selectByOrderNo(orderNo);
    }

    @Override
    public StockOutBillInfo selectSobillInfoByOrderNo(String orderNo) {
        if (StringUtils.isBlank(orderNo)) return null;
        StockOutBill stockOutBill = this.getMapper(StockOutBillMapper.class).selectByOrderNo(orderNo);
        if (stockOutBill == null) return null;
        // 查询商品种类数
        int count = this.getMapper(StockOutInfoMapper.class).selectCountByOrderNo(orderNo);
        StockOutBillInfo info = new StockOutBillInfo();
        // 返回必要的数据，不需要全部返回
        WmsUtil.copyPropertiesIgnoreNull(stockOutBill, info);
        info.setOrderNo(orderNo);
        info.setSkuNum(count);
        return info;
    }

    @Override
    public StockOutBillInfo getPinkingTask(StockOutBill param, Integer userId, String userName, int orderStatus) {
        if (param == null || StringUtils.isBlank(param.getOrderNo())) return null;
        //拣货单绑定用户信息
        param.setPickingStatus(Constant.PickingStatus.PICKING);
        this.getMapper(StockOutBillMapper.class).updateByPrimaryKeySelective(param);
        // 查询订单详情数据
        StockOutInfo infoParam = new StockOutInfo();
        infoParam.setOrderNo(param.getOrderNo());
        List<StockOutInfo> outInfos = this.getMapper(StockOutInfoMapper.class).selectByCriteria(infoParam);
        // 出货单信息
        StockOutBillInfo billInfo = new StockOutBillInfo();
        billInfo.setOrderNo(param.getOrderNo());
        List<LocationGoodsBindOut> bindOutInfos = new ArrayList<>();
        for (StockOutInfo temp : outInfos) {
            // 查询有效的拣货库位
            LocationGoodsBindOut outParam = new LocationGoodsBindOut(param.getOrderNo(), temp.getGoodsCode(), param.getWareId());
            List<LocationGoodsBindOut> bindOuts = this.getMapper(LocationGoodsBindOutMapper.class).selectValidOutByOrderNo(outParam);
            bindOutInfos.addAll(bindOuts);
        }
        billInfo.setBindOutInfos(bindOutInfos);
        /*
         * 如果订单是103时，修改订单状态为104
         */
        if (OrderStatus.ORDER_CONFIRM.getValue() == orderStatus) {
            Integer otv = Integer.valueOf(param.getOrderNo().substring(0, 1));
            OrderType orderType = OrderType.getOrderType(otv);
            OperateUserVo operUser = new OperateUserVo();
            operUser.setUserId(userId);
            operUser.setUserName(userName);
            operUser.setRemark("wms修改订单状态");
            // 封装mq数据
            MessageQueue mq = new MessageQueue();
            mq.setBusinessType(Constant.MessageQueueType.ALTER_ORDER_STATUS);
            mq.setBusinessId(param.getOrderNo());
            mq.setOperation(Constant.MessageQueueAOSOper.FIVE);
            Date now = new Date();
            mq.setCreatetime(now);
            mq.setUpdatetime(now);
            UpdateOrderStatusParam uosParam = new UpdateOrderStatusParam(orderType, param.getOrderNo(), operUser, OrderStatus.ORDER_FINISH_PREPARE);
            mq.setParam(JSON.toJSONString(uosParam));
            try {
                Notification<Boolean> noti = orderBaseService.updateOrderStatus(orderType, param.getOrderNo(), operUser, OrderStatus.ORDER_FINISH_PREPARE);
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
        return billInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus doPicking(StockOutBillInfo param) throws WMSException {
        ServerStatus ss = new ServerStatus();
        /*
         * 1、校验数据
         */
        if (param == null || StringUtils.isBlank(param.getOrderNo()) || param.getBindOutInfos() == null || param.getPickingStatus() == null) {
            ss.setStatus("101");
            ss.setMessage("参数错误");
            return ss;
        }
        StockOutBill stockOutBill = this.getMapper(StockOutBillMapper.class).selectByOrderNo(param.getOrderNo());
        if (stockOutBill == null || !param.getWareId().equals(stockOutBill.getWareId())) {
            ss.setStatus("102");
            ss.setMessage("出货单不存在");
            return ss;
        }
        if (Constant.PickingStatus.FINISH == stockOutBill.getPickingStatus()) {
            ss.setStatus("103");
            ss.setMessage("不可重复提交");
            return ss;
        }
        List<LocationGoodsBindOut> outInfosParam = param.getBindOutInfos();
        // 校验提交的数据
        Map<String, Object> bindOutMap = new HashMap<>();
        bindOutMap.put("orderNo", param.getOrderNo());
        // 查询订单下的所有商品
        List<LocationGoodsBindOut> bindOuts = this.getMapper(LocationGoodsBindOutMapper.class).selectByOrderNoCode(bindOutMap);
        if (bindOuts == null || bindOuts.isEmpty()) {
            ss.setStatus("104");
            ss.setMessage("该订单无锁库记录");
            return ss;
        }

        /*
         * 2、修改已拣数量和出库单状态
         */
        // 批量修改已拣数量
        this.getMapper(LocationGoodsBindOutMapper.class).updateBatchByPrimaryKey(outInfosParam);
        // 正常则修改拣货单状态，报损及取消订单不修改订单状态，只有在报损过程中缺货了，修改主订单状态
        if (Constant.PickingStatus.FINISH == param.getPickingStatus() && stockOutBill.getPickingStatus() != Constant.PickingStatus.ERROR) {
            stockOutBill.setPickingStatus(param.getPickingStatus());
            stockOutBill.setRecheckStatus(Constant.RecheckStatus.WATTING);
            stockOutBill.setStatus(Constant.StockOutBillStatus.PACK_RECHECK);
            this.getMapper(StockOutBillMapper.class).updateByPrimaryKeySelective(stockOutBill);
        }
        /*
         * 3、修改货位和仓库的库存数
         */
        for (LocationGoodsBindOut paramTemp : outInfosParam) {
            if (paramTemp == null || paramTemp.getId() == null || paramTemp.getPickedNum() == null) {
                logger.debug(Constant.LINE + "详情参数错误");
                throw new WMSException("101", "参数错误");
            }
            for (LocationGoodsBindOut outTemp : bindOuts) {
                if (outTemp.getId().equals(paramTemp.getId())) {
                    if (paramTemp.getPickedNum() > outTemp.getLockedNum()) {
                        throw new WMSException("101", "参数错误");
                    }
                    // 修改的数量 = 每次提交的拣货数量 - 之前已经提交的数量
                    int alterNum = paramTemp.getPickedNum() - (outTemp.getPickedNum() == null ? 0 : outTemp.getPickedNum());
                    if (alterNum <= 0) {
                        continue;
                    }
                    // 修改库存数量
                    try {
                        wmsStockService.alterWmsStock(outTemp.getLocationNo(), outTemp.getGoodsCode(), outTemp.getWareId(), outTemp.getAreaId(), Constant.TYPE_STATUS_YES, -alterNum, null);
                    } catch (WMSException e) {
                        logger.error(Constant.LINE + "出库拣货修改库存错误： " + e.getMsg(), e);
                        e.setCode("105");
                        throw e;
                    }
                }
            }
        }
        ss.setStatus("200");
        ss.setMessage("操作成功");
        return ss;
    }

    @Override
    public StockOutBillInfo getRecheckTask(StockOutBill param) {
        if (param == null || StringUtils.isBlank(param.getOrderNo())) return null;
        //打包复检单绑定用户信息
        param.setRecheckStatus(Constant.RecheckStatus.RECHECKING);
        param.setRecheckStartTime(new Date());
        this.getMapper(StockOutBillMapper.class).updateByPrimaryKeySelective(param);
        // 查询订单详情数据
        StockOutInfo infoParam = new StockOutInfo();
        infoParam.setOrderNo(param.getOrderNo());
        List<StockOutInfo> outInfos = this.getMapper(StockOutInfoMapper.class).selectByCriteria(infoParam);
        StockOutBillInfo billInfo = new StockOutBillInfo();
        billInfo.setSoInfos(outInfos);
        return billInfo;
    }

    @Override
    public ServerStatus doRechecking(StockOutBill param) {
        ServerStatus json = new ServerStatus();
        if (param == null || StringUtils.isBlank(param.getOrderNo()) || param.getParcelWeight() == null) {
            json.setStatus("101");
            json.setMessage("参数错误");
            return json;
        }
        StockOutBill stockOutBill = this.getMapper(StockOutBillMapper.class).selectByOrderNo(param.getOrderNo());
        if (stockOutBill == null) {
            json.setStatus("102");
            json.setMessage("出库单不存在");
            return json;
        }
        if (Constant.RecheckStatus.FINISH == stockOutBill.getRecheckStatus()) {
            json.setStatus("103");
            json.setMessage("出库单已复检");
            return json;
        }
        WmsUtil.copyPropertiesIgnoreNull(param, stockOutBill);
        stockOutBill.setRecheckEndTime(new Date());
        stockOutBill.setRecheckStatus(Constant.RecheckStatus.FINISH);
        stockOutBill.setStatus(Constant.StockOutBillStatus.ENTER_THE_AWB);
        this.getMapper(StockOutBillMapper.class).updateByPrimaryKeySelective(stockOutBill);
        json.setStatus("200");
        json.setMessage("操作成功");
        return json;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Notification<Object> insertStockOutBillInfo(OrderInfo param) throws WMSException {
        Notification<Object> noti = new Notification<>();
        /*
         * 1、校验数据
         */
        // 校验参数
        if (param == null || StringUtils.isBlank(param.getOrderNo()) || param.getOrderLogiInfo() == null || param.getStockOutInfos() == null) {
            noti = NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
            noti.setNotifInfo("参数错误");
            return noti;
        }
        // 校验订单
        StockOutBill stockOutBill = this.getMapper(StockOutBillMapper.class).selectByOrderNo(param.getOrderNo());
        if (stockOutBill != null) {
            noti.setNotifCode(102);
            noti.setNotifInfo("该订单已存在");
            return noti;
        }
        /*
         * 2、插入订单数据
         */
        //插入出库单数据
        StockOutBill soBillParam = new StockOutBill();
        WmsUtil.copyPropertiesIgnoreNull(param, soBillParam);
        soBillParam.setPickingStatus(Constant.PickingStatus.ERROR);// 默认为异常状态
        soBillParam.setStatus(Constant.StockOutBillStatus.PICKING);//拣货阶段
        soBillParam.setFreeze(Constant.TYPE_STATUS_NO); //默认为未冻结
        soBillParam.setDobinningPrint(Constant.TYPE_STATUS_NO);//装箱单打印
        soBillParam.setDopickingPrint(Constant.TYPE_STATUS_NO);//拣货单打印
        soBillParam.setDowaybillPrint(Constant.TYPE_STATUS_NO);//运单打印
        this.getMapper(StockOutBillMapper.class).insertSelective(soBillParam);
        // 插入收货信息数据
        this.getMapper(OrderLogisticsInfoMapper.class).insertSelective(param.getOrderLogiInfo());
        // 插入商品详情数据
        for (StockOutInfo paramTemp : param.getStockOutInfos()) {
            // 设置详情中的订单号
            paramTemp.setWareId(param.getWareId());
            paramTemp.setWareName(param.getWareName());
            paramTemp.setOrderNo(param.getOrderNo());
            // 设置单商品的总价价格
            if (paramTemp.getSalesPrice() != null && paramTemp.getStockOutNum() != null) {
                paramTemp.setStockOutSum(paramTemp.getSalesPrice().multiply(new BigDecimal(paramTemp.getStockOutNum())));
            }
        }
        this.getMapper(StockOutInfoMapper.class).insertBatch(param.getStockOutInfos());
        noti.setNotifCode(200);
        noti.setNotifInfo("添加订单数据成功");
        return noti;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Notification<Object> lockedStockOfOrder(String orderNo) throws WMSException {
        Notification<Object> noti = new Notification<>();
        /*
         * 1、校验数据
         */
        // 校验参数
        if (orderNo == null || StringUtils.isBlank(orderNo)) {
            noti = NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
            noti.setNotifInfo("参数错误");
            return noti;
        }
        // 校验订单
        StockOutBill stockOutBill = this.getMapper(StockOutBillMapper.class).selectByOrderNo(orderNo);
        if (stockOutBill == null) {
            noti.setNotifCode(102);
            noti.setNotifInfo("该订单不存在");
            return noti;
        }
        /*
         * 2、锁库操作
         */
        boolean oosFlag = false; // 是否缺货
        StockOutInfo infoParam = new StockOutInfo();
        infoParam.setOrderNo(orderNo);
        List<StockOutInfo> stockOutInfos = this.getMapper(StockOutInfoMapper.class).selectByCriteria(infoParam);
        for (StockOutInfo infoTemp : stockOutInfos) {
            boolean oosFlagLock = stockOutService.lockedOneSku(infoTemp.getOrderNo(), infoTemp.getId(), infoTemp.getGoodsCode(), infoTemp.getStockOutNum(), infoTemp.getWareId());
            oosFlag = oosFlag || oosFlagLock;
        }
        // 修改订单状态
        if (oosFlag) {
            stockOutBill.setPickingStatus(Constant.PickingStatus.STOCKOUT);
        } else {
            stockOutBill.setPickingStatus(Constant.PickingStatus.WATTING);
        }
        this.getMapper(StockOutBillMapper.class).updateByPrimaryKeySelective(stockOutBill);
        noti.setNotifCode(200);
        noti.setNotifInfo("锁定成功");
        return noti;
    }

    @Override
    public Notification<Object> orderLock(OrderInfo param) {
        Notification<Object> noit = new Notification<>();
        if (param == null || param.getWareId() == null) {
            return NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
        }
        // 校验仓库
        Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(param.getWareId());
        if (warehouse == null || Constant.WarehouseStatus.DISABLE == warehouse.getStatus()) {
            return NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
        }
        // 如果该仓库在大盘中，不接收订单
        if (Constant.WarehouseStatus.CHECKING == warehouse.getStatus()) {
            noit.setNotifCode(Constant.PACKING_FLAG);
            noit.setNotifInfo("该仓库大盘中");
            return noit;
        }
        try {
            // 先进行入库
            logger.info(Constant.LINE + "准备生成出库单：" + JSON.toJSONString(param));
            noit = insertStockOutBillInfo(param);
            logger.info(Constant.LINE + "生成出库单：" + JSON.toJSONString(noit));
            // 如果添加出库单成功，异步进行锁库操作
            if (noit != null && Notifications.OK.getNotifCode() == noit.getNotifCode()) {
                lockedStockOfOrderWithLog(param.getOrderNo());
            }
        } catch (WMSException e) {
            noit.setNotifCode(-200);
            noit.setNotifInfo(StringUtils.isBlank(e.getMsg()) ? "下单锁库失败" : e.getMsg());
            logger.error(Constant.LINE + "下单锁库异常", e);
        }
        return noit;
    }

    /*
     * 锁库操作加处理机制
     * @param orderNo
     */
    private void lockedStockOfOrderWithLog(String orderNo) {
        logger.debug(Constant.LINE + "已添加订单数据，待锁库...");
        Notification<Object> noit = lockedStockOfOrder(orderNo);
        logger.info(Constant.LINE + "锁库结果：" +JSON.toJSONString(noit));
        // 如果锁库请求失败或者锁库失败，记录在消息队列表中
        if (noit == null || Notifications.OK.getNotifCode() != noit.getNotifCode()) {
            // 封装mq数据
            MessageQueue mq = new MessageQueue();
            mq.setBusinessType(Constant.MessageQueueType.RELOCK);
            mq.setBusinessId(orderNo);
            mq.setOperation(Constant.TYPE_STATUS_YES + "");
            Date now = new Date();
            mq.setCreatetime(now);
            mq.setUpdatetime(now);
            mq.setParam(orderNo);
            mq.setState(Constant.MessageQueueState.FAILURE);
            this.getMapper(MessageQueueMapper.class).insertSelective(mq);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Notification<Object> alterStockOutBill(AlterSOBillParam param) {
        logger.debug(Constant.LINE + "AlterSOBillParam : " + JSON.toJSONString(param));
        /*
         * 1、校验数据
         */
        // 参数校验(状态为 0 解冻，1 冻结， 2 取消订单)
        Notification<Object> noit = new Notification<>();
        if (param == null || StringUtils.isBlank(param.getOrderNo()) || param.getOrderType() == null || param.getStatus() == null) {
            noit = NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
            noit.setNotifInfo("参数错误");
            return noit;
        }
        // 校验出库单
        StockOutBill soBill = this.getMapper(StockOutBillMapper.class).selectByOrderNo(param.getOrderNo());
        if (soBill == null) {
            noit.setNotifCode(102);
            noit.setNotifInfo("订单不存在");
            return noit;
        }
        if (soBill.getStatus() == null || !(soBill.getStatus() == Constant.StockOutBillStatus.PICKING || soBill.getStatus() == Constant.StockOutBillStatus.PACK_RECHECK)) {
            noit.setNotifCode(103);
            noit.setNotifInfo("该订单不可以取消");
            return noit;
        }
        /*
         * 2、业务处理
         */
        // 修改出库单冻结或解冻状态
        if (Constant.AlterSOBillStatusParam.FREEZE == param.getStatus() || Constant.AlterSOBillStatusParam.UMFREEZE == param.getStatus()) {
            StockOutBill soParam = new StockOutBill();
            soParam.setId(soBill.getId());
            soParam.setFreeze(param.getStatus());
            this.getMapper(StockOutBillMapper.class).updateByPrimaryKeySelective(soParam);
        // 出库单取消
        } else if (Constant.AlterSOBillStatusParam.CANTEL == param.getStatus()) {
            // 拣货节点
            if (Constant.StockOutBillStatus.PICKING == soBill.getStatus()) {
                switch (soBill.getPickingStatus()) {
                    // 1、待拣货直接还原库存
                    case Constant.PickingStatus.WATTING :
                        try {
                            stockOutService.unlockedAllLockSku(param.getOrderNo(), soBill.getWareId());
                        } catch (WMSException e) {
                            logger.error(Constant.LINE + "待拣货取消订单解锁库存失败", e);
                            noit.setNotifCode(104);
                            noit.setNotifInfo("待拣货取消订单解锁库存失败");
                            return noit;
                        }
                        break;
                    // 2、拣货中取消订单不进行上架处理，需要等拣货完成之后再处理
                    case Constant.PickingStatus.PICKING :
                        InStockBill isParam = new InStockBill(null, param.getOrderNo(), Constant.InStockBillType.CANCEL_BILL, Constant.TYPE_STATUS_YES, null, null);
                        // 封装mq数据
                        MessageQueue mq = new MessageQueue();
                        mq.setBusinessId(param.getOrderNo());
                        mq.setBusinessType(Constant.MessageQueueType.PICKING_CANTEL);
                        mq.setOperation(Constant.MessageQueueAOSOper.ONE);
                        Date now = new Date();
                        mq.setCreatetime(now);
                        mq.setUpdatetime(now);
                        mq.setParam(JSON.toJSONString(isParam));
                        mq.setState(Constant.MessageQueueState.WATTING);
                        this.getMapper(MessageQueueMapper.class).insertSelective(mq);
                        break;
                    // 3、拣货完成直接上架处理即可
                    case Constant.PickingStatus.FINISH :
                        try {
                            InStockBill inStockBill = new InStockBill(null, param.getOrderNo(), Constant.InStockBillType.CANCEL_BILL, Constant.TYPE_STATUS_YES, null, null);
                            inStockService.generateInStockBill(inStockBill, null);
                        } catch (WMSException e) {
                            logger.error(Constant.LINE + "拣货完成取消订单生成入库单失败", e);
                            noit.setNotifCode(105);
                            noit.setNotifInfo("拣货完成取消订单生成入库单失败");
                            return noit;
                        }
                        break;
                    // 4、缺货分两部分，1> 缺货部分解锁还原库存，2> 已拣部分上架处理
                    case Constant.PickingStatus.STOCKOUT :
                        try {
                            stockOutService.unlockedAllLockSku(param.getOrderNo(), soBill.getWareId());
                            InStockBill inStockBill = new InStockBill(null, param.getOrderNo(), Constant.InStockBillType.CANCEL_BILL, Constant.TYPE_STATUS_YES, null, null);
                            inStockService.generateInStockBill(inStockBill, null);
                        } catch (WMSException e) {
                            logger.error(Constant.LINE + "缺货业务处理失败", e);
                            noit.setNotifCode(106);
                            noit.setNotifInfo("缺货业务处理失败");
                            return noit;
                        }
                        break;
                    default :
                        break;
                }
                // 拣货节点，生成入库单，出库单拣货状态改为异常
                soBill.setPickingStatus(Constant.PickingStatus.ERROR);
            // 复检节点
            } else if (Constant.StockOutBillStatus.PACK_RECHECK == soBill.getStatus() && StringUtils.isBlank(soBill.getLogisticsNo())) {
                // 复检直接上架处理
                try {
                    InStockBill inStockBill = new InStockBill(null, param.getOrderNo(), Constant.InStockBillType.CANCEL_BILL, Constant.TYPE_STATUS_YES, null, null);
                    inStockService.generateInStockBill(inStockBill, null);
                } catch (WMSException e) {
                    logger.error(Constant.LINE + "打包复检后取消订单生成入库单失败", e);
                    noit.setNotifCode(107);
                    noit.setNotifInfo("打包复检后取消订单生成入库单失败");
                    return noit;
                }
                // 复检节点，直接生成入库单和修改拣货状态为异常（复检状态异常状态使用拣货的异常）
                soBill.setPickingStatus(Constant.PickingStatus.ERROR);
//            } else if (Constant.StockOutBillStatus.ENTER_THE_AWB == soBill.getStatus() ) {
//                // 复检直接上架处理
//                try {
//                    InStockBill inStockBill = new InStockBill(null, param.getOrderNo(), Constant.InStockBillType.CANCEL_BILL, Constant.TYPE_STATUS_YES, null, null);
//                    inStockService.generateInStockBill(inStockBill, null);
//                } catch (WMSException e) {
//                    logger.error(Constant.LINE + "录入运单号节点取消订单生成入库单失败", e);
//                    noit.setNotifCode(108);
//                    noit.setNotifInfo("录入运单号节点取消订单生成入库单失败");
//                    return noit;
//                }
//                // 录入运单号节点，直接生成入库单和修改拣货状态为异常
//                soBill.setPickingStatus(Constant.PickingStatus.ERROR);
            }
            this.getMapper(StockOutBillMapper.class).updateByPrimaryKeySelective(soBill);
        }
        noit.setNotifCode(200);
        noit.setNotifInfo("操作成功");
        return noit;
    }

    @Override
    public List<Map<String, Object>> listAllByParmMap(Map<String, Object> parmaMap) {
        return this.getMapper(StockOutBillMapper.class).selectAllByParmMap(parmaMap);
    }
}
