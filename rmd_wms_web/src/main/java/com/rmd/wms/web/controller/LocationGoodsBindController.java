package com.rmd.wms.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.rmd.commons.servutils.Notification;
import com.rmd.lygp.back.entity.GoodsSaleInfoVo;
import com.rmd.lygp.back.model.GoodsSaleInfo;
import com.rmd.lygp.back.service.GoodsBaseService;
import com.rmd.wms.bean.Location;
import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.WarehouseArea;
import com.rmd.wms.bean.vo.LocationGoodsBindVo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.bean.vo.web.SearchBindsParam;
import com.rmd.wms.enums.LocationStatus;
import com.rmd.wms.enums.WarehouseAreaStatus;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.LocationGoodsBindService;
import com.rmd.wms.service.LocationService;
import com.rmd.wms.service.WarehouseAreaService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.constant.Constant;
import com.rmd.wms.web.util.DateJsonValueProcessor;
import com.rmd.wms.web.util.DownloadUtils;
import com.rmd.wms.web.util.ExcelCommon;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value="/locationGoodsBind")
public class LocationGoodsBindController extends AbstractAjaxController{
	
	private static Logger logger = Logger.getLogger(WarehouseAreaController.class);
	
	@Resource(name = "locationGoodsBindService")
	private LocationGoodsBindService locationGoodsBindService; // 仓库管理
	
	@Resource(name = "warehouseAreaService")
	private WarehouseAreaService warehouseAreaService; // 库区管理
	
	@Resource(name = "locationService")
	private LocationService locationService; // 货位管理

	@Autowired
	private GoodsBaseService goodsBaseService;
	
