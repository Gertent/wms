package com.rmd.wms.web.controller;

import com.github.pagehelper.StringUtil;
import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.PurchaseBill;
import com.rmd.wms.bean.PurchaseInInfo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.enums.SortType;
import com.rmd.wms.service.*;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.util.DateJsonValueProcessor;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
* @ClassName: PurchaseBillController 
* @Description: TODO(采购单) 
* @author ZXLEI
* @date Mar 17, 2017 9:19:18 AM 
*
 */
@Controller
@RequestMapping(value = "/purchase")
public class PurchaseBillController extends AbstractAjaxController {

	private static Logger logger = Logger.getLogger(PurchaseBillController.class);
	@Resource(name = "purchaseBillService")
	private PurchaseBillService purchaseBillService; // 采购单

	@Resource(name = "purchaseInInfoService")
	private PurchaseInInfoService purchaseInInfoService; // 采购单，出库单，上架单商品详情

	@Resource(name = "inStockBillService")
	private InStockBillService inStockBillService; // 入库单
	/**
	 * 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/PurchaseBiLL")
	public ModelAndView jumpView() {
		ModelAndView mv = new ModelAndView("purchase/purchaseManage");
		return mv;
	}

	/**
	 * 查询所有采购单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ListPurchaseBiLL")
	@ResponseBody
	public Map<String, Object> listBiLL(@RequestParam(value="page")  Integer page
			                           ,@RequestParam(value="rows")  Integer rows 
			                           ,@RequestParam(value="purchaseNo",required=false)  String purchaseNo
			                           ,@RequestParam(value="inDbData",required=false)  String inDbData
										,@RequestParam(value="inDbDataEnd",required=false)  String inDbDataEnd
			                           ,@RequestParam(value="status",required=false)  Integer status
			                           ,@RequestParam(value="supplierName",required=false)  String supplierName
			                           ,@RequestParam(value="inStockNo",required=false)  String inStockNo,HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> whereParams = new HashMap<String, Object>();
			Integer wareId=super.getCurrentWareId();
			whereParams.put("wareId",wareId);
			if(StringUtil.isNotEmpty(purchaseNo)){		//采购单号
				whereParams.put("purchaseNo", purchaseNo);
			}
			if(StringUtil.isNotEmpty(inDbData)){		//计划入库日期开始时间
				whereParams.put("inDbData_gt", inDbData);
			}
			if(StringUtil.isNotEmpty(inDbDataEnd)){		//计划入库日期结束时间
				whereParams.put("inDbData_lt", inDbDataEnd);
			}
			if(status!=null&&status>0){					//入库状态
				whereParams.put("status", status);
			}
			if(StringUtil.isNotEmpty(supplierName)){	//供应商
				whereParams.put("supplierName", supplierName);
			}
			if(StringUtil.isNotEmpty(inStockNo)){		//入库单号
				whereParams.put("inStockNo", inStockNo);
				Map<String, Object> parmaMap=new HashMap<>();
				parmaMap.put("inStockNo", inStockNo);
				List<InStockBill> list=inStockBillService.ListInStockBills(parmaMap);
				if(list.size()>0){
					StringBuilder sb=new StringBuilder("(");
					for(InStockBill i:list){
						sb.append("'").append(i.getPurchaseNo()).append("',");
					}
					String str=sb.toString();
					str=str.substring(0,str.length()-1)+")";
					whereParams.put("purchaseNo_in", str);
				}
			}
			whereParams.put("name_sort","purchase_no");
			whereParams.put("order_sort", SortType.DESC.getValue());
			PageBean<PurchaseBill> pageBean=purchaseBillService.ListPurchaseBills(page, rows, whereParams);
			List<PurchaseBill> pList = pageBean.getList();
			map.put("total", pageBean.getTotal());
			map.put("rows", pList);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
			JSONObject json = JSONObject.fromObject(map, config);
			// 格式化时间后填入返回结果
			map.put("datarow", json);
		} catch (Exception e) {
			logger.error("ListPurchaseBiLL：采购单查询异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return map;
	}

	/**
	 * 查询采购单下的商品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/PurchaseBiLLInfo")
	public ModelAndView PurchaseBiLLInfo(@RequestParam(value = "Id", required = false) Integer Id,HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("purchase/purchaseInfo");
		PurchaseBill purchaseBill = new PurchaseBill();
		Map<String, Object> whereParams = new HashMap<String, Object>();// 动态条件
		List<PurchaseInInfo> pInInfos = new ArrayList<PurchaseInInfo>(10);
		Integer wareId=super.getCurrentWareId();
		if (Id>0) {
			purchaseBill = purchaseBillService.selectByPrimaryKey(Id);
		}
		if (!purchaseBill.equals(null)) {
			whereParams.put("purchaseNo", purchaseBill.getPurchaseNo());
			whereParams.put("wareId",wareId);
			pInInfos = purchaseInInfoService.ListPurchaseInInfo(whereParams);
		}
		// 采购单基本信息
		mv.addObject("purchaseBill", purchaseBill);
		// 采购单下商品
		mv.addObject("pInInfos", pInInfos);
		int purchaseNum=0;
		int inStockNum=0;
		BigDecimal totalPrice=BigDecimal.ZERO;
		for(PurchaseInInfo o:pInInfos){
			purchaseNum+=o.getPurchaseNum();
			totalPrice=totalPrice.add(new BigDecimal(o.getPurchaseNum()).multiply(o.getPurchasePrice()));
		}
		mv.addObject("purchaseNum", purchaseNum);
		mv.addObject("totalPrice", totalPrice);
		return mv;
	}
}
