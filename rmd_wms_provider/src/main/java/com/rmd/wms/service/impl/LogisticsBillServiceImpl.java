package com.rmd.wms.service.impl;

import org.springframework.stereotype.Service;

import com.rmd.wms.bean.LogisticsBill;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.LogisticsBillMapper;
import com.rmd.wms.service.LogisticsBillService;

/**
 * 
* @ClassName: LogisticsBillServiceImpl 
* @Description: TODO(订单物流信息) 
* @author ZXLEI
* @date Feb 22, 2017 7:16:25 PM 
*
 */
@Service("logisticsBillService")
public class LogisticsBillServiceImpl extends BaseService implements LogisticsBillService {

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return this.getMapper(LogisticsBillMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(LogisticsBill record) {
		return this.getMapper(LogisticsBillMapper.class).insert(record);
	}

	@Override
	public int insertSelective(LogisticsBill record) {
		
		return this.getMapper(LogisticsBillMapper.class).insertSelective(record);
	}

	@Override
	public LogisticsBill selectByPrimaryKey(Integer id) {
		
		return this.getMapper(LogisticsBillMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public LogisticsBill selectByOrderNo(String orderNo) {
	
		return this.getMapper(LogisticsBillMapper.class).selectByOrderNo(orderNo);
	}

	@Override
	public int updateByPrimaryKeySelective(LogisticsBill record) {
		
		return this.getMapper(LogisticsBillMapper.class).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(LogisticsBill record) {
		
		return this.getMapper(LogisticsBillMapper.class).updateByPrimaryKey(record);
	}

}
