package com.rmd.wms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.Notifications;
import com.rmd.oms.constant.OrderStatus;
import com.rmd.oms.constant.OrderType;
import com.rmd.oms.entity.OrderReceiveAddress;
import com.rmd.oms.entity.vo.OperateUserVo;
import com.rmd.oms.entity.vo.OrderPackageInfoVo;
import com.rmd.oms.service.OrderBaseService;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.InStockInfo;
import com.rmd.wms.bean.vo.OrderInfo;
import com.rmd.wms.bean.vo.ServerInStockParam;
import com.rmd.wms.bean.vo.app.InStockBillInfo;
import com.rmd.wms.bean.vo.app.PurchaseInInfoAndUnBinds;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.InStockService;
import com.rmd.wms.common.vo.UpdateOrderStatusParam;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.*;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.InStockBillService;
import com.rmd.wms.service.MovementService;
import com.rmd.wms.service.StockOutBillService;
import com.rmd.wms.service.wms2fms.SalesSlipService;
import com.rmd.wms.service.wms2scm.PurGroundingService;
import com.rmd.wms.util.AsyncExcutor;
import com.rmd.wms.util.WmsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author ZXLEI
 * @ClassName: InStockBillServiceImpl
 * @Description: TODO(入库表接口实现)
 * @date Feb 22, 2017 11:40:43 AM
 */
@Service("inStockBillService")
public class InStockBillServiceImpl extends BaseService implements InStockBillService {

    private Logger logger = Logger.getLogger(InStockBillServiceImpl.class);

    @Autowired
    private InStockService inStockService;
    @Autowired
    private MovementService movementService;
    @Autowired
    private OrderBaseService orderBaseService;
    @Autowired
    private StockOutBillService stockOutBillService;
    @Autowired
    private SalesSlipService salesSlipService;
    @Autowired
    private PurGroundingService purGroundingService;

