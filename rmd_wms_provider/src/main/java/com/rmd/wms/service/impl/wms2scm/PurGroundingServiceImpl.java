package com.rmd.wms.service.impl.wms2scm;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.rmd.bms.bean.MessageWraper;
import com.rmd.bms.mq.producer.Producer;
import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.PurchaseInInfo;
import com.rmd.wms.bean.po.GoodsGroundingNumVO;
import com.rmd.wms.bean.po.PurGroundingNumVO;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.InStockBillMapper;
import com.rmd.wms.dao.PurchaseInInfoMapper;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.wms2scm.PurGroundingService;
import com.rmd.wms.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * 采购上架相关服务实现
 *
 * @author : liu
 * @Date : 2017/4/27
 */
@Service("purGroundingService")
public class PurGroundingServiceImpl extends BaseService implements PurGroundingService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 消息队列用户的固定参数
    final static String topic;
    final static String tag;

    static {
        try {
            topic = PropertiesUtil.getStringByKey("rocketmq.wms2scmTopic", "resources.properties");
            tag = PropertiesUtil.getStringByKey("rocketmq.purGroundingTags", "resources.properties");
        } catch (Exception e) {
            throw new RuntimeException("初始化消息队列参数异常" + e.getMessage());
        }
    }

    @Resource(name = "wmsProducer")
    private Producer wmsProducer;

    @Override
    public void pushPurGrounding(String inStockNo) throws WMSException {
        // 准备数据，进行校验
        if (StringUtils.isBlank(inStockNo)) {
            return;
        }
        InStockBill inStockBill = this.getMapper(InStockBillMapper.class).selectByInStockNo(inStockNo);
        if (inStockBill == null || StringUtils.isBlank(inStockBill.getPurchaseNo())) {
            return;
        }
        // 返回采购单上架状态和上架数量
        PurGroundingNumVO vo = new PurGroundingNumVO(inStockBill.getPurchaseNo(), inStockBill.getInGoodsAmount());
        vo.setUserId(inStockBill.getOuserId());
        Date opDate = new Date();
        vo.setOperatingTime(opDate);
        //返回采购单下商品编码和商品上架数量
        List<GoodsGroundingNumVO> goodsGroundingNumVOs = new ArrayList<GoodsGroundingNumVO>();
        GoodsGroundingNumVO goodsGroundingNumVO = null;
        Map<String, Object> parmaMap = new HashMap<String, Object>();
        if (!StringUtils.isBlank(inStockBill.getPurchaseNo())) {
            parmaMap.put("inStockNo", inStockBill.getInStockNo());
            List<PurchaseInInfo> lPurchaseInInfos = this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(parmaMap);
            for (PurchaseInInfo o : lPurchaseInInfos) {
                goodsGroundingNumVO = new GoodsGroundingNumVO();
                goodsGroundingNumVO.setGoodsCode(o.getGoodsCode());
                goodsGroundingNumVO.setStorageCount(o.getInStockNum());
                goodsGroundingNumVO.setUserId(inStockBill.getOuserId());
                goodsGroundingNumVO.setOperatingTime(opDate);
                goodsGroundingNumVOs.add(goodsGroundingNumVO);
            }
        }
        vo.setGoodsGroundingNumVOs(goodsGroundingNumVOs);
        // 将数据放入消息队列中
        logger.info(Constant.LINE + "topic: " + topic + ",tag: " + tag + ",PurGroundingNumVO : " + JSON.toJSONString(vo));
        MessageWraper mw = new MessageWraper(Constant.SYSTEM_NAME, "PurGroundingService.pushPurGrounding", topic, tag, JSON.toJSONString(vo));
        try {
            boolean flag = wmsProducer.sendMessage(mw);
            logger.info(Constant.LINE + "消息发送结果 :" + flag);
        } catch (Exception e) {
            logger.error(Constant.LINE + "消息发送失败", e);
        }
    }

}
