package com.rmd.task.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

public class TestDataFlowJob implements DataflowJob<String>{

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	public List<String> fetchData(ShardingContext arg0) {
		int i = arg0.getShardingItem();
		log.info("数据抓取区域：" + i);
		List<String> list = new ArrayList<String>();
		list.add("区域：" + i + "123");
		list.add("区域：" + i + "456");
		return list;
	}

	public void processData(ShardingContext arg0, List<String> list) {
		for(String str : list){
			log.info("数据处理:" + str);
		}
		
	}

}
