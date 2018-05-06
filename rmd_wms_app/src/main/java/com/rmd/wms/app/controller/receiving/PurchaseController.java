package com.rmd.wms.app.controller.receiving;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.rmd.bms.entity.User;
import com.rmd.wms.app.common.AbstractAjaxController;
import com.rmd.wms.app.common.LoginRequired;
import com.rmd.wms.bean.PurchaseBill;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.app.PurchaseBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.PurchaseBillService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liu on 2017/2/21.
 */
@RequestMapping("/purchase")
@Controller
public class PurchaseController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(PurchaseController.class);

    @Autowired
    private PurchaseBillService purchaseBillService;

    /**
     * 获取采购单基本详情
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/show_purchase_bill", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object getPurchase(HttpServletRequest request, HttpServletResponse response, @RequestBody PurchaseBill param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getPurchaseNo())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        Warehouse currentWare = super.getCurrentWare();
        try {
            PurchaseBill purchaseBill = purchaseBillService.selectByPurchaseNo(param.getPurchaseNo());
            if (purchaseBill == null || !currentWare.getId().equals(purchaseBill.getWareId())) {
                json.put("status", "102");
                json.put("message", "采购任务不存在");
                return json;
            }
            if (Constant.PurchaseBillStatus.FINISH == purchaseBill.getStatus()) {
                json.put("status", "103");
                json.put("message", "采购任务已完成");
                return json;
            }
            json.put("status", "200");
            json.put("message", "查询成功");
            json.put("result", purchaseBill);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "查询失败");
            logger.error("查询采购单异常", e);
        }
        return json;
    }

    /**
     * 领取采购单任务
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/get_purchase_bill_task", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object getPurchaseBillTask(HttpServletRequest request, HttpServletResponse response, @RequestBody PurchaseBill param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getPurchaseNo())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        Warehouse currentWare = super.getCurrentWare();
        try {
            PurchaseBill purchaseBill = purchaseBillService.selectByPurchaseNo(param.getPurchaseNo());
            if (purchaseBill == null || !currentWare.getId().equals(purchaseBill.getWareId())) {
                json.put("status", "102");
                json.put("message", "采购任务不存在");
                return json;
            }
            if (Constant.PurchaseBillStatus.FINISH == purchaseBill.getStatus()) {
                json.put("status", "103");
                json.put("message", "采购任务已完成");
                return json;
            }
            PurchaseBillInfo info = purchaseBillService.getPurBillTask(purchaseBill);
            json.put("status", "200");
            json.put("message", "操作成功");
            json.put("result", info);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
            logger.error("领取采购单任务异常", e);
        }
        return json;
    }

    /**
     * 提交采购任务（入库）
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/in_stock", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object inStock(HttpServletRequest request, HttpServletResponse response, @RequestBody PurchaseBillInfo param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getPurchaseNo()) || param.getInfos() == null) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        try {
            param.setOuserId(currentUser.getId());
            param.setOuserName(super.getCurrentUserName());
            param.setWareId(currentWare.getId());
            param.setWareName(currentWare.getWareName());
            ServerStatus serverStatus = purchaseBillService.purInStock(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (WMSException e) {
            json.put("status", e.getCode());
            json.put("message", e.getMsg());
            logger.error(Constant.LINE + e.getMessage(), e);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
            logger.error("提交采购任务（入库）异常", e);
        }
        return json;
    }
}
