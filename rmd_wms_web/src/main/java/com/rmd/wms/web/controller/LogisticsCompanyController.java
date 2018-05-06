package com.rmd.wms.web.controller;

import com.github.pagehelper.StringUtil;
import com.rmd.bms.bean.AreaInfo;
import com.rmd.bms.entity.SysArea;
import com.rmd.bms.service.AreaService;
import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.DeliveryRange;
import com.rmd.wms.bean.LogisticsCompany;
import com.rmd.wms.bean.LogisticsFreightCity;
import com.rmd.wms.bean.LogisticsFreightTemplate;
import com.rmd.wms.bean.vo.LogisticsFreightTemplateVo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.DeliveryRangeService;
import com.rmd.wms.service.LogisticsCompanyService;
import com.rmd.wms.service.LogisticsFreightCityService;
import com.rmd.wms.service.LogisticsFreightTemplateService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.util.DateJsonValueProcessor;
import com.rmd.wms.web.util.JsonBinder;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 
* @ClassName: LogisticsCompanyController 
* @Description: TODO(承运商) 
* @author ZXLEI
* @date Mar 17, 2017 9:18:51 AM 
*
 */
@Controller
@RequestMapping(value = "/logistics")
public class LogisticsCompanyController extends AbstractAjaxController {

	private Logger logger = Logger.getLogger(LogisticsCompanyController.class);

	@Resource(name = "logisticsCompanyService")
	private LogisticsCompanyService logisticsCompanyService; // 物流公司
	
	@Resource(name = "deliveryRangeService")
    private DeliveryRangeService deliveryRangeService;  //物流公司配送范围
	
	@Resource(name="areaService")
	private AreaService areaService;

	@Resource(name="logisticsFreightTemplateService")
	private LogisticsFreightTemplateService logisticsFreightTemplateService;

	@Resource(name="logisticsFreightCityService")
	private LogisticsFreightCityService logisticsFreightCityService;
	
	/**
	 * 承运商列表页
	 * @return
	 */
	@RequestMapping("/Company")
	public ModelAndView jumpView(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("logistics/logisticsManage");
		LogisticsCompany lCompany=new LogisticsCompany();
		Integer wareId=super.getCurrentWareId();
		lCompany.setWareId(wareId);
		lCompany.setStatus(Constant.TYPE_STATUS_YES);
		List<LogisticsCompany> pList = logisticsCompanyService.selectByCriteria(lCompany);
		mv.addObject("pList", pList);
		return mv;
	}

