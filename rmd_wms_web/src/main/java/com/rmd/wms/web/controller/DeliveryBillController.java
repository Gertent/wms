package com.rmd.wms.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.StringUtil;
import com.rmd.wms.bean.DeliveryBill;
import com.rmd.wms.bean.LogisticsCompany;
import com.rmd.wms.bean.StockOutBill;
import com.rmd.wms.bean.StockOutInfo;
import com.rmd.wms.bean.vo.web.DeliveryBillPrint;
import com.rmd.wms.bean.vo.web.DeliveryGoodsBase;
import com.rmd.wms.bean.vo.web.DeliveryGoodsInfo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.enums.SortType;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.*;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.util.DateJsonValueProcessor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
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
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;


/**
 * @author ZXLEI
 * @ClassName: DeliveryBillController
 * @Description: TODO(发运交接单)
 * @date Mar 17, 2017 9:16:36 AM
 */
@Controller
@RequestMapping(value = "/delivery")
public class DeliveryBillController extends AbstractAjaxController {
    private Logger logger = Logger.getLogger(DeliveryBillController.class);
    // 出库单
    @Resource(name = "stockOutBillService")
    private StockOutBillService stockOutBillService;
    // 出库单商品
    @Resource(name = "stockOutInfoService")
    private StockOutInfoService stockOutInfoService;
    // 运单商品
    @Resource(name = "deliveryBillService")
    private DeliveryBillService deliveryBillService;
    //承运商
    @Resource(name = "logisticsCompanyService")
    private LogisticsCompanyService logisticsCompanyService; // 物流公司
    @Resource(name = "logisticsFreightBillService")
    private LogisticsFreightBillService logisticsFreightBillService;

    /**
     * 页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/DeliveryBiLL")
    public ModelAndView jumpView(@RequestParam(value = "view") Integer view) {
        ModelAndView mv = new ModelAndView();
        String viewName = "";
        if (1 == view) {
            viewName = "delivery/deliveryManage";
        }
        Integer wareId = super.getCurrentWareId();
        LogisticsCompany logisticsCompany = new LogisticsCompany();
        logisticsCompany.setWareId(wareId);
        List<LogisticsCompany> companies = logisticsCompanyService.selectByCriteria(logisticsCompany);
        mv.addObject("companies", companies);
        mv.setViewName(viewName);
        return mv;
    }

    /**
     * 查询所有发货单
     *
     * @return
     */
    @RequestMapping(value = "/ListDeliveryBiLL")
    @ResponseBody
    public Map<String, Object> listDeliveryBiLL(@RequestParam(value = "page", required = false) Integer page
            , @RequestParam(value = "orderNo", required = false) String orderNo
            , @RequestParam(value = "deliveryNo", required = false) String deliveryNo
            , @RequestParam(value = "deliveryUser", required = false) String deliveryUser
            , @RequestParam(value = "logisComId", required = false) Integer logisComId
            , @RequestParam(value = "doPrint", required = false) String doPrint
            , @RequestParam(value = "rows", required = false) Integer rows
            , @RequestParam(value = "starTime", required = false) String starTime
            , @RequestParam(value = "endTime", required = false) String endTime, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Integer wareId = super.getCurrentWareId();
            Map<String, Object> whereParams = new HashMap<String, Object>();
            whereParams.put("wareId", wareId);
            if (StringUtil.isNotEmpty(orderNo)) {
                Map<String, Object> parmaMap = new HashMap<>();
                parmaMap.put("orderNo", orderNo);
                List<StockOutBill> list = stockOutBillService.ListStockOutBills(parmaMap);
                if (list.size() > 0) {
                    StringBuilder sb = new StringBuilder("(");
                    for (StockOutBill o : list) {
                        sb.append("'").append(o.getDeliveryNo()).append("',");
                    }
                    String str = sb.toString();
                    str = str.substring(0, str.length() - 1) + ")";
                    whereParams.put("deliveryNo_in", str);
                }
            }
            if (StringUtil.isNotEmpty(deliveryNo)) {
                whereParams.put("deliveryNo", deliveryNo);
            }
            // 承运商ID
            if (!logisComId.equals(null)) {
                if (logisComId != 0) {
                    whereParams.put("logiscomId", logisComId);
                }
            }
            if ("0".equals(doPrint)) {//交接单未打印
                whereParams.put("dodeliveryPrint_search", com.rmd.wms.web.constant.Constant.SEARCH_FLAG);
            }
            // 交接开始时间大于指定时间
            if (StringUtil.isNotEmpty(starTime)) {
                whereParams.put("deliveryEndTime_gt", starTime);
            }
            // 交接开始时间小于指定时间
            if (StringUtil.isNotEmpty(endTime)) {
                whereParams.put("deliveryEndTime_lt", endTime);
            }
            whereParams.put("name_sort","delivery_end_time");
            whereParams.put("order_sort", SortType.DESC.getValue());
            PageBean<DeliveryBill> pageBean = deliveryBillService.ListDeliveryBills(page, rows, whereParams);
            List<DeliveryBill> dList = pageBean.getList();
            map.put("total", pageBean.getTotal());
            map.put("rows", dList);
            JsonConfig config = new JsonConfig();
            config.registerJsonValueProcessor(Date.class,
                    new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
            JSONObject json = JSONObject.fromObject(map, config);
            // 格式化时间后填入返回结果
            map.put("datarow", json);
        } catch (Exception e) {
            logger.error("ListDeliveryBiLL：发货单查询异常！"+ com.rmd.wms.constant.Constant.LINE,e);
        }
        return map;
    }

