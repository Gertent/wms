package com.rmd.wms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rmd.wms.bean.DeliveryRange;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.DeliveryRangeMapper;
import com.rmd.wms.service.DeliveryRangeService;

@Service("deliveryRangeService")
public class DeliveryRangeServiceImpl extends BaseService implements DeliveryRangeService {

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return this.getMapper(DeliveryRangeMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	public int deleteByCriteria(DeliveryRange record) {
		return this.getMapper(DeliveryRangeMapper.class).deleteByCriteria(record);
	}

	@Override
	public int insert(DeliveryRange record) {
		return this.getMapper(DeliveryRangeMapper.class).insert(record);
	}

	@Override
	public int insertSelective(DeliveryRange record) {
		return this.getMapper(DeliveryRangeMapper.class).insertSelective(record);
	}

	@Override
	public DeliveryRange selectByPrimaryKey(Integer id) {
		return this.getMapper(DeliveryRangeMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(DeliveryRange record) {
		return this.getMapper(DeliveryRangeMapper.class).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DeliveryRange record) {
		return this.getMapper(DeliveryRangeMapper.class).updateByPrimaryKey(record);
	}

	@Override
	public List<DeliveryRange> selectAllByWhere(Map<String, Object> parmaMap) {
		return this.getMapper(DeliveryRangeMapper.class).selectAllByWhere(parmaMap);
	}

	@Override
	public int insertBatch(List<DeliveryRange> list) {
	
		return this.getMapper(DeliveryRangeMapper.class).insertBatch(list);
	}
	

}
