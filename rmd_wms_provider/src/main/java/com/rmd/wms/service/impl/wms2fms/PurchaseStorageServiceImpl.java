package com.rmd.wms.service.impl.wms2fms;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.rmd.bms.bean.MessageWraper;
import com.rmd.bms.mq.producer.Producer;
import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.PurchaseBill;
import com.rmd.wms.bean.PurchaseInInfo;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.po.PurchaseStorageBillVO;
import com.rmd.wms.bean.po.PurchaseStorageDetailBillVO;
import com.rmd.wms.bean.po.PurchaseStorageEnum;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.InStockBillMapper;
import com.rmd.wms.dao.PurchaseBillMapper;
import com.rmd.wms.dao.PurchaseInInfoMapper;
import com.rmd.wms.dao.WarehouseMapper;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.wms2fms.PurchaseStorageService;
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
 * 采购单入库数据推送给财务系统
 * @author : liu
 * @Date : 2017/4/20
 */
@Service("purchaseStorageService")
public class PurchaseStorageServiceImpl extends BaseService implements PurchaseStorageService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 消息队列用户的固定参数
    final static String topic;
    final static String tag;

    static {
        try {
            topic = PropertiesUtil.getStringByKey("rocketmq.wms2fmsTopic", "resources.properties");
            tag = PropertiesUtil.getStringByKey("rocketmq.fmsPurchaseStorageTags", "resources.properties");
        } catch (Exception e) {
            throw new RuntimeException("初始化消息队列参数异常" + e.getMessage());
        }
    }

    @Resource(name = "wmsProducer")
    private Producer wmsProducer;

    @Override
    public void pushPurchaseInStock(String billNo) throws WMSException {
        if (StringUtils.isBlank(billNo)) {
            return;
        }
        InStockBill inStockBill = this.getMapper(InStockBillMapper.class).selectByInStockNo(billNo);
        if (inStockBill == null || Constant.InStockBillType.PURCHASE_BILL != inStockBill.getType()) {
            return;
        }
        Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(inStockBill.getWareId());
        if (warehouse == null) {
            return;
        }
        PurchaseBill purchaseBill = this.getMapper(PurchaseBillMapper.class).selectByPurchaseNo(inStockBill.getPurchaseNo());
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("inStockNo", billNo);
        mapParam.put("wareId", inStockBill.getWareId());
        List<PurchaseInInfo> infos = this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(mapParam);
        if (infos == null || infos.size() < 1) {
            return;
        }
        PurchaseStorageBillVO pvo = new PurchaseStorageBillVO();
        // 复制到pvo的wareName，supplierName
        WmsUtil.copyPropertiesIgnoreNull(inStockBill, pvo);
        pvo.setWareCode(warehouse.getCode());
        pvo.setDate(inStockBill.getInStockTime());
        pvo.setOddNumbers(inStockBill.getInStockNo());
        pvo.setPurchaseOrderNumber(inStockBill.getPurchaseNo());
        pvo.setCategory(PurchaseStorageEnum.采购入库.name());
        pvo.setNumber(inStockBill.getInGoodsAmount());
        pvo.setTaxAmount(inStockBill.getInGoodsSum());
        pvo.setSalesman(inStockBill.getOuserName());
        pvo.setSupplierNumber(purchaseBill.getSupplierCode());
        pvo.setDepartment(purchaseBill.getDepartment());
        pvo.setFinanceCode(purchaseBill.getFinanceCode());
        List<PurchaseStorageDetailBillVO> dvoList = new ArrayList<>();
        for (PurchaseInInfo infoTemp : infos) {
            PurchaseStorageDetailBillVO dvo = new PurchaseStorageDetailBillVO();
            // 复制到dvo的goodsCode，goodsName，unit，
            WmsUtil.copyPropertiesIgnoreNull(infoTemp, dvo);
            dvo.setGoodsBarcode(infoTemp.getGoodsBarCode());
            dvo.setSpecifications(infoTemp.getSpec());
            dvo.setNumber(infoTemp.getInStockNum() + "");
            dvo.setUnitPrice(infoTemp.getPurchasePrice());
            dvo.setTaxAmount(infoTemp.getInStockSum());
            dvoList.add(dvo);
        }
        pvo.setPurchaseStorageDetailBillVOs(dvoList);
        // 将数据放入消息队列中
        logger.info(Constant.LINE + "topic: "+ topic + ",tag: " + tag + ",PurchaseStorageBillVO : " + JSON.toJSONString(pvo));
        MessageWraper mw = new MessageWraper(Constant.SYSTEM_NAME, "PurchaseStorageService.pushPurchaseInStock", topic, tag, JSON.toJSONString(pvo));
        try {
            boolean flag = wmsProducer.sendMessage(mw);
            logger.info(Constant.LINE + "消息发送结果 :" + flag);
        } catch (Exception e) {
            logger.error(Constant.LINE + "消息发送失败", e);
        }

    }

}
