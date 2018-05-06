package com.rmd.wms.service.impl;

import java.util.List;

import com.rmd.wms.bean.vo.app.MoveInUsersParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rmd.wms.bean.WarehouseUser;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.WarehouseUserMapper;
import com.rmd.wms.service.WarehouseUserService;

@Service("warehouseUserService")
public class WarehouseUserServiceImpl extends BaseService implements WarehouseUserService{

	@Override
	public int insertSelective(WarehouseUser warehouseUser) {
		return this.getMapper(WarehouseUserMapper.class).insertSelective(warehouseUser);		
	}

	@Transactional
	@Override	
	public int insertBatchSelective(Integer wareId,List<WarehouseUser> list) {
		int result = 0;
		this.getMapper(WarehouseUserMapper.class).deleteByWareId(wareId);
		for(WarehouseUser o:list){
			this.getMapper(WarehouseUserMapper.class).insertSelective(o);
			result++;
		}
		return result;
	}

	@Override
	public List<WarehouseUser> selectByWareId(Integer wareId) {
		List<WarehouseUser> list=this.getMapper(WarehouseUserMapper.class).selectByWareId(wareId);
		return list;
	}

	@Override
	public List<WarehouseUser> selectByWareIdAndUserIds(MoveInUsersParam param) {
		return this.getMapper(WarehouseUserMapper.class).selectByWareIdAndUserIds(param);
	}
}
