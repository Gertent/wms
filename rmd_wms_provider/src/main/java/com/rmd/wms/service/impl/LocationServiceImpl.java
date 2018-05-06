package com.rmd.wms.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.rmd.wms.bean.Location;
import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.WarehouseArea;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.LocationGoodsBindMapper;
import com.rmd.wms.dao.LocationMapper;
import com.rmd.wms.dao.WarehouseAreaMapper;
import com.rmd.wms.dao.WarehouseMapper;
import com.rmd.wms.service.LocationService;
import org.springframework.transaction.annotation.Transactional;

@Service("locationService")
public class LocationServiceImpl extends BaseService implements LocationService{

	private static Logger logger= Logger.getLogger(LocationServiceImpl.class);
	
	@Override
	public PageBean<Location> listLocation(Integer page, Integer rows,
			Location record) {
		  PageHelper.startPage(page, rows);
		  return new PageBean<Location>(this.getMapper(LocationMapper.class).selectByCriteria(record));
	}

	@Override
	public void updateByPrimaryKeySelective(Location location) {
		
		Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(location.getWareId());
		location.setWareName(warehouse.getWareName());
		WarehouseArea warehouseArea =this.getMapper(WarehouseAreaMapper.class).selectByPrimaryKey(location.getAreaId());
		location.setAreaName(warehouseArea.getAreaName());
		this.getMapper(LocationMapper.class).updateByPrimaryKeySelective(location);
	}

	@Override
	public int selectByLocationNo(Location location) {
		return this.getMapper(LocationMapper.class).selectByLocationNo(location);
	}

	@Override
	public void insertSelective(Location location) {
		Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(location.getWareId());
		location.setWareName(warehouse.getWareName());
		WarehouseArea warehouseArea =this.getMapper(WarehouseAreaMapper.class).selectByPrimaryKey(location.getAreaId());
		location.setAreaName(warehouseArea.getAreaName());
		location.setType(warehouseArea.getType());
		this.getMapper(LocationMapper.class).insertSelective(location);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ServerStatus insertBatch(List<Location> list) throws WMSException {
		ServerStatus ss = new ServerStatus();
		if (list == null || list.isEmpty()) {
			ss.setFlag(Constant.TYPE_STATUS_NO);
			ss.setMessage("参数为空");
			return ss;
		}
		try {
			this.getMapper(LocationMapper.class).insertBatch(list);
			ss.setFlag(Constant.TYPE_STATUS_YES);
			ss.setMessage("添加成功");
		} catch (Exception e) {
			Throwable cause = e.getCause();
			String message = cause.getMessage();
			if (message.contains("location_unique_index")) {
				String alertStr = message.split("'")[1];
				throw new WMSException("23000", alertStr, e.getMessage(), e);
			} else {
				logger.error(Constant.LINE + "批量插入货位异常", e);
				throw new WMSException("500", "批量插入货位异常", e.getMessage(), e);
			}
		}
		return ss;
	}

	@Override
	public Location selectByPrimaryKey(Integer id) {
		return this.getMapper(LocationMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public void updateByType(Location location) {
		this.getMapper(LocationMapper.class).updateByType(location);
	}

	@Override
	public List<Location> selectByCriteria(Location location) {
		return this.getMapper(LocationMapper.class).selectByCriteria(location);
	}

	@Override
	public int updateByStatus(String ids, Integer status) {
		String[] arr = ids.split(",");
		Location location = new Location();
		LocationGoodsBind locationGoodsBind = new LocationGoodsBind();
		int result = 0;
		for (String str : arr) {
			/**查询此仓库对应库存中是否有商品 有则不能禁用*/
			if(status == 0){
				locationGoodsBind.setLocationId(Integer.valueOf(str));
				int locationNumCount = this.getMapper(LocationGoodsBindMapper.class).selectCountByLocationNum(locationGoodsBind);
				logger.info("仓库对应库存商品数量："+locationNumCount+ Constant.LINE);
				if(locationNumCount >0){
					return result =-1;
				}
			}
			location.setId(Integer.valueOf(str));
			location.setStatus(status);
			int updateResult = this.getMapper(LocationMapper.class).updateByPrimaryKeySelective(location);
			if(updateResult >0){
				result++;
			}
		}
		return result;
	}
}
