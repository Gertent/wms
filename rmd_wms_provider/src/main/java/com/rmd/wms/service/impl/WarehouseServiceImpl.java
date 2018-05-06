package com.rmd.wms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.NotificationBuilder;
import com.rmd.commons.servutils.Notifications;
import com.rmd.wms.constant.Constant;

import com.rmd.wms.enums.YesAdNoFlag;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.WareDeliverRange;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.LocationGoodsBindMapper;
import com.rmd.wms.dao.WarehouseMapper;
import com.rmd.wms.service.WareDeliverRangeService;
import com.rmd.wms.service.WarehouseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by liu on 2017/2/27.
 */
@Service("warehouseService")
public class WarehouseServiceImpl extends BaseService implements WarehouseService {

	private static Logger logger= Logger.getLogger(WarehouseService.class);
	
	@Resource(name="wareDeliverRangeService")
	private WareDeliverRangeService wareDeliverRangeService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getMapper(WarehouseMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Warehouse record) {
    	this.getMapper(WarehouseMapper.class).insert(record);
    	int id=record.getId();
        return id;
    }
    @Transactional
    @Override
    public int insert(Warehouse record,String areaCode,String areaName) {
    	this.getMapper(WarehouseMapper.class).insert(record);
    	int wareId=record.getId();
    	
    	if(StringUtil.isNotEmpty(areaCode)){
			//省份编码
			String[] areaCodeArr = areaCode.split(",");
			//省份名称
			String[] areaNameArr = areaName.split(",");
			if (areaCodeArr.length > 0) {
				//添加配送范围--BEGIN
				List<WareDeliverRange> liDeliveryRanges = new ArrayList<WareDeliverRange>();
				for (int i=0;i<areaCodeArr.length;i++) {
					WareDeliverRange deliveryRange = new WareDeliverRange();
					deliveryRange.setProvCode(areaCodeArr[i]);
					deliveryRange.setName(areaNameArr[i]);
					deliveryRange.setStatus(1);
					deliveryRange.setWareId(wareId);
					liDeliveryRanges.add(deliveryRange);
				}
				if (liDeliveryRanges.size() > 0 && wareId > 0) {
					wareDeliverRangeService.insertBatch(liDeliveryRanges);
				}
			}
		}
        return wareId;
    }

    @Override
    public int insertSelective(Warehouse record) {
        return this.getMapper(WarehouseMapper.class).insertSelective(record);
    }

    @Override
    public Warehouse selectByPrimaryKey(Integer id) {
        return this.getMapper(WarehouseMapper.class).selectByPrimaryKey(id);
    }

	@Override
	public Notification<Warehouse> selectByWareId(Integer id) {
		if (id == null) {
			return NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
		}
		Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(id);
		Notification<Warehouse> noti = NotificationBuilder.buildOne(Notifications.OK);
		noti.setResponseData(warehouse);
		return noti;
	}

	@Override
	public Notification<Warehouse> selectByWareCode(String wareCode) {
		if (StringUtils.isBlank(wareCode)) {
			return NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
		}
		Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByWareCode(wareCode);
		Notification<Warehouse> noti = NotificationBuilder.buildOne(Notifications.OK);
		noti.setResponseData(warehouse);
		return noti;
	}

	@Override
	public Notification<List<Warehouse>> selectAllWarehouse() {
		Notification<List<Warehouse>> noti = NotificationBuilder.buildOne(Notifications.OK);
		noti.setResponseData(this.getMapper(WarehouseMapper.class).selectByCriteria(null));
		return noti;
	}

	@Override
    public int updateByPrimaryKeySelective(Warehouse record) {
        return this.getMapper(WarehouseMapper.class).updateByPrimaryKeySelective(record);
    }
	
	@Override
    public int updateByPrimaryKeySelective(Warehouse record,String areaCode,String areaName) {
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("wareId", record.getId());
		List<WareDeliverRange> list=wareDeliverRangeService.selectAllByWhere(paramMap);
		for(WareDeliverRange d:list){
			wareDeliverRangeService.deleteByPrimaryKey(d.getId());
		}
		if(StringUtil.isNotEmpty(areaCode)){
			//省份编码
			String[] areaCodeArr = areaCode.split(",");
			//省份名称
			String[] areaNameArr = areaName.split(",");
			if (areaCodeArr.length > 0) {
				//添加配送范围--BEGIN
				List<WareDeliverRange> liDeliveryRanges = new ArrayList<WareDeliverRange>();
				for (int i=0;i<areaCodeArr.length;i++) {
					WareDeliverRange deliveryRange = new WareDeliverRange();
					deliveryRange.setProvCode(areaCodeArr[i]);
					deliveryRange.setName(areaNameArr[i]);
					deliveryRange.setStatus(YesAdNoFlag.A001.getValue());
					deliveryRange.setWareId(record.getId());
					liDeliveryRanges.add(deliveryRange);
				}
				if (liDeliveryRanges.size() > 0 && record.getId() > 0) {
					wareDeliverRangeService.insertBatch(liDeliveryRanges);
				}
			}
		}
        return this.getMapper(WarehouseMapper.class).updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Warehouse record) {
        return this.getMapper(WarehouseMapper.class).updateByPrimaryKey(record);
    }

    @Override
    public List<Warehouse> selectByCriteria(Warehouse record) {
        return this.getMapper(WarehouseMapper.class).selectByCriteria(record);
    }

	@Override
	public PageBean<Warehouse> listWarehouse(Integer page, Integer rows,
			Warehouse record) {
			  
	    PageHelper.startPage(page, rows);
	    return new PageBean<Warehouse>(this.getMapper(WarehouseMapper.class).selectByCriteria(record));
	}

	@Override
	public int selectByName(Warehouse warehouse) {
		return this.getMapper(WarehouseMapper.class).selectByName(warehouse);
	}

	@Override
	public int selectByCode(Warehouse warehouse) {
		return this.getMapper(WarehouseMapper.class).selectByCode(warehouse);
	}

	@Override
	public int updateByStatus(String ids, Integer status) {
		int result = 0;
		Warehouse warehouse = new Warehouse();
		LocationGoodsBind locationGoodsBind = new LocationGoodsBind();
		String[] arr = ids.split(",");
		for (String str : arr) {
			/**查询此仓库对应库存中是否有商品 有则不能禁用*/
			if(status == 0){
				locationGoodsBind.setWareId(Integer.valueOf(str));
				int locationNumCount = this.getMapper(LocationGoodsBindMapper.class).selectCountByLocationNum(locationGoodsBind);
				logger.info("仓库对应库存商品数量；"+locationNumCount+Constant.LINE);
				if(locationNumCount >0){
					return result =-1;
				}
			}
			warehouse.setId(Integer.valueOf(str));
			warehouse.setStatus(status);
			int updateResult = this.getMapper(WarehouseMapper.class).updateByPrimaryKeySelective(warehouse);
			if(updateResult >0){
				result++;
			}
		}
		return result;
	}

	@Override
	public List<Warehouse> selectByParam(Map<String, Object> paraMap) {
		 return this.getMapper(WarehouseMapper.class).selectByParam(paraMap);
	}

	@Override
	public List<Warehouse> selectByUserId(Integer userId) {
		return this.getMapper(WarehouseMapper.class).selectByUserId(userId);
	}
}