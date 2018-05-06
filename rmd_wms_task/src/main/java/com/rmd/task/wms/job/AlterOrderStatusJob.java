package com.rmd.task.wms.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.rmd.wms.common.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 重新修改订单状态任务
 * @author zuoguodong
 */
public class AlterOrderStatusJob implements SimpleJob {
    
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TaskService taskService;
	
	public void execute(ShardingContext shardingContext) {
		log.info("执行重新修改订单状态任务");
		taskService.alterOrderStatus();
	}

}
