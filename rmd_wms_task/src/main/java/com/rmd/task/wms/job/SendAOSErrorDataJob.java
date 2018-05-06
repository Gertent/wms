package com.rmd.task.wms.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.rmd.task.util.MailUtil;
import com.rmd.wms.bean.MessageQueue;
import com.rmd.wms.common.service.TaskService;
import com.rmd.wms.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 发送修改订单状态错误数据
 * @author zuoguodong
 */
public class SendAOSErrorDataJob implements DataflowJob<MessageQueue>{

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TaskService taskService;

	public List<MessageQueue> fetchData(ShardingContext shardingContext) {
		log.debug("获取修改订单状态错误数据");
		List<MessageQueue> list = taskService.getAOSErrorData();
		log.debug("获取修改订单状态错误数据：" + list.size() + "条");
		return list;
	}

	public void processData(ShardingContext shardingContext, List<MessageQueue> data) {
		log.info("开始修改订单状态错误发送出邮件");
		if (data == null || data.isEmpty()) {
			return;
		}
		for(MessageQueue message : data){
			message.setState(Constant.MessageQueueState.WAIT_CHECK);
			try {
				taskService.updateMQByPrimaryKeySelective(message);
				log.info("==============修改订单：" + message.getBusinessId() + "的处理状态成功！");
			} catch (Exception e) {
				log.error("==============修改订单：" + message.getBusinessId() + "的处理状态失败！", e);
			}
			MailUtil.sendMail("作业平台数据出错邮件", "改订单状态错误,订单号：" + message.getBusinessId());
		}
	}

}
