package com.rmd.wms.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.rmd.bms.entity.User;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.CheckInfoVo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.CheckInfoPrint;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.bean.vo.web.SearchCheckBillVo;
import com.rmd.wms.bean.vo.web.SubmitChecksVo;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.enums.CheckBillType;
import com.rmd.wms.enums.CheckInfoSubmitStatus;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.oss.OssService;
import com.rmd.wms.oss.bean.OssFile;
import com.rmd.wms.service.CheckBillService;
import com.rmd.wms.service.CheckInfoService;
import com.rmd.wms.service.CheckUserService;
import com.rmd.wms.service.WarehouseService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.util.DateJsonValueProcessor;
import com.rmd.wms.web.util.DownloadUtils;
import com.rmd.wms.web.util.ExcelCommon;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 盘点控制层
 *
 * @author : liu
 * @Date : 2017/4/14
 */
@RequestMapping("/check")
@Controller
public class CheckBillController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(CheckBillController.class);

    @Autowired
    private CheckBillService checkBillService;
    @Autowired
    private CheckUserService checkUserService;

    @Resource(name = "checkInfoService")
    private CheckInfoService checkInfoService;
    
    @Resource(name = "warehouseService")
    private WarehouseService warehouseService;

    /**
     * 跳转到盘点主页面
     *
     * @return
     */
    @RequestMapping(value = "/checkManage")
    public ModelAndView jumpView() {
    	ModelAndView mvAndView=new ModelAndView("check/checkManage");
        Warehouse warehouse=warehouseService.selectByPrimaryKey(getCurrentWare().getId());
    	mvAndView.addObject("wareStatus", warehouse.getStatus());
        return mvAndView;
    }

    /**
     * 创建盘点单
     *
     * @param request
     * @param response
     * @param session
     * @param ids
     * @return
     */
    @RequestMapping(value = "/createCheck", method = RequestMethod.POST)
    @ResponseBody
    public Object createCheck(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam(value = "ids") String ids, @RequestParam(value = "type") Integer type) {
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(ids) || type == null) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        Warehouse ware = super.getCurrentWare();
        // 限制仓库为盘点中可通过创建大盘
        if (ware != null && Constant.CheckBillType.LARGE_CHECK == type && Constant.WarehouseStatus.CHECKING != ware.getStatus()) {
            json.put("status", "102");
            json.put("message", "仓库需设置为盘点中");
            return json;
        }
        List<Integer> idList = JSON.parseArray(ids, Integer.class);
        User user = super.getCurrentUser();
        try {
            ServerStatus ss = checkBillService.createCheckBill(super.getCurrentUserId(), super.getCurrentUserName(), ware, idList, type);
            json.put("status", ss.getStatus());
            json.put("message", ss.getMessage());
        } catch (WMSException e) {
            json.put("status", "-200");
            json.put("message", e.getMsg());
            logger.error(e.getMsg(), e);
        }
        return json;
    }
    
    /**
     * 大盘信息查询
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/checkSubmitStatus", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkSubmitStatus(HttpServletRequest request, HttpServletResponse response) {
    	JSONObject json = new JSONObject();
    	Map<String, Object> paraMap=new HashMap<String, Object>();
    	paraMap.put("wareId", getCurrentWare().getId());
    	paraMap.put("type", CheckBillType.A002.getValue());//大盘
    	paraMap.put("submitStatus", CheckInfoSubmitStatus.A000.getValue());//提报状态，-1：不可提交，0：未提交，1：已提交
    	List<CheckInfo> list= checkInfoService.selectByCriteria(paraMap);
    	if(list.size()>0){
    		json.put("status", 200);//当前仓库下存在大盘未提交的数据
    	}
        return json;
    }
    
    /**
     * 开放/关闭订单
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/orderOperate", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject orderOperate(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "operateStatus") String operateStatus) {
    	JSONObject json = new JSONObject();
    	if (StringUtils.isBlank(operateStatus)) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
    	checkInfoService.updateCheckInfoStatus(getCurrentWare().getId(), operateStatus);  
    	Warehouse ware = warehouseService.selectByPrimaryKey(getCurrentWare().getId());
        super.setCurrentWare(ware);
    	json.put("status", 200);    	
        return json;
    }


    /**
     * 搜索采购单
     *
     * @param request
     * @param response
     * @param session
     * @param checkNo
     * @param starTime
     * @param endTime
     * @param status
     * @param type
     * @param wareName
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/searchChecks")
    @ResponseBody
    public Object getCheckBills(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                @RequestParam(value = "checkNo", required = false) String checkNo,
                                @RequestParam(value = "starTime", required = false) String starTime,
                                @RequestParam(value = "endTime", required = false) String endTime,
                                @RequestParam(value = "status", required = false) String status,
                                @RequestParam(value = "type", required = false) String type,
                                @RequestParam(value = "wareName", required = false) String wareName,
                                @RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "rows", required = false) Integer rows) {
        JSONObject json = new JSONObject();
        try {
            List<SearchCheckBillVo> voList = new ArrayList<>();
            Integer wareId=super.getCurrentWareId();
            Map<String, Object> mapParam = new HashMap<>();
            mapParam.put("wareId",wareId);
            if(StringUtil.isNotEmpty(checkNo)){//盘点单号
                mapParam.put("checkNo",checkNo);
            }
            if(StringUtil.isNotEmpty(starTime)){
                mapParam.put("createTime_gt",starTime);
            }
            if(StringUtil.isNotEmpty(endTime)){
                mapParam.put("createTime_lt",endTime);
            }
            if(StringUtil.isNotEmpty(status)&&!"-1".equals(status)){
                mapParam.put("status",status);
            }
            if(StringUtil.isNotEmpty(type)&&!"-1".equals(type)){//类型
                mapParam.put("type",type);
            }
            if(StringUtil.isNotEmpty(wareName)){//仓库
                mapParam.put("wareName",wareName);
            }
            PageBean<CheckBill> pageBean = checkBillService.searchCheckBills(page, rows, mapParam);
            if (pageBean != null || pageBean.getList().size() > 0) {
                // 封装页面数据
                for (CheckBill temp : pageBean.getList()) {
                    SearchCheckBillVo vo = new SearchCheckBillVo();
                    BeanUtils.copyProperties(temp, vo);
                    List<CheckUser> checkUsers = checkUserService.selectByCheckNo(temp.getCheckNo());
                    // 拼装盘点人
                    String checkUserNames = "";
                    if (checkUsers != null && checkUsers.size() > 0) {
                        for (CheckUser userTemp : checkUsers) {
                            checkUserNames += userTemp.getUserName() + ",";
                        }
                    }
                    if (StringUtils.isNotEmpty(checkUserNames)) {
                        // 添加盘点人名称，去掉最后的逗号
                        vo.setCheckUserNames(checkUserNames.substring(0, checkUserNames.length() - 1));
                    }
                    voList.add(vo);
                }
            }
            json.put("status", "200");
            json.put("message", "查询成功");
            json.put("rows", voList);
            json.put("total", pageBean.getTotal());
        } catch (WMSException e) {
            json.put("status", "-200");
            json.put("message", "查询失败");
            logger.error(Constant.LINE + "查询异常", e);
        }
        return json;
    }

    /**
     * 跳转到盘点记录界面
     *
     * @return
     */
    @RequestMapping(value = "/checkRecode")
    public ModelAndView checkInfoPage() {
        return new ModelAndView("checkInfo/checkInfoManage");
    }

    @RequestMapping(value = "/listCheckInfo")
    @ResponseBody
    public Map<String, Object> listCheckInfo(HttpServletRequest request, HttpServletResponse response
            , @RequestParam(value = "page") Integer page
            , @RequestParam(value = "rows") Integer rows) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String goodsCode = request.getParameter("goodsCode");
            String starTime = request.getParameter("starTime");
            String endTime = request.getParameter("endTime");
            String submitStatus = request.getParameter("submitStatus");
            String checkState = request.getParameter("checkState");
            String checkNo = request.getParameter("checkNo");
            Map<String, Object> recoreMap = new HashMap<String, Object>();
            Map<String, Object> billMap = new HashMap<String, Object>();
            Integer wareId=super.getCurrentWareId();
            if("".equals(wareId)){
                wareId=-1;
            }
            recoreMap.put("wareId",wareId);
            if (StringUtils.isNotEmpty(goodsCode)) {
                recoreMap.put("goodsCode", goodsCode);
            }
            if (StringUtils.isNotEmpty(starTime)) {
                billMap.put("firstStartTime_gt",starTime);
            }
            if (StringUtils.isNotEmpty(endTime)) {
                billMap.put("firstStartTime_lt",endTime);
            }
            if (StringUtils.isNotEmpty(submitStatus)) {
                recoreMap.put("submitStatus", submitStatus);
            }
            if (StringUtils.isNotEmpty(checkState)) {
                if ("1".equals(checkState)) {//盘盈
                    recoreMap.put("checkMore_search",com.rmd.wms.web.constant.Constant.SEARCH_FLAG);
                } else if ("2".equals(checkState)) {//盘亏
                    recoreMap.put("checkLess_search",com.rmd.wms.web.constant.Constant.SEARCH_FLAG);
                }
            }
            if (StringUtils.isNotEmpty(checkNo)) {
                recoreMap.put("checkNo", checkNo);
            }
            if (billMap.size()>0) {
                List<CheckBill> listBills = checkBillService.selectByCriteria(billMap);
                if (listBills.size() > 0) {
                    StringBuilder sb = new StringBuilder(" (");
                    for (CheckBill o : listBills) {
                        sb.append("'").append(o.getCheckNo()).append("',");
                    }
                    String str = sb.toString();
                    str = str.substring(0, str.length() - 1) + ")";
                    recoreMap.put("checkNo_in",str);
                } else {
                    recoreMap.put("noData_search",com.rmd.wms.web.constant.Constant.SEARCH_FLAG);
                }
            }
            PageBean<CheckInfo> pageBean = checkInfoService.listCheckInfo(page, rows, recoreMap);
            //盘点人信息
            List<CheckUser> uList=checkUserService.selectByCriteria(new HashedMap());
            List<CheckInfo> pList = pageBean.getList();
            List<CheckInfoVo> lList =new LinkedList<>();
            CheckInfoVo ck=null;
            String checkUser="";
            for(CheckInfo o:pList){
                ck=new CheckInfoVo();
                BeanUtils.copyProperties(o,ck);
                for(CheckUser u:uList){
                    if(o.getCheckNo().equals(u.getCheckNo())){
                        checkUser+=u.getUserName()+",";
                    }
                }
                if(checkUser!=""){
                    ck.setCheckUserName(checkUser.substring(0,checkUser.length()-1));
                    checkUser="";
                }
                lList.add(ck);
            }
            map.put("total", pageBean.getTotal());
            map.put("rows", lList);
        } catch (Exception e) {
            logger.error("checkRecode：盘点记录列表查询异常！", e);
        }
        return map;
    }

    /**
     * 盘点单详情
     *
     * @param request
     * @param response
     * @param checkNo  盘点单号
     * @return
     */
    @RequestMapping(value = "/getCheckGoodsInfo")
    public ModelAndView getCheckGoods(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "checkNo") String checkNo) {
        ModelAndView mv = new ModelAndView("check/checkGoodsInfo");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("checkNo", checkNo);
            List<CheckInfo> list = checkInfoService.selectByCriteria(map);
            mv.addObject("checkGoodsList", list);
        } catch (WMSException e) {
            logger.error(Constant.LINE + "查询异常", e);
        }
        return mv;
    }
    /**
     * 盘点单提报
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getHandInPage")
    public ModelAndView getHandIn(HttpServletRequest request, HttpServletResponse response) {
    	String ids=request.getParameter("ids");
    	ModelAndView mv=new ModelAndView("checkInfo/checkHandIn");
        mv.addObject("ids", ids);
        return mv;
    }

    /**
     * 盘点打印方法
     *
     * @param Id
     * @param model
     * @return
     */
    @RequestMapping(value = "/checkBillPrint", method = RequestMethod.GET)
    public String checkBillPrint(@RequestParam(value = "Id") String Id, Model model) {
        List<CheckInfoPrint> checkInfoPrints = new ArrayList<>(10);
        CheckInfoPrint checkInfoPrint = null;
        CheckBill checkBill = null;//盘点单
        String[] IdArray = Id.split(",");
        if (IdArray.length > 0) {
            Map<String, Object> parmMap = new HashMap<>();
            for (String dId : IdArray) {
                checkInfoPrint = new CheckInfoPrint();
                checkBill = checkBillService.selectByPrimaryKey(Integer.parseInt(dId));
                parmMap.put("checkNo", checkBill.getCheckNo());
                List<CheckInfo> lCheckInfos = checkInfoService.selectByCriteria(parmMap);

                checkInfoPrint.setCheckNo(checkBill.getCheckNo());
                checkInfoPrint.setCheckInfos(lCheckInfos);
                checkInfoPrints.add(checkInfoPrint);
            }
        }


        JRDataSource jrDataSource = new JRBeanCollectionDataSource(checkInfoPrints);
        model.addAttribute("url", "/WEB-INF/jasper/checkBill.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "checkBillView"; // 对应jasper-defs.xml中的bean id
    }

    /**
     * 提交盘点报告
     *
     * @param request
     * @param response
     * @param files
     * @return
     */
    @RequestMapping(value = "/submitChecks", method = RequestMethod.POST)
    @ResponseBody
    public Object submitChecks(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile[] files,
                               @RequestParam(value = "description") String desc, @RequestParam(value = "idsStr") String idsStr) {
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(idsStr) || files == null || files.length < 1) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User curUser = super.getCurrentUser();
        OssFile ossFileTemp;// 提报的文件
        StringBuffer fileUrls = new StringBuffer(); // 已提报到oss服务器上的文件url
        try {
            OssService ossService = new OssService();
            for (MultipartFile file : files) {
                ossFileTemp = ossService.Ossupload(file);
                fileUrls.append(ossFileTemp.getUrl());
                fileUrls.append(",");
            }
            fileUrls.deleteCharAt(fileUrls.length()-1);
            String[] idStrArr = idsStr.split(",");
            List<Integer> ids = new ArrayList<>(idStrArr.length);
            for(String strTemp : idStrArr) {
                ids.add(Integer.valueOf(strTemp));
            }
            ServerStatus ss = checkInfoService.submitChecksReport(new SubmitChecksVo(curUser.getId(), curUser.getRealname(), desc, ids, fileUrls.toString()));
            json.put("status", ss.getStatus());
            json.put("message", ss.getMessage());
        } catch (IOException e) {
            json.put("status", "500");
            json.put("message", "图片上传失败");
            logger.error(Constant.LINE + "上传图片异常", e);
        } catch (WMSException e) {
            json.put("status", "500");
            json.put("message", "提报失败");
            logger.error(Constant.LINE + "提报异常", e);
        }
        return json;
    }


    @RequestMapping(value="/checkInfoExport",method = RequestMethod.POST)
    public void checkInfoExport(HttpServletRequest request,HttpServletResponse response){
        String columns=request.getParameter("columns");
        JSONArray columnJarr=JSONArray.fromObject(columns);
        Map<String, Object> parmaMap=new HashedMap();//Map<String, Object>) getSessionOfShiro().getAttribute(com.rmd.wms.web.constant.Constant.OUTSTOCK_LISTWHERE);
        LocationGoodsBind locationGoodsBind = new LocationGoodsBind();
        List<CheckInfo> list= checkInfoService.selectByCriteria(parmaMap);
        JsonConfig config = new JsonConfig();

        config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
        //config.registerJsonValueProcessor(StockOutBill.class, new StockOutBillProcessor());
        JSONArray dataJsonArray=JSONArray.fromObject(list,config);
        JSONArray resultArray=new JSONArray();
        net.sf.json.JSONObject json=null;
        int size=dataJsonArray.size();
        //盘点人信息
        List<CheckUser> uList=checkUserService.selectByCriteria(new HashedMap());
        CheckInfoVo ck=null;
        String checkUser="";//盘点人
        for(int i=0;i<size;i++){
            json=dataJsonArray.getJSONObject(i);
            json.put("orderNum",i+1);
            for(CheckUser u:uList){
                if(json.getString("checkNo").equals(u.getCheckNo())){
                    checkUser+=u.getUserName()+",";
                }
            }
            if(checkUser!=""){
                checkUser=checkUser.substring(0,checkUser.length()-1);
            }
            json.put("checkUserName",checkUser);
            checkUser="";
            resultArray.add(json);
        }
        try{
            ExcelCommon excelUtil = new ExcelCommon();
            File excel = excelUtil.dataToExcel(columnJarr, resultArray,"列表数据",request);
            DownloadUtils.download( request, response,  excel, "列表数据.xls");
        }catch(Exception e){
            logger.error("checkInfoExport：数据导出失败", e);
        }
    }

}
