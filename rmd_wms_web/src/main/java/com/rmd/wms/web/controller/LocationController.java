package com.rmd.wms.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.rmd.wms.bean.Location;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.WarehouseArea;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.LocationService;
import com.rmd.wms.service.WarehouseAreaService;
import com.rmd.wms.service.WarehouseService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.common.ResponseUtils;
import com.rmd.wms.web.common.WebMessageCode;
import com.rmd.wms.web.util.ExcelCommon;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
@Controller
@RequestMapping(value = "/location")
public class LocationController extends AbstractAjaxController{
	//日志管理器
	private static Logger logger = Logger.getLogger(LocationController.class);
	@Resource(name = "locationService")
	private LocationService locationService; // 货位管理
	
	@Resource(name = "warehouseService")
	private WarehouseService warehouseService; // 仓库管理
	
	@Resource(name = "warehouseAreaService")
	private WarehouseAreaService warehouseAreaService; // 库区管理
	
	/**
	  * @Description:跳转货位页面
	  * @return
	  * 2017年3月14日 
	  */
	@RequestMapping(value = "/Location")
	public ModelAndView jumpView() {
		ModelAndView model = new ModelAndView("location/locationManage");
		Warehouse warehouse =new Warehouse();
		Integer wareId=super.getCurrentWareId();
		warehouse.setId(wareId);
		List<Warehouse> warehouseList = warehouseService.selectByCriteria(warehouse);
		model.addObject("warehouseList", warehouseList);
		return model;
	}
	/**
	  * @Description:货位列表
	  * @param request
	  * @param response
	  * @param page
	  * @param rows
	  * @return
	  * 2017年3月14日 
	  */
	@RequestMapping(value = "/listLocation")
	@ResponseBody
	public Map<String, Object> listWarehouse(HttpServletRequest request,HttpServletResponse response
											,@RequestParam(value="page")  Integer page
            						  		,@RequestParam(value="rows")  Integer rows ) {
		Location location = new Location();
		Map<String, Object> locationMap = new HashMap<String, Object>();
		try {
			String code = request.getParameter("code");
			if(!StringUtil.isEmpty(code)){
				location.setCode(code);
			}
			String locationNo = request.getParameter("locationNo");
			if(!StringUtil.isEmpty(locationNo)){
				location.setLocationNo(locationNo);
			}
			String areaName = request.getParameter("areaName");
			if(!StringUtil.isEmpty(areaName)){
				location.setAreaName(areaName);
			}
			String status = request.getParameter("status");
			if(!StringUtil.isEmpty(status)){
				location.setStatus(Integer.valueOf(status));
			}
			String type = request.getParameter("type");
			if(!StringUtil.isEmpty(type)){
				location.setType(Integer.valueOf(type));
			}
			String wareId = request.getParameter("wareId");
			if(!StringUtil.isEmpty(wareId) && 0 != Integer.valueOf(wareId)){
				location.setWareId(Integer.valueOf(wareId));
			}else{
				wareId=super.getCurrentWareId().toString();
				location.setWareId(Integer.valueOf(wareId));
			}
			PageBean<Location> pageBean=locationService.listLocation(page, rows,location);
			List<Location> pList = pageBean.getList();
			locationMap.put("total", pageBean.getTotal());
			locationMap.put("rows", pList);
		} catch (Exception e) {
			logger.error("listLocation：货位列表查询异常！"+Constant.LINE,e);
		}
		return locationMap;
	}
	
	/**
	  * @Description:启用 和 禁用
	  * @param request
	  * @param response
	  * @param ids
	  * @param status
	  * 2017年3月14日 
	 * @return 
	  */
	@RequestMapping(value = "/updateStatusById")
	@ResponseBody
	public String updateStatusById(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("ids") String ids,@ModelAttribute("status") Integer status){
		
		try{
			int result = locationService.updateByStatus(ids,status);
			if(result > 0){
				return "true";
			}
			if(result<0){
				return "updateNo";
			}
		} catch (Exception e) {
			logger.error("updateStatusById：根据id修改货位状态异常！"+Constant.LINE,e);
		}
		return "false";
	}
	
