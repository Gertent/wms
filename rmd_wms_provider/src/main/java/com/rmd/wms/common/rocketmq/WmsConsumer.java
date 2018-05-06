package com.rmd.wms.common.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.CheckInfoService;
import com.rmd.wms.service.PurchaseBillService;
import com.rmd.wms.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * wms消息消费者
 *
 * @author : liu
 * @Date : 2017/4/20
 */
public class WmsConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DefaultMQPushConsumer defaultMQPushConsumer;
    private String namesrvAddr;
    private String consumerGroup;

    @Autowired
    private CheckInfoService checkInfoService;
    @Autowired
    private PurchaseBillService purchaseBillService;

    /**
     * Spring bean init-method
     */
    public void init() throws InterruptedException, MQClientException {

        // 参数信息
        logger.info("DefaultMQPushConsumer initialize!");
        logger.info(consumerGroup);
        logger.info(namesrvAddr);

        // 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
        // 注意：ConsumerGroupName需要由应用来保证唯一
        defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setInstanceName(String.valueOf(System.currentTimeMillis()));
        // 财务消息队列
        final String fms2wmsTopic = PropertiesUtil.getStringByKey("rocketmq.fms2wmsTopic", "resources.properties");
        final String doChecksTags = PropertiesUtil.getStringByKey("rocketmq.doChecksTags", "resources.properties");
        // 采购消息队列
        final String scm2wmsTopic = PropertiesUtil.getStringByKey("rocketmq.scm2wmsTopic", "resources.properties");
        final String purchaseTags = PropertiesUtil.getStringByKey("rocketmq.purchaseTags", "resources.properties");

        // 订阅指定MyTopic下tags等于MyTag
        defaultMQPushConsumer.subscribe(fms2wmsTopic, doChecksTags);
        defaultMQPushConsumer.subscribe(scm2wmsTopic, purchaseTags);

        // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 设置为集群消费(区别于广播消费)
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);

        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {

            // 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                boolean result = false;
                for (MessageExt msg : msgs) {
                    if (msg.getTopic().equals(fms2wmsTopic) && doChecksTags.equals(msg.getTags())) {
                        try {
//                            result = checkInfoService.doChecksReturn(msg.getBody());
                        } catch (Exception e) {
                            logger.error(Constant.LINE + "接收盘点审核消息异常", e);
                            result = false;
                        }
                    } else if (msg.getTopic().equals(scm2wmsTopic) && purchaseTags.equals(msg.getTags())) {
                        try {
//                            result = purchaseBillService.receivePurchase(msg.getBody());
                        } catch (Exception e) {
                            logger.error(Constant.LINE + "接收采购单消息异常", e);
                            result = false;
                        }
                    }
                }
                // 如果没有return success ，consumer会重新消费该消息，直到return success
                if (result) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } else {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });

        // Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
        defaultMQPushConsumer.start();
        logger.info("DefaultMQPushConsumer start success!");
    }

    /**
     * Spring bean destroy-method
     */
    public void destroy() {
        defaultMQPushConsumer.shutdown();
    }

    // ----------------- setter --------------------

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

}