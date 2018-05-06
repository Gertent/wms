package com.rmd.wms.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.WarehouseArea;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.service.LocationService;
import com.rmd.wms.service.WarehouseAreaService;
import com.rmd.wms.service.WarehouseService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.constant.Constant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/warehouseArea")
public class WarehouseAreaController extends AbstractAjaxController{

	//日志管理器
	private static Logger logger = Logger.getLogger(WarehouseAreaController.class);
	@Resource(name = "warehouseService")
	private WarehouseService warehouseService; // 仓库管理
	
	@Resource(name = "warehouseAreaService")
	private WarehouseAreaService warehouseAreaService; // 库区管理
	
	@Resource(name = "locationService")
	private LocationService locationService; // 货位管理
	
	@RequestMapping(value = "/WarehouseArea")
	public ModelAndView jumpView() {
		Warehouse warehouse =new Warehouse();
		ModelAndView model = new ModelAndView("warehouseArea/warehouseAreaManage");
		Integer wareId=super.getCurrentWareId();
		warehouse.setId(wareId);
		List<Warehouse> warehouseList = warehouseService.selectByCriteria(warehouse);
		model.addObject("warehouseList", warehouseList);
		return model;
	}
	
	/**
	  * @Description:库区列表
	  * @param request
	  * @param response
	  * @param page
	  * @param rows
	  * @return
	  * 2017年3月13日 
	  */
	@RequestMapping(value = "/listWarehouse")
	@ResponseBody
	public Map<String, Object> listWarehouse(HttpServletRequest request,HttpServletResponse response
											,@RequestParam(value="page")  Integer page
            						  		,@RequestParam(value="rows")  Integer rows ) {
		WarehouseArea warehouseArea = new WarehouseArea();
		Map<String, Object> wareaHouseAreaMap = new HashMap<String, Object>();
		try {
			String code = request.getParameter("code");
			if(!StringUtil.isEmpty(code)){
				warehouseArea.setCode(code);
			}
			String areaName = request.getParameter("areaName");
			if(!StringUtil.isEmpty(areaName)){
				warehouseArea.setAreaName(areaName);
			}
			String status = request.getParameter("status");
			if(!StringUtil.isEmpty(status)){
				warehouseArea.setStatus(Integer.valueOf(status));
			}
			String type = request.getParameter("type");
			if(!StringUtil.isEmpty(type)){
				warehouseArea.setType(Integer.valueOf(type));
			}
			String wareId = request.getParameter("wareId");
			if(!StringUtil.isEmpty(wareId)){
				warehouseArea.setWareId(Integer.valueOf(wareId));
			}else{
				wareId=super.getCurrentWareId().toString();
				warehouseArea.setWareId(Integer.valueOf(wareId));
			}
			PageBean<WarehouseArea> pageBean=warehouseAreaService.listWarehouseArea(page, rows,warehouseArea);
			List<WarehouseArea> pList = pageBean.getList();
			wareaHouseAreaMap.put("total", pageBean.getTotal());
			wareaHouseAreaMap.put("rows", pList);
		} catch (Exception e) {
			logger.error("listWarehouse：库区列表查询异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return wareaHouseAreaMap;
	}

	/**
	 * 查询该仓库下的所有库区
	 * @param request
	 * @param response
	 * @param session
     * @return
     */
	@RequestMapping(value = "/getAllWarehouseArea")
	@ResponseBody
	public Object getAllWarehouseArea(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		JSONObject json = new JSONObject();
		Integer wareId = super.getCurrentWareId();
		WarehouseArea area = new WarehouseArea();
		area.setWareId(wareId);
		try {
			List<WarehouseArea> areas = warehouseAreaService.selectByCriteria(area);
			json.put("status", "200");
			json.put("message", "查询成功");
			json.put("result", areas);
		} catch (Exception e) {
			json.put("status", "-200");
			json.put("message", "查询失败");
			logger.error("查询库区失败"+ com.rmd.wms.constant.Constant.LINE, e);
		}
		return json;
	}


	/**
	  * @Description:启用 和 禁用
	  * @param request
	  * @param response
	  * @param ids
	  * @param status
	  * 2017年3月13日 
	 * @return 
	  */
	@RequestMapping(value = "/updateStatusById")
	@ResponseBody
	public String updateStatusById(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("ids") String ids,@ModelAttribute("status") Integer status){
		if (StringUtils.isBlank(ids) || status == null) {
			return "false";
		}
		int result = warehouseAreaService.updateByStatus(ids, status, super.getCurrentWareId());
		try {
			if(result > 0){
				return "true";
			}
			if(result < 0){
				return "updateNo";
			}
		} catch (Exception e) {
			logger.error("updateStatusById：根据id修改库区状态异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return "false";
	}
	
	/**
	  * @Description:添加库区弹出框
	  * @param request
	  * @param response
	  * @return
	  * 2017年3月13日 
	  */
	@RequestMapping(value = "/warehouseAreaAddPage")
	public ModelAndView warehouseAreaAddPage(HttpServletRequest request,HttpServletResponse response) {
		Warehouse warehouse =new Warehouse();
		ModelAndView model = new ModelAndView("warehouseArea/warehouseAreaAdd");
		warehouse.setId(super.getCurrentWareId());
		List<Warehouse> warehouseList = warehouseService.selectByCriteria(warehouse);
		model.addObject("warehouseList", warehouseList);
		return model;
	}
	
	/**
	  * @Description:添加库区
	  * @param request
	  * @param response
	  * 2017年3月13日
	 * @return 
	  */
	@RequestMapping("/addWarehouseArea")
	@ResponseBody
	public String addWarehouseArea(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("warehouseArea") WarehouseArea warehouseArea){
		try {
			/**验证库区名称和编号是否重复*/
			int resultCode=warehouseAreaService.selectByCode(warehouseArea);
			int resultName=warehouseAreaService.selectByName(warehouseArea);
			if(resultCode>0){
				return "warehouseAreaCodeFalse";
			}else if(resultName>0){
				return "warehouseAreaNameFalse";
			}else{
				warehouseArea.setCreateTime(new Date());
				if(warehouseArea.getStatus() == null){
					warehouseArea.setStatus(Constant.STATUS_ONE);
				}
				warehouseAreaService.insertSelective(warehouseArea);
				return "true";
			}
		} catch (Exception e) {
			logger.error("addWarehouseArea：添加库区异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return "false";
	}
	
	/**
	  * @Description:编辑库区弹窗
	  * @param request
	  * @param response
	  * @param id
	  * @return
	  * 2017年3月13日 
	  */
	@RequestMapping("/wareAreaEditPage")
	public ModelAndView wareEditPage(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("id")Integer id){
		Warehouse warehouse =new Warehouse();
		ModelAndView model = new ModelAndView("warehouseArea/warehouseAreaEdit");
		WarehouseArea warehouseArea = warehouseAreaService.selectByPrimaryKey(id);
		warehouse.setId(super.getCurrentWareId());
		List<Warehouse> warehouseList = warehouseService.selectByCriteria(warehouse);
		model.addObject("warehouseList", warehouseList);
		model.addObject("warehouseArea", warehouseArea);
		return model;
	}
	
	
	
	/**
	  * @Description:编辑库区
	  * @param request
	  * @param response
	  * 2017年3月13日
	 * @return 
	  */
	@RequestMapping("/editWarehouseArea")
	@ResponseBody
	public String editWarehouseArea(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("warehouseArea") WarehouseArea warehouseArea){
		try {
			/**查询仓库名称是否重复*/
			int resultCode=warehouseAreaService.selectByCode(warehouseArea);
			int resultName=warehouseAreaService.selectByName(warehouseArea);
			if(resultCode>0){
				return "warehouseAreaCodeFalse";
			}
			if(resultName>0){
				return "warehouseAreaNameFalse";
			}
			int updateResult = warehouseAreaService.updateByPrimaryKeySelective(warehouseArea);
			if(updateResult ==1){
				return "warehouseAreaType";
			}
			if(updateResult ==2){
				return "true";
			}
		} catch (Exception e) {
			logger.error("editWarehouseArea：库区编辑异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return "false";
	}	
}
