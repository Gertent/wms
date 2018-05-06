package com.rmd.wms.web.controller;

import com.rmd.bms.entity.Vo.UserRoleVo;
import com.rmd.bms.service.BmsApiService;
import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.WareDeliverRange;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.WarehouseUser;
import com.rmd.wms.bean.vo.WareUserRoleVo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.service.WareDeliverRangeService;
import com.rmd.wms.service.WarehouseService;
import com.rmd.wms.service.WarehouseUserService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.common.WebMessageCode;
import com.rmd.wms.web.constant.Constant;
import com.rmd.wms.web.util.PropertiesLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/warehouse")
public class WarehouseController extends AbstractAjaxController{

	//日志管理器
	private static Logger logger = Logger.getLogger(WarehouseController.class);

	@Resource(name = "warehouseService")
	private WarehouseService warehouseService; // 仓库管理

	@Resource(name = "warehouseUserService")
	private WarehouseUserService warehouseUserService; //员工和仓库

	@Resource(name="wareDeliverRangeService")
	private WareDeliverRangeService wareDeliverRangeService;

	@Resource(name = "bmsApiService")
	private BmsApiService bmsApiService;

	protected static PropertiesLoader propertiesLoader =
            new PropertiesLoader("classpath:/shiro.properties");
	/**
	  * @Description:页面跳转
	  * @return
	  * 2017年3月13日
	  */
	@RequestMapping(value = "/Warehouse")
	public ModelAndView jumpView() {
		ModelAndView model = new ModelAndView("warehouse/warehouseManage");
		return model;
	}

