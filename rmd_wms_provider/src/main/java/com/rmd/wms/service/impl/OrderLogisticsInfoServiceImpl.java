package com.rmd.wms.service.impl;

import org.springframework.stereotype.Service;

import com.rmd.wms.bean.OrderLogisticsInfo;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.OrderLogisticsInfoMapper;
import com.rmd.wms.service.OrderLogisticsInfoService;

/**
 * 
* @ClassName: OrderLogisticsInfoServiceImpl 
* @Description: TODO(订单收货人信息) 
* @author ZXLEI
* @date Feb 22, 2017 7:15:48 PM 
*
 */
@Service("orderLogisticsInfoService")
public class OrderLogisticsInfoServiceImpl  extends BaseService implements OrderLogisticsInfoService{

	@Override
	public int deleteByPrimaryKey(Integer id) {
		
		return this.getMapper(OrderLogisticsInfoMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderLogisticsInfo record) {
		
		return this.getMapper(OrderLogisticsInfoMapper.class).insert(record);
	}

	@Override
	public int insertSelective(OrderLogisticsInfo record) {
		return this.getMapper(OrderLogisticsInfoMapper.class).insertSelective(record);
	}

	@Override
	public OrderLogisticsInfo selectByPrimaryKey(Integer id) {
		
		return this.getMapper(OrderLogisticsInfoMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public OrderLogisticsInfo selectByOrderNo(String orderNo) {
		return this.getMapper(OrderLogisticsInfoMapper.class).selectByOrderNo(orderNo);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderLogisticsInfo record) {
		return this.getMapper(OrderLogisticsInfoMapper.class).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrderLogisticsInfo record) {
		return this.getMapper(OrderLogisticsInfoMapper.class).updateByPrimaryKey(record);
	}

}
