package com.rmd.wms.web.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.StringUtil;
import com.rmd.wms.bean.LocationGoodsBindIn;
import com.rmd.wms.bean.vo.web.MovementLocation;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.enums.SortType;
import com.rmd.wms.service.LocationGoodsBindInService;
import com.rmd.wms.service.MovementService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.constant.Constant;
import com.rmd.wms.web.util.DateJsonValueProcessor;
import com.rmd.wms.web.util.DownloadUtils;
import com.rmd.wms.web.util.ExcelCommon;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * 
* @ClassName: MoveMentController 
* @Description: TODO(移库交接单Controller) 
* @author ZXLEI
* @date Mar 20, 2017 11:36:28 AM 
*
 */
@Controller
@RequestMapping(value = "/movement")
public class MoveMentController extends AbstractAjaxController {

	private Logger logger = Logger.getLogger(MoveMentController.class);

	@Resource(name = "movementService")
	// 出库单
	private MovementService movementService;
	
	@Resource(name = "locationGoodsBindInService")
	private LocationGoodsBindInService locationGoodsBindInService; 

	/**
	 * 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Movement")
	public ModelAndView jumpView(@RequestParam(value = "view") Integer view) {
		ModelAndView mv = new ModelAndView();
		String viewName = "";
		if (1 == view) {
			viewName = "movement/brokenManage";
		}else if(2==view){
			viewName = "movement/moveManage";
		}
		mv.setViewName(viewName);
		return mv;
	}

	/**
	 * 查询所有发货单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ListMovement")
	@ResponseBody
	public Map<String, Object> listMovement(@RequestParam(value="page",required=false) Integer page
										   ,@RequestParam(value = "flag",required=false) Integer flag
											    ,@RequestParam(value = "locationNoOut",required=false) String locationNoOut
											   ,@RequestParam(value = "locationNoIn",required=false) String locationNoIn
											   ,@RequestParam(value = "moveOutTimeStart",required=false) String moveOutTimeStart
											   ,@RequestParam(value = "moveOutTimeEnd",required=false) String moveOutTimeEnd
									           ,@RequestParam(value = "outUserName", required = false) String  outUserName
									           ,@RequestParam(value = "inUserName", required = false) String  inUserName
									           ,@RequestParam(value = "goodsCode", required = false) String  goodsCode
									           ,@RequestParam(value = "moveInTimeStart", required = false) String moveInTimeStart
									           ,@RequestParam(value = "moveInTimeEnd", required = false) String moveInTimeEnd
									           ,@RequestParam(value = "status", required = false) String status
			                                   ,@RequestParam(value="rows", required = false) Integer rows,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> whereParams = new HashMap<String, Object>();

			Integer wareId=super.getCurrentWareId();
			whereParams.put("wareId", wareId);
			if (1 == flag) { // 报损
				whereParams.put("wareGoodBad_search", Constant.SEARCH_FLAG);
			} else if (2 == flag) { // 移库 whereParams.put("type", 3);
				whereParams.put("wareGoodMove_search", Constant.SEARCH_FLAG);
			}
			if(StringUtil.isNotEmpty(locationNoOut)){
				whereParams.put("locationNoOut", locationNoOut);//移出货位号
			}
			if(StringUtil.isNotEmpty(locationNoIn)){
				whereParams.put("locationNoIn", locationNoIn);//移入货位号
			}
			if(StringUtil.isNotEmpty(goodsCode)){
				whereParams.put("goodsCode", goodsCode);//商品编码
			}
			if(StringUtil.isNotEmpty(outUserName)){
				whereParams.put("outUserName", outUserName);//移出人员
			}
			if(StringUtil.isNotEmpty(inUserName)){
				whereParams.put("inUserName", inUserName);//移入人员
			}
			//移库状态
			if (StringUtil.isNotEmpty(status)&&Integer.valueOf(status)>0) {
				whereParams.put("status", status);
			}
			//移出时间
			if (StringUtil.isNotEmpty(moveOutTimeStart)) {
				whereParams.put("moveOutTime_gt", moveOutTimeStart);
			}
			if (StringUtil.isNotEmpty(moveOutTimeEnd)) {
				whereParams.put("moveOutTime_lt", moveOutTimeEnd);
			}
			//移入时间
			if (StringUtil.isNotEmpty(moveInTimeStart)) {
				whereParams.put("moveInTime_gt", moveInTimeStart);
			}
			if (StringUtil.isNotEmpty(moveInTimeEnd)) {
				whereParams.put("moveInTime_lt", moveInTimeEnd);
			}
			whereParams.put("name_sort","move_out_time");
			whereParams.put("order_sort", SortType.DESC.getValue());
			PageBean<MovementLocation> pageBean = movementService
					.ListMovementBillInfo(page, rows, whereParams);
			List<MovementLocation> mList = pageBean.getList();
			map.put("total", pageBean.getTotal());
			map.put("rows", mList);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
			JSONObject json = JSONObject.fromObject(map, config);
			// 格式化时间后填入返回结果
			map.put("datarow", json);
			ObjectMapper om = new ObjectMapper();
			try {
			//json字符串时500 Internal Server Error的处理方案
				om.writeValueAsString(json);
			} catch (JsonGenerationException | JsonMappingException e) {
				logger.error("object转json字符串异常", e);
			}
		} catch (Exception e) {
			logger.error("ListMovement：发货单查询异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return map;
	}

	/**
	 * 查询所有发货单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ListGoodsBindIn")
	@ResponseBody
	public Map<String, Object> listGoodsBindIn(@RequestParam(value="ginfoId", required = false) String ginfoId,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<LocationGoodsBindIn> mList = new LinkedList<>();
			if(StringUtil.isNotEmpty(ginfoId)){
				Map<String, Object> whereParams = new HashMap<String, Object>();
				whereParams.put("ginfoId", ginfoId);
				whereParams.put("wareId",super.getCurrentWareId());
				whereParams.put("name_sort","location_no");
				whereParams.put("order_sort", SortType.DESC.getValue());
				mList = locationGoodsBindInService.listLocationGoodsBindIns(whereParams);
			}
			map.put("rows", mList);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd", "Date"));
			JSONObject json = JSONObject.fromObject(map, config);
			// 格式化时间后填入返回结果
			map.put("datarow", json);
			ObjectMapper om = new ObjectMapper();
			try {
			//json字符串时500 Internal Server Error的处理方案
				om.writeValueAsString(json);
			} catch (JsonGenerationException | JsonMappingException e) {
				logger.error("object转json字符串异常", e);
			}
		} catch (Exception e) {
			logger.error("ListGoodsBindIn：发货单查询异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return map;
	}

	@RequestMapping(value="/moveMentExport",method = RequestMethod.POST)
	public void outStockBillExport(HttpServletRequest request,HttpServletResponse response){
		String columns=request.getParameter("columns");
		String flag=request.getParameter("flag");
		String name="";
		JSONArray columnJarr=JSONArray.fromObject(columns);
		Map<String, Object> parmaMap=new HashedMap();// getSessionOfShiro().getAttribute(Constant.OUTSTOCK_LISTWHERE);
		parmaMap.put("wareId",getCurrentWare().getId());
		if("1".equals(flag)){
			parmaMap.put("wareGoodBad_search", Constant.SEARCH_FLAG);
			name="报损列表数据";
		}else if("2".equals(flag)){
			parmaMap.put("wareGoodMove_search", Constant.SEARCH_FLAG);
			name="移库列表数据";
		}
		List<Map<String,Object>> list= movementService.listAllByParmMap(parmaMap);
		JsonConfig config = new JsonConfig();

		config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
		JSONArray dataJsonArray=JSONArray.fromObject(list,config);
		JSONArray resultArray=new JSONArray();
		JSONObject json=null;
		int size=dataJsonArray.size();
		for(int i=0;i<size;i++){
			json=dataJsonArray.getJSONObject(i);
			json.put("orderNum",i+1);
			resultArray.add(json);
		}
		try{
			ExcelCommon excelUtil = new ExcelCommon();
			File excel = excelUtil.dataToExcel(columnJarr, resultArray,name,request);
			DownloadUtils.download( request, response,  excel, name+".xls");
		}catch(Exception e){
			logger.error("moveMentExport：数据导出失败！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
	}

}