	/**
	  * @Description:跳转到添加页面
	  * @param request
	  * @param response
	  * @return
	  * 2017年3月14日 
	  */
	@RequestMapping(value = "/locationAddPage")
	public ModelAndView locationAddPage(HttpServletRequest request,HttpServletResponse response) {
		Warehouse warehouse =new Warehouse();
		WarehouseArea warehouseArea = new WarehouseArea();
		ModelAndView model = new ModelAndView("location/locationAdd");
		warehouse.setId(super.getCurrentWareId());
		List<Warehouse> warehouseList = warehouseService.selectByCriteria(warehouse);
		warehouseArea.setWareId(super.getCurrentWareId());
		List<WarehouseArea> warehouseAreaList = warehouseAreaService.selectByCriteria(warehouseArea);
		model.addObject("warehouseList", warehouseList);
		model.addObject("warehouseAreaList", warehouseAreaList);
		return model;
	}
	/**
	  * @Description:查询仓库下库区
	  * @param request
	  * @param response
	  * 2017年4月13日
	  */
	@RequestMapping("/getWareArea")
	@ResponseBody
	public JSONArray getWareArea(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("wareId") String wareId){
		JSONArray jarr=new JSONArray();
		try {
			WarehouseArea warehouseArea = new WarehouseArea();
			warehouseArea.setWareId(Integer.parseInt(wareId));
			warehouseArea.setStatus(Constant.TYPE_STATUS_YES);
			List<WarehouseArea> warehouseAreaList = warehouseAreaService.selectByCriteria(warehouseArea);
			for(WarehouseArea o:warehouseAreaList){
				Map<String, Object> obj=new LinkedHashMap<>();
				obj.put("id", o.getId());
				obj.put("areaName", o.getAreaName());
				jarr.add(obj);
			}			
			
		} catch (Exception e) {
			logger.error("addLocation：添加货位异常！"+Constant.LINE,e);
		}
		return jarr;
	}
	/**
	  * @Description:添加货位
	  * @param request
	  * @param response
	  * 2017年3月14日
	  */
	@RequestMapping("/addLocation")
	public void addLocation(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("location") Location location){
		try {
			//查询是否有此
			location.setWareId(super.getCurrentWareId());
			int result=locationService.selectByLocationNo(location);
			if(result>0){
				 writeOutWebMessage(WebMessageCode.INFO_EXIST, request, response);
			}else{
				location.setCreateTime(new Date());
				if(location.getStatus() == null){
					location.setType(Constant.LocationType.DISABLE_SALE);
				}
				location.setSortNum(Constant.TYPE_STATUS_NO);
				locationService.insertSelective(location);
				writeOutWebMessage(WebMessageCode.OPERATE_SUCESS, request, response);
			}
		} catch (Exception e) {
			logger.error("addLocation：添加货位异常！"+Constant.LINE,e);
			writeOutWebMessage(WebMessageCode.OPERATE_FAILED, request, response);
		}
	}
	
	
	/**
	  * @Description:编辑货位号弹窗
	  * @param request
	  * @param response
	  * @param id
	  * @return
	  * 2017年3月14日 
	  */
	@RequestMapping("/locationEditPage")
	public ModelAndView locationEditPage(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("id")String id){
		
		Warehouse warehouse =new Warehouse();
		WarehouseArea warehouseArea = new WarehouseArea();
		ModelAndView model = new ModelAndView("location/locationEdit");
		Location location = locationService.selectByPrimaryKey(Integer.valueOf(id));
		warehouse.setId(super.getCurrentWareId());
		List<Warehouse> warehouseList = warehouseService.selectByCriteria(warehouse);
		warehouseArea.setWareId(super.getCurrentWareId());
		List<WarehouseArea> warehouseAreaList = warehouseAreaService.selectByCriteria(warehouseArea);
		model.addObject("warehouseList", warehouseList);
		model.addObject("warehouseAreaList", warehouseAreaList);
		model.addObject("location", location);
		return model;
	}
	
	/**
	  * @Description:编辑货位
	  * @param request
	  * @param response
	  * 2017年3月14日
	  */
	@RequestMapping("/editLocation")
	@ResponseBody
	public void editLocation(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("location") Location location){
		try {
			
			int result=locationService.selectByLocationNo(location);
			if(result>0){
				writeOutWebMessage(WebMessageCode.INFO_EXIST, request, response);
			}else{
				location.setCreateTime(new Date());
				location.setSortNum(Constant.TYPE_STATUS_NO);
				locationService.updateByPrimaryKeySelective(location);
				writeOutWebMessage(WebMessageCode.OPERATE_SUCESS, request, response);
			}
		} catch (Exception e) {
			logger.error("editLocation：编辑货位异常！"+Constant.LINE,e);
			writeOutWebMessage(WebMessageCode.OPERATE_FAILED, request, response);
		}
	}