    @Override
    public List<InStockBill> ListInStockBills(Map<String, Object> parmaMap) {
        return this.getMapper(InStockBillMapper.class).selectAllByWhere(parmaMap);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {

        return this.getMapper(InStockBillMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int insert(InStockBill record) {
        return this.getMapper(InStockBillMapper.class).insert(record);
    }

    @Override
    public int insertSelective(InStockBill record) {

        return this.getMapper(InStockBillMapper.class).insertSelective(record);
    }

    @Override
    public InStockBill selectByPrimaryKey(Integer id) {

        return this.getMapper(InStockBillMapper.class).selectByPrimaryKey(id);
    }

    @Override
    public InStockBill selectByInStockNo(String inStockNo) {
        return this.getMapper(InStockBillMapper.class).selectByInStockNo(inStockNo);
    }

    @Override
    public int updateByPrimaryKeySelective(InStockBill record) {
        return this.getMapper(InStockBillMapper.class).updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(InStockBill record) {
        return this.getMapper(InStockBillMapper.class).updateByPrimaryKey(record);
    }

    @Override
    public InStockBillInfo getInBillTask(GroundingBill groundingBill) {
        if (groundingBill == null || StringUtils.isBlank(groundingBill.getInStockNo())) return null;
        // 上架单绑定用户信息
        groundingBill.setStartTime(new Date());
        groundingBill.setStatus(Constant.GroundingBillStatus.PART);
        this.getMapper(GroundingBillMapper.class).updateByPrimaryKeySelective(groundingBill);
        // 获取上架单详情
        Map<String, Object> param = new HashMap<>();
        param.put("inStockNo", groundingBill.getInStockNo());
        List<PurchaseInInfo> purinfos = this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(param);
        // 获取不可以上架的货位信息
        if (purinfos == null) return null;
        List<PurchaseInInfoAndUnBinds> infos = new ArrayList<>();
        for (PurchaseInInfo temp : purinfos) {
            // 封装上架商品详情数据
            PurchaseInInfoAndUnBinds piInfoAndUnBinds = new PurchaseInInfoAndUnBinds();
            WmsUtil.copyPropertiesIgnoreNull(temp, piInfoAndUnBinds);
            // 封装商品上架不可以上架的货位信息
            LocationGoodsBind bind = new LocationGoodsBind();
            bind.setGoodsCode(temp.getGoodsCode());
            bind.setWareId(temp.getWareId());
            bind.setValidityTime(temp.getValidityTime());
            List<LocationGoodsBind> unBinds = this.getMapper(LocationGoodsBindMapper.class).selectUnBindLocations(bind);
            piInfoAndUnBinds.setUnBinds(unBinds);
            infos.add(piInfoAndUnBinds);
        }
        InStockBillInfo inInfo = new InStockBillInfo();
        WmsUtil.copyPropertiesIgnoreNull(groundingBill, inInfo);
        inInfo.setInfos(infos);
        return inInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus putaway(InStockBillInfo param) {
        ServerStatus json = new ServerStatus();
        /*
         * 1、校验数据
         */
        // 校验参数
        if (param == null || StringUtils.isBlank(param.getInStockNo()) || param.getBindIns() == null || param.getWareId() == null) {
            json.setStatus("101");
            json.setMessage("参数错误");
            return json;
        }
        final Integer wareId = param.getWareId();
        final String inStockNo = param.getInStockNo();
        GroundingBill groundingBill = this.getMapper(GroundingBillMapper.class).selectByInStockNo(inStockNo);
        // 校验入库上架单是否本仓库
        if (groundingBill == null || !param.getWareId().equals(groundingBill.getWareId())) {
            json.setStatus("102");
            json.setMessage("上架单不存在");
            return json;
        }
        // 校验上架单是否已完成
        if (Constant.GroundingBillStatus.FINISH == groundingBill.getStatus()) {
            json.setStatus("103");
            json.setMessage("上架已完成，不可重复上架");
            return json;
        }
        // 校验入库单数据
        List<LocationGoodsBindIn> paramBindIns = param.getBindIns();
        Map<String, Object> map = new HashMap<>();
        map.put("inStockNo", inStockNo);
        // 上架单商品详情
        List<PurchaseInInfo> inInfos = this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(map);

        for (int i = 0; i < inInfos.size(); i++) {
            int numTemp = 0;
            for (int j = 0; j < paramBindIns.size(); j++) {
                // 是同一商品把货位上的数量相加
                if (inInfos.get(i).getGoodsCode().equals(paramBindIns.get(j).getGoodsCode())) {
                    numTemp += paramBindIns.get(j).getGroundingNum();
                }
                // 校验货位信息是否被禁用
                if (i == 0) {
                    Location location = new Location(paramBindIns.get(j).getLocationNo(), wareId);
                    location = this.getMapper(LocationMapper.class).selectByLocaNoAndWareId(location);
                    if (location == null || Constant.TYPE_STATUS_NO == location.getStatus()) {
                        json.setStatus("106");
                        json.setMessage("货位号：" + paramBindIns.get(j).getLocationNo() + "异常");
                        return json;
                    }
                }
            }
            // 所有货位上的总数量等于入库数量置空，进行下一个商品比较，不相等，则返回错误
            if (inInfos.get(i).getInStockNum() == numTemp) {
                numTemp = 0;
            } else {
                json.setStatus("104");
                json.setMessage("上架数量错误");
                return json;
            }
        }
        /*
         * 2、上架：添加上架记录，修改或添加货位库存及仓库库存数据
         */
        // 先上架，然后添加上架记录
        for (LocationGoodsBindIn bindIn : paramBindIns) {
            // 封装上架记录信息,然后批量插入
            for (PurchaseInInfo info : inInfos) {
                if (bindIn.getGoodsCode().equals(info.getGoodsCode())) {
                    bindIn.setGoodsBarCode(info.getGoodsBarCode());
                    bindIn.setValidityTime(info.getValidityTime());
                }
            }
            bindIn.setGroundingNo(inStockNo);// 上架单号改为存入库单号
            // 上架修改库存信息
            try {
                inStockService.putawayAlterStock(bindIn, param.getWareId());
            } catch (WMSException e) {
                throw new WMSException("105", e.getMsg(), "上架修改库存失败", e);
            }
        }
        // 批量插入上架记录
        this.getMapper(LocationGoodsBindInMapper.class).insertBatch(paramBindIns);

        /*
         * 3、修改上架单的状态
         */
        // 上架完，修改上架单的状态和其他内容
        groundingBill.setStatus(Constant.GroundingBillStatus.FINISH);
        groundingBill.setEndTime(new Date());
        this.getMapper(GroundingBillMapper.class).updateByPrimaryKeySelective(groundingBill);
        // 推送到采购系统上架数量
        try {
            logger.info(Constant.LINE + "推送到采购系统上架数量...");
            purGroundingService.pushPurGrounding(inStockNo);
        } catch (WMSException e) {
            logger.error(Constant.LINE + "推送到采购系统异常：", e);
        }
        /*
         * 4、异步重新锁库
         */
        AsyncExcutor.excute(new Runnable() {
            @Override
            public void run() {
                // 异步重新锁库
                try {
                    Thread.sleep(5000);// 5s后执行重新锁库
                    movementService.relockedStock(wareId);
                } catch (WMSException e) {
                    logger.error(Constant.LINE + "重新锁库异常：", e);
                } catch (InterruptedException e) {
                    logger.error(Constant.LINE + "重新锁库异常：", e);
                }
            }
        });
        json.setStatus("200");
        json.setMessage("操作成功");
        return json;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus serverInStock(ServerInStockParam param) throws WMSException {
        ServerStatus json = new ServerStatus();
        /*
         * 1、校验参数
         */
        if (param == null || StringUtils.isBlank(param.getServerNo()) || param.getAgree() == null || (Constant.TYPE_STATUS_YES == param.getAgree() && (param.getWareId() == null || param.getUserId() == null || param.getParams().size() < 1))) {
            json.setStatus("101");
            json.setMessage("参数错误");
            return json;
        }
        final String serverNo = param.getServerNo();
        // 封装修改订单条件
        Integer otv = Integer.valueOf(serverNo.substring(0, 1));
        OrderType orderType = OrderType.getOrderType(otv);
        if (!(OrderType.BACK_ORDER.getValue() == orderType.getValue() || OrderType.CHANGE_ORDER.getValue() == orderType.getValue())) {
            json.setStatus("102");
            json.setMessage("服务单类型错误");
            return json;
        }
        OperateUserVo operUser = new OperateUserVo();
        operUser.setUserId(param.getUserId());
        operUser.setUserName(param.getUserName());
        operUser.setRemark(param.getRemark());
        // 封装mq数据
        MessageQueue mq = new MessageQueue();
        mq.setBusinessType(Constant.MessageQueueType.ALTER_ORDER_STATUS);
        mq.setBusinessId(serverNo);
        Date now = new Date();
        mq.setCreatetime(now);
        mq.setUpdatetime(now);
        OrderStatus orderStatus = null;
        /*
         * 2、判断是同意或拒绝，同意进行入库操作再通知oms，拒绝直接通知oms
         */
        if (Constant.TYPE_STATUS_YES == param.getAgree()) {
            // 如果是补货单需要封装出库的数据
            OrderInfo orderInfo = new OrderInfo();
            List<StockOutInfo> stockOutInfos = new ArrayList<>();
            // 处理入库操作
            for (InStockInfo paramTemp : param.getParams()) {
                paramTemp.setServerNo(serverNo);
                paramTemp.setType(Constant.InStockBillType.SERVER_BILL);
                paramTemp.setWareId(param.getWareId());
                paramTemp.setWareName(param.getWareName());
                paramTemp.setOuserId(param.getUserId());
                paramTemp.setOuserName(param.getUserName());
                // 判断订单类型是退货单或则进行入库操作()
                try {
                    inStockService.generateInStockBill(paramTemp, paramTemp.getInInfos());
                    logger.info(Constant.LINE + "生成入库单完成");
                } catch (WMSException e) {
                    logger.error(Constant.LINE + "生成入库单异常", e);
                    throw e;
                }
                // 如果是换货单，封装出库部分的数据
                if (OrderType.CHANGE_ORDER.getValue() == orderType.getValue()) {
                    // 合并订单的总计和总数量
                    orderInfo.setGoodsAmount(orderInfo.getGoodsAmount() != null ? orderInfo.getGoodsAmount() + paramTemp.getInGoodsAmount() : paramTemp.getInGoodsAmount());
                    orderInfo.setGoodsSum(orderInfo.getGoodsSum() != null ? orderInfo.getGoodsSum().add(paramTemp.getInGoodsSum()) : paramTemp.getInGoodsSum());
                    for (PurchaseInInfo inParamTemp : paramTemp.getInInfos()) {
                        // 如果已经添加过数据，再次循环的时候更新数量即可
                        if (stockOutInfos.isEmpty()) {
                            StockOutInfo outInfo = new StockOutInfo();
                            WmsUtil.copyPropertiesIgnoreNull(inParamTemp, outInfo);
                            outInfo.setOrderNo(serverNo);
                            // 这里的价格暂时使用采购价格，需要讨论修改
                            outInfo.setSalesPrice(inParamTemp.getPurchasePrice());
                            outInfo.setStockOutNum(inParamTemp.getInStockNum());
                            outInfo.setStockOutSum(inParamTemp.getInStockSum());
                            stockOutInfos.add(outInfo);
                        } else {
                            // 进行合并数量和金额
                            for (StockOutInfo outInfoTemp : stockOutInfos) {
                                if (outInfoTemp.getGoodsCode().equals(inParamTemp.getGoodsCode())) {
                                    outInfoTemp.setStockOutNum(outInfoTemp.getStockOutNum() + inParamTemp.getInStockNum());
                                    BigDecimal sumTemp = inParamTemp.getInStockSum() == null ? BigDecimal.ZERO : inParamTemp.getInStockSum();
                                    outInfoTemp.setStockOutSum(outInfoTemp.getStockOutSum() == null ? BigDecimal.ZERO : outInfoTemp.getStockOutSum().add(sumTemp));
                                }
                            }
                        }
                    }
                }
            }
            // 如果是换货单，同时进行锁库操作
            if (OrderType.CHANGE_ORDER.getValue() == orderType.getValue()) {
                // 设置出库单单号，这里的订单号为补货单号
                orderInfo.setOrderNo(serverNo);
                orderInfo.setOrderType(otv);
                orderInfo.setOrderdate(param.getOrderdate());
                orderInfo.setOrderLogiInfo(param.getOrderLogiInfo());
                orderInfo.setWareId(param.getWareId());
                orderInfo.setWareName(param.getWareName());
                orderInfo.setStockOutInfos(stockOutInfos);
                try {
                    logger.debug(Constant.LINE + "订单锁库开始：" + JSON.toJSONString(orderInfo));
                    Notification<Object> noti = stockOutBillService.orderLock(orderInfo);
                    logger.debug(Constant.LINE + "订单锁库结束：" + JSON.toJSONString(noti));
                    if (noti == null || Notifications.OK.getNotifCode() != noti.getNotifCode()) {
                        logger.info(Constant.LINE + "订单锁库错误结果" + JSON.toJSONString(noti));
                        throw new WMSException("103", "订单锁库失败");
                    }
                } catch (WMSException e) {
                    logger.error(Constant.LINE + "换货单出库锁库异常", e);
                    e.setCode("103");
                    throw e;
                }
            }

            /*
             * 处理修改oms状态
             */
            if (OrderType.BACK_ORDER.getValue() == orderType.getValue()) {
                // 退货单同意为203
                orderStatus = OrderStatus.BACK_WAREHOUSE_CONFIRM;
            } else if (OrderType.CHANGE_ORDER.getValue() == orderType.getValue()) {
                // 换货单同意为103
                orderStatus = OrderStatus.ORDER_CONFIRM;
            }
            UpdateOrderStatusParam uosParam = new UpdateOrderStatusParam(orderType, serverNo, operUser, orderStatus);
            mq.setParam(JSON.toJSONString(uosParam));
            mq.setOperation(Constant.MessageQueueAOSOper.ONE);
            /*
             * 同意入库时，需要把服务单推送到财库系统
             */
            logger.info(Constant.LINE + "服务单入库推送到财务系统:" + serverNo);
            salesSlipService.pushSalesSlipIn(serverNo);
            logger.info(Constant.LINE + "服务单入库推送到财务系统结束");
        } else if (Constant.TYPE_STATUS_NO == param.getAgree()) {
            // 封装mq数据
            if (OrderType.BACK_ORDER.getValue() == orderType.getValue()) {
                // 退货单拒绝为205
                orderStatus = OrderStatus.BACK_WAREHOUSE_REJECT;
            } else if (OrderType.CHANGE_ORDER.getValue() == orderType.getValue()) {
                // 换货单拒绝为304
                orderStatus = OrderStatus.CHANGE_PUT_IN_STORAGE_REFUSED;
            }
            UpdateOrderStatusParam uosParam = new UpdateOrderStatusParam(orderType, serverNo, operUser, orderStatus);
            mq.setParam(JSON.toJSONString(uosParam));
            mq.setOperation(Constant.MessageQueueAOSOper.TWO);
        } else {
            json.setStatus("101");
            json.setMessage("参数错误");
            return json;
        }
        // 添加消息中间表数据
        try {
            Notification<Boolean> noti = orderBaseService.updateOrderStatus(orderType, serverNo, operUser, orderStatus);
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
        json.setStatus("200");
        json.setMessage("操作成功");
        return json;
    }

    /**
     * 售后入库，批量添加入库单，入库单详情，上架单
     *
     * @param serverListMap
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public int insertBatch(Map<String, Object> serverListMap) {
        Integer flag = 0;
        try {
            //入库表
            List<InStockBill> inStockBillList = (List<InStockBill>) serverListMap.get("inStockBillList");
            //入库商品详情表
            List<PurchaseInInfo> puList = (List<PurchaseInInfo>) serverListMap.get("puList");
            //上架单基础表
            List<GroundingBill> groundingBillList = (List<GroundingBill>) serverListMap.get("groundingBillList");
            this.getMapper(InStockBillMapper.class).insertBatch(inStockBillList);
            this.getMapper(GroundingBillMapper.class).insertBatch(groundingBillList);
            this.getMapper(PurchaseInInfoMapper.class).insertBatch(puList);
            flag = 1;
        } catch (Exception e) {
            flag = 0;
            e.printStackTrace();
            throw e;
        }

        return flag;
    }

    @Override
    public PageBean<InStockBill> ListInStockBills(Integer page, Integer rows,
                                                  Map<String, Object> parmaMap) {
        PageHelper.startPage(page, rows);
        return new PageBean<InStockBill>(this.getMapper(InStockBillMapper.class).selectAllByWhere(parmaMap));
    }
    @Override
    public List<Map<String,Object>> selectAllByParmMap(Map<String,Object> parmaMap){
        return this.getMapper(InStockBillMapper.class).selectAllByParmMap(parmaMap);
    }


}
