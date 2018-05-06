package com.rmd.wms.web.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.rmd.bms.entity.User;
import com.rmd.bms.service.BmsApiService;
import com.rmd.commons.servutils.Notification;
import com.rmd.oms.entity.OrderBase;
import com.rmd.oms.entity.OrderGoodsList;
import com.rmd.oms.entity.OrderReceiveAddress;
import com.rmd.oms.entity.vo.*;
import com.rmd.oms.service.OrderBackService;
import com.rmd.oms.service.OrderBaseService;
import com.rmd.oms.service.OrderGoodslistService;
import com.rmd.oms.service.PullOrderService;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.InStockInfo;
import com.rmd.wms.bean.vo.ServerInStockParam;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.InStockBillPrint;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.enums.GroundingBillStatus;
import com.rmd.wms.enums.SortType;
import com.rmd.wms.service.InStockBillService;
import com.rmd.wms.service.PurchaseBillService;
import com.rmd.wms.service.PurchaseInInfoService;
import com.rmd.wms.service.WarehouseService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.util.DateJsonValueProcessor;
import com.rmd.wms.web.util.DownloadUtils;
import com.rmd.wms.web.util.ExcelCommon;
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
import java.util.*;

/**
 * @author ZXLEI
 * @ClassName: InStockBillController
 * @Description: TODO(入库单)
 * @date Mar 17, 2017 9:18:22 AM
 */