	/**
	 * 货位excel导入
	 * 详情：导入最多为500条，每条需要验证，验证失败，事务回滚
	 * @param excelFile
	 * @param request
	 * @param response
     * @return
     */
	@RequestMapping("/locaExcelImport")
	@ResponseBody
	public Object fileUpload(MultipartFile excelFile, HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		// 校验是否为空
		if (excelFile == null || excelFile.isEmpty()) {
			json.put("status", "101");
			json.put("message", "文件为空");
			return json;
		}
		String fileName = excelFile.getOriginalFilename();//文件名
		if (fileName == null || "".equals(fileName)) {
			json.put("status", "102");
			json.put("message", "文件名称不能为空");
			return json;
		}
		String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		if (!(fileType.equalsIgnoreCase(".xls") || fileType.equalsIgnoreCase(".xlsx"))) {
			json.put("status", "103");
			json.put("message", "文件类型错误");
			return json;
		}
		try {
			// 封装库区到map中
			Warehouse ware = super.getCurrentWare();
			WarehouseArea area = new WarehouseArea();
			area.setWareId(ware.getId());
			List<WarehouseArea> areas = warehouseAreaService.selectByCriteria(area);
			if (areas == null || areas.isEmpty()) {
				json.put("status", "104");
				json.put("message", "当前仓库无库区");
				return json;
			}
			Map<String, WarehouseArea> areaMap = new HashMap<>();
			for (WarehouseArea areaTemp : areas) {
				areaMap.put(areaTemp.getAreaName(), areaTemp);
			}
			Workbook work = null;
			if (fileType.equalsIgnoreCase(".xls")) {
				work = new HSSFWorkbook(excelFile.getInputStream());
			} else if (fileType.equalsIgnoreCase(".xlsx")) {
				work = new XSSFWorkbook(excelFile.getInputStream());
			}
			ServerStatus ss = null;
			for (int i = 0; i < work.getNumberOfSheets(); i++) {
				// 获取sheet页的内容
				Sheet sheet = work.getSheetAt(i);
				// 判断该sheet第三行是否是空的
				String isNull = ExcelCommon.queryExcelisNull(sheet, 2, 0);
				if (StringUtil.isNotEmpty(isNull)) {
					// 获取该excel所有列（第一列）的编码
					List<String> colNames = this.getColNames(sheet);
					if (colNames == null || colNames.isEmpty()) {
						continue;
					}
					ss = this.addExcelDataToDB(sheet, colNames, ware, areaMap);
					if (ss.getFlag() == Constant.TYPE_STATUS_NO) {
						json.put("status", "105");
						json.put("message", ss.getMessage());
						return json;
					}
				}
			}
			if (ss != null && ss.getFlag() == Constant.TYPE_STATUS_YES) {
				json.put("status", "200");
				json.put("message", ss.getMessage());
			}
		} catch (IOException e) {
			json.put("status", "500");
			json.put("message", "校验excel数据异常");
			logger.error(Constant.LINE + "校验excel数据异常", e);
		} catch (WMSException e) {
			if ("23000".equals(e.getCode())) {
				String msg = e.getMsg();
				int index = msg.indexOf("-") + 1;
				String locationNo = msg.substring(index);
				json.put("status", "106");
				json.put("message", "当前仓库存在货位号：" + locationNo + "，请修改！");
			} else {
				json.put("status", "500");
				json.put("message", e.getMsg());
				logger.error(Constant.LINE + e.getMsg(), e);
			}
		} catch (Exception e) {
			json.put("status", "500");
			json.put("message", e.getMessage());
			logger.error(Constant.LINE + "系统异常", e);
		}
		return json;
	}

