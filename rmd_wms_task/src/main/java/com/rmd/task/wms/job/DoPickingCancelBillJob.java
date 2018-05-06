package com.rmd.task.wms.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.rmd.wms.common.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 拣货中取消订单任务处理
 * @author : liu
 * @Date : 2017/7/4
 */
public class DoPickingCancelBillJob implements SimpleJob {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TaskService taskService;

    public void execute(ShardingContext shardingContext) {
        log.info("处理拣货中取消的订单任务");
        taskService.doPickingCancelBill();
    }
}
