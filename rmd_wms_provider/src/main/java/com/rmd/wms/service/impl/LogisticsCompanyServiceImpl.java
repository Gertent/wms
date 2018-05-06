package com.rmd.wms.service.impl;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.NotificationBuilder;
import com.rmd.commons.servutils.Notifications;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.LogisticsFreightTemplateVo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.LogisticsCompanyMapper;
import com.rmd.wms.dao.WareDeliverRangeMapper;
import com.rmd.wms.dao.WarehouseMapper;
import com.rmd.wms.enums.YesAdNoFlag;
import com.rmd.wms.service.DeliveryRangeService;
import com.rmd.wms.service.LogisticsCompanyService;
import com.rmd.wms.service.LogisticsFreightCityService;
import com.rmd.wms.service.LogisticsFreightTemplateService;
import com.rmd.wms.util.WmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("logisticsCompanyService")
public class LogisticsCompanyServiceImpl extends BaseService implements LogisticsCompanyService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DeliveryRangeService deliveryRangeService;  //物流公司配送范围

	@Autowired
	private LogisticsFreightTemplateService logisticsFreightTemplateService;//运费模板

	@Autowired
	private LogisticsFreightCityService logisticsFreightCityService;	//运费模板地区范围

	@Override
	public int deleteByPrimaryKey(Integer id) {
	
		return this.getMapper(LogisticsCompanyMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(LogisticsCompany record) {
		this.getMapper(LogisticsCompanyMapper.class).insert(record);
		return this.getMapper(LogisticsCompanyMapper.class).selectMaxId();
	}

	@Override
	public int insertSelective(LogisticsCompany record) {
		return this.getMapper(LogisticsCompanyMapper.class).insertSelective(record);
	}

	@Override
	public LogisticsCompany selectByPrimaryKey(Integer id) {
		return this.getMapper(LogisticsCompanyMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public List<LogisticsCompany> selectByCriteria(LogisticsCompany record) {
		
		return this.getMapper(LogisticsCompanyMapper.class).selectByCriteria(record);
	}

	@Override
	public int updateByPrimaryKeySelective(LogisticsCompany record) {
	
		return this.getMapper(LogisticsCompanyMapper.class).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(LogisticsCompany record) {
	
		return this.getMapper(LogisticsCompanyMapper.class).updateByPrimaryKey(record);
	}

	@Override
	public PageBean<LogisticsCompany> ListLogisticsCompanys(int page, int rows,
			Map<String, Object> parmaMap) {
		PageHelper.startPage(page, rows);
		return new PageBean<LogisticsCompany>(this.getMapper(LogisticsCompanyMapper.class).selectAllByWhere(parmaMap));
	}

	@Override
	public Notification<LogisticsCompany> selectByProvCodeAndWeight(String provCode, Double parcelWeight) {
		Notification<LogisticsCompany> noti = new Notification<>();
		if (StringUtils.isBlank(provCode) || parcelWeight == null) {
			noti = NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
			noti.setNotifInfo("参数错误");
			return noti;
		}
		WareDeliverRange record = new WareDeliverRange();
		record.setProvCode(provCode);
		record.setStatus(Constant.TYPE_STATUS_YES);
		List<WareDeliverRange> deliverranges = this.getMapper(WareDeliverRangeMapper.class).selectByCriteria(record);
		if (deliverranges == null || deliverranges.size() < 1) {
			noti.setNotifCode(101);
			noti.setNotifInfo("该地区不支持配送");
			return noti;
		}
		Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(deliverranges.get(0).getWareId());
		if (warehouse == null || warehouse.getStatus() != Constant.TYPE_STATUS_YES) {
			noti.setNotifCode(102);
			noti.setNotifInfo("数据错误，仓库被停用或不存在");
			return noti;
		}
		// 查询出所有可以承运该省份的承运商
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("status", Constant.TYPE_STATUS_YES);
		paramMap.put("wareId", warehouse.getId());
		paramMap.put("provCode", provCode);
		paramMap.put("parcelWeight", parcelWeight);
		List<LogisticsCompany> lcs = this.getMapper(LogisticsCompanyMapper.class).selectByProvCodeAndWeight(paramMap);
		if (lcs == null || lcs.size() < 1) {
			noti.setNotifCode(103);
			noti.setNotifInfo("暂时无承运商提供服务");
			return noti;
		}
		noti = NotificationBuilder.buildOne(Notifications.OK);
		noti.setResponseData(lcs.get(0));
		return noti;
	}

	@Override
	public Notification<List<LogisticsCompany>> selectAllLogisComp() {
		Notification<List<LogisticsCompany>> noti = NotificationBuilder.buildOne(Notifications.OK);
		LogisticsCompany record = new LogisticsCompany();
		record.setStatus(Constant.TYPE_STATUS_YES);
		noti.setResponseData(this.getMapper(LogisticsCompanyMapper.class).selectByCriteria(record));
		return noti;
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public ServerStatus addLogisticsCompanyAdFreight(LogisticsCompany logisticsCompany, List<DeliveryRange> deliveryRangeList, List<LogisticsFreightTemplateVo> logisticsFreightList){
		ServerStatus ss=new ServerStatus();
		// 添加承运商数据
		this.getMapper(LogisticsCompanyMapper.class).insert(logisticsCompany);
		//添加承运商配送范围------begin
		List<DeliveryRange> newDeliveryRangeList=new ArrayList<>();
		DeliveryRange newDeliveryRange=null;
		for(DeliveryRange deliveryRange:deliveryRangeList){
			newDeliveryRange=new DeliveryRange();
			WmsUtil.copyPropertiesIgnoreNull(deliveryRange,newDeliveryRange);
			newDeliveryRange.setLogisticsId(logisticsCompany.getId());
			newDeliveryRangeList.add(newDeliveryRange);
		}
		deliveryRangeService.insertBatch(newDeliveryRangeList);
		//添加承运商配送范围------end
		//添加运费模板
		LogisticsFreightTemplate logisticsFreightTemplate=null;
		List<LogisticsFreightCity> logisticsFreightCityList=null;//运费模板城市关系
		List<LogisticsFreightCity> newLogisticsFreightCityList=new ArrayList<>();//运费模板城市关系
		LogisticsFreightCity newLogisticsFreightCity=null;
		for(LogisticsFreightTemplateVo logisticsFreightTemplateVo:logisticsFreightList){
			logisticsFreightTemplate=new LogisticsFreightTemplate();
			WmsUtil.copyPropertiesIgnoreNull(logisticsFreightTemplateVo,logisticsFreightTemplate);
			logisticsFreightTemplate.setLogisticsId(logisticsCompany.getId());
			logisticsFreightTemplate.setLogisticsName(logisticsCompany.getName());
			logisticsFreightTemplate=logisticsFreightTemplateService.insertSelective(logisticsFreightTemplate);
			//添加运费模板地区映射
			if(YesAdNoFlag.A000.getValue()==logisticsFreightTemplate.getFreightType()) {
				logisticsFreightCityList = logisticsFreightTemplateVo.getLogisticsFreightCityList();
				for (LogisticsFreightCity logisticsFreightCity : logisticsFreightCityList) {
					newLogisticsFreightCity = new LogisticsFreightCity();
					WmsUtil.copyPropertiesIgnoreNull(logisticsFreightCity, newLogisticsFreightCity);
					newLogisticsFreightCity.setLogisticsId(logisticsCompany.getId());
					newLogisticsFreightCity.setTemplateId(logisticsFreightTemplate.getId());
					newLogisticsFreightCity.setWareId(logisticsFreightTemplate.getWareId());
					newLogisticsFreightCity.setWareName(logisticsFreightTemplate.getWareName());
					newLogisticsFreightCityList.add(newLogisticsFreightCity);
				}
				if(newLogisticsFreightCityList.size()>0) {
					logisticsFreightCityService.insertBatch(newLogisticsFreightCityList);
				}
			}

		}
		ss.setStatus("200");
		ss.setMessage("操作成功");
		logger.info(Constant.LINE+"承运商相关数据保存成功，logisticsId="+logisticsCompany.getId());
		return ss;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ServerStatus updateLogisticsCompany(LogisticsCompany logisticsCompany, List<DeliveryRange> deliveryRangeList){
		ServerStatus ss=new ServerStatus();
		// 添加承运商数据
		int logisticsId = this.getMapper(LogisticsCompanyMapper.class).updateByPrimaryKeySelective(logisticsCompany);
		//添加承运商配送范围------begin
		DeliveryRange oldDeliveryRange=new DeliveryRange();
		oldDeliveryRange.setLogisticsId(logisticsCompany.getId());
		//清除历史数据
		deliveryRangeService.deleteByCriteria(oldDeliveryRange);
		List<DeliveryRange> newDeliveryRangeList=new ArrayList<>();
		DeliveryRange newDeliveryRange=null;
		for(DeliveryRange deliveryRange:deliveryRangeList){
			newDeliveryRange=new DeliveryRange();
			WmsUtil.copyPropertiesIgnoreNull(deliveryRange,newDeliveryRange);
			newDeliveryRange.setLogisticsId(logisticsId);
			newDeliveryRangeList.add(newDeliveryRange);
		}
		deliveryRangeService.insertBatch(newDeliveryRangeList);
		//添加承运商配送范围------end

		ss.setStatus("200");
		ss.setMessage("操作成功");
		logger.info(Constant.LINE+"承运商相关数据保存成功，logisticsId="+logisticsId);
		return ss;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ServerStatus updateFreightTemplate(Integer logisticsId, List<LogisticsFreightTemplateVo> logisticsFreightList){
		ServerStatus ss=new ServerStatus();
		// 获取承运商数据
		LogisticsCompany logisticsCompany=this.getMapper(LogisticsCompanyMapper.class).selectByPrimaryKey(logisticsId);
		//清除运费模板历史数据
		LogisticsFreightTemplate oldTemplate=new LogisticsFreightTemplate();
		oldTemplate.setLogisticsId(logisticsId);
		logisticsFreightTemplateService.deleteByCriteria(oldTemplate);
		//清除运费模板范围历史数据
		LogisticsFreightCity oldCitys=new LogisticsFreightCity();
		oldCitys.setLogisticsId(logisticsId);
		logisticsFreightCityService.deleteByCriteria(oldCitys);
		//添加运费模板
		LogisticsFreightTemplate logisticsFreightTemplate=null;
		List<LogisticsFreightCity> logisticsFreightCityList=null;//运费模板城市关系
		List<LogisticsFreightCity> newLogisticsFreightCityList=new ArrayList<>();//运费模板城市关系
		LogisticsFreightCity newLogisticsFreightCity=null;
		for(LogisticsFreightTemplateVo logisticsFreightTemplateVo:logisticsFreightList){
			logisticsFreightTemplate=new LogisticsFreightTemplate();
			WmsUtil.copyPropertiesIgnoreNull(logisticsFreightTemplateVo,logisticsFreightTemplate);
			logisticsFreightTemplate.setLogisticsId(logisticsCompany.getId());
			logisticsFreightTemplate.setLogisticsName(logisticsCompany.getName());
			logisticsFreightTemplate=logisticsFreightTemplateService.insertSelective(logisticsFreightTemplate);
			//添加运费模板地区映射
			if(YesAdNoFlag.A000.getValue()==logisticsFreightTemplate.getFreightType()) {
				logisticsFreightCityList = logisticsFreightTemplateVo.getLogisticsFreightCityList();
				for (LogisticsFreightCity logisticsFreightCity : logisticsFreightCityList) {
					newLogisticsFreightCity = new LogisticsFreightCity();
					WmsUtil.copyPropertiesIgnoreNull(logisticsFreightCity, newLogisticsFreightCity);
					newLogisticsFreightCity.setLogisticsId(logisticsCompany.getId());
					newLogisticsFreightCity.setTemplateId(logisticsFreightTemplate.getId());
					newLogisticsFreightCity.setWareId(logisticsFreightTemplate.getWareId());
					newLogisticsFreightCity.setWareName(logisticsFreightTemplate.getWareName());
					newLogisticsFreightCityList.add(newLogisticsFreightCity);
				}
				if(newLogisticsFreightCityList.size()>0) {
					logisticsFreightCityService.insertBatch(newLogisticsFreightCityList);
				}
			}

		}
		ss.setStatus("200");
		ss.setMessage("操作成功");
		logger.info(Constant.LINE+"承运商相关数据保存成功，logisticsId="+logisticsCompany.getId());
		return ss;
	}
}
