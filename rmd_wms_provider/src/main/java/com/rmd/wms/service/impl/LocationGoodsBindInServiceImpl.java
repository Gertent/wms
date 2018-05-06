package com.rmd.wms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rmd.wms.bean.LocationGoodsBindIn;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.LocationGoodsBindInMapper;
import com.rmd.wms.service.LocationGoodsBindInService;


@Service("locationGoodsBindInService")
public class LocationGoodsBindInServiceImpl extends BaseService implements LocationGoodsBindInService {

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getMapper(LocationGoodsBindInMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int insert(LocationGoodsBindIn record) {
        return this.getMapper(LocationGoodsBindInMapper.class).insert(record);
    }

    @Override
    public int insertSelective(LocationGoodsBindIn record) {
        return this.getMapper(LocationGoodsBindInMapper.class).insertSelective(record);
    }

    @Override
    public LocationGoodsBindIn selectByPrimaryKey(Integer id) {
        return this.getMapper(LocationGoodsBindInMapper.class).selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(LocationGoodsBindIn record) {
        return this.getMapper(LocationGoodsBindInMapper.class).updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKey(LocationGoodsBindIn record) {
        return this.getMapper(LocationGoodsBindInMapper.class).updateByPrimaryKey(record);
    }    


	@Override
	public List<LocationGoodsBindIn> listLocationGoodsBindIns(Map<String, Object> parmaMap) {
		return this.getMapper(LocationGoodsBindInMapper.class).selectAllByWhere(parmaMap);
	}


}
