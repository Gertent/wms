package com.rmd.wms.service.impl.wms2fms;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.rmd.bms.bean.MessageWraper;
import com.rmd.bms.mq.producer.Producer;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.po.SalesSlipDetailVO;
import com.rmd.wms.bean.po.SalesSlipVO;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.*;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.wms2fms.SalesSlipService;
import com.rmd.wms.util.PropertiesUtil;
import com.rmd.wms.util.WmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送订单相关数据给财务系统
 *
 * @author : liu
 * @Date : 2017/4/20
 */
@Service("salesSlipService")
public class SalesSlipServiceImpl extends BaseService implements SalesSlipService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 消息队列用户的固定参数
    final static String topic;
    final static String tag;

    static {
        try {
            topic = PropertiesUtil.getStringByKey("rocketmq.wms2fmsTopic", "resources.properties");
            tag = PropertiesUtil.getStringByKey("rocketmq.fmsSalesSlipTags", "resources.properties");
        } catch (Exception e) {
            throw new RuntimeException("初始化消息队列参数异常" + e.getMessage());
        }
    }

    @Resource(name = "wmsProducer")
    private Producer wmsProducer;

    @Override
    public void pushSalesSlipIn(String billNo) throws WMSException {
        logger.info(Constant.LINE + "billNo:" + billNo );
        if (StringUtils.isBlank(billNo)) {
            logger.info(Constant.LINE + "单号不能为空");
            return;
        }
        // 查询出所有服务单对应的入库单
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("serverNo", billNo);
        List<InStockBill> inStockBills = this.getMapper(InStockBillMapper.class).selectAllByWhere(mapParam);
        logger.info(Constant.LINE + "inStockBills:" + JSON.toJSONString(inStockBills) );
        if (inStockBills == null || inStockBills.isEmpty()) {
            logger.info(Constant.LINE + "没有服务单对应的入库单");
            return;
        }
        // 封装商品的数据
        SalesSlipVO svo = null;
        int goodsAmount = 0;
        Map<String, SalesSlipDetailVO> mapTemp = new HashMap<>();
        // 封装每个入库单的数据
        for (InStockBill inTemp : inStockBills) {
            // 插入所有的商品详情
            Map<String, Object> infoMapParam = new HashMap<>();
            infoMapParam.put("inStockNo", inTemp.getInStockNo());
            infoMapParam.put("wareId", inTemp.getWareId());
            List<PurchaseInInfo> infos = this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(infoMapParam);
            logger.info(Constant.LINE + "List<PurchaseInInfo> :" + JSON.toJSONString(infos));
            if (infos == null || infos.size() < 1) {
                continue;
            }
            // 设置服务单信息
            if (svo == null) {
                svo = new SalesSlipVO();
                // 复制到svo的inStockTime，wareId，wareName，
                WmsUtil.copyPropertiesIgnoreNull(inTemp, svo);
                svo.setOrderNo(billNo);// 给fms的订单号应是服务单号
            }
            // 封装商品详情的数据
            for (PurchaseInInfo infoTemp : infos) {
                int oneInStockNum = infoTemp.getInStockNum() == null ? 0 : infoTemp.getInStockNum();
                // 设置服务单的总数量
                goodsAmount += oneInStockNum;
                SalesSlipDetailVO dvo = new SalesSlipDetailVO();
                // 复制到dvo的goodsCode,goodsBarCode,goodsName,inStockTime,wareId,wareName
                WmsUtil.copyPropertiesIgnoreNull(infoTemp, dvo);
                dvo.setOrderNo(svo.getOrderNo());
                dvo.setInStockTime(svo.getInStockTime());
                dvo.setStockOutNum(oneInStockNum);
                // 合并详情数据
                if (mapTemp.containsKey(dvo.getGoodsCode())) {
                    SalesSlipDetailVO detailVO = mapTemp.get(dvo.getGoodsCode());
                    detailVO.setStockOutNum(detailVO.getStockOutNum() + oneInStockNum);
                } else {
                    mapTemp.put(dvo.getGoodsCode(), dvo);
                }
            }
        }
        if (svo != null) {
            svo.setSalesSlipDetailList(new ArrayList<>(mapTemp.values()));
            svo.setGoodsAmount(goodsAmount);
        }
        // 将数据放入消息队列中
        logger.info(Constant.LINE + "topic: "+ topic + ",tag: " + tag + ",SalesSlipVO : " + JSON.toJSONString(svo));
        MessageWraper mw = new MessageWraper(Constant.SYSTEM_NAME, "SalesSlipService.pushSalesSlipIn", topic, tag, JSON.toJSONString(svo));
        try {
            boolean flag = wmsProducer.sendMessage(mw);
            logger.info(Constant.LINE + "消息发送结果 :" + flag);
        } catch (Exception e) {
            logger.error(Constant.LINE + "消息发送失败", e);
        }
    }

    @Override
    public void pushSalesSlipOut(String billNo) throws WMSException {
        if (StringUtils.isBlank(billNo)) {
            return;
        }
        DeliveryBill deliveryBill = this.getMapper(DeliveryBillMapper.class).selectByDeliveryNo(billNo);
        if (deliveryBill == null || deliveryBill.getDeliveryEndTime() == null) {
            return;
        }
        // 查询出所有服务单对应的入库单
        Map<String, Object> soMapParam = new HashMap<>();
        soMapParam.put("status", Constant.StockOutBillStatus.SHIPPING);
        soMapParam.put("deliveryNo", billNo);
        List<StockOutBill> stockOutBills = this.getMapper(StockOutBillMapper.class).selectAllByWhere(soMapParam);
        if (stockOutBills == null || stockOutBills.size() < 1) {
            return;
        }
        // 封装每个入库单的数据
        for (StockOutBill soTemp : stockOutBills) {
            // 插叙所有的商品详情
            Map<String, Object> infoMapParam = new HashMap<>();
            infoMapParam.put("orderNo", soTemp.getOrderNo());
            infoMapParam.put("wareId", soTemp.getWareId());
            List<StockOutInfo> infos = this.getMapper(StockOutInfoMapper.class).selectAllByWhere(infoMapParam);
            if (infos == null || infos.size() < 1) {
                continue;
            }
            // 封装商品的数据
            SalesSlipVO svo = new SalesSlipVO();
            // 复制到svo的orderNo，deliveryNo，wareId，wareName，logisComName，logisticsNo，goodsAmount
            WmsUtil.copyPropertiesIgnoreNull(soTemp, svo);
            svo.setOrderDate(soTemp.getOrderdate());
            svo.setDeliveryEndTime(deliveryBill.getDeliveryEndTime());
            List<SalesSlipDetailVO> dvoList = new ArrayList<>();
            // 封装商品详情的数据
            for (StockOutInfo infoTemp : infos) {
                SalesSlipDetailVO dvo = new SalesSlipDetailVO();
                // 复制到dvo的orderNo，goodsCode，goodsBarCode，goodsName，wareId，wareName，stockOutNum
                WmsUtil.copyPropertiesIgnoreNull(infoTemp, dvo);
                dvo.setDeliveryNo(soTemp.getDeliveryNo());
                dvo.setOrderDate(soTemp.getOrderdate());
                dvo.setDeliveryEndTime(deliveryBill.getDeliveryEndTime());
                dvoList.add(dvo);
            }
            svo.setSalesSlipDetailList(dvoList);
            // 将数据放入消息队列中
            logger.info(Constant.LINE + "topic: "+ topic + ",tag: " + tag + ",SalesSlipVO : " + JSON.toJSONString(svo));
            MessageWraper mw = new MessageWraper(Constant.SYSTEM_NAME, "SalesSlipService.pushSalesSlipOut", topic, tag, JSON.toJSONString(svo));
            try {
                boolean flag = wmsProducer.sendMessage(mw);
                logger.info(Constant.LINE + "消息发送结果 :" + flag);
            } catch (Exception e) {
                logger.error(Constant.LINE + "消息发送失败", e);
            }
        }
    }

}
