package com.rmd.task.example;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class TestSimpleJob implements SimpleJob {
    
	Logger log = LoggerFactory.getLogger(this.getClass());
	
    public void execute(final ShardingContext shardingContext) {
        log.info(String.format("------Thread ID: %s, Date: %s, Sharding Context: %s, Action: %s", Thread.currentThread().getId(), new Date(), shardingContext, "simple job"));
//        try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			
//		}
    }


}
