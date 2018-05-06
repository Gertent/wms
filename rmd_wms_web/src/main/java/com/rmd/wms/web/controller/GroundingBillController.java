package com.rmd.wms.web.controller;

import com.github.pagehelper.StringUtil;
import com.rmd.wms.bean.GroundingBill;
import com.rmd.wms.bean.PurchaseInInfo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.enums.GroundingBillStatus;
import com.rmd.wms.enums.SortType;
import com.rmd.wms.service.GroundingBillService;
import com.rmd.wms.service.PurchaseInInfoService;
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
import java.util.*;

/**
 * 
* @ClassName: GroundingBillController 
* @Description: TODO(上架单) 
* @author ZXLEI
* @date Mar 17, 2017 9:17:54 AM 
*
 */
@Controller
@RequestMapping(value = "/grounding")
public class GroundingBillController extends AbstractAjaxController{

	private Logger logger = Logger.getLogger(GroundingBillController.class);

	@Resource(name = "groundingBillService")
	private GroundingBillService groundingBillService; // 上架单

	@Resource(name = "purchaseInInfoService")
	private PurchaseInInfoService purchaseInInfoService; // 采购单，出库单，上架单商品详情

	/**
	 * 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/GroundingBiLL")
	public ModelAndView jumpView() {
		ModelAndView mv = new ModelAndView("grounding/groundingManage");
		return mv;
	}

	/**
	 * 查询上架采购单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ListGroundingBiLL")
	@ResponseBody
	public Map<String, Object> listBiLL(@RequestParam(value = "page",required=false) Integer page
			   						   ,@RequestParam(value = "inStockNo",required=false) String inStockNo
			   						   ,@RequestParam(value = "status",required=false) Integer status
			   						   ,@RequestParam(value = "createTime",required=false) String createTime
									   ,@RequestParam(value = "starTime",required=false) String starTime
									   ,@RequestParam(value = "endTime",required=false) String endTime
			                           ,@RequestParam(value = "rows",required=false) Integer rows
			                           ,@RequestParam(value = "twostatus",required=false) Integer twostatus,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> whereParams = new HashMap<String, Object>();
			Integer wareId=super.getCurrentWareId();
			whereParams.put("wareId",wareId);
			if (StringUtil.isNotEmpty(inStockNo)) {
				whereParams.put("inStockNo", inStockNo);
			}
			if (status!=null&&status > 0) {
				whereParams.put("status", status);
			}
			// 入库时间
			if (StringUtil.isNotEmpty(createTime)) {
				whereParams.put("createTime", createTime);
			}
			// 上架时间大于指定时间
			if (StringUtil.isNotEmpty(starTime)) {
				whereParams.put("endTime_gt", Constant.SEARCH_FLAG);
			}
			// 上架时间小于指定时间
			if (StringUtil.isNotEmpty(endTime)) {
				whereParams.put("endTime_lt", Constant.SEARCH_FLAG);
			}
			// 上架状态
			if (twostatus!=null&&twostatus > 0) {
				whereParams.put("twostatus_search", Constant.SEARCH_FLAG);
			}
			whereParams.put("name_sort","in_stock_no");
			whereParams.put("order_sort", SortType.DESC.getValue());
			PageBean<GroundingBill> pageBean = groundingBillService
					.ListGroundingBills(page, rows, whereParams);
			List<GroundingBill> pList = pageBean.getList();
			map.put("total", pageBean.getTotal());
			map.put("rows", pList);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
			JSONObject json = JSONObject.fromObject(map, config);
			// 格式化时间后填入返回结果
			map.put("datarow", json);
		} catch (Exception e) {
			logger.error("ListGroundingBiLL：上架采购单查询异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return map;
	}

	/**
	 * 查询采购单下的商品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/GroundingBiLLInfo")
	public ModelAndView groundingBiLLInfo(@RequestParam(value = "Id", required = false) Integer Id,HttpServletRequest request) {
		Integer wareId=(Integer) request.getSession().getAttribute("wareId");
		ModelAndView mv = new ModelAndView("grounding/groundingInfo");
		GroundingBill groundingBill = new GroundingBill(); // 上架单
		Map<String, Object> whereParams = new HashMap<String, Object>();// 动态条件
		List<PurchaseInInfo> pInInfos = new ArrayList<PurchaseInInfo>(10); // 订单详情
        List<Map> mapList=new ArrayList<>();
		try {
			if (Id > 0) {
				groundingBill = groundingBillService.selectByPrimaryKey(Id);
			}
			if (!groundingBill.equals(null)) {
				whereParams.put("inStockNo", groundingBill.getInStockNo());
				whereParams.put("wareId", wareId);
                mapList=purchaseInInfoService.ListAllByParmMap(whereParams);
			}

			// 上架单基本信息
			mv.addObject("groundingbill", groundingBill);
			// 入库单下商品
			mv.addObject("pInInfos", mapList);
			mv.addObject("goodcount", pInInfos.size());
		} catch (Exception e) {
			logger.error("GroundingBiLLInfo：查询采购单下的商品列表异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}

		return mv;
	}

	@RequestMapping(value="/groundingBillExport",method = RequestMethod.POST)
	public void inStockExport(HttpServletRequest request,HttpServletResponse response){
		String columns=request.getParameter("columns");
		JSONArray columnJarr=JSONArray.fromObject(columns);
		Map<String, Object> parmaMap=new HashedMap();
		parmaMap.put("wareId",getCurrentWare().getId());
		List<Map<String,Object>> list= groundingBillService.listAllByParmMap(parmaMap);
		JsonConfig config = new JsonConfig();

		config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
		JSONArray dataJsonArray=JSONArray.fromObject(list,config);
		JSONArray resultArray=new JSONArray();
		JSONObject json=null;
		int size=dataJsonArray.size();
		for(int i=0;i<size;i++){
			json=dataJsonArray.getJSONObject(i);
			json.put("orderNum",i+1);
			if(GroundingBillStatus.A003.getValue().equals(json.getString("status"))){
				json.put("status", GroundingBillStatus.A003.getInfo());
			}else if(GroundingBillStatus.A002.getValue().equals(json.getString("status"))){
				json.put("status", GroundingBillStatus.A002.getInfo());
			}else{
				json.put("status", GroundingBillStatus.A001.getInfo());
			}
			resultArray.add(json);
		}
		try{
			ExcelCommon excelUtil = new ExcelCommon();
			File excel = excelUtil.dataToExcel(columnJarr, resultArray,"上架单列表数据",request);
			DownloadUtils.download( request, response,  excel, "上架单列表数据.xls");
		}catch(Exception e){
			logger.error("groundingBillExport：数据导出异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
	}

}
