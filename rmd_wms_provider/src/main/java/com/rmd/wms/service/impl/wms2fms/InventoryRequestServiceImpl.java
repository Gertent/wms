package com.rmd.wms.service.impl.wms2fms;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.rmd.bms.bean.MessageWraper;
import com.rmd.bms.mq.producer.Producer;
import com.rmd.wms.bean.po.InventoryRequestMqVo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.wms2fms.InventoryRequestService;
import com.rmd.wms.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 提报对接接口实现
 *
 * @author : liu
 * @Date : 2017/4/26
 */
@Service("inventoryRequestService")
public class InventoryRequestServiceImpl implements InventoryRequestService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 消息队列用户的固定参数
    final static String topic;
    final static String tag;

    static {
        try {
            topic = PropertiesUtil.getStringByKey("rocketmq.wms2fmsTopic", "resources.properties");
            tag = PropertiesUtil.getStringByKey("rocketmq.fmsInventoryRequestTags", "resources.properties");
        } catch (Exception e) {
            throw new RuntimeException("初始化消息队列参数异常" + e.getMessage());
        }
    }

    @Resource(name = "wmsProducer")
    private Producer wmsProducer;

    @Override
    public boolean pushInventoryReport(List<InventoryRequestMqVo> mqVoList) throws WMSException {
        boolean flag = false;
        if (mqVoList == null || mqVoList.isEmpty()) {
            return flag;
        }
        // 将数据放入消息队列中
        logger.info(Constant.LINE + "topic: "+ topic + ",tag: " + tag + ",InventoryRequestMqVo : " + JSON.toJSONString(mqVoList));
        MessageWraper mw = new MessageWraper(Constant.SYSTEM_NAME, "InventoryRequestService.pushInventoryReport", topic, tag, JSON.toJSONString(mqVoList));
        mw.setConsumerfailTryTimes(1);
        try {
            flag = wmsProducer.sendMessage(mw);
            logger.info(Constant.LINE + "消息发送结果 :" + flag);
        } catch (Exception e) {
            logger.error(Constant.LINE + "消息发送失败", e);
        }
        return flag;
    }
}
