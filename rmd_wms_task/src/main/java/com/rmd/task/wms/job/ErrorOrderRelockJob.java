package com.rmd.task.wms.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.rmd.wms.common.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 锁库失败订单重新锁库
 * @author : liu
 * Date : 2017/4/6
 */
public class ErrorOrderRelockJob implements SimpleJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TaskService taskService;

    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("错误订单重新锁库开始...");
        taskService.errorOrderRelock();
        logger.info("错误订单重新锁库结束...");
    }
}