    /**
     * 查询发货单下的订单列表
     *
     * @return
     */
    @RequestMapping(value = "/DeliveryInfo")
    public ModelAndView deliveryInfo(
            @RequestParam(value = "Id", required = false) Integer Id) {

        ModelAndView mv = new ModelAndView("delivery/deliveryInfo");
        DeliveryBill deliveryBill = new DeliveryBill();
        List<StockOutBill> sOutList = new ArrayList<StockOutBill>(10);
        List<StockOutInfo> sInfos = new ArrayList<StockOutInfo>(10);
        List<StockOutInfo> sInfoList = new ArrayList<StockOutInfo>(10);
        Map<String, Object> whereParams = new HashMap<String, Object>();
        BigDecimal deliveryGoodsSum = new BigDecimal(0);//发运单下商品总价
        try {
            if (Id != 0) {
                deliveryBill = deliveryBillService.selectByPrimaryKey(Id);
                whereParams.put("deliveryNo", deliveryBill.getDeliveryNo());
                sOutList = stockOutBillService.ListStockOutBills(whereParams);
                if (sOutList.size() > 0) {
                    Map<String, Object> parmaMap = new HashMap<String, Object>();
                    for (StockOutBill stockOutBill : sOutList) {
                        parmaMap.put("orderNo", stockOutBill.getOrderNo());
                        sInfos = stockOutInfoService
                                .ListStockOutInfos(parmaMap);
                        for (StockOutInfo s : sInfos) {
                            sInfoList.add(s);
                        }
                        deliveryGoodsSum = deliveryGoodsSum.add(stockOutBill.getGoodsSum());
                    }

                }
            }
        } catch (Exception e) {
            logger.error("DeliveryInfo：发货单订单列表查询异常！"+ com.rmd.wms.constant.Constant.LINE,e);
        }
        mv.addObject("deliveryBill", deliveryBill);
        mv.addObject("sOutList", sOutList);
        mv.addObject("sInfoList", sInfoList);
        mv.addObject("deliveryGoodsSum", deliveryGoodsSum);
        return mv;
    }

