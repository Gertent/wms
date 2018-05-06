package com.rmd.wms.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.StringUtil;
import com.rmd.commons.servutils.Notification;
import com.rmd.oms.entity.DeliverBill;
import com.rmd.oms.entity.OrderBase;
import com.rmd.oms.entity.vo.OperateUserVo;
import com.rmd.oms.entity.vo.OrderPackageInfoVo;
import com.rmd.oms.service.OrderBaseService;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.app.StockOutBillInfo;
import com.rmd.wms.bean.vo.web.*;
import com.rmd.wms.enums.*;
import com.rmd.wms.service.*;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.common.CodingUtil;
import com.rmd.wms.web.constant.Constant;
import com.rmd.wms.web.util.DateJsonValueProcessor;
import com.rmd.wms.web.util.DownloadUtils;
import com.rmd.wms.web.util.ExcelCommon;
import com.rmd.wms.web.util.PropertiesLoader;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ZXLEI
 * @ClassName: OutStockBillController
 * @Description: TODO(出库单)
 * @date Mar 17, 2017 9:19:05 AM
 */
@Controller
@RequestMapping(value = "/outstock")
public class OutStockBillController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(OutStockBillController.class);

    //出库单
    @Resource(name = "stockOutBillService")
    private StockOutBillService stockOutBillService;

    //出库单物流信息
    @Resource(name = "logisticsBillService")
    private LogisticsBillService logisticsBillService;

    //出库单收货人信息
    @Resource(name = "orderLogisticsInfoService")
    private OrderLogisticsInfoService orderLogisticsInfoService;

    //出库单商品
    @Resource(name = "stockOutInfoService")
    private StockOutInfoService stockOutInfoService;

    //出库单商品货位锁定
    @Resource(name = "locationGoodsBindOutService")
    private LocationGoodsBindOutService locationGoodsBindOutService;

    //商品货位绑定
    @Resource(name = "locationGoodsBindService")
    private LocationGoodsBindService locationGoodsBindService;

    //承运商
    @Resource(name = "logisticsCompanyService")
    private LogisticsCompanyService logisticsCompanyService; // 物流公司

    //OMS订单服务
    @Resource(name = "orderBaseService")
    private OrderBaseService orderBaseService;
    //发货单
    @Resource(name = "deliveryBillService")
    private DeliveryBillService deliveryBillService;
    //仓库
    @Resource(name = "warehouseService")
    private WarehouseService warehouseService;


    /**
     * 页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/OutStockBiLL")
    public ModelAndView jumpView(@RequestParam(value = "view") Integer view) {
        ModelAndView mv = new ModelAndView();
        String viewName = "";
        if (1 == view) {
            // 出库制单
            viewName = "outstock/outstockManage";
        } else if (2 == view) {
            // 打包复检
            viewName = "outstock/recheckManage";
        } else if (3 == view) {
            // 录入运单号
            viewName = "outstock/logisticsNumberManage";
        } else if (4 == view) {
            // 交接任务
            viewName = "outstock/finishManage";
        } else if (5 == view) {
            // 拣货计划
            viewName = "outstock/pickingManage";
        } else if (6 == view) {
            // 拣货异常
            viewName = "outstock/exceptionPackManage";
        }
        Integer wareId = super.getCurrentWareId();
        LogisticsCompany logisticsCompany = new LogisticsCompany();
        logisticsCompany.setWareId(wareId);
        logisticsCompany.setStatus(Constant.STATUS_ONE);
        List<LogisticsCompany> companies = logisticsCompanyService.selectByCriteria(logisticsCompany);
        mv.addObject("companies", companies);
        mv.setViewName(viewName);
        return mv;
    }


    /**
     * 查询所有拣货计划
     *
     * @return
     */
    @RequestMapping(value = "/ListPickBiLL")
    @ResponseBody
    public Map<String, Object> ListPickBiLL(@RequestParam(value = "page", required = false) Integer page
            , @RequestParam(value = "rows", required = false) Integer rows
            , @RequestParam(value = "starTime", required = false) String starTime
            , @RequestParam(value = "endTime", required = false) String endTime
            , @RequestParam(value = "logisComId", required = false) Integer logisComId
            , @RequestParam(value = "logisticsNo", required = false) String logisticsNo
            , @RequestParam(value = "startWeight", required = false) String startWeight
            , @RequestParam(value = "endWeight", required = false) String endWeight
            , @RequestParam(value = "orderNo", required = false) String orderNo
            , @RequestParam(value = "receivername", required = false) String receivername
            , @RequestParam(value = "orderEx", required = false) String orderEx
            , @RequestParam(value = "packEx", required = false) String packEx
            , @RequestParam(value = "flag", required = false) Integer flag) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //拣货单要查看订单商品是否可以生成拣货计划
            Map<String, Object> whereParams = new HashMap<String, Object>();
            Integer wareId = super.getCurrentWareId();
            whereParams.put("wareId", wareId);
            if (flag == 1) {
                // 拣货计划
                whereParams.put("pickingPlan_search", Constant.SEARCH_FLAG);
            }
            if (flag == 2) {
                // 拣货异常
                whereParams.put("pickingException_search", Constant.SEARCH_FLAG);
            }
            //缺货异常，下单缺货，拣货缺货复选框
            if ("0".equals(packEx) && "on".equals(orderEx)) {
                whereParams.put("pickingStatus", PickingStatus.A000.getValue());
            }
            if ("4".equals(orderEx) && "on".equals(packEx)) {
                whereParams.put("pickingStatus", PickingStatus.A004.getValue());
            }
            // 订单时间大于指定时间
            if (StringUtil.isNotEmpty(starTime)) {
                whereParams.put("orderdate_gt", starTime);
            }
            // 订单时间小于指定时间
            if (StringUtil.isNotEmpty(endTime)) {
                whereParams.put("orderdate_lt", endTime);
            }
            // 订单重量大于开始重量
            if (StringUtil.isNotEmpty(startWeight)) {
                whereParams.put("weight_gt", startWeight);
            }
            // 订单重量小于开始重量
            if (StringUtil.isNotEmpty(endWeight)) {
                whereParams.put("weight_lt", endWeight);
            }
            if (StringUtil.isNotEmpty(orderNo)) {
                whereParams.put("orderNo", orderNo);
            }
            // 承运商ID
            if (null != logisComId) {
                if (logisComId != 0) {
                    whereParams.put("logisComId", logisComId);
                }
            }
            if (StringUtil.isNotEmpty(logisticsNo)) {
                whereParams.put("logisticsNo", logisticsNo);
            }
            whereParams.put("name_sort","orderdate");
            whereParams.put("order_sort", SortType.DESC.getValue());
            PageBean<StockOutBill> pageBean = stockOutBillService.ListStockOutBills(page, rows, whereParams);
            List<StockOutBill> pList = pageBean.getList();
            map.put("total", pageBean.getTotal());
            map.put("rows", pList);
            JsonConfig config = new JsonConfig();
            config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
            JSONObject json = JSONObject.fromObject(map, config);
            // 格式化时间后填入返回结果
            map.put("datarow", json);
            getSessionOfShiro().setAttribute(Constant.OUTSTOCK_LISTWHERE, whereParams);
        } catch (Exception e) {
            logger.error("ListPickBiLL：捡货计划查询异常！" + com.rmd.wms.constant.Constant.LINE, e);
        }
        return map;
    }

    /**
     * 查询所有出库单
     *
     * @return
     */
    @RequestMapping(value = "/ListOutStockBiLL")
    @ResponseBody
    public Map<String, Object> listBiLL(@RequestParam(value = "page", required = false) Integer page
            , @RequestParam(value = "rows", required = false) Integer rows
            , @RequestParam(value = "starTime", required = false) String starTime
            , @RequestParam(value = "endTime", required = false) String endTime
            , @RequestParam(value = "logisComId", required = false) Integer logisComId
            , @RequestParam(value = "logisticsNo", required = false) String logisticsNo
            , @RequestParam(value = "startWeight", required = false) String startWeight
            , @RequestParam(value = "endWeight", required = false) String endWeight
            , @RequestParam(value = "orderNo", required = false) String orderNo
            , @RequestParam(value = "deliveryNo", required = false) String deliveryNo
            , @RequestParam(value = "receivername", required = false) String receivername
            , @RequestParam(value = "dobinningPrint", required = false) String dobinningPrint
            , @RequestParam(value = "dopickingPrint", required = false) String dopickingPrint
            , @RequestParam(value = "dowaybillPrint", required = false) String dowaybillPrint
            , @RequestParam(value = "pickingStatus", required = false) String pickingStatus
            , @RequestParam(value = "flag", required = false) Integer flag
            , @RequestParam(value = "noLogisticsNo", required = false) String noLogisticsNo,
                                        @RequestParam(value = "status", required = false) String status, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Map<String, Object> whereParams = new HashMap<String, Object>();
            Integer wareId = super.getCurrentWareId();
            whereParams.put("wareId", wareId);
            if (1 == flag) {
                // 出库制单条件查询
                whereParams.put("outStockBill_search", Constant.SEARCH_FLAG);
            } else if (2 == flag) {
                // 打包复检条件查询，(recheck_status!=3,打包复检！=3)
                whereParams.put("recheck_search", Constant.SEARCH_FLAG);
            } else if (3 == flag) {
                // 录入运单号，(recheck_status=3,打包复检完成)
                whereParams.put("recheckAdLogistics_search", Constant.SEARCH_FLAG);
            } else if (4 == flag) {
                // 交接任务条件查询，订单会有订单号
                whereParams.put("transferTask_search", Constant.SEARCH_FLAG);
            }
            // 订单时间大于指定时间
            if (StringUtil.isNotEmpty(starTime)) {
                whereParams.put("orderdate_gt", starTime);
            }
            // 订单时间小于指定时间
            if (StringUtil.isNotEmpty(endTime)) {
                whereParams.put("orderdate_lt", endTime);
            }
            // 订单重量大于开始重量
            if (StringUtil.isNotEmpty(startWeight)) {
                whereParams.put("weight_gt", startWeight);
            }
            // 订单重量小于开始重量
            if (StringUtil.isNotEmpty(endWeight)) {
                whereParams.put("weight_lt", endWeight);
            }
            if (StringUtil.isNotEmpty(orderNo)) {
                whereParams.put("orderNo", orderNo);
            }
            if (StringUtil.isNotEmpty(deliveryNo)) {
                whereParams.put("deliveryNo", deliveryNo);
            }
            // 承运商ID
            if (null != logisComId) {
                if (logisComId != 0) {
                    whereParams.put("logisComId", logisComId);
                }
            }
            //无运单号
            if (YesAdNoFlag.A000.getValue().toString().equals(noLogisticsNo)) {
                whereParams.put("noLogisticsNo_search", Constant.SEARCH_FLAG);
            }
            // 装箱单未打印
            if (YesAdNoFlag.A000.getValue().toString().equals(dobinningPrint)) {
                whereParams.put("dobinningPrint", dobinningPrint);
            }
            // 拣货单未打印
            if (YesAdNoFlag.A000.getValue().toString().equals(dopickingPrint)) {
                whereParams.put("dopickingPrint", dopickingPrint);
            }
            // 运单未打印
            if (YesAdNoFlag.A000.getValue().toString().equals(dowaybillPrint)) {
                whereParams.put("dowaybillPrint", dowaybillPrint);
            }
            // 拣货缺货
            if ("4".equals(pickingStatus)) {
                whereParams.put("pickingStatus", pickingStatus);
            }
            if (StringUtil.isNotEmpty(logisticsNo)) {
                whereParams.put("logisticsNo", logisticsNo);
            }
            if (StringUtil.isNotEmpty(status) && !"-1".equals(status)) {
                if ("-12104".equals(status)) {//未交接状态
                    whereParams.put("unTransferStatus_search", Constant.SEARCH_FLAG);
                } else {
                    whereParams.put("status", status);
                }
            }
            whereParams.put("name_sort","orderdate");
            whereParams.put("order_sort", SortType.DESC.getValue());
            PageBean<StockOutBill> pageBean = stockOutBillService.ListStockOutBills(page, rows, whereParams);
            List<StockOutBill> pList = pageBean.getList();
            map.put("total", pageBean.getTotal());
            map.put("rows", pList);
            JsonConfig config = new JsonConfig();
            config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
            JSONObject json = JSONObject.fromObject(map, config);
            // 格式化时间后填入返回结果
            map.put("datarow", json);
            getSessionOfShiro().setAttribute(Constant.OUTSTOCK_LISTWHERE, whereParams);
        } catch (Exception e) {
            logger.error("ListOutStockBiLL：出库单查询异常！" + com.rmd.wms.constant.Constant.LINE, e);
        }
        return map;
    }

    /**
     * 查询出库单下的商品列表
     *
     * @return
     */
    @RequestMapping(value = "/OutstockBiLLInfo")
    public ModelAndView outstockBiLLInfo(@RequestParam(value = "Id", required = false) Integer Id, HttpServletRequest request) {
        DeliveryBill deliveryBill = null;
        LogisticsCompany lCompany = null;
        ModelAndView mv = new ModelAndView("outstock/outstockInfo");
        StockOutBill stockOutBill = new StockOutBill();  //出库单
        LogisticsBill logisticsBill = new LogisticsBill(); //出库单物流信息
        OrderLogisticsInfo orderLogisticsInfo = new OrderLogisticsInfo(); //出库单收货人信息
        List<StockOutGoodsInfo> StockOutGoodsList = new ArrayList<StockOutGoodsInfo>(10);
        Integer wareId = super.getCurrentWareId();
        try {
            if (Id > 0) {
                stockOutBill = stockOutBillService.selectByPrimaryKey(Id);
            }
            if (!stockOutBill.equals(null)) {
                logisticsBill = logisticsBillService.selectByOrderNo(stockOutBill.getOrderNo());
                orderLogisticsInfo = orderLogisticsInfoService.selectByOrderNo(stockOutBill.getOrderNo());
                Map<String, Object> whereParams = new HashMap<String, Object>();
                whereParams.put("wareId", wareId);
                whereParams.put("orderNo", stockOutBill.getOrderNo());
                StockOutGoodsList = stockOutInfoService.ListStockOutGoodsInfo(whereParams);
                //发货单
                Map<String, Object> parmaMap = new HashMap<>();
                parmaMap.put("deliveryNo", stockOutBill.getDeliveryNo());
                parmaMap.put("wareId", stockOutBill.getWareId());
                List<DeliveryBill> dList = deliveryBillService.ListDeliveryBills(parmaMap);
                if (dList.size() > 0) {
                    deliveryBill = dList.get(0);
                }
                //承运商
                lCompany = logisticsCompanyService.selectByPrimaryKey(stockOutBill.getLogisComId());
            }
        } catch (Exception e) {
            logger.error("OutstockBiLLInfo：出库单商品查询异常！" + com.rmd.wms.constant.Constant.LINE, e);
        }
        mv.addObject("lCompany", lCompany);
        mv.addObject("deliveryBill", deliveryBill);
        mv.addObject("stockOutBill", stockOutBill);
        mv.addObject("logisticsBill", logisticsBill);
        mv.addObject("orderLogisticsInfo", orderLogisticsInfo);
        mv.addObject("StockOutGoodsList", StockOutGoodsList);

        return mv;
    }

    /**
     * 出库单---修改承运商
     *
     * @return
     */
    @RequestMapping("/editLogisticsCompany")
    @ResponseBody
    public String editLogisticsCompany(HttpServletRequest request) {
        String id = request.getParameter("id");
        String logisComId = request.getParameter("logisComId");
        String logisComName = request.getParameter("logisComName");

        String result = "false";
        try {
            Integer flag = 0;
            String[] arr = id.split(",");
            for (String idx : arr) {
                StockOutBill stockOutBill = new StockOutBill();
                stockOutBill.setId(Integer.parseInt(idx));
                stockOutBill.setLogisComId(Integer.parseInt(logisComId));
                stockOutBill.setLogisComName(logisComName);
                flag = stockOutBillService.updateByPrimaryKeySelective(stockOutBill);
            }
            if (flag > 0) {
                result = "true";
            }
        } catch (Exception e) {
            logger.error("editLogisticsCompany：修改承运商异常！" + com.rmd.wms.constant.Constant.LINE, e);
        }
        return result;
    }

    /**
     * 运单号check
     *
     * @param stockOutBill
     * @return
     */
    @RequestMapping("/CheckLogistics")
    @ResponseBody
    public String checkLogistics(StockOutBill stockOutBill) {

        String result = "false";
        try {
            StockOutBill soBill = stockOutBillService.selectByPrimaryKey(stockOutBill.getId());
            Map<String, Object> parmaMap = new HashMap<String, Object>();
            parmaMap.put("logisticsNo", stockOutBill.getLogisticsNo());
            parmaMap.put("logisComId", soBill.getLogisComId());
            parmaMap.put("id_neq", stockOutBill.getId());
            List<StockOutBill> list = stockOutBillService.ListStockOutBills(parmaMap);
            if (list.size() > 0) {
                result = "true";
            }

        } catch (Exception e) {
            logger.error("CheckLogistics：运单号check异常！" + com.rmd.wms.constant.Constant.LINE, e);
        }
        return result;
    }

    /**
     * 录入运单号
     *
     * @param stockOutBill
     * @return
     */
    @RequestMapping("/EditLogistics")
    @ResponseBody
    public String editLogistics(StockOutBill stockOutBill) {

        String result = "false";
        if (stockOutBill == null || stockOutBill.getId() == null || StringUtils.isBlank(stockOutBill.getLogisticsNo())) {
            return result;
        }
        // 录入运单号之后到承运交接节点
        try {
            // 添加录入运单号操作人和时间
            stockOutBill.setInLogisticsUser(super.getCurrentUserId());
            stockOutBill.setInLogisticsTime(new Date());
            Integer flag = stockOutBillService.updateByPrimaryKeySelective(stockOutBill);
            if (flag > 0) {
                result = "true";
                StockOutBill sBill = stockOutBillService.selectByPrimaryKey(stockOutBill.getId());
                DeliverBill deliverBill = new DeliverBill();
                deliverBill.setBillnumber(sBill.getLogisticsNo());
                // 添加物流公司名称和url
                if (sBill != null && sBill.getLogisComId() != null) {
                    LogisticsCompany lc = logisticsCompanyService.selectByPrimaryKey(sBill.getLogisComId());
                    deliverBill.setCompanyid(sBill.getLogisComId());
                    deliverBill.setCompanyname(lc.getName());
                    deliverBill.setUrl(lc.getUrl());
                }
                deliverBill.setCreateTime(new Date());
                deliverBill.setOrdernumber(sBill.getOrderNo());
                deliverBill.setRepositoryName(sBill.getWareName());
                OperateUserVo operUser = new OperateUserVo();
                operUser.setUserId(super.getCurrentUser().getId());
                operUser.setUserName(super.getCurrentUserName());
                //与oms通信，传递运单信息
                logger.info("录入运单与oms通信begin------------");
                Notification<Boolean> notification = orderBaseService.saveDeliverInfo(deliverBill, operUser);
                logger.info("录入运单与oms通信end  返回结果：------------" + notification.getNotifCode() + "---" + notification.getResponseData());
            }
        } catch (Exception e) {
            logger.error("EditLogistics：录入运单报错！" + com.rmd.wms.constant.Constant.LINE, e);
        }
        return result;
    }


    /**
     * 出库制单，查询商品上架锁定货位数量是否可以生成拣货计划
     * 根据商品code和仓库id去location_goods_bind(商品货位绑定表)
     * 查询该商品的售卖类型字段为可售卖，并且货位可用数量大于订单的数量，锁库形成拣货计划(t_location_goods_bind_out)
     * 如货位可用数量小于订单数量，锁定当前剩余库存数量，并生成锁定缺省数量数据记录，形成拣货异常(t_location_goods_bind_out)
     * 如货位可用数量为0，锁定缺省数量数据记录货位值为-1(缺货)
     */
    @RequestMapping(value = "/LockGoodBindNum", method = RequestMethod.POST)
    @ResponseBody
    public String lockGoodBindNum(@RequestParam(value = "Id", required = false) Integer Id, HttpServletRequest request) {
        //出库商品和货位绑定集合
        List<LocationGoodsBindOut> lOutsList = new ArrayList<LocationGoodsBindOut>(10);
        String hasLock = "false";
        try {
            StockOutBill stockOutBill = stockOutBillService.selectByPrimaryKey(Id);
            //订单商品总数量
            List<StockOutInfo> stockOutInfosList = new ArrayList<StockOutInfo>(10);
            //查询订单下所有的商品
            Integer wareId = super.getCurrentWareId();
            Map<String, Object> parmaMap = new HashMap<String, Object>();
            parmaMap.put("orderNo", stockOutBill.getOrderNo());
            parmaMap.put("wareId", wareId);
            stockOutInfosList = stockOutInfoService.ListStockOutInfos(parmaMap);
            List<LocationGoodsBindOut> goodsOutBindList = locationGoodsBindOutService.selectByOrderNoCode(parmaMap);
            if (goodsOutBindList.size() <= 0) {
                hasLock = "true";
                Boolean outBillFlag = true;

                for (StockOutInfo stockOutInfo : stockOutInfosList) {
                    String code = stockOutInfo.getGoodsCode();
                    LocationGoodsBind locationGoodsBind = new LocationGoodsBind();
                    if (code != null) {
                        //查询上架商品和货位绑定
                        locationGoodsBind.setGoodsCode(code);
                        locationGoodsBind.setSaleType(1);//售卖类型 1:可售卖 ,0:不可售卖
                        locationGoodsBind.setValidityNum(1);
                        List<LocationGoodsBind> goodsBinds = locationGoodsBindService.selectByCriteria(locationGoodsBind);
                        if (goodsBinds.size() > 0) {
                            //绑定货位可用数量
                            Integer validitySumNum = 0;
                            for (LocationGoodsBind goodsBind : goodsBinds) {

                                //已锁数量小于订单数量，继续
                                if (validitySumNum < stockOutInfo.getStockOutNum()) {
                                    LocationGoodsBindOut lGoodsBindOut = new LocationGoodsBindOut();
                                    lGoodsBindOut.setGinfoId(Id);
                                    lGoodsBindOut.setOrderNo(stockOutBill.getOrderNo());
                                    lGoodsBindOut.setGoodsCode(goodsBind.getGoodsCode());
                                    lGoodsBindOut.setGoodsBarCode(goodsBind.getGoodsBarCode());
                                    lGoodsBindOut.setValidityTime(goodsBind.getValidityTime());
                                    lGoodsBindOut.setSortNum(1);//拣货权重
                                    lGoodsBindOut.setWareId(goodsBind.getWareId());
                                    lGoodsBindOut.setWareName(goodsBind.getWareName());
                                    lGoodsBindOut.setAreaId(goodsBind.getAreaId());
                                    lGoodsBindOut.setAreaName(goodsBind.getAreaName());
                                    lGoodsBindOut.setCreateTime(new Date());
                                    lGoodsBindOut.setLocationNo(goodsBind.getLocationNo());//绑定货位
                                    //一个货位上的可用商品数量满足订单数量,锁定对应商品sku的数量
                                    //goodsBind.getValidityNum()  货位可用商品数量
                                    //stockOutInfo.getStockOutNum()   订单数量
                                    if (goodsBind.getValidityNum() >= stockOutInfo.getStockOutNum()) {
                                        if (validitySumNum != 0) {
                                            lGoodsBindOut.setLockedNum(stockOutInfo.getStockOutNum() - validitySumNum);//锁定库存数量
                                            goodsBind.setValidityNum(goodsBind.getValidityNum() - (stockOutInfo.getStockOutNum() - validitySumNum));//可用数量-sku下单数量
                                            validitySumNum = stockOutInfo.getStockOutNum();
                                        } else {
                                            lGoodsBindOut.setLockedNum(stockOutInfo.getStockOutNum());//锁定库存数量
                                            validitySumNum += stockOutInfo.getStockOutNum();
                                            goodsBind.setValidityNum(goodsBind.getValidityNum() - stockOutInfo.getStockOutNum());//可用数量-sku下单数量
                                        }
                                    } else if (goodsBind.getValidityNum() < stockOutInfo.getStockOutNum()) {//一个货位上的可用商品数量不能满足订单数量
                                        //第二次(已经绑定多次货位)
                                        if (validitySumNum != 0) {
                                            //绑定数量=订单数量-已经锁定数量
                                            lGoodsBindOut.setLockedNum(stockOutInfo.getStockOutNum() - validitySumNum);//锁定库存数量
                                            //当前货位绑定后可用数量          =    当前货位绑定前可用数量                 -(              订单数量                       -订单已经在其他货位上绑定的数量)
                                            goodsBind.setValidityNum(goodsBind.getValidityNum() - (stockOutInfo.getStockOutNum() - validitySumNum));//可用数量-锁定数量
                                            validitySumNum += stockOutInfo.getStockOutNum() - validitySumNum;//已锁定商品数量
                                        } else {
                                            //第一次锁定该货位上的所有可用数量
                                            lGoodsBindOut.setLockedNum(goodsBind.getValidityNum());//锁定库存数量
                                            //保留该货位上已锁定的数量，用户第二次锁定其他货位数量的依据
                                            validitySumNum += goodsBind.getValidityNum();
                                            goodsBind.setValidityNum(goodsBind.getValidityNum() - goodsBind.getValidityNum());//可用数量-锁定数量
                                        }
                                    } else if (goodsBind.getValidityNum() == 0) {
                                        Integer lostNum = stockOutBill.getGoodsAmount() - validitySumNum;//缺省数量
                                        lGoodsBindOut.setLocationNo("-1");//缺货
                                        lGoodsBindOut.setLockedNum(lostNum);
                                        validitySumNum += lostNum;
                                    }
                                    lOutsList.add(lGoodsBindOut);
                                    //修改t_location_goods_bind表的可用数量
                                    locationGoodsBindService.updateByPrimaryKey(goodsBind);
                                }

                            }
                            //如果当前所有绑定数量小于订单商品数量，应记录当前订单缺少锁定数量
                            if (validitySumNum < stockOutInfo.getStockOutNum()) {
                                LocationGoodsBindOut lGoodsBindOut = new LocationGoodsBindOut();
                                lGoodsBindOut.setGinfoId(Id);
                                lGoodsBindOut.setOrderNo(stockOutBill.getOrderNo());
                                lGoodsBindOut.setGoodsCode(stockOutInfo.getGoodsCode());
                                lGoodsBindOut.setGoodsBarCode(stockOutInfo.getGoodsBarCode());
                                lGoodsBindOut.setValidityTime(stockOutInfo.getValidityTime());
                                lGoodsBindOut.setSortNum(1);//拣货权重
                                lGoodsBindOut.setWareId(stockOutInfo.getWareId());
                                lGoodsBindOut.setWareName(stockOutInfo.getWareName());
                                lGoodsBindOut.setCreateTime(new Date());
                                Integer lostNum = stockOutInfo.getStockOutNum() - validitySumNum;//缺省数量
                                lGoodsBindOut.setLocationNo("-1");//缺货
                                lGoodsBindOut.setLockedNum(lostNum);
                                validitySumNum += lostNum;
                                lOutsList.add(lGoodsBindOut);
                                outBillFlag = false;
                                hasLock = "true";
                                //此处不需要修改t_location_goods_bind表的可用数量，可用数量已经为0
                            }

                        } else {
                            //货位没有可用数量
                            LocationGoodsBindOut lGoodsBindOut = new LocationGoodsBindOut();
                            lGoodsBindOut.setGinfoId(Id);
                            lGoodsBindOut.setOrderNo(stockOutBill.getOrderNo());
                            lGoodsBindOut.setLocationNo("-1");//缺货显示
                            lGoodsBindOut.setGoodsCode(stockOutInfo.getGoodsCode());
                            lGoodsBindOut.setGoodsBarCode(stockOutInfo.getGoodsBarCode());
                            lGoodsBindOut.setValidityTime(stockOutInfo.getValidityTime());
                            lGoodsBindOut.setSortNum(1);//拣货权重
                            lGoodsBindOut.setWareId(stockOutInfo.getWareId());
                            lGoodsBindOut.setWareName(stockOutInfo.getWareName());
                            lGoodsBindOut.setCreateTime(new Date());
                            lGoodsBindOut.setLockedNum(stockOutInfo.getStockOutNum());
                            lOutsList.add(lGoodsBindOut);
                            outBillFlag = false;
                        }
                        for (LocationGoodsBindOut locationGoodsBindOut : lOutsList) {
                            //logger.info("货位"+locationGoodsBindOut.getLocationNo() +"锁定数量"+locationGoodsBindOut.getLockedNum()+"商品名称"+locationGoodsBindOut.getGoodsCode());
                        }
                    } else {
                        logger.info("没有商品编码");
                    }
                }//for-end

                if (lOutsList.size() > 0) {
                    //批量添加缺货锁库
                    locationGoodsBindOutService.insertBatch(lOutsList);
                    //修改主出库单状态
                    if (outBillFlag) {
                        //可以生成拣货计划的单子
                        stockOutBill.setPickingStatus(1);//0：异常，1：待拣货，2：拣货中，3：已完成，4：缺货
                        stockOutBill.setStatus(12101);//12101-拣货，12102-打包复检，12103-录入运单号，12104-交接发货
                    } else {
                        stockOutBill.setPickingStatus(4);
                    }
                    stockOutBillService.updateByPrimaryKey(stockOutBill);
                }
            }//锁库条件判断if--end
            else {
                //已经锁库的商品
                hasLock = "true";
            }
        } catch (Exception e) {
            logger.error("LockGoodBindNum：锁库报错！" + com.rmd.wms.constant.Constant.LINE, e);
        }
        return hasLock;
    }

    /**
     * 获取承运公司列表
     *
     * @return
     */
    @RequestMapping(value = "/getLogisticsCompany", produces = "application/json;charset=utf-8")
    @ResponseBody
    public List<LogisticsCompany> getLogisticsCompany() {
        List<LogisticsCompany> companies = null;
        try {
            LogisticsCompany logisticsCompany = new LogisticsCompany();
            logisticsCompany.setWareId(super.getCurrentWareId());
            // 查询可用的物流公司
            logisticsCompany.setStatus(com.rmd.wms.constant.Constant.TYPE_STATUS_YES);
            companies = logisticsCompanyService.selectByCriteria(logisticsCompany);
        } catch (Exception e) {
            logger.error("getLogisticsCompany:获取承运商异常" + com.rmd.wms.constant.Constant.LINE, e);
        }
        return companies;
    }

    /**
     * 拣货 单批量打印方法
     *
     * @param Id
     * @param model
     * @return 存在问题，仓库需要动态变化，添加packagenum(规格)
     */
    @RequestMapping(value = "/packBillPrint", method = RequestMethod.GET)
    public String packBillPrint(@RequestParam(value = "Id") String Id, Model model) {
        List<PackBillPrint> packBillPrintsList = new ArrayList<PackBillPrint>(10);
        try {
            StockOutBill stockOutBill = new StockOutBill();  //出库单
            List<StockOutInfo> stockOutInfos = new ArrayList<StockOutInfo>(10);
            //Id="211,227";
            //承运单批量打印Id
            String[] IdArray = Id.split(",");
            if (IdArray.length > 0) {
                for (String dId : IdArray) {
                    PackBillPrint packBillPrint = new PackBillPrint();
                    stockOutBill = stockOutBillService.selectByPrimaryKey(Integer.valueOf(dId));
                    if (stockOutBill != null) {
                        Map<String, Object> whereParams = new HashMap<String, Object>();

                        whereParams.put("orderNo", stockOutBill.getOrderNo());
                        stockOutInfos = stockOutInfoService.ListStockOutInfos(whereParams);
                        List<PackBill> packList = new ArrayList<PackBill>(10);
                        int lockedSum = 0;
                        for (StockOutInfo info : stockOutInfos) {
                            whereParams.put("goodsCode", info.getGoodsCode());
                            List<LocationGoodsBindOut> loList = locationGoodsBindOutService.selectByOrderNoCode(whereParams);
                            for (LocationGoodsBindOut loBindOut : loList) {
                                PackBill packBill = new PackBill();
                                BeanUtils.copyProperties(info, packBill);
                                packBill.setLocationNo(loBindOut.getLocationNo());
                                packBill.setLockedNum(loBindOut.getLockedNum());
                                packList.add(packBill);
                                lockedSum += loBindOut.getLockedNum();
                            }
                        }
                        packBillPrint.setStockOutInfos(packList);
                        packBillPrint.setOrderNo(stockOutBill.getOrderNo());
                        packBillPrint.setWareName(super.getCurrentWare().getWareName() + "-拣货单");
                        packBillPrint.setBillNum(lockedSum);
                    }
                    packBillPrintsList.add(packBillPrint);
                }

            }
        } catch (Exception e) {
            logger.error("packBillPrint:拣货单批量打印异常" + com.rmd.wms.constant.Constant.LINE, e);
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(packBillPrintsList);
        model.addAttribute("url", "/WEB-INF/jasper/packGoodsBill.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "packBillView"; // 对应jasper-defs.xml中的bean id
    }

    /**
     * 装箱 单批量打印方法
     *
     * @param Id
     * @param model
     * @return
     */
    @RequestMapping(value = "/inBoxBillPrint", method = RequestMethod.GET)
    public String inBoxBillPrint(@RequestParam(value = "Id") String Id, Model model, HttpServletRequest request) {
        PropertiesLoader propertiesLoader =
                new PropertiesLoader("classpath:/application.properties");
        List<InBoxBillPrint> packBillPrintsList = new ArrayList<InBoxBillPrint>(10);
        try {
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StockOutBill stockOutBill = new StockOutBill();  //出库单
            List<StockOutInfo> stockOutInfos = new ArrayList<StockOutInfo>(10);
            String[] IdArray = Id.split(",");
            if (IdArray.length > 0) {
                for (String dId : IdArray) {
                    InBoxBillPrint inBoxBillPrint = new InBoxBillPrint();
                    inBoxBillPrint.setLogoPth(basePath + propertiesLoader.getProperty("logo.path"));
                    stockOutBill = stockOutBillService.selectByPrimaryKey(Integer.valueOf(dId));
                    if (stockOutBill != null) {
                        Map<String, Object> whereParams = new HashMap<String, Object>();
                        whereParams.put("orderNo", stockOutBill.getOrderNo());
                        stockOutInfos = stockOutInfoService.ListStockOutInfos(whereParams);
                        List<InBoxBill> packList = new ArrayList<InBoxBill>(10);
                        for (StockOutInfo info : stockOutInfos) {
                            whereParams.put("goodsCode", info.getGoodsCode());
                            List<LocationGoodsBindOut> loList = locationGoodsBindOutService.selectByOrderNoCode(whereParams);
                            for (LocationGoodsBindOut loBindOut : loList) {
                                InBoxBill inBoxBill = new InBoxBill();
                                BeanUtils.copyProperties(info, inBoxBill);
                                inBoxBill.setLocationNo(loBindOut.getLocationNo());
                                inBoxBill.setLockedNum(loBindOut.getLockedNum());
                                packList.add(inBoxBill);
                            }

                        }
                        //查询收货人信息
                        OrderLogisticsInfo orderLogisticsInfo = orderLogisticsInfoService.selectByOrderNo(stockOutBill.getOrderNo());
                        //订单详情
                        OrderPackageInfoVo orderPackageInfoVo = orderBaseService.selectOrderPackageInfo(stockOutBill.getOrderNo());

                        inBoxBillPrint.setStockOutInfos(packList);
                        inBoxBillPrint.setOrderNo(stockOutBill.getOrderNo());
                        inBoxBillPrint.setGoodsSum(stockOutBill.getGoodsSum());
                        if (orderPackageInfoVo != null) {
                            OrderBase orderBase = orderPackageInfoVo.getOrderBase();
                            inBoxBillPrint.setDeliveryFee(orderBase.getFreight());//运费
                            inBoxBillPrint.setDiscount(orderBase.getDiscount());//(orderBase.getGoodspricesum().subtract(orderBase.getOrdersum()));//优惠
                            inBoxBillPrint.setTotalFee(inBoxBillPrint.getGoodsSum().subtract(inBoxBillPrint.getDeliveryFee()).subtract(inBoxBillPrint.getDiscount()));//应付总金额
                        }
                        inBoxBillPrint.setOrderDate(sdf.format(stockOutBill.getOrderdate()));
                        if (orderLogisticsInfo != null) {
                            inBoxBillPrint.setReceiveName(orderLogisticsInfo.getReceivername());
                            inBoxBillPrint.setReceiveMobile(orderLogisticsInfo.getReceiveMobile());
                        }
                    }
                    if (inBoxBillPrint.getStockOutInfos() != null && inBoxBillPrint.getStockOutInfos().size() > 0) {
                        packBillPrintsList.add(inBoxBillPrint);
                    }
                }

            }
        } catch (Exception e) {
            logger.error("inBoxBillPrint:装箱 单批量打印异常" + com.rmd.wms.constant.Constant.LINE, e);
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(packBillPrintsList);
        model.addAttribute("url", "/WEB-INF/jasper/inboxBill.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "inBoxBillView"; // 对应jasper-defs.xml中的bean id
    }

    /**
     * 运单批量打印方法
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/logisticsBillPrintIpt", method = RequestMethod.GET)
    public String logicBillPrint(@RequestParam(value = "ids") String ids, Model model) {
        List<LogisticsBillPrint> logisticsBillPrints = new ArrayList<LogisticsBillPrint>(10);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StockOutBill stockOutBill = new StockOutBill();  //出库单
            String[] IdArray = ids.split(",");
            if (IdArray.length > 0) {
                for (String dId : IdArray) {
                    LogisticsBillPrint logisticsBillPrint = new LogisticsBillPrint();
                    //出库单
                    stockOutBill = stockOutBillService.selectByPrimaryKey(Integer.valueOf(dId));
                    if (stockOutBill != null) {
                        //查询收货人信息
                        OrderLogisticsInfo orderLogisticsInfo = orderLogisticsInfoService.selectByOrderNo(stockOutBill.getOrderNo());
                        //订单详情
                        //OrderPackageInfoVo orderPackageInfoVo=  orderBaseService.selectOrderPackageInfo(stockOutBill.getOrderNo());
                        //仓库信息
                        Warehouse warehouse = warehouseService.selectByPrimaryKey(stockOutBill.getWareId());

                        //寄件人信息
                        if (warehouse != null) {
                            logisticsBillPrint.setSenderFromName(warehouse.getContactName());        //寄件人姓名
                            logisticsBillPrint.setSenderAddr(warehouse.getAddress());                //寄件地址
                            logisticsBillPrint.setSenderMobile(warehouse.getContactTel());            //电话
                            logisticsBillPrint.setSenderCompanyName("猎鹰全球国际电子商务有限公司");
                        }
                        //收件人信息
                        if (orderLogisticsInfo != null) {
                            logisticsBillPrint.setToName("张小霞");        //收件人姓名orderLogisticsInfo.getReceivername()
                            logisticsBillPrint.setToProvince("河北省");        //省orderLogisticsInfo.getProvName()
                            logisticsBillPrint.setToCity("保定市");            //市orderLogisticsInfo.getCityName()
                            logisticsBillPrint.setToDistrict("雄安县");//县/区orderPackageInfoVo.getOrderReceiveAddress().getArea()
                            logisticsBillPrint.setToAddr("雄安县皇军大街胜利大厦三单元502");    //详细地址orderLogisticsInfo.getDetailedAddress()
                            logisticsBillPrint.setToMobile("13866995577");    //手机orderLogisticsInfo.getReceiveMobile()
                            logisticsBillPrint.setToTel(orderLogisticsInfo.getReceiveTel());        //电话
                        }
                        //物品信息
                        logisticsBillPrint.setQuanlity(stockOutBill.getGoodsAmount());            //数量
                        logisticsBillPrint.setWeight(new BigDecimal(stockOutBill.getWeight() == null ? BigDecimal.ZERO.toString() : stockOutBill.getWeight().toString()));    //重量
                    }
                    logisticsBillPrints.add(logisticsBillPrint);
                }

            }
        } catch (Exception e) {
            logger.error("logisticsBillPrintIpt:运单批量打印异常" + com.rmd.wms.constant.Constant.LINE, e);
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(logisticsBillPrints);
        model.addAttribute("url", "/WEB-INF/jasper/logisticsBill.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "logisticsBillView"; // 对应jasper-defs.xml中的bean id
    }

    /**
     * 打包复检列表
     *
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/recheckPage")
    public ModelAndView recheckPage(@RequestParam(value = "orderNo", required = false) String orderNo, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("outstock/recheckInfo");
        try {
            StockOutBill stockOutBill = stockOutBillService.selectByOrderNo(orderNo);
            StockOutBillInfo stockOutBillInfo = stockOutBillService.getRecheckTask(stockOutBill);
            mv.addObject("StockOutGoodsList", stockOutBillInfo.getSoInfos());
            mv.addObject("orderNo", orderNo);
        } catch (Exception e) {
            logger.error("recheckPage:打包复检异常" + com.rmd.wms.constant.Constant.LINE, e);
        }
        return mv;
    }

    /**
     * 打包复检
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/recheckConfirm")
    @ResponseBody
    public Object recheckConfirm(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "orderNo", required = false) String orderNo,
                                 @RequestParam(value = "parcelWeight", required = false) String parcelWeight,
                                 @RequestParam(value = "weightUnit", required = false) String weightUnit) {
        JSONObject json = new JSONObject();
        try {
            StockOutBill param = new StockOutBill();
            param.setOrderNo(orderNo);
            param.setParcelWeight(Double.parseDouble(parcelWeight));
            ServerStatus serverStatus = stockOutBillService.doRechecking(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
        }
        return json;
    }

    @RequestMapping(value = "/outStockExport", method = RequestMethod.POST)
    public void outStockExport(HttpServletRequest request, HttpServletResponse response) {
        String columns = request.getParameter("columns");
        JSONArray columnJarr = JSONArray.fromObject(columns);
        Map<String, Object> parmaMap = (Map<String, Object>) getSessionOfShiro().getAttribute(Constant.OUTSTOCK_LISTWHERE);
        List<StockOutBill> list = stockOutBillService.ListStockOutBills(parmaMap);
        JsonConfig config = new JsonConfig();

        config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
        //config.registerJsonValueProcessor(StockOutBill.class, new StockOutBillProcessor());
        JSONArray dataJsonArray = JSONArray.fromObject(list, config);
        JSONArray resultArray = new JSONArray();
        JSONObject json = null;
        int size = dataJsonArray.size();
        for (int i = 0; i < size; i++) {
            json = dataJsonArray.getJSONObject(i);
            if (YesAdNoFlag.A001.getValue().toString().equals(json.getString("dobinningPrint"))) {
                json.put("dobinningPrint", DoPrintFlag.A001.getInfo());
            } else {
                json.put("dobinningPrint", DoPrintFlag.A000.getInfo());
            }
            if (YesAdNoFlag.A001.getValue().toString().equals(json.getString("dopickingPrint"))) {
                json.put("dopickingPrint", DoPrintFlag.A001.getInfo());
            } else {
                json.put("dopickingPrint", DoPrintFlag.A000.getInfo());
            }
            if (YesAdNoFlag.A001.getValue().toString().equals(json.getString("dowaybillPrint"))) {
                json.put("dowaybillPrint", DoPrintFlag.A001.getInfo());
            } else {
                json.put("dowaybillPrint", DoPrintFlag.A000.getInfo());
            }
            String pickingStatus = json.getString("pickingStatus");//拣货状态
            if (PickingStatus.A000.getValue().toString().equals(pickingStatus)) {
                json.put("pickingStatus", PickingStatus.A000.getInfo());
            } else if (PickingStatus.A001.getValue().toString().equals(pickingStatus)) {
                json.put("pickingStatus", PickingStatus.A001.getInfo());
            } else if (PickingStatus.A002.getValue().toString().equals(pickingStatus)) {
                json.put("pickingStatus", PickingStatus.A002.getInfo());
            } else if (PickingStatus.A003.getValue().toString().equals(pickingStatus)) {
                json.put("pickingStatus", PickingStatus.A003.getInfo());
            } else if (PickingStatus.A004.getValue().toString().equals(pickingStatus)) {
                json.put("pickingStatus", PickingStatus.A004.getInfo());
            }
            String recheckStatus = json.getString("recheckStatus");//打包复检
            if (RecheckStatus.A000.getValue().toString().equals(recheckStatus)) {
                json.put("recheckStatus", RecheckStatus.A000.getInfo());
            } else if (RecheckStatus.A001.getValue().toString().equals(recheckStatus)) {
                json.put("recheckStatus", RecheckStatus.A001.getInfo());
            } else if (RecheckStatus.A002.getValue().toString().equals(recheckStatus)) {
                json.put("recheckStatus", RecheckStatus.A002.getInfo());
            } else if (RecheckStatus.A003.getValue().toString().equals(recheckStatus)) {
                json.put("recheckStatus", RecheckStatus.A003.getInfo());
            }
            String status = json.getString("status");//状态
            if (StockOutBillStatus.A12101.getValue().toString().equals(status)) {
                json.put("status", StockOutBillStatus.A12101.getInfo());
            } else if (StockOutBillStatus.A12102.getValue().toString().equals(status)) {
                json.put("status", StockOutBillStatus.A12102.getInfo());
            } else if (StockOutBillStatus.A12103.getValue().toString().equals(status)) {
                json.put("status", StockOutBillStatus.A12103.getInfo());
            } else if (StockOutBillStatus.A12104.getValue().toString().equals(status)) {
                json.put("status", StockOutBillStatus.A12104.getInfo());
            }
            String orderType = json.getString("orderType");//类型
            if (StockOutOrderType.A001.getValue().toString().equals(orderType)) {
                json.put("orderType", StockOutOrderType.A001.getInfo());
            } else if (StockOutOrderType.A003.getValue().toString().equals(orderType)) {
                json.put("orderType", StockOutOrderType.A003.getInfo());
            } else if (StockOutOrderType.A004.getValue().toString().equals(orderType)) {
                json.put("orderType", StockOutOrderType.A004.getInfo());
            }
            resultArray.add(json);
        }
        try {
            ExcelCommon excelUtil = new ExcelCommon();
            File excel = excelUtil.dataToExcel(columnJarr, resultArray, "列表数据", request);
            DownloadUtils.download(request, response, excel, "列表数据.xls");
        } catch (Exception e) {
            logger.error("outStockExport:数据导出异常" + com.rmd.wms.constant.Constant.LINE, e);
        }
    }

    @RequestMapping(value = "/outStockBillExport", method = RequestMethod.POST)
    public void outStockBillExport(HttpServletRequest request, HttpServletResponse response) {
        String columns = request.getParameter("columns");
        String flag = request.getParameter("flag");
        String name = "";
        JSONArray columnJarr = JSONArray.fromObject(columns);
        Map<String, Object> parmaMap = new HashedMap();
        parmaMap.put("wareId", getCurrentWare().getId());
        if ("1".equals(flag)) {
            name = "出库单列表数据";
            parmaMap.put("pickingInStock_search", Constant.SEARCH_FLAG);//不包含缺货异常数据
        } else if ("2".equals(flag)) {
            name = "缺货异常列表数据";
            parmaMap.put("pickingoutStock_search", Constant.SEARCH_FLAG);//缺货异常
        }
        List<Map<String, Object>> list = stockOutBillService.listAllByParmMap(parmaMap);
        JsonConfig config = new JsonConfig();

        config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
        JSONArray dataJsonArray = JSONArray.fromObject(list, config);
        JSONArray resultArray = new JSONArray();
        JSONObject json = null;
        int size = dataJsonArray.size();
        for (int i = 0; i < size; i++) {
            json = dataJsonArray.getJSONObject(i);
            json.put("orderNum", i + 1);
            if (StockOutBillStatus.A12101.getValue().toString().equals(json.getString("status"))) {
                json.put("status", StockOutBillStatus.A12101.getInfo());
            } else if (StockOutBillStatus.A12102.getValue().toString().equals(json.getString("status"))) {
                json.put("status", StockOutBillStatus.A12102.getInfo());
            } else if (StockOutBillStatus.A12103.getValue().toString().equals(json.getString("status"))) {
                json.put("status", StockOutBillStatus.A12103.getInfo());
            } else if (StockOutBillStatus.A12104.getValue().toString().equals(json.getString("status"))) {
                json.put("status", StockOutBillStatus.A12104.getInfo());
            }
            resultArray.add(json);
        }
        try {
            ExcelCommon excelUtil = new ExcelCommon();
            File excel = excelUtil.dataToExcel(columnJarr, resultArray, name, request);
            DownloadUtils.download(request, response, excel, name + ".xls");
        } catch (Exception e) {
            logger.error("outStockBillExport:数据导出异常" + com.rmd.wms.constant.Constant.LINE, e);
        }
    }

    /**
     * 更新打印状态
     *
     * @param ids
     * @param status
     * @param request
     * @return
     */
    @RequestMapping("updatePrintStatus")
    @ResponseBody
    public Map<String, Object> updatePrintStatus(@RequestParam(value = "ids", required = false) String ids, @RequestParam(value = "status", required = false) Integer status, @RequestParam(value = "printType", required = false) Integer printType
            , HttpServletRequest request) {
        Map<String, Object> map = new HashedMap();
        if (StringUtil.isEmpty(ids) || status == null || printType == null) {
            map.put("status", "101");
            map.put("message", "参数为空！");
            return map;
        }
        StockOutBill stockOutBill = null;
        String[] arr = ids.split(",");
        for (String id : arr) {
            stockOutBill = new StockOutBill();
            stockOutBill.setId(Integer.parseInt(id));
            if (printType == 1) {            //装箱单打印
                stockOutBill.setDobinningPrint(status);
            } else if (printType == 2) {    //拣货单打印
                stockOutBill.setDopickingPrint(status);
            } else if (printType == 3) {    //运单打印
                stockOutBill.setDowaybillPrint(status);
            }
            stockOutBillService.updateByPrimaryKeySelective(stockOutBill);
        }
        map.put("status", "200");
        map.put("message", "操作成功！");
        return map;
    }

}
