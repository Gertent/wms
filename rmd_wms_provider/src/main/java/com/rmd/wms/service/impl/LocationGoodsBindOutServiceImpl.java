package com.rmd.wms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rmd.wms.bean.LocationGoodsBindOut;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.LocationGoodsBindOutMapper;
import com.rmd.wms.service.LocationGoodsBindOutService;
/**
 * 
* @ClassName: LocationGoodsBindOutServiceImpl 
* @Description: TODO(出库商品货位绑定) 
* @author ZXLEI
* @date Feb 23, 2017 1:43:14 PM 
*
 */
@Service("locationGoodsBindOutService")
public class LocationGoodsBindOutServiceImpl  extends BaseService implements LocationGoodsBindOutService{

	@Override
	public int deleteByPrimaryKey(Integer id) {
		
		return this.getMapper(LocationGoodsBindOutMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(LocationGoodsBindOut record) {
		return this.getMapper(LocationGoodsBindOutMapper.class).insert(record);
	}

	@Override
	public int insertSelective(LocationGoodsBindOut record) {
		return this.getMapper(LocationGoodsBindOutMapper.class).insertSelective(record);
	}

	@Override
	public LocationGoodsBindOut selectByPrimaryKey(Integer id) {
		return this.getMapper(LocationGoodsBindOutMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(LocationGoodsBindOut record) {
	
		return this.getMapper(LocationGoodsBindOutMapper.class).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(LocationGoodsBindOut record) {
		
		return this.getMapper(LocationGoodsBindOutMapper.class).updateByPrimaryKey(record);
	}

	@Override
	public List<LocationGoodsBindOut> selectByOrderNoCode(Map<String, Object> parmaMap) {
		
		return this.getMapper(LocationGoodsBindOutMapper.class).selectByOrderNoCode(parmaMap);
	}

	/**
	 * 出库单锁库操作
	 */
	@Override
	public int insertBatch(List<LocationGoodsBindOut> list) {
	    Integer flag=0;
	    try {
		flag=this.getMapper(LocationGoodsBindOutMapper.class).insertBatch(list);
	    } catch (Exception e) {
		flag=0;
		e.printStackTrace();
	    }
	    return flag;
	}

}
