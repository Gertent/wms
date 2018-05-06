package com.rmd.wms.service.impl;

import com.github.pagehelper.PageHelper;
import com.rmd.wms.bean.Location;
import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.WarehouseArea;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.LocationGoodsBindMapper;
import com.rmd.wms.dao.LocationMapper;
import com.rmd.wms.dao.WarehouseAreaMapper;
import com.rmd.wms.dao.WarehouseMapper;
import com.rmd.wms.enums.WarehouseAreaType;
import com.rmd.wms.service.WarehouseAreaService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("warehouseAreaService")
public class WarehouseAreaServiceImpl extends BaseService implements WarehouseAreaService{

	private static Logger logger= Logger.getLogger(WarehouseAreaService.class);

	@Override
	public PageBean<WarehouseArea> listWarehouseArea(Integer page,
			Integer rows, WarehouseArea record) {
		  PageHelper.startPage(page, rows);
		  return new PageBean<WarehouseArea>(this.getMapper(WarehouseAreaMapper.class).selectByCriteria(record));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateByPrimaryKeySelective(WarehouseArea warehouseArea) {
		LocationGoodsBind locationGoodsBind = new LocationGoodsBind();
		/**库区编辑不可买时 判断是否有商品，如果有商品，且商品数量大于0，  则不可改变*/
		if(warehouseArea.getType() == WarehouseAreaType.A000.getValue()){//库区性质 0：不可卖，1：可卖
			locationGoodsBind.setAreaId(warehouseArea.getId());
			int locationNumCount = this.getMapper(LocationGoodsBindMapper.class).selectCountByLocationNum(locationGoodsBind);
			if(locationNumCount>0){
				return 1;
			}
		}
		Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(warehouseArea.getWareId());
		warehouseArea.setWareName(warehouse.getWareName());
		int updateResult = this.getMapper(WarehouseAreaMapper.class).updateByPrimaryKeySelective(warehouseArea);
		//修改该库区下的库位性质
		Location location=new Location();
		location.setAreaId(warehouseArea.getId());
		List<Location> listLocations=this.getMapper(LocationMapper.class).selectByCriteria(location);
		for(Location o:listLocations){
			Location l=new Location();
			l.setId(o.getId());
			l.setType(warehouseArea.getType());
			this.getMapper(LocationMapper.class).updateByPrimaryKeySelective(l);
		}
		if(updateResult>0){
			return 2;
		}
		return 0;
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void insertSelective(WarehouseArea warehouseArea) {
		Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(warehouseArea.getWareId());
		warehouseArea.setWareName(warehouse.getWareName());
		Location location = new Location();
		location.setAreaId(warehouseArea.getId());
		if(warehouseArea.getType() ==WarehouseAreaType.A000.getValue()){//库区性质 0：不可卖，1：可卖
			location.setType(WarehouseAreaType.A000.getValue());
		}else{
			location.setType(WarehouseAreaType.A001.getValue());
		}
		this.getMapper(LocationMapper.class).updateByPrimaryKeySelective(location);
		this.getMapper(WarehouseAreaMapper.class).insertSelective(warehouseArea);
	}

	@Override
	public WarehouseArea selectByPrimaryKey(Integer id) {
		return this.getMapper(WarehouseAreaMapper.class).selectByPrimaryKey(id);	
	}

	@Override
	public List<WarehouseArea> selectByCriteria(WarehouseArea warehouseArea) {
		return this.getMapper(WarehouseAreaMapper.class).selectByCriteria(warehouseArea);
	}

	@Override
	public int selectByCode(WarehouseArea warehouseArea) {
		return this.getMapper(WarehouseAreaMapper.class).selectByCode(warehouseArea);
	}
	
	@Override
	public int selectByName(WarehouseArea warehouseArea) {
		return this.getMapper(WarehouseAreaMapper.class).selectByName(warehouseArea);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public int updateByStatus(String ids, Integer status, Integer wareId) {
		int result = 0;
		WarehouseArea warehouseArea = new WarehouseArea();
		LocationGoodsBind locationGoodsBind = new LocationGoodsBind();
		String[] arr = ids.split(",");
		for (String str : arr) {
			/**查询此仓库对应库存中是否有商品 有则不能禁用*/
			if(status == Constant.TYPE_STATUS_NO){
				locationGoodsBind.setAreaId(Integer.valueOf(str));
				int locationNumCount = this.getMapper(LocationGoodsBindMapper.class).selectCountByLocationNum(locationGoodsBind);
				logger.info("仓库对应库存中商品数量："+locationNumCount+ Constant.LINE);
				if(locationNumCount >0){
					return -1;
				}
			}
			warehouseArea.setId(Integer.valueOf(str));
			warehouseArea.setStatus(status);
			int updateResult = this.getMapper(WarehouseAreaMapper.class).updateByPrimaryKeySelective(warehouseArea);
			if(updateResult > 0){
				result++;
				//如果修改成功需要把库区下的所有货位禁止掉
				Location locationTemp = new Location();
				locationTemp.setAreaId(Integer.valueOf(str));
				locationTemp.setWareId(wareId);
				locationTemp.setStatus(status);
				this.getMapper(LocationMapper.class).updateStatusByCriteria(locationTemp);
			}
		}
		return result;
	}
}
