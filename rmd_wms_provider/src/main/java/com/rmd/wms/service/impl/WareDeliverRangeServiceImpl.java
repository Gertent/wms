package com.rmd.wms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rmd.wms.bean.WareDeliverRange;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.WareDeliverRangeMapper;
import com.rmd.wms.service.WareDeliverRangeService;

@Service("wareDeliverRangeService")
public class WareDeliverRangeServiceImpl extends BaseService implements WareDeliverRangeService {

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return this.getMapper(WareDeliverRangeMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(WareDeliverRange record) {
		int insert = this.getMapper(WareDeliverRangeMapper.class).insert(record);
		return insert;
	}

	@Override
	public int insertSelective(WareDeliverRange record) {
		return this.getMapper(WareDeliverRangeMapper.class).insertSelective(record);
	}

	@Override
	public WareDeliverRange selectByPrimaryKey(Integer id) {
		return this.getMapper(WareDeliverRangeMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(WareDeliverRange record) {
		return this.getMapper(WareDeliverRangeMapper.class).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(WareDeliverRange record) {
		return this.getMapper(WareDeliverRangeMapper.class).updateByPrimaryKey(record);
	}

	@Override
	public List<WareDeliverRange> selectAllByWhere(Map<String, Object> parmaMap) {
		return this.getMapper(WareDeliverRangeMapper.class).selectAllByWhere(parmaMap);
	}

	@Override
	public int insertBatch(List<WareDeliverRange> list) {
	
		return this.getMapper(WareDeliverRangeMapper.class).insertBatch(list);
	}
	

}