	/**
	 * 将sheet页的内容插入到数据库
	 * @param sheet
	 * @param colNames
	 * @return
     */
	private ServerStatus addExcelDataToDB(Sheet sheet, List<String> colNames, Warehouse ware, Map<String, WarehouseArea> areaMap) throws WMSException{
		ServerStatus ss = new ServerStatus();
		List<Location> locations = new ArrayList<>(500);
		int rows = sheet.getLastRowNum(); // 总行数
		int i = 2;
		for (; i <= rows; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			Location location;
			try {
				location = transCell2Location(row, colNames);
			} catch (WMSException e) {
				ss.setFlag(Constant.TYPE_STATUS_NO);
				if (StringUtils.isBlank(e.getMsg())) {
					ss.setMessage("第"+ (row.getRowNum() + 1)+"行，数据异常，请检查！");
				} else {
					ss.setMessage(e.getMsg());
				}
				return ss;
			}
			if (!areaMap.containsKey(location.getAreaName().trim())) {
				ss.setFlag(Constant.TYPE_STATUS_NO);
				ss.setMessage("第"+ (row.getRowNum() + 1)+"行，库区不存在，请检查！");
				return ss;
			}
			WarehouseArea area = areaMap.get(location.getAreaName());
			location.setStatus(area.getStatus());
			location.setType(area.getType());
			location.setWareId(ware.getId());
			location.setWareName(ware.getWareName());
			location.setAreaId(area.getId());
			location.setAreaName(area.getAreaName());
			location.setSortNum(Constant.TYPE_STATUS_NO);
			location.setCreateTime(new Date());
			locations.add(location);
			// 每读取了500条数据。就插入一次数据库
			if (locations.size() > 0 && locations.size() % 500 == 0) {
				ss = locationService.insertBatch(locations);
				locations.clear();
			}
		}
		//跳出那个continue
		if (locations.size() > 0 && (i - 1) % 500 == rows) {
			ss = locationService.insertBatch(locations);
		}
		return ss;
	}

	private Location transCell2Location(Row row, List<String> colNames) throws WMSException {
		Location location = new Location();
		for (int j = 0; j < colNames.size(); j++) {
			Cell cell = row.getCell(j);
			// 如果cell为空
			if (null == cell) {
				throw new WMSException("第" + (row.getRowNum() + 1) + "行，第" + (j+1) + "列为空，请检查！");
			}
			String cellValue = ExcelCommon.getCallValue(cell);
			if (StringUtils.isBlank(cellValue)) {
				throw new WMSException("第" + (row.getRowNum() + 1) + "行，第" + (j+1) + "列为空，请检查！");
			}
			if (colNames.get(j).equalsIgnoreCase("location_no")){
				location.setLocationNo(cellValue);
			} else if (colNames.get(j).equalsIgnoreCase("area_name")) {
				location.setAreaName(cellValue);
			} else {
				throw new WMSException("第" + (j+1) + "列标题被修改，请检查！");
			}
		}
		return location;
	}

	/**
	 * 通过sheet页获取列名称（即表的列名）
	 *
	 * @param sheet
	 * @return List<String>
	 */
	public List<String> getColNames(Sheet sheet) {
		Row row = sheet.getRow(0);
		int columns = row.getPhysicalNumberOfCells();
		// 得到第一行（第一行为模板预设的值，对于需要插入数据库的字段名称）
		List<String> dataKeys = new ArrayList<>();
		// 循环第一行的数据，从0到columns
		for (int j = 0; j < columns; j++) {
			Cell cell;
			try {
				// 获取cell，如果报异常，说明整个row是空的null，直接在catch里面捕获，并赋值为空
				cell = row.getCell(j);
			} catch (NullPointerException e1) {
				dataKeys.add(j + "");
				continue;
			}
			// 如果cell为空
			if (null == cell) {
				dataKeys.add(j + "");
				continue;
			}
			// 匹配cell的类型
			switch (cell.getCellType()) {
				// 如果是空白
				case Cell.CELL_TYPE_BLANK:
					dataKeys.add(j + "");
					break;
				case Cell.CELL_TYPE_NUMERIC:
					// 如果cell里面包含E或者e，说明是科学计数法，要用特殊方法处理
					if (String.valueOf(cell.getNumericCellValue()).matches(".*[E|e].*")) {
						DecimalFormat df = new DecimalFormat("#.#");
						// 指定最长的小数点位为10
						df.setMaximumFractionDigits(10);
						dataKeys.add(df.format((cell.getNumericCellValue())));
					} else {
						dataKeys.add(cell.getNumericCellValue() + "");
					}
					break;
				case Cell.CELL_TYPE_STRING:
					dataKeys.add(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					String value;
					try {
						value = cell.getRichStringCellValue().getString();
						dataKeys.add(value);
					} catch (Exception e) {
						value = cell.getNumericCellValue() + "";
						dataKeys.add(value);
					}
					break;
				default:
					dataKeys.add(j + "");
					break;
			}
		}
		return dataKeys;
	}

}