@Controller
@RequestMapping(value = "/instock")
public class InStockBillController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(InStockBillController.class);
    @Resource(name = "purchaseBillService")
    private PurchaseBillService purchaseBillService; // 采购单

    @Resource(name = "inStockBillService")
    private InStockBillService inStockBillService; // 入库单

    @Resource(name = "purchaseInInfoService")
    private PurchaseInInfoService purchaseInInfoService; // 采购单，出库单，上架单商品详情

    //OMS获取待入库订单
    @Resource(name = "pullOrderService")
    private PullOrderService pullOrderService;

    //OMS获取待入库订单下商品信息
    @Resource(name = "orderGoodslistService")
    private OrderGoodslistService orderGoodslistService;

    //OMS订单说或地址
    @Resource(name = "orderBaseService")
    private OrderBaseService orderBaseService;

    //OMS退货商品信息
    @Resource(name = "orderBackService")
    private OrderBackService orderBackService;

    @Resource(name = "warehouseService")
    private WarehouseService warehouseService;

    @Resource(name = "bmsApiService")
    private BmsApiService bmsApiService;


    @Autowired
    private HttpServletRequest request;

    /**
     * 页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/InStockBiLL")
    public ModelAndView jumpView(@RequestParam(value = "view") Integer view) {
        ModelAndView mv = new ModelAndView();
        String viewName = "";
        if (1 == view) {
            //出库制单
            viewName = "instock/instockManage";
        } else if (2 == view) {
            //打包复检
            viewName = "instock/serverManage";
        }
        mv.setViewName(viewName);
        return mv;
    }

    /**
     * 查询所有上架单
     *
     * @return
     */
    @RequestMapping(value = "/ListInStockBiLL")
    @ResponseBody
    public Map<String, Object> listBiLL(@RequestParam(value = "page", required = false) Integer page
            , @RequestParam(value = "inStockNo", required = false) String inStockNo
            , @RequestParam(value = "purchaseNo", required = false) String purchaseNo
            , @RequestParam(value = "instockTime", required = false) String instockTime
            , @RequestParam(value = "supplierName", required = false) String supplierName
            , @RequestParam(value = "doPrint", required = false) Integer doPrint
            , @RequestParam(value = "rows", required = false) Integer rows, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Integer wareId = super.getCurrentWareId();
            Map<String, Object> whereParams = new HashMap<String, Object>();
            whereParams.put("wareId", wareId);
            if (StringUtil.isNotEmpty(inStockNo)) {
                whereParams.put("inStockNo", inStockNo);
            }
            if (StringUtil.isNotEmpty(purchaseNo)) {
                whereParams.put("purchaseNo", purchaseNo);
            }
            if (StringUtil.isNotEmpty(instockTime)) {
                whereParams.put("instockTime", instockTime);
            }
            if (StringUtil.isNotEmpty(supplierName)) {
                whereParams.put("supplierName", supplierName);
            }
            if (doPrint != null && doPrint >= 0) {
                whereParams.put("doPrint", doPrint);
            }
            whereParams.put("name_sort","in_stock_no");
            whereParams.put("order_sort", SortType.DESC.getValue());
            PageBean<InStockBill> pageBean = inStockBillService.ListInStockBills(page, rows, whereParams);
            List<InStockBill> pList = pageBean.getList();
            map.put("total", pageBean.getTotal());
            map.put("rows", pList);
            JsonConfig config = new JsonConfig();
            config.registerJsonValueProcessor(Date.class,
                    new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
            JSONObject json = JSONObject.fromObject(map, config);
            // 格式化时间后填入返回结果
            map.put("datarow", json);
        } catch (Exception e) {
            logger.error("ListInStockBiLL：上架单查询异常！" + com.rmd.wms.constant.Constant.LINE, e);
        }
        return map;
    }

    /**
     * 查询上架单下的商品列表
     *
     * @return
     */
    @RequestMapping(value = "/InstockBiLLInfo")
    public ModelAndView instockBiLLInfo(@RequestParam(value = "Id", required = false) Integer Id
            , HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("instock/instockInfo");
        InStockBill inStockBill = new InStockBill();// 入库单
        PurchaseBill purchaseBill = new PurchaseBill();// 采购单
        Map<String, Object> whereParams = new HashMap<String, Object>();// 动态条件
        String deliveryUserName="";//收货员名称
        Integer wareId = super.getCurrentWareId();
        List<PurchaseInInfo> pInInfos = new ArrayList<PurchaseInInfo>(10);
        try {
            if (Id > 0) {
                inStockBill = inStockBillService.selectByPrimaryKey(Id);
            }
            if (!inStockBill.equals(null)) {
                purchaseBill = purchaseBillService
                        .selectByPurchaseNo(inStockBill.getPurchaseNo());
                whereParams.put("inStockNo", inStockBill.getInStockNo());
                whereParams.put("wareId", wareId);
                pInInfos = purchaseInInfoService
                        .ListPurchaseInInfo(whereParams);
                //收货员
                Notification<User> userNotification = bmsApiService.selectUserByUserid(inStockBill.getOuserId());
                User user = userNotification.getResponseData();
                if (user != null) {
                    deliveryUserName=user.getRealname();
                }
            }
            // 入库单基本信息
            mv.addObject("purchaseBill", purchaseBill);
            mv.addObject("instockBill", inStockBill);
            // 入库单下商品
            mv.addObject("pInInfos", pInInfos);
            int purchaseNum = 0;
            int inStockNum = 0;
            BigDecimal totalPrice = new BigDecimal(0);
            for (PurchaseInInfo o : pInInfos) {
                purchaseNum += o.getPurchaseNum();
                inStockNum += o.getInStockNum();
                totalPrice = totalPrice.add(new BigDecimal(o.getInStockNum()).multiply(o.getPurchasePrice()));
            }
            mv.addObject("purchaseNum", purchaseNum);
            mv.addObject("inStockNum", inStockNum);
            mv.addObject("totalPrice", totalPrice);
            mv.addObject("deliveryUserName", deliveryUserName);
        } catch (Exception e) {
            logger.error("InstockBiLLInfo：上架单下的商品查询异常！" + com.rmd.wms.constant.Constant.LINE, e);
        }

        return mv;
    }

    /**
     * 售后收货服务单列表
     *
     * @return
     */
    @RequestMapping("/ServerList")
    @ResponseBody
    public Map<String, Object> ServerList(@RequestParam(value = "page", required = false) Integer pageIndex
            , @RequestParam(value = "rows", required = false) Integer pageSize) {

        //获取当前登录的仓库ID,查询对应的服务单状态为202(退货)或302(换货)
        Integer wareId = super.getCurrentWareId();
        if ("".equals(wareId)) {
            wareId = -1;
        }
        String serviceId = request.getParameter("serviceId");
        String ordernumber = request.getParameter("ordernumber");
        String flag = request.getParameter("flag");
        //Notification<List<OrderBase>> orderBaseList = pullOrderService.loadPendingStorageOrders(pageIndex, pageSize, wareId);
        OrderBaseWaitOrFinsh orderBaseWaitOrFinsh = new OrderBaseWaitOrFinsh();
        orderBaseWaitOrFinsh.setWarehouseId(wareId);
        if (StringUtil.isNotEmpty(serviceId)) {//服务单号
            orderBaseWaitOrFinsh.setServiceId(serviceId);
        }
        if (StringUtil.isNotEmpty(ordernumber)) {//订单号
            orderBaseWaitOrFinsh.setOrdernumber(ordernumber);
        }
        if (StringUtil.isNotEmpty(flag)) {//状态
            orderBaseWaitOrFinsh.setFlag(Integer.parseInt(flag));
        } else {
            orderBaseWaitOrFinsh.setFlag(0);
        }
        Notification<List<OrderBase>> orderBaseList = null;
        try {
            logger.info("售后收货查询BEGIN************");
            orderBaseList = pullOrderService.loadWaitOrFinshOrders(pageIndex, pageSize, orderBaseWaitOrFinsh);
            logger.info("售后收货查询END************" + orderBaseList.getNotifCode());
        } catch (Exception e) {
            logger.error("售后收货查询报错********", e);
        }
        List<InStockBill> inStockBills = new ArrayList<InStockBill>(10);
        for (OrderBase orderBase : orderBaseList.getResponseData()) {
            InStockBill inBill = new InStockBill();
            inBill.setServerNo(orderBase.getOrdernumber());
            inBill.setOrderNo(orderBase.getSourceordernumber());
            inBill.setWareId(orderBase.getWarehouseid());
            inBill.setWareName(warehouseService.selectByPrimaryKey(wareId).getWareName());
            inBill.setType(orderBase.getOrdertype());
            inBill.setStatus(orderBase.getOrderstatus());//等待
            inStockBills.add(inBill);
        }
        inStockBills = Lists.reverse(inStockBills);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", inStockBills.size());
        map.put("rows", inStockBills);
        return map;

    }

    /**
     * 售后收货商品详情列表
     *
     * @param serverNo
     * @param orderNo
     * @return
     */
    @RequestMapping("/ServerGoodsInfo")
    public ModelAndView ServerGoodsInfo(@RequestParam(value = "serverNo") String serverNo
            , @RequestParam(value = "orderNo") String orderNo) {
//        ModelAndView mv = new ModelAndView("instock/serverGoodsInfo");
        ModelAndView mv = new ModelAndView("instock/returnpolicy");
        //查询退货说明和客服审核说明
        Notification<OrderBackExplainVo> orderBackExplainVoNotification= orderBackService.selectExplainByServiceId(serverNo);
        String[] customerImageUrls=orderBackExplainVoNotification.getResponseData().getCustomerImageUrls().split(",");//客户退换货图片
        String customerExplain=orderBackExplainVoNotification.getResponseData().getCustomerExplain();
        String[] serviceImageUrls=orderBackExplainVoNotification.getResponseData().getServiceImageUrls().split(",");//客服审核图片
        String serviceExplain=orderBackExplainVoNotification.getResponseData().getServiceExplain();
        //调用OMS根据订单号查询商品
        List<OrderGoodsList> goodsLists = orderGoodslistService.selectByOnbmberList(serverNo);
        int goodsNum=0;
        BigDecimal goodsSum=new BigDecimal(0.00);
        for (OrderGoodsList orderGood : goodsLists) {
            goodsNum+=orderGood.getGoodsamount();
            goodsSum=goodsSum.add(orderGood.getUnitprice().multiply(new BigDecimal(orderGood.getGoodsamount())));
        }
        mv.addObject("customerImageUrls", customerImageUrls);
        mv.addObject("customerExplain", customerExplain);
        mv.addObject("serviceImageUrls", serviceImageUrls);
        mv.addObject("serviceExplain", serviceExplain);
        mv.addObject("serverNo", serverNo);
        mv.addObject("orderNo", orderNo);
        mv.addObject("goodsNum", goodsNum);//商品总数
        mv.addObject("goodsSum", goodsSum);//商品总价格
        mv.addObject("goodsLists", goodsLists);
        return mv;

    }

    /**
     * 售后收货商品详情列表
     *
     * @param serverNo
     * @param orderNo
     * @return
     */
    @RequestMapping("/ServerListInfo")
    public ModelAndView ServerListInfo(@RequestParam(value = "serverNo") String serverNo
            , @RequestParam(value = "orderNo") String orderNo) {
        ModelAndView mv = new ModelAndView("instock/serverInfo");
        //调用OMS根据订单号查询商品
        List<OrderGoodsList> goodsLists = orderGoodslistService.selectByOnbmberList(serverNo);

        List<PurchaseInInfo> purchaseInInfos = new ArrayList<PurchaseInInfo>(10);

        for (OrderGoodsList orderGood : goodsLists) {
            PurchaseInInfo purchaseInInfo = new PurchaseInInfo();
            purchaseInInfo.setGoodsName(orderGood.getGoodsname());
//            purchaseInInfo.setGoodsBarCode(orderGood.getBrandname());
            purchaseInInfo.setGoodsCode(orderGood.getGoodsCode());
            purchaseInInfo.setSpec(orderGood.getSpec());
            purchaseInInfo.setPackageNum(orderGood.getPacknum().toString());
            purchaseInInfo.setUnit(orderGood.getSaleunit());
            purchaseInInfo.setPurchaseNum(orderGood.getGoodsamount());
            purchaseInInfo.setGroundingNo(serverNo);//服务单号
            purchaseInInfo.setPurchaseNo(orderNo);//订单号
            purchaseInInfos.add(purchaseInInfo);
        }
        //加载仓库信息
        Warehouse warehouse = new Warehouse();
        warehouse.setId(getCurrentWareId());
        PageBean<Warehouse> pageBean = warehouseService.listWarehouse(1, 20, warehouse);
        List<Warehouse> wList = pageBean.getList();
        mv.addObject("serverNo", serverNo);
        mv.addObject("wList", wList);
        mv.addObject("orderNo", orderNo);
        mv.addObject("purchaseInInfos", purchaseInInfos);
        return mv;

    }

    /**
     * 售后手动验货收货
     *
     * @param params    服务单基础数据
     * @param gCode     服务单所有商品编码
     * @param counts    提交数量
     * @param oldcounts 退货数量
     * @param status    残次品或良品状态
     * @return
     */
    @RequestMapping(value = "/InstockBillSave", method = RequestMethod.POST)
    @ResponseBody
    public String instockBillSave(@RequestParam(value = "params") String params
            , @RequestParam(value = "gCode") String gCode
            , @RequestParam(value = "counts") String counts
            , @RequestParam(value = "oldcounts") String oldcounts
            , @RequestParam(value = "status") String status) {
        String result = "false";
        try {
            String[] instockData = params.split(",");
            String serverNo = instockData[0];//服务单号
            String orderNo = instockData[1];// 根据订单号查询收货
            Integer agree = Integer.parseInt(instockData[2]);
            String remark = instockData[3];
            User user = super.getCurrentUser();
            Warehouse ware = super.getCurrentWare();
            ServerInStockParam serverInStockParam = new ServerInStockParam();
            serverInStockParam.setAgree(agree);//serverNo
            serverInStockParam.setServerNo(serverNo);
            serverInStockParam.setWareId(ware.getId());
            serverInStockParam.setWareName(ware.getWareName());
            serverInStockParam.setRemark(remark);
            serverInStockParam.setUserId(user.getId());
            serverInStockParam.setUserName(super.getCurrentUserName());

            OrderPackageInfoVo orderPackageInfoVo = orderBaseService.selectOrderPackageInfo(orderNo);
            //OMS收货地址
            OrderReceiveAddress orderReceiveAddress = orderPackageInfoVo.getOrderReceiveAddress();
            //WMS收货地址BEGIN
            OrderLogisticsInfo orderLogisticsInfo = new OrderLogisticsInfo();
            orderLogisticsInfo.setOrderNo(serverNo);// 这里注意设置的是补货单的单号
            orderLogisticsInfo.setCityCode(orderReceiveAddress.getCityid());
            orderLogisticsInfo.setCityName(orderReceiveAddress.getCity());
            orderLogisticsInfo.setProvCode(orderReceiveAddress.getProvid());
            orderLogisticsInfo.setProvName(orderReceiveAddress.getProvince());
            orderLogisticsInfo.setAreaCode(orderReceiveAddress.getCounty() == null ? null : Integer.valueOf(orderReceiveAddress.getCounty()));
            orderLogisticsInfo.setAreaName(orderReceiveAddress.getArea());
            orderLogisticsInfo.setReceivername(orderReceiveAddress.getReceivername());
            orderLogisticsInfo.setReceiveMobile(orderReceiveAddress.getReceivemobile());
            orderLogisticsInfo.setReceiveTel(orderReceiveAddress.getReceivetel());
            orderLogisticsInfo.setDetailedAddress(orderReceiveAddress.getStreet());
            //WMS收货地址END
            serverInStockParam.setOrderLogiInfo(orderLogisticsInfo);
            serverInStockParam.setOrderdate(orderPackageInfoVo.getOrderBase().getOrderdate());

            List<InStockInfo> inStockInfoArrayList = new ArrayList<>();
            //订单商品入库数量
            String[] countList = counts.split(",");
            //订单商品数量
            String[] oldCountList = oldcounts.split(",");
            //入库状态集合
            String[] statusList = status.split(",");
            int goodTotalCnt = 0;//良品总数量
            int badTotalCnt = 0;//残次品总数量
            for (int i = 0; i < statusList.length; i++) {
                if (statusList[i].equals("1")) {//只入库良品
                    goodTotalCnt += Integer.parseInt(oldCountList[i]);
                } else {
                    goodTotalCnt += (Integer.parseInt(oldCountList[i]) - Integer.parseInt(countList[i]));//良品入库数量
                    badTotalCnt += Integer.parseInt(countList[i]);//残次品入库数量
                }
            }
            Map<String, Object> mapPurList = returnPurchaseInInfoList(serverNo, ware.getId(), ware.getWareName(), gCode, counts, oldcounts, status);
            if (goodTotalCnt > 0) {//良品
                InStockInfo inStockInfo = returnServerInStockBill(goodTotalCnt, ware.getId(), ware.getWareName(), serverNo, orderNo, remark);
                inStockInfo.setStatus(Constant.TYPE_STATUS_YES);//入库订单类型1良品入库
                inStockInfo.setInInfos((List<PurchaseInInfo>) mapPurList.get("goodList"));
                inStockInfo.setInGoodsSum((BigDecimal) mapPurList.get("puGoodsSum"));
                inStockInfoArrayList.add(inStockInfo);
            }
            if (badTotalCnt > 0) {//残次品
                InStockInfo inStockInfo = returnServerInStockBill(goodTotalCnt, ware.getId(), ware.getWareName(), serverNo, orderNo, remark);
                inStockInfo.setStatus(Constant.TYPE_STATUS_NO);//入库订单类型1良品入库
                inStockInfo.setInInfos((List<PurchaseInInfo>) mapPurList.get("badList"));
                inStockInfo.setInGoodsSum((BigDecimal) mapPurList.get("badGoodsSum"));
                inStockInfoArrayList.add(inStockInfo);
            }
            serverInStockParam.setOrderNo(orderNo);
            serverInStockParam.setParams(inStockInfoArrayList);
            logger.debug(Constant.LINE + "serverInStockParam : " + inStockInfoArrayList);
            ServerStatus serverStatus = inStockBillService.serverInStock(serverInStockParam);
            logger.debug(Constant.LINE + "ServerStatus : " + JSON.toJSONString(serverStatus));
            if ("200".equals(serverStatus.getStatus())) {
                result = "true";
            }
        } catch (Exception e) {
            result = "false";
            logger.error(Constant.LINE + "InstockBillSave:售后收货错误", e);
        }
        return result;
    }

    /**
     * 获取采购详情数据
     *
     * @param serverNo    服务单号
     * @param wareId    仓库id
     * @param gCode     服务单所有商品编码
     * @param counts    提交数量
     * @param oldcounts 退货数量
     * @param status    残次品或良品状态
     * @return
     */
    public Map<String, Object> returnPurchaseInInfoList(String serverNo, Integer wareId, String wareName, String gCode, String counts, String oldcounts, String status) {
        Map<String, Object> serverListMap = new HashMap<>();
        //良品入库商品详情表
        List<PurchaseInInfo> puList = new ArrayList<>();
        //残次品入库商品详情表
        List<PurchaseInInfo> badList = new ArrayList<>();
        // 添加入库商品金额
        BigDecimal puGoodsSum = BigDecimal.ZERO;
        BigDecimal badGoodsSum = BigDecimal.ZERO;
        try {
            //商品code集合
            String[] codeList = gCode.split(",");
            //订单商品入库数量
            String[] countList = counts.split(",");
            //订单商品数量
            String[] oldCountList = oldcounts.split(",");
            //入库状态集合
            String[] statusList = status.split(",");
            for (int i = 0; i < statusList.length; i++) {
                //入库单商品信息
                PurchaseInInfo purchaseInInfo = returnServerPurchaseInInfo(serverNo, codeList[i], wareId, wareName);
                if (statusList[i].equals("1")) {//只入库良品
                    Integer goodCount = Integer.parseInt(oldCountList[i]);
                    purchaseInInfo.setInStockNum(goodCount);//入库数量
                    purchaseInInfo.setInStockBeNum(goodCount);//应入库数量
                    purchaseInInfo.setPurchaseNum(goodCount);//采购数量
                    purchaseInInfo.setPurchaseWaitNum(goodCount);//未上架数量
                    purchaseInInfo.setInStockSum(purchaseInInfo.getPurchasePrice().subtract(new BigDecimal(goodCount))); //服务单入库金额=销售单价*入库数量
                    puGoodsSum = puGoodsSum.add(purchaseInInfo.getInStockSum());
                    puList.add(purchaseInInfo);
                } else if (statusList[i].equals("0")) {
                    //残次品入库数量
                    Integer badCount = Integer.parseInt(countList[i]);
                    purchaseInInfo.setInStockNum(badCount);
                    purchaseInInfo.setInStockBeNum(badCount);//应入库数量
                    purchaseInInfo.setPurchaseWaitNum(badCount);
                    purchaseInInfo.setPurchaseNum(badCount);
                    purchaseInInfo.setInStockSum(purchaseInInfo.getPurchasePrice().subtract(new BigDecimal(badCount))); //服务单入库金额=销售单价*入库数量
                    badGoodsSum = badGoodsSum.add(purchaseInInfo.getInStockSum());
                    badList.add(purchaseInInfo);
                    //良品入库数量
                    Integer goodCount = (Integer.parseInt(oldCountList[i]) - Integer.parseInt(countList[i]));
                    if (goodCount != 0) {
                        PurchaseInInfo purchaseInInfo2 = new PurchaseInInfo();
                        BeanUtils.copyProperties(purchaseInInfo, purchaseInInfo2);
                        purchaseInInfo2.setInStockNum(goodCount);  //入库数量
                        purchaseInInfo2.setInStockBeNum(goodCount);//应入库数量
                        purchaseInInfo2.setPurchaseNum(goodCount); //采购数量
                        purchaseInInfo2.setPurchaseWaitNum(goodCount); //待采购数量
                        purchaseInInfo2.setInStockSum(purchaseInInfo2.getPurchasePrice().subtract(new BigDecimal(goodCount))); //服务单入库金额=销售单价*入库数量
                        puGoodsSum = puGoodsSum.add(purchaseInInfo.getInStockSum());
                        puList.add(purchaseInInfo2);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取商品详情报错********************", e);
        }
        serverListMap.put("goodList", puList);
        serverListMap.put("badList", badList);
        serverListMap.put("puGoodsSum", puGoodsSum);
        serverListMap.put("badGoodsSum", badGoodsSum);
        return serverListMap;
    }

    /**
     * 返回上架单实体
     *
     * @param inStockNo
     * @param type
     * @param wareId
     * @param wareName
     * @return
     */
    public GroundingBill returnServerGrounding(String inStockNo, Integer type
            , Integer wareId, String wareName) {
        GroundingBill groundingBill = new GroundingBill();
        groundingBill.setInStockNo(inStockNo);
        groundingBill.setStatus(GroundingBillStatus.A001.getValue());//上架状态,1:等待,2:上架中,3已完成
        groundingBill.setType(type);
        groundingBill.setStartTime(new Date());
        groundingBill.setWareId(wareId);
        groundingBill.setWareName(wareName);
        groundingBill.setCreateTime(new Date());
        return groundingBill;
    }

    /**
     * 返回入库单VO
     *
     * @param goodTotalCnt
     * @param wareId
     * @param wareName
     * @param serverNo
     * @param orderNo
     * @param remark
     * @return
     */
    public InStockInfo returnServerInStockBill(Integer goodTotalCnt, Integer wareId, String wareName, String serverNo, String orderNo, String remark) {
        InStockInfo inStockInfo = new InStockInfo();
        Date now = new Date();
        inStockInfo.setInGoodsAmount(goodTotalCnt);
        Integer sType = Integer.parseInt(serverNo.substring(0, 1));//入库单类型
        inStockInfo.setType(sType);
        inStockInfo.setPurchaseNo(serverNo);
        inStockInfo.setWareId(wareId);
        inStockInfo.setWareName(wareName);//查询仓库ID对应仓库名称
        inStockInfo.setDoPrint(Constant.TYPE_STATUS_NO);
        inStockInfo.setDoPrint(Constant.TYPE_STATUS_NO);
        inStockInfo.setInStockTime(now);
        inStockInfo.setCreateTime(now);
        inStockInfo.setServerNo(serverNo);
        inStockInfo.setOrderNo(orderNo);
        inStockInfo.setRemark(remark);
        return inStockInfo;
    }


    /**
     * 返回上架单商品详情类表
     *
     * @param serverNo
     * @param goodsCode
     * @param wareId
     * @return
     */
    public PurchaseInInfo returnServerPurchaseInInfo(String serverNo, String goodsCode, Integer wareId, String wareName) {
        List<OrderGoodsList> goodsLists = orderGoodslistService.selectByOnbmberList(serverNo);
        PurchaseInInfo purchaseInInfo = new PurchaseInInfo();
        for (OrderGoodsList orderGood : goodsLists) {
            if (goodsCode.equals(orderGood.getGoodsCode())) {
                purchaseInInfo.setGoodsCode(goodsCode);
                purchaseInInfo.setGoodsName(orderGood.getGoodsname());
                purchaseInInfo.setGoodsCode(orderGood.getGoodsCode());
                purchaseInInfo.setSpec(orderGood.getSpec());
                purchaseInInfo.setPackageNum(orderGood.getPacknum().toString());
                purchaseInInfo.setUnit(orderGood.getSaleunit());
                //售后采购单价，存储销售单价，用于计算入库总价
                purchaseInInfo.setPurchasePrice(orderGood.getPrice());
                purchaseInInfo.setWareId(wareId);
                purchaseInInfo.setWareName(wareName);
            }
        }
        return purchaseInInfo;
    }

    @RequestMapping(value = "/InstockBillPrint", method = RequestMethod.GET)
    public String InstockBillPrint(@RequestParam(value = "instockNo") String instockNo
            , @RequestParam(value = "Id") Integer Id
            , Model model) {
        List<InStockBillPrint> stockList = new ArrayList<InStockBillPrint>();
        Map<String, Object> whereParams = new HashMap<String, Object>();
        whereParams.put("inStockNo", instockNo);
        InStockBill inStockBill = inStockBillService.selectByPrimaryKey(Id);
        Notification<User> userNotification = bmsApiService.selectUserByUserid(inStockBill.getOuserId());
        User user = userNotification.getResponseData();
        PurchaseBill purchaseBill = purchaseBillService.selectByPurchaseNo(inStockBill.getPurchaseNo());
        List<PurchaseInInfo> purchaseInInfos = purchaseInInfoService.ListPurchaseInInfo(whereParams);
        InStockBillPrint stockInPurchaseInfo = new InStockBillPrint();
        stockInPurchaseInfo.setInStockTime(inStockBill.getInStockTime());   //入库日期
        stockInPurchaseInfo.setWareName(inStockBill.getWareName());         //收货单位名称
        if (user != null) {
            stockInPurchaseInfo.setInStockUserName(user.getRealname());
        }
        if (purchaseBill != null) {
            stockInPurchaseInfo.setSupplierName(purchaseBill.getSupplierName());
            stockInPurchaseInfo.setPurchaseNo(purchaseBill.getPurchaseNo());
            stockInPurchaseInfo.setInStockNo(purchaseInInfos.get(0).getInStockNo());
        }
        int purchaseNum=0;//采购总数量
        int inStockNum=0;//入库总数量
        for (PurchaseInInfo purchaseInInfo : purchaseInInfos) {
            purchaseNum+=(purchaseInInfo.getPurchaseNum()==null?0:purchaseInInfo.getPurchaseNum());
            inStockNum+=(purchaseInInfo.getInStockNum()==null?0:purchaseInInfo.getInStockNum());
        }
        stockInPurchaseInfo.setPurchaseNum(purchaseNum);
        stockInPurchaseInfo.setInStockNum(inStockNum);
        stockInPurchaseInfo.setPurchaseInInfoList(purchaseInInfos);
            stockList.add(stockInPurchaseInfo);

       // }

        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(stockList);
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/installBill.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "instockBillView"; // 对应jasper-defs.xml中的bean id
    }

    @RequestMapping(value = "/inStockExport", method = RequestMethod.POST)
    public void inStockExport(HttpServletRequest request, HttpServletResponse response) {
        String columns = request.getParameter("columns");
        JSONArray columnJarr = JSONArray.fromObject(columns);
        Map<String, Object> parmaMap = new HashedMap();// getSessionOfShiro().getAttribute(Constant.OUTSTOCK_LISTWHERE);
        parmaMap.put("wareId", getCurrentWare().getId());
        List<Map<String, Object>> list = inStockBillService.selectAllByParmMap(parmaMap);
        JsonConfig config = new JsonConfig();

        config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
        //config.registerJsonValueProcessor(StockOutBill.class, new StockOutBillProcessor());
        JSONArray dataJsonArray = JSONArray.fromObject(list, config);
        JSONArray resultArray = new JSONArray();
        JSONObject json = null;
        int size = dataJsonArray.size();
        for (int i = 0; i < size; i++) {
            json = dataJsonArray.getJSONObject(i);
            json.put("orderNum", i + 1);
            if ("3".equals(json.getString("status"))) {
                json.put("status", "全部收货");
            } else if ("2".equals(json.getString("status"))) {
                json.put("status", "部分收货");
            } else {
                json.put("status", "等待收货");
            }
            resultArray.add(json);
        }
        try {
            ExcelCommon excelUtil = new ExcelCommon();
            File excel = excelUtil.dataToExcel(columnJarr, resultArray, "入库列表数据", request);
            DownloadUtils.download(request, response, excel, "入库列表数据.xls");
        } catch (Exception e) {
            logger.error("数据导出失败", e);
        }
    }

    /**
     * 售后收货导出
     */
    @RequestMapping(value = "/afterReceiptInStockExport", method = RequestMethod.POST)
    public void afterReceiptInStockExport(HttpServletRequest request, HttpServletResponse response) {
        JSONArray dataJsonArray = new JSONArray();
        JSONObject jsonObject = null;
        String columns = request.getParameter("columns");
        JSONArray columnJarr = JSONArray.fromObject(columns);
        Notification<List<OrderBackListVo>> notification = orderBackService.selectOrderBackOrGoodsList(getCurrentWare().getId());
        logger.info("售后收货数据信息：------------------" + notification.getNotifCode() + ":" + notification.getResponseData());
        List<OrderBackListVo> listVos = notification.getResponseData();
        for (OrderBackListVo o : listVos) {
            List<OrderBackGoodsListVo> listVoList = o.getBackGoodsListVos();
            for (OrderBackGoodsListVo v : listVoList) {
                jsonObject = new JSONObject();
                jsonObject.put("serverNo", o.getServiceid());
                jsonObject.put("orderNo", o.getOrdernumber());
                jsonObject.put("barCode", v.getBarcode() == null ? "" : v.getBarcode());
                jsonObject.put("goodsCode", v.getGoodscode());
                jsonObject.put("goodsName", v.getGoodsname());
                jsonObject.put("spec", v.getSpec());
                jsonObject.put("unit", v.getSaleunit());
                jsonObject.put("backNum", v.getGoodsamount());
                jsonObject.put("purchaseNum", v.getGoodsamount());
                jsonObject.put("inStockSum", "");
                jsonObject.put("ouserName", "");
                jsonObject.put("status", "");
                dataJsonArray.add(jsonObject);
            }
        }
        JSONArray resultArray = new JSONArray();
        JSONObject json = null;
        int size = dataJsonArray.size();
        for (int i = 0; i < size; i++) {
            json = dataJsonArray.getJSONObject(i);
            json.put("orderNum", i + 1);

            resultArray.add(json);
        }
        try {
            ExcelCommon excelUtil = new ExcelCommon();
            File excel = excelUtil.dataToExcel(columnJarr, resultArray, "售后收货列表数据", request);
            DownloadUtils.download(request, response, excel, "售后收货列表数据.xls");
        } catch (Exception e) {
            logger.error("afterReceiptInStockExport:数据导出失败" + Constant.LINE, e);
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
    public Map<String, Object> updatePrintStatus(@RequestParam(value = "ids", required = false) String ids, @RequestParam(value = "status", required = false) Integer status
            , HttpServletRequest request) {
        Map<String, Object> map = new HashedMap();
        if (StringUtil.isEmpty(ids) || status == null) {
            map.put("status", "101");
            map.put("message", "参数为空！");
            return map;
        }
        InStockBill inStockBill = null;
        String[] arr = ids.split(",");
        for (String id : arr) {
            inStockBill = new InStockBill();
            inStockBill.setId(Integer.parseInt(id));
            inStockBill.setDoPrint(status);
            inStockBillService.updateByPrimaryKeySelective(inStockBill);
        }
        map.put("status", "200");
        map.put("message", "操作成功！");
        return map;
    }

}