	/**
	  * @Description:查询仓库列表信息
	  * @param page
	  * @param rows
	  * @return
	  * 2017年3月13日
	  */
	@RequestMapping(value = "/ListWarehouse")
	@ResponseBody
	public Map<String, Object> listWarehouse(HttpServletRequest request,HttpServletResponse response
											,@RequestParam(value="page")  Integer page
            						  		,@RequestParam(value="rows")  Integer rows ) {
		Map<String, Object> wareaHouseMap = new HashMap<String, Object>();
		try {
			Warehouse warehouse = new Warehouse();
			PageBean<Warehouse> pageBean=warehouseService.listWarehouse(page, rows,warehouse);
			List<Warehouse> pList = pageBean.getList();
			wareaHouseMap.put("total", pageBean.getTotal());
			wareaHouseMap.put("rows", pList);
		} catch (Exception e) {
			logger.error("ListWarehouse：仓库列表查询异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return wareaHouseMap;
	}

	/**
	  * @Description:禁用   启用
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
		try {
			int result = warehouseService.updateByStatus(ids ,status);
			if(result > 0){
				return "true";
			}
			if(result<0){
				return "updateNo";
			}
		} catch (Exception e) {
			logger.error("updateStatusById：根据id修改仓库状态异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return "false";
	}

	/**
	 * @Description: 添加网站信息跳转
	 * @author lc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/warehouseAddPage")
	public ModelAndView warehouseAddPage(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView model = new ModelAndView("warehouse/warehouseAdd");
		return model;
	}



	/**
	  * @Description:添加仓库信息
	  * @param request
	  * @param response
	  * 2017年3月13日
	 * @return
	  */
	@RequestMapping("/AddWarehouse")
	@ResponseBody
	public String AddWarehouse(HttpServletRequest request,HttpServletResponse response, @ModelAttribute("warehouse") Warehouse warehouse){
		try {
			/**验证编码和仓库名称*/
			String areaCode=request.getParameter("areaCode");
			String areaName=request.getParameter("areaName");
			int resultName=warehouseService.selectByName(warehouse);
			int	resultCode=warehouseService.selectByCode(warehouse);
			if(resultCode>0){
				return "warehouseCodeFalse";
			}else if(resultName>0){
				return "warehouseNameFalse";
			}else{
				warehouse.setCreateTime(new Date());
				if(warehouse.getStatus() != null){
					warehouse.setStatus(Constant.STATUS_ONE);
				}
				warehouseService.insert(warehouse,areaCode,areaName);

				return "true";
			}
		} catch (Exception e) {
			logger.error("AddWarehouse：添加仓库异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return "false";
	}

	/**
	  * @Description:跳转到编辑页
	  * @param request
	  * @param response
	  * @param id
	  * @return
	  * 2017年3月13日
	  */
	@RequestMapping("/wareEditPage")
	public ModelAndView wareEditPage(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("id")Integer id){
		ModelAndView model = new ModelAndView("warehouse/warehouseEdit");
		Warehouse warehouse = warehouseService.selectByPrimaryKey(id);
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("wareId", id);
		List<WareDeliverRange> list=wareDeliverRangeService.selectAllByWhere(paramMap);
		StringBuilder sb=new StringBuilder();
		for(WareDeliverRange d:list){
			sb.append(d.getProvCode()).append(",");
		}
		model.addObject("warehouse", warehouse);
		model.addObject("areaCode",sb.toString());
		return model;
	}


	/**
	  * @Description:编辑
	  * @param request
	  * @param response
	  * 2017年3月13日
	 * @return
	  */
	@RequestMapping("/editWarehouse")
	@ResponseBody
	public String editWarehouse(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("warehouse") Warehouse warehouse){
		try {
			/**验证编码和仓库名称*/
			String areaCode=request.getParameter("areaCode");
			String areaName=request.getParameter("areaName");
			int	resultCode=warehouseService.selectByCode(warehouse);
			int resultName=warehouseService.selectByName(warehouse);
			if(resultCode>0){
				return "warehouseCodeFalse";
			}else if(resultName>0){
				return "warehouseNameFalse";
			}else{
				warehouseService.updateByPrimaryKeySelective(warehouse,areaCode,areaName);
				return "true";
			}
		} catch (Exception e) {
			logger.error("editWarehouse：编辑仓库异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return "false";
	}

	/**
	  * @Description:分配员工弹出框
	  * @param request
	  * @param response
	  * 2017年3月17日
	 * @return
	  */
	@RequestMapping("/allocationWarehouse")
	public ModelAndView allocationWarehouse(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("id")String id){

		ModelAndView model = new ModelAndView("warehouse/allocationInfo");
		model.addObject("id", id);
		return model;
	}
	/**
	 * @Description 员工列表
	 * @param request
	 * @param response
	 * @param id 仓库id
	 * @return
	 * */
	@RequestMapping("/listAllocationWarehouse")
	@ResponseBody
	public List<WareUserRoleVo> listAllocationWarehouse(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("id")String id){
		String appNo=propertiesLoader.getProperty("shiro.client.id");
		//该系统角色下人员
		Notification<List<UserRoleVo>> notification = bmsApiService.selectUserandRoleBySystemId(appNo);
		List<UserRoleVo> list=notification.getResponseData();

		List<WarehouseUser> wUsers= warehouseUserService.selectByWareId(Integer.parseInt(id));
		List<WareUserRoleVo> noWareList=new ArrayList<>();
		List<WareUserRoleVo> wareList=new LinkedList<>();
		//判断是否为仓库下人员
		boolean flag=false;
		WareUserRoleVo wu=null;
		for(UserRoleVo u:list){
			wu=new WareUserRoleVo();
			 BeanUtils.copyProperties(u, wu);
			flag=false;
			for(WarehouseUser w:wUsers){
				if(wu.getId().equals(w.getUserId())){
					flag=true;
					break;
				}
			}
			if(flag){
				wu.setIsWareUser(1);
				wareList.add(wu);
			}else{
				wu.setIsWareUser(0);
				noWareList.add(wu);
			}
		}
		wareList.addAll(noWareList);
		return wareList;
	}
	/**
	  * @Description:分配员工信息
	  * @param request
	  * @param response
	  * @param ids
	  * @return
	  * 2017年3月20日
	  */
	@RequestMapping("allocationWarehouseInfo")
	@ResponseBody
	public void allocationWarehouseInfo(HttpServletRequest request,HttpServletResponse response,
										  @RequestParam("ids")String ids,
										  @RequestParam("wareId")String wareId){
		int result = 0;
		try {
			String[] arr = ids.split(",");
			List<WarehouseUser> list=new ArrayList<>();
			WarehouseUser warehouseUser=null;
			for (String string : arr) {
				warehouseUser = new WarehouseUser();
				warehouseUser.setWareId(Integer.valueOf(wareId));
				warehouseUser.setUserId(Integer.valueOf(string));
				list.add(warehouseUser);
			}
			if(arr.length>0){
				result=warehouseUserService.insertBatchSelective(Integer.parseInt(wareId),list);
			}
			if(result>0){
				writeOutWebMessage(WebMessageCode.OPERATE_SUCESS, request, response);
			}else{
				writeOutWebMessage(WebMessageCode.OPERATE_FAILED, request, response);
			}
		} catch (Exception e) {
			logger.error("ListWarehouse：员工分配异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
	}
}