	/**
	  * @Description:跳转到库存列表
	  * @return
	  * 2017年3月20日 
	  */
	@RequestMapping(value = "/LocationGoodsBind")
	public ModelAndView jumpView() {
		WarehouseArea warehouseArea = new WarehouseArea();
		warehouseArea.setWareId(super.getCurrentWareId());
		warehouseArea.setStatus(WarehouseAreaStatus.A001.getValue());
		ModelAndView model = new ModelAndView("locationGoodsBind/locationGoodsBindManage");
		List<WarehouseArea> warehouseAreaList = warehouseAreaService.selectByCriteria(warehouseArea);
		model.addObject("warehouseAreaList", warehouseAreaList);
		return model;
	}
	/**
     * 根据库区获取库位信息
     * */
    @RequestMapping(value = "/getLocationListByArea")
    @ResponseBody
	public List<Location> getLocationListByArea(HttpServletRequest request,HttpServletResponse response
            ,@RequestParam(value="areaId")  Integer areaId){
        Location location = new Location();
        location.setWareId(super.getCurrentWareId());
        location.setAreaId(areaId);
        location.setStatus(LocationStatus.A001.getValue());
        List<Location> locationList = locationService.selectByCriteria(location);
        return locationList;
    }
	@RequestMapping(value = "/listLocationGoodsBind")
	@ResponseBody
	public Map<String, Object> listWarehouse(HttpServletRequest request,HttpServletResponse response
											,@RequestParam(value="page")  Integer page
            						  		,@RequestParam(value="rows")  Integer rows ) {
		Map<String,Object> parmMap=new HashedMap();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Integer wareId=super.getCurrentWareId();
			parmMap.put("wareId",wareId);
			String goodsCode = request.getParameter("goodsCode");
			if(!StringUtil.isEmpty(goodsCode)){
				parmMap.put("goodsCode",goodsCode);
			}
			String validityTime = request.getParameter("validityTime");
			if(!StringUtil.isEmpty(validityTime)){
				SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
				Date parse = sdf.parse(validityTime);
				parmMap.put("validityTime",parse);
			}
			String createTime = request.getParameter("createTime");
			if(!StringUtil.isEmpty(createTime)){
				SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
				Date parse = sdf.parse(createTime);
				parmMap.put("createTime",parse);
			}
			String areaId = request.getParameter("areaId");
			if(!StringUtil.isEmpty(areaId)){
				parmMap.put("areaId",areaId);
			}
			String locationId = request.getParameter("locationId");
			if(!StringUtil.isEmpty(locationId)){
				String[] locationIds=locationId.split(",");
				parmMap.put("locationIds","("+locationId+")");
			}
			String goodCodes="";
			String goodsName = request.getParameter("goodsName");
			if(!StringUtil.isEmpty(goodsName)){
				Notification<List<GoodsSaleInfo>> notification= goodsBaseService.selectSkuInfoByName(goodsName, null);
				List<GoodsSaleInfo> goodsSaleInfoList=notification.getResponseData();
				for(GoodsSaleInfo goodsSaleInfo:goodsSaleInfoList){
					goodCodes+="'"+goodsSaleInfo.getCode()+"',";
				}
				if(!StringUtil.isEmpty(goodCodes)){
					goodCodes=goodCodes.substring(0,goodCodes.length()-1);
					parmMap.put("goodsCodes","("+goodCodes+")");
				}
			}
			PageBean<LocationGoodsBindVo> pageBean=locationGoodsBindService.selectByParmMapAdPage(page, rows,parmMap);
			List<LocationGoodsBindVo> pList = pageBean.getList();
			List<String> codesList=new ArrayList<>();
			for (LocationGoodsBindVo locationGoodsBindVo:pList){
				codesList.add(locationGoodsBindVo.getGoodsCode());
			}
			//获取商品详细信息，设置商品名称、销售单位
			List<LocationGoodsBindVo> locationGoodsBindVoList=new ArrayList<>();
			try {
				Notification<List<GoodsSaleInfoVo>> notification = goodsBaseService.selectGoodsSaleInfoVoByListCode(codesList);
				List<GoodsSaleInfoVo> goodsSaleInfoVoList = notification.getResponseData();
				LocationGoodsBindVo locationGoodsBindVo = null;
				for (LocationGoodsBindVo goodsBindVo : pList) {
					locationGoodsBindVo = new LocationGoodsBindVo();
					BeanUtils.copyProperties(locationGoodsBindVo, goodsBindVo);
					for (GoodsSaleInfoVo goodsSaleInfoVo : goodsSaleInfoVoList) {
						if (goodsBindVo.getGoodsCode().equals(goodsSaleInfoVo.getGoodsSaleInfo().getCode())) {
							locationGoodsBindVo.setGoodsName(goodsSaleInfoVo.getGoodsBase().getName());        //商品名称
							locationGoodsBindVo.setUnit(goodsSaleInfoVo.getGoodsSaleInfo().getSaleunit());    //销售单位
							locationGoodsBindVo.setPackageNum(goodsSaleInfoVo.getGoodsSaleInfo().getPacknum().toString());//包装数量
							break;
						}
					}
					locationGoodsBindVoList.add(locationGoodsBindVo);
				}
			}catch (Exception e){
				logger.info("库存列表查询调用商品服务异常！************",e);
			}

			map.put("total", pageBean.getTotal());
			map.put("rows", locationGoodsBindVoList.size()<=0?pList:locationGoodsBindVoList);
		} catch (Exception e) {
			logger.error("listWarehouse：库存列表查询异常！",e);
		}
		return map;
	}

	/**
	 * 生成盘点单查询货位列表
	 * @param request
	 * @param response
	 * @param session
	 * @param param
     * @return
     */
	@RequestMapping(value = "/searchBindsByCreateCheck")
	@ResponseBody
	public Object searchBindsByCreateCheck(HttpServletRequest request, HttpServletResponse response, HttpSession session,
										   @ModelAttribute SearchBindsParam param) {
		JSONObject json = new JSONObject();
		if (param != null && StringUtils.isNotEmpty(param.getAreaIds())) {
			param.setAreaIdList(JSON.parseArray(param.getAreaIds(), Integer.class));
		}
		param.setWareId(super.getCurrentWareId());
		try {
			List<LocationGoodsBind> locationGoodsBinds = locationGoodsBindService.searchBindsByCreateCheck(param);
			json.put("status", "200");
			json.put("message", "查询成功");
			json.put("result", locationGoodsBinds);
		} catch (WMSException e) {
			json.put("status", "-200");
			json.put("message", "查询失败");
			logger.error("查询异常", e);
		}
		return json;
	}

	@RequestMapping(value="/locationGoodsBindkExport",method = RequestMethod.POST)
	public void locationGoodsBindkExport(HttpServletRequest request,HttpServletResponse response){
		String columns=request.getParameter("columns");
		JSONArray columnJarr=JSONArray.fromObject(columns);
		Map<String, Object> parmaMap=(Map<String, Object>) getSessionOfShiro().getAttribute(Constant.OUTSTOCK_LISTWHERE);
		LocationGoodsBind locationGoodsBind = new LocationGoodsBind();
		List<LocationGoodsBind> list= locationGoodsBindService.selectByCriteria(locationGoodsBind);
		List<String> codesList=new ArrayList<>();
		for (LocationGoodsBind locationGoodsBindVo:list){
			codesList.add(locationGoodsBindVo.getGoodsCode());
		}
		//获取商品详细信息，设置商品名称、销售单位
		List<LocationGoodsBindVo> locationGoodsBindVoList=new ArrayList<>();
		try {
			Notification<List<GoodsSaleInfoVo>> notification = goodsBaseService.selectGoodsSaleInfoVoByListCode(codesList);
			List<GoodsSaleInfoVo> goodsSaleInfoVoList = notification.getResponseData();
			LocationGoodsBindVo locationGoodsBindVo = null;
			for (LocationGoodsBind goodsBindVo : list) {
				locationGoodsBindVo = new LocationGoodsBindVo();
				BeanUtils.copyProperties(locationGoodsBindVo, goodsBindVo);
				for (GoodsSaleInfoVo goodsSaleInfoVo : goodsSaleInfoVoList) {
					if (goodsBindVo.getGoodsCode().equals(goodsSaleInfoVo.getGoodsSaleInfo().getCode())) {
						locationGoodsBindVo.setGoodsName(goodsSaleInfoVo.getGoodsBase().getName());        //商品名称
						locationGoodsBindVo.setUnit(goodsSaleInfoVo.getGoodsSaleInfo().getSaleunit());    //销售单位
						break;
					}
				}
				locationGoodsBindVoList.add(locationGoodsBindVo);
			}
		}catch (Exception e){
			logger.info("locationGoodsBindkExport：库存列表查询调用商品服务异常！************",e);
		}
		JsonConfig config = new JsonConfig();

		config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
		//config.registerJsonValueProcessor(StockOutBill.class, new StockOutBillProcessor());
		JSONArray dataJsonArray=JSONArray.fromObject(locationGoodsBindVoList,config);
		JSONArray resultArray=new JSONArray();
		net.sf.json.JSONObject json=null;
		int size=dataJsonArray.size();
		for(int i=0;i<size;i++){
			json=dataJsonArray.getJSONObject(i);
			json.put("orderNum",i+1);
			resultArray.add(json);
		}
		try{
			ExcelCommon excelUtil = new ExcelCommon();
			File excel = excelUtil.dataToExcel(columnJarr, resultArray,"列表数据",request);
			DownloadUtils.download( request, response,  excel, "列表数据.xls");
		}catch(Exception e){
			logger.info("locationGoodsBindkExport：数据导出异常！************",e);
		}
	}
}