	/**
	 * 承运商列表查询方法
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/ListLogisticsCompany")
	@ResponseBody
	public Map<String, Object> listLogisticsCompany(@RequestParam(value = "page") Integer page
												   ,@RequestParam(value="logisComId",required = false) Integer logisComId
			                                       ,@RequestParam(value = "rows") Integer rows,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> whereParams = new HashMap<String, Object>();
			Integer wareId=super.getCurrentWareId();
			if(logisComId!=null&&logisComId>0){
				whereParams.put("Id", logisComId);
			}
			whereParams.put("wareId", wareId);

			PageBean<LogisticsCompany> pageBean = logisticsCompanyService
					.ListLogisticsCompanys(page, rows, whereParams);
			List<LogisticsCompany> pList = pageBean.getList();	
			map.put("total", pageBean.getTotal());
			map.put("rows", pList);
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
			JSONObject json = JSONObject.fromObject(map, config);
			// 格式化时间后填入返回结果
			map.put("datarow", json);
		} catch (Exception e) {
			logger.error("ListLogisticsCompany：承运商查询异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return map;
	}
	
	/**
	 * 添加页面跳转
	 * @return
	 */
	@RequestMapping(value = "/saveLogistics")
	public ModelAndView addLogistics() {
        ModelAndView mv = new ModelAndView("logistics/addLogisticsAdFreight");
		List<AreaInfo> areaInfoList=areaService.listAllAreaInfo();
		mv.addObject("areaList",areaInfoList);
		return mv;
	}
	/**
	 * 新增承运商
	 * @param logisticsCompany
	 * @return
	 */
	@RequestMapping(value = "/addLogisticsCompany")
	@ResponseBody
	public JSONObject addLogisticsCompany(@RequestParam(value="logisticsCompany",required=false) String logisticsCompany, @RequestParam(value="deliveryRange",required=false) String deliveryRange,
										  @RequestParam(value="freightTemplate",required=false) String freightTemplate, HttpServletRequest request){
		JSONObject json=new JSONObject();
        if(StringUtil.isEmpty(logisticsCompany)||StringUtil.isEmpty(deliveryRange)||StringUtil.isEmpty(freightTemplate)){
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        //承运商信息
        LogisticsCompany logisticsCompanyBean= JsonBinder.fromJson(logisticsCompany,LogisticsCompany.class);
		logisticsCompanyBean.setWareId(super.getCurrentWareId());
		logisticsCompanyBean.setWareName(super.getCurrentWare().getWareName());
		logisticsCompanyBean.setUpdateuser(super.getCurrentUserId());
		logisticsCompanyBean.setUpdatetime(new Date());
		//承运商配送范围
		List<DeliveryRange> deliveryRangeList= JsonBinder.getListFromJson(deliveryRange,DeliveryRange.class);
		List<DeliveryRange> newDeliverRangeList=new ArrayList<>();
		DeliveryRange newDeliverRange=null;
		for(DeliveryRange dr:deliveryRangeList){
			newDeliverRange=new DeliveryRange();
			BeanUtils.copyProperties(dr,newDeliverRange);
			newDeliverRange.setWareId(super.getCurrentWareId());
			newDeliverRange.setWareName(super.getCurrentWare().getWareName());
			newDeliverRangeList.add(newDeliverRange);
		}
		//运费模板
		List<LogisticsFreightTemplateVo> logisticsFreightTemplateVoList= JsonBinder.getListFromJson(freightTemplate,LogisticsFreightTemplateVo.class);
		List<LogisticsFreightTemplateVo> newLogisticsFreightTemplateVoList=new ArrayList<>();
		LogisticsFreightTemplateVo newLogisticsFreightTemplateVo=null;
		List<LogisticsFreightCity> logisticsFreightCityList=null;
		for(LogisticsFreightTemplateVo lft:logisticsFreightTemplateVoList){
			newLogisticsFreightTemplateVo=new LogisticsFreightTemplateVo();
			BeanUtils.copyProperties(lft,newLogisticsFreightTemplateVo);
			newLogisticsFreightTemplateVo.setWareId(super.getCurrentWareId());
			newLogisticsFreightTemplateVo.setWareName(super.getCurrentWare().getWareName());
			newLogisticsFreightTemplateVo.setCreateTime(new Date());
			newLogisticsFreightTemplateVo.setCreateUserId(super.getCurrentUserId());
			newLogisticsFreightTemplateVo.setCreateUserName(super.getCurrentUserName());
			if(StringUtil.isNotEmpty(newLogisticsFreightTemplateVo.getFreightRange())){
				logisticsFreightCityList=JsonBinder.getListFromJson(newLogisticsFreightTemplateVo.getFreightRange(), LogisticsFreightCity.class);
				newLogisticsFreightTemplateVo.setLogisticsFreightCityList(logisticsFreightCityList);
			}
			newLogisticsFreightTemplateVoList.add(newLogisticsFreightTemplateVo);
		}
		ServerStatus serverStatus= logisticsCompanyService.addLogisticsCompanyAdFreight(logisticsCompanyBean, newDeliverRangeList, newLogisticsFreightTemplateVoList);
		return json;
	}

	/**
	 * 修改承运商
	 * @param logisticsCompany
	 * @return
	 */
	@RequestMapping(value = "/updateLogisticsCompany")
	@ResponseBody
	public JSONObject updateLogisticsCompany(@RequestParam(value="logisticsCompany",required=false) String logisticsCompany, @RequestParam(value="deliveryRange",required=false) String deliveryRange, HttpServletRequest request){
		JSONObject json=new JSONObject();
		if(StringUtil.isEmpty(logisticsCompany)||StringUtil.isEmpty(deliveryRange)){
			json.put("status", "101");
			json.put("message", "参数错误");
			return json;
		}
		//承运商信息
		LogisticsCompany logisticsCompanyBean= JsonBinder.fromJson(logisticsCompany,LogisticsCompany.class);
		logisticsCompanyBean.setWareId(super.getCurrentWareId());
		logisticsCompanyBean.setWareName(super.getCurrentWare().getWareName());
		logisticsCompanyBean.setUpdateuser(super.getCurrentUserId());
		logisticsCompanyBean.setUpdatetime(new Date());
		//承运商配送范围
		List<DeliveryRange> deliveryRangeList= JsonBinder.getListFromJson(deliveryRange,DeliveryRange.class);
		List<DeliveryRange> newDeliverRangeList=new ArrayList<>();
		DeliveryRange newDeliverRange=null;
		for(DeliveryRange dr:deliveryRangeList){
			newDeliverRange=new DeliveryRange();
			BeanUtils.copyProperties(dr,newDeliverRange);
			newDeliverRange.setWareId(super.getCurrentWareId());
			newDeliverRange.setWareName(super.getCurrentWare().getWareName());
			newDeliverRangeList.add(newDeliverRange);
		}

		ServerStatus serverStatus= logisticsCompanyService.updateLogisticsCompany(logisticsCompanyBean, newDeliverRangeList);
		return json;
	}

	/**
	 * 修改运费模板界面
	 * @param logisticsId
	 * @return
	 */
	@RequestMapping(value = "/editFreightTemplate")
	public ModelAndView editFreightTemplate(@RequestParam(value="logisticsId",required = false) Integer logisticsId,HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("logistics/editFreightTemplate");
		List<AreaInfo> areaInfoList=areaService.listAllAreaInfo();
		mv.addObject("areaList",areaInfoList);
		LogisticsFreightTemplate logisticsFreightTemplate=new LogisticsFreightTemplate();

		Map<String,Object> paramMap=new HashedMap();
		paramMap.put("logisticsId",logisticsId);
		paramMap.put("freightType", Constant.TYPE_STATUS_YES);//默认运费
		List<LogisticsFreightTemplate> defaultTemplateList=logisticsFreightTemplateService.selectByCriteria(paramMap);
        LogisticsFreightTemplate defaultTemplate=new LogisticsFreightTemplate();
        if(defaultTemplateList.size()>0){
            defaultTemplate=defaultTemplateList.get(0);
        }

		paramMap.put("freightType",Constant.TYPE_STATUS_NO);//非默认运费
		List<LogisticsFreightTemplate> templateList=logisticsFreightTemplateService.selectByCriteria(paramMap);
        //获取承运商所有运费模板城市映射关系
		Map<String,Object> paramMapCity=new HashedMap();
		paramMap.put("logisticsId",logisticsId);
		List<LogisticsFreightCity> logisticsFreightCityList= logisticsFreightCityService.selectByCriteria(paramMapCity);
		List<LogisticsFreightTemplateVo> logisticsFreightTemplateVoList=new ArrayList<>();
        JSONObject cityJson=null;
        LogisticsFreightTemplateVo logisticsFreightTemplateVo=null;
        //遍历将每个模板对应的城市范围返回
        for(LogisticsFreightTemplate template:templateList){
            logisticsFreightTemplateVo=new LogisticsFreightTemplateVo();
            BeanUtils.copyProperties(template,logisticsFreightTemplateVo);
            JSONArray jsonArray=new JSONArray();
            for(LogisticsFreightCity logisticsFreightCity:logisticsFreightCityList){
                if(template.getId()==logisticsFreightCity.getTemplateId()){
                    cityJson=new JSONObject();
                    cityJson.put("provCode",logisticsFreightCity.getProvCode());
                    cityJson.put("provName",logisticsFreightCity.getProvName());
                    cityJson.put("cityCode",logisticsFreightCity.getCityCode());
                    cityJson.put("cityName",logisticsFreightCity.getCityName());
                    jsonArray.add(cityJson);
                }
            }
            if(jsonArray.size()>0){
                logisticsFreightTemplateVo.setFreightRange(jsonArray.toString());
            }else{
                logisticsFreightTemplateVo.setFreightRange("");
            }
            logisticsFreightTemplateVoList.add(logisticsFreightTemplateVo);
        }
        mv.addObject("logisticsId",logisticsId);
		mv.addObject("defaultTemplate",defaultTemplate);
		mv.addObject("templateList",logisticsFreightTemplateVoList);
		return mv;
	}

    /**
     * 修改承运商运费模板
     * @param logisticsId
     * @return
     */
    @RequestMapping(value = "/updateFreightTemplate")
    @ResponseBody
    public JSONObject updateFreightTemplate(@RequestParam(value="logisticsId",required = false) Integer logisticsId,
                                          @RequestParam(value="freightTemplate",required=false) String freightTemplate, HttpServletRequest request){
        JSONObject json=new JSONObject();
        if(logisticsId==null||StringUtil.isEmpty(freightTemplate)){
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        //运费模板
        List<LogisticsFreightTemplateVo> logisticsFreightTemplateVoList= JsonBinder.getListFromJson(freightTemplate,LogisticsFreightTemplateVo.class);
        List<LogisticsFreightTemplateVo> newLogisticsFreightTemplateVoList=new ArrayList<>();
        LogisticsFreightTemplateVo newLogisticsFreightTemplateVo=null;
        List<LogisticsFreightCity> logisticsFreightCityList=null;
        for(LogisticsFreightTemplateVo lft:logisticsFreightTemplateVoList){
            newLogisticsFreightTemplateVo=new LogisticsFreightTemplateVo();
            BeanUtils.copyProperties(lft,newLogisticsFreightTemplateVo);
            newLogisticsFreightTemplateVo.setWareId(super.getCurrentWareId());
            newLogisticsFreightTemplateVo.setWareName(super.getCurrentWare().getWareName());
            newLogisticsFreightTemplateVo.setCreateTime(new Date());
            newLogisticsFreightTemplateVo.setCreateUserId(super.getCurrentUserId());
            newLogisticsFreightTemplateVo.setCreateUserName(super.getCurrentUserName());
            if(StringUtil.isNotEmpty(newLogisticsFreightTemplateVo.getFreightRange())){
                logisticsFreightCityList=JsonBinder.getListFromJson(newLogisticsFreightTemplateVo.getFreightRange(), LogisticsFreightCity.class);
                newLogisticsFreightTemplateVo.setLogisticsFreightCityList(logisticsFreightCityList);
            }
            newLogisticsFreightTemplateVoList.add(newLogisticsFreightTemplateVo);
        }
        ServerStatus serverStatus= logisticsCompanyService.updateFreightTemplate(logisticsId, newLogisticsFreightTemplateVoList);
        return json;
    }


	/**
	 * 保存承运商
	 * @return
	 */
	@RequestMapping(value = "/saveLogisticsCompany")
	@ResponseBody
	public String saveLogisticsCompany(@RequestParam(value="name",required = false) String name
									  ,@RequestParam(value="code",required = false) String code
		                              ,@RequestParam(value="phone",required = false) String phone
		                              ,@RequestParam(value="minWeight",required = false) Integer minWeight
		                              ,@RequestParam(value="maxWeight",required = false) Integer maxWeight
		                              ,@RequestParam(value="provinceTemp",required = false) String provinceTemp
		                              ,@RequestParam(value="provinceName",required = false) String provinceName
										,@RequestParam(value="contactName",required = false) String contactName
		                              ,@RequestParam(value="status",required = false) Integer status,HttpServletRequest request) {
		String flag="false";
		try {
			if (StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(code)) {
				Integer wareId=super.getCurrentWareId();
				if("".equals(wareId)){
					wareId=-1;
				}
				LogisticsCompany logisticsCompany=new LogisticsCompany();
				logisticsCompany.setName(name);
				logisticsCompany.setCode(code);
				logisticsCompany.setPhone(phone);
				logisticsCompany.setMinWeight(minWeight);
				logisticsCompany.setMaxWeight(maxWeight);
				logisticsCompany.setStatus(status);
				logisticsCompany.setWareId(wareId);
				logisticsCompany.setWareName(super.getCurrentWare().getWareName());
				logisticsCompany.setUpdatetime(new Date());
				logisticsCompany.setContactName(contactName);
				logisticsCompany.setUpdateuser(super.getCurrentUserId());
				if(StringUtil.isNotEmpty(provinceTemp)){
					//省份编码
					String[] provinceArr = provinceTemp.split(",");
					//省份名称
					String[] provinceNameArr = provinceName.split(",");
					if (provinceArr.length > 0) {
						// 添加承运商数据
						int LogisticsId = logisticsCompanyService.insert(logisticsCompany);
						//添加配送范围--BEGIN
						List<DeliveryRange> liDeliveryRanges = new ArrayList<DeliveryRange>();
						for (int i=0;i<provinceArr.length;i++) {
							DeliveryRange deliveryRange = new DeliveryRange();
							deliveryRange.setProvCode(provinceArr[i]);
							deliveryRange.setName(provinceNameArr[i]);
							deliveryRange.setStatus(0);
							deliveryRange.setWareId(wareId);
							deliveryRange.setWareName(super.getCurrentWare().getWareName());
							deliveryRange.setLogisticsId(LogisticsId);
							liDeliveryRanges.add(deliveryRange);
						}
						if (liDeliveryRanges.size() > 0 && LogisticsId > 0) {
							deliveryRangeService.insertBatch(liDeliveryRanges);
						}
						//添加配送范围--END
						flag = "true";
					}
				}

			}
		} catch (Exception e) {
			logger.error("saveLogisticsCompany：承运商保存异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return flag;
	}
	
	/**
	 * 修改承运商查询实体方法
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/editLogistics")
	public ModelAndView editLogistics(@RequestParam(value="id",required = false) Integer id,HttpServletRequest request) {
				ModelAndView mv = new ModelAndView("logistics/editLogistics");
				//承运商公司
				LogisticsCompany logisticsCompany= logisticsCompanyService.selectByPrimaryKey(id);
				//承运商配送范围
				Integer wareId=super.getCurrentWareId();
				if("".equals(wareId)){
					wareId=-1;
				}
				Map<String, Object> whereParams = new HashMap<String, Object>();
				whereParams.put("wareId",wareId);
				whereParams.put("logisticsId",id);
				List<DeliveryRange>  rangeList= deliveryRangeService.selectAllByWhere(whereParams);
				StringBuilder sBuilder=new StringBuilder();
				String rangeId="";
				for (DeliveryRange deliveryRange : rangeList) {
					sBuilder.append(deliveryRange.getProvCode()+","); 
					rangeId+=deliveryRange.getId()+",";
				}
				List<AreaInfo> areaInfoList=areaService.listAllAreaInfo();
				mv.addObject("areaList",areaInfoList);
				mv.addObject("Company",logisticsCompany);
				//承运范围集合
				mv.addObject("rangeList",sBuilder.toString());
				//承运id集合
				mv.addObject("rangeId",rangeId);
				return mv;
	}
	
	
	/**
	 * 修改承运商
	 * @return
	 */
	@RequestMapping(value = "/editLogisticsCompany")
	@ResponseBody
	public String editLogisticsCompany(@RequestParam(value="name",required = false) String name
									  ,@RequestParam(value="code",required = false) String code
									  ,@RequestParam(value="id",required = false) String id
		                              ,@RequestParam(value="phone",required = false) String phone
		                              ,@RequestParam(value="minWeight",required = false) Integer minWeight
		                              ,@RequestParam(value="maxWeight",required = false) Integer maxWeight
		                              ,@RequestParam(value="provinceTemp",required = false) String provinceTemp
		                              ,@RequestParam(value="rangeId",required = false) String rangeId
		                              ,@RequestParam(value="provinceName",required = false) String provinceName
			                          ,@RequestParam(value="contactName",required = false) String contactName
		                              ,@RequestParam(value="status",required = false) Integer status,HttpServletRequest request) {
		String flag="false";
		try {
			if (StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(code)
					&& StringUtil.isNotEmpty(id)) {
				Integer LogisticsId=Integer.parseInt(id);
				LogisticsCompany logisticsCompany=logisticsCompanyService.selectByPrimaryKey(LogisticsId);
				
				Integer wareId=super.getCurrentWareId();
				if("".equals(wareId)){
					wareId=-1;
				}
				logisticsCompany.setName(name);
				logisticsCompany.setCode(code);
				logisticsCompany.setPhone(phone);
				logisticsCompany.setMinWeight(minWeight);
				logisticsCompany.setMaxWeight(maxWeight);
				logisticsCompany.setStatus(status);
				logisticsCompany.setWareId(wareId);
				logisticsCompany.setWareName(super.getCurrentWare().getWareName());
				logisticsCompany.setContactName(contactName);
				logisticsCompany.setUpdatetime(new Date());
				logisticsCompany.setUpdateuser(super.getCurrentUserId());
				//删除承运商下已存在的配送范围
				if(StringUtil.isNotEmpty(rangeId)){
					String[] rangeIdArr = rangeId.split(",");
					if (rangeIdArr.length > 0) {
						for (String rId : rangeIdArr) {
							if(rId!=""){
							int result=deliveryRangeService.deleteByPrimaryKey(Integer.parseInt(rId));
								if(result==1){
									flag = "true";
								}
							}
						}
						//修改承运商基本信息
					int upflag=logisticsCompanyService.updateByPrimaryKey(logisticsCompany);
						if(upflag!=1){
							flag = "false";
						}
					}
				}
				if(flag == "true"||StringUtil.isEmpty(rangeId)){
						if(StringUtil.isNotEmpty(provinceTemp)){
							//省份编码
							String[] provinceArr = provinceTemp.split(",");
							//省份名称
							String[] provinceNameArr = provinceName.split(",");
							if (provinceArr.length > 0) {
								//添加配送范围--BEGIN
								List<DeliveryRange> liDeliveryRanges = new ArrayList<DeliveryRange>();
								for (int i=0;i<provinceArr.length;i++) {
									DeliveryRange deliveryRange = new DeliveryRange();
									deliveryRange.setProvCode(provinceArr[i]);
									deliveryRange.setName(provinceNameArr[i]);
									deliveryRange.setStatus(0);
									deliveryRange.setWareId(wareId);
									deliveryRange.setWareName(super.getCurrentWare().getWareName());
									deliveryRange.setLogisticsId(LogisticsId);
									liDeliveryRanges.add(deliveryRange);
								}
								if (liDeliveryRanges.size() > 0 && LogisticsId > 0) {
									deliveryRangeService.insertBatch(liDeliveryRanges);
								}
								//添加配送范围--END
							}
						}
					flag = "true";
				   }
			}
		} catch (Exception e) {
			flag="false";
			logger.error("editLogisticsCompany：承运商修改异常！"+ com.rmd.wms.constant.Constant.LINE,e);
		}
		return flag;
	}
	
	/**
	 * 检查承运商名称时候重复
	 * @param name
	 * @param request
	 * @return
	 */
	@RequestMapping("checkCompanyName")
	@ResponseBody
	public String checkCompanyName(@RequestParam(value="id",required = false) String id,@RequestParam(value="name",required = false) String name
			                       ,HttpServletRequest request){
		String flag="false";
		Integer wareId=super.getCurrentWareId();
		LogisticsCompany logisticsCompany=new LogisticsCompany();
		logisticsCompany.setName(name.trim());
		logisticsCompany.setWareId(wareId);
		List<LogisticsCompany> companies= logisticsCompanyService.selectByCriteria(logisticsCompany);
		List<LogisticsCompany> list=new ArrayList<>();
		if(StringUtil.isNotEmpty(id)){
			for(LogisticsCompany o:companies){
				if(o.getId()!=Integer.parseInt(id)){
					list.add(o);
				}
			}
		}else{
			list=companies;
		}
		if (list.size()>0) {
			flag="true";
		}
		return flag;
	}

	/**
	 * 检查承运商编码重复
	 * @param code
	 * @param request
	 * @return
	 */
	@RequestMapping("checkCompanyCode")
	@ResponseBody
	public String checkCompanyCode(@RequestParam(value="id",required = false) String id,@RequestParam(value="code",required = false) String code
			,HttpServletRequest request){
		String flag="false";

		Integer wareId=super.getCurrentWareId();
		LogisticsCompany logisticsCompany=new LogisticsCompany();
		logisticsCompany.setCode(code.trim());
		logisticsCompany.setWareId(wareId);
		List<LogisticsCompany> companies= logisticsCompanyService.selectByCriteria(logisticsCompany);
		List<LogisticsCompany> list=new ArrayList<>();
		if(StringUtil.isNotEmpty(id)){
			for(LogisticsCompany o:companies){
				if(o.getId()!=Integer.parseInt(id)){
					list.add(o);
				}
			}
		}else{
			list=companies;
		}
		if (list.size()>0) {
			flag="true";
		}
		return flag;
	}
	
	/**
	 * 获取区域信息
	 * @param parentCode
	 * @param request
	 * @return
	 */
	@RequestMapping("/getAreaInfo")
	@ResponseBody
	public List<SysArea> getAreaInfo(@RequestParam(value="parentCode",required = false) String parentCode
			                       ,HttpServletRequest request){
		Notification<List<SysArea>> notification=areaService.listAreaInfo(parentCode);
		List<SysArea> list=notification.getResponseData();
		return list;
	}

	/**
	 * 更新承运商状态
	 * @param ids
	 * @param status
	 * @param request
	 * @return
	 */
	@RequestMapping("updateLogisStatus")
	@ResponseBody
	public Map<String,Object> updateLogisStatus(@RequestParam(value="ids",required = false) String ids,@RequestParam(value="status",required = false) Integer status
			,HttpServletRequest request){
		Map<String,Object> map=new HashedMap();
		if(StringUtil.isEmpty(ids)||status==null){
			map.put("status","101");
			map.put("message","参数为空！");
			return map;
		}

		String[] arr=ids.split(",");
		for(String id:arr){
			LogisticsCompany logisticsCompany=new LogisticsCompany();
			logisticsCompany.setId(Integer.parseInt(id));
			logisticsCompany.setStatus(status);
			logisticsCompanyService.updateByPrimaryKeySelective(logisticsCompany);
		}
		map.put("status","200");
		map.put("message","操作成功！");
		return map;
	}

}
