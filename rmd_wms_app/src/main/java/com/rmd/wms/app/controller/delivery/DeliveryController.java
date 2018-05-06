package com.rmd.wms.app.controller.delivery;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.rmd.bms.entity.User;
import com.rmd.wms.app.common.AbstractAjaxController;
import com.rmd.wms.app.common.LoginRequired;
import com.rmd.wms.bean.DeliveryBill;
import com.rmd.wms.bean.LogisticsCompany;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.app.DeliveryBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.DeliveryBillService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by liu on 2017/2/25.
 */
@RequestMapping("/delivery")
@Controller
public class DeliveryController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(DeliveryController.class);

    @Autowired
    private DeliveryBillService deliveryBillService;

    /**
     * 显示物流公司
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/show_Logistics_companies", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object getLogisticsCompanyList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        LogisticsCompany lc = new LogisticsCompany();
        lc.setWareId(super.getCurrentWare().getId());
        try {
            List<LogisticsCompany> companyList = deliveryBillService.getLogisticsCompanyList(lc);
            json.put("result", companyList);
            json.put("status", "200");
            json.put("message", "查询成功");
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "查询失败");
            logger.error("查询异常", e);
        }
        return json;
    }

    /**
     * 准备交接
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/go_delivery", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object goDelivery(HttpServletRequest request, HttpServletResponse response, @RequestBody DeliveryBill param) {
        JSONObject json = new JSONObject();
        if (param == null || param.getLogisComId() == null || StringUtils.isBlank(param.getLogisComName())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        try {
            param.setDeliveryUser(super.getCurrentUser().getId());
            param.setDeliveryUserName(super.getCurrentUserName());
            DeliveryBillInfo deliveryBillInfo = deliveryBillService.goDelivery(param, super.getCurrentWare());
            json.put("status", "200");
            json.put("message", "查询成功");
            json.put("result", deliveryBillInfo);
        } catch (WMSException e) {
            json.put("status", "102");
            json.put("message", "创建发货单异常");
            logger.error(Constant.LINE + "创建发货单异常", e);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "查询失败");
            logger.error(Constant.LINE + "查询异常", e);
        }
        return json;
    }

    /**
     * 交接
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/do_delivery", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object delivery(HttpServletRequest request, HttpServletResponse response, @RequestBody DeliveryBillInfo param) {
        JSONObject json = new JSONObject();
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        param.setUserId(currentUser.getId());
        param.setUserName(super.getCurrentUserName());
        param.setWareId(currentWare.getId());
        param.setWareName(currentWare.getWareName());
        try {
            ServerStatus serverStatus = deliveryBillService.delivery(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
            logger.error("交接异常", e);
        }
        return json;
    }

}