    /**
     * 发运交接单批量打印方法
     *
     * @param Id
     * @param model
     * @return
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/deliveryBillPrint", method = RequestMethod.GET)
    public String deliveryBillPrint(@RequestParam(value = "Id") String Id
            , Model model, HttpServletResponse response, OutputStream out) {
        //发运交接单打印集合
        List<DeliveryBillPrint> deliveryPrintList = new ArrayList<DeliveryBillPrint>(10);
        try {
            DeliveryBill deliveryBill = new DeliveryBill();
            List<StockOutBill> sOutList = new ArrayList<StockOutBill>(10);
            List<StockOutInfo> sInfos = new ArrayList<StockOutInfo>(10);
            Map<String, Object> whereParams = new HashMap<String, Object>();
            List<DeliveryGoodsInfo> deliveryGoodsInfos = null;
            DeliveryGoodsInfo deliveryGoodsInfo = null;
            //承运单批量打印Id
            String[] IdArray = Id.split(",");
            if (IdArray.length > 0) {
                for (String dId : IdArray) {
                    int index=0;
                    deliveryBill = deliveryBillService.selectByPrimaryKey(Integer.valueOf(dId));
                    whereParams.put("deliveryNo", deliveryBill.getDeliveryNo());
                    sOutList = stockOutBillService.ListStockOutBills(whereParams);
                    deliveryGoodsInfos = new ArrayList<>();
                    if (sOutList.size() > 0) {
                        Map<String, Object> parmaMap = new HashMap<String, Object>();
                        DeliveryBillPrint deliveryBillPrint = new DeliveryBillPrint();
                        int deliveryGoodsAmout = 0;                     //发运单下商品总数
                        BigDecimal deliveryGoodsSum = new BigDecimal(0);//发运单下商品总价
                        for (StockOutBill stockOutBill : sOutList) {
                            index++;
                            parmaMap.put("orderNo", stockOutBill.getOrderNo());
                            sInfos = stockOutInfoService.ListStockOutInfos(parmaMap);
                            deliveryGoodsAmout += stockOutBill.getGoodsAmount();
                            deliveryGoodsSum = deliveryGoodsSum.add(stockOutBill.getGoodsSum());

                            for (StockOutInfo stockOutInfo : sInfos) {
                                deliveryGoodsInfo = new DeliveryGoodsInfo();
                                deliveryGoodsInfo.setIndex(index);
                                deliveryGoodsInfo.setDeliveryNo(deliveryBill.getDeliveryNo());            //发货单号
                                //订单信息
                                deliveryGoodsInfo.setOrderNo(stockOutBill.getOrderNo());
                                deliveryGoodsInfo.setInLogisticsTime(stockOutBill.getInLogisticsTime());
                                deliveryGoodsInfo.setGoodsAmount(stockOutBill.getGoodsAmount());        //订单商品总数
                                deliveryGoodsInfo.setGoodsSum(stockOutBill.getGoodsSum());              //订单商品总价
                                deliveryGoodsInfo.setLogisticsNo(stockOutBill.getLogisticsNo());        //运单号
                                deliveryGoodsInfo.setParcelWeight(stockOutBill.getParcelWeight());      //包裹重量(包含商品重量)
                                //订单下商品信息
                                deliveryGoodsInfo.setGoodsName(stockOutInfo.getGoodsName());    //商品名称
                                deliveryGoodsInfo.setSalesPrice(stockOutInfo.getSalesPrice());  //商品价格
                                deliveryGoodsInfo.setSpec(stockOutInfo.getSpec());              //商品规格
                                deliveryGoodsInfo.setValidityTime(stockOutInfo.getValidityTime());//商品有效期
                                deliveryGoodsInfo.setStockOutNum(stockOutInfo.getStockOutNum());//出库数量
                                deliveryGoodsInfo.setStockOutSum(stockOutInfo.getStockOutSum());//出库商品总价
                                deliveryGoodsInfos.add(deliveryGoodsInfo);
                            }
//                            deliveryGoodsInfos.add(deliveryGoodsInfo);
                            if(index>0&&index<sOutList.size()){//添加一行空数据
                                deliveryGoodsInfo=new DeliveryGoodsInfo();
                                deliveryGoodsInfos.add(deliveryGoodsInfo);
                            }
                        }
                        //承运单信息
                        deliveryBillPrint.setDeliveryNo(deliveryBill.getDeliveryNo());      //发货单号
                        deliveryBillPrint.setOrderSum(deliveryBill.getOrderSum());          //运单件数
                        deliveryBillPrint.setLogisComId(deliveryBillPrint.getLogisComId());
                        deliveryBillPrint.setLogisComName(deliveryBill.getLogisComName());
                        deliveryBillPrint.setWareId(deliveryBill.getWareId());
                        deliveryBillPrint.setWareName(deliveryBill.getWareName());
                        deliveryBillPrint.setDeliveryGoodsAmount(deliveryGoodsAmout);
                        deliveryBillPrint.setDeliveryGoodsSum(deliveryGoodsSum);
                        deliveryBillPrint.setDeliveryGoodsInfoList(deliveryGoodsInfos);
                        deliveryPrintList.add(deliveryBillPrint);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(Constant.LINE + "打印交接单", e);
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(deliveryPrintList);
        model.addAttribute("url", "/WEB-INF/jasper/deliveryBill.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "deliveryBillView"; // 对应jasper-defs.xml中的bean id
    }

    /**
     * 更新打印状态
     *
     * @param ids
     * @param request
     * @return
     */
    @RequestMapping("updatePrintStatus")
    @ResponseBody
    public Map<String, Object> updatePrintStatus(@RequestParam(value = "ids", required = false) String ids, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isEmpty(ids)) {
            map.put("status", "101");
            map.put("message", "参数为空！");
            return map;
        }
        String[] arr = ids.split(",");
        // 异步生成运费单
        List<Integer> idList = new ArrayList<>(arr.length);
        for (String idTemp : arr) {
            idList.add(Integer.valueOf(idTemp));
        }
        try {
            logisticsFreightBillService.generateFreightBill(idList);
        } catch (WMSException e) {
            if (StringUtils.isBlank(e.getCode())) {
                map.put("status", "102");
                map.put("message", "生成运费单失败");
                logger.error(Constant.LINE + "生成运费单失败", e);
            } else {
                map.put("status", e.getCode());
                map.put("message", e.getMsg());
            }
            return map;
        } catch (Exception e) {
            map.put("status", "500");
            map.put("message", "操作失败");
            logger.error(Constant.LINE + "生成运费单异常", e);
            return map;
        }
        // 修改打印状态
        DeliveryBill deliveryBill = null;
        for (String id : arr) {
            deliveryBill = new DeliveryBill();
            deliveryBill.setId(Integer.parseInt(id));
            deliveryBill.setDodeliveryPrint(Constant.TYPE_STATUS_YES);
            deliveryBillService.updateByPrimaryKeySelective(deliveryBill);
        }
        map.put("status", "200");
        map.put("message", "操作成功！");
        return map;
    }

}
