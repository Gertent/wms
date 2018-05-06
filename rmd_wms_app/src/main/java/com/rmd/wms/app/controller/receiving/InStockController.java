package com.rmd.wms.app.controller.receiving;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.rmd.bms.entity.User;
import com.rmd.wms.app.common.AbstractAjaxController;
import com.rmd.wms.app.common.LoginRequired;
import com.rmd.wms.bean.GroundingBill;
import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.app.InStockBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.GroundingBillService;
import com.rmd.wms.service.InStockBillService;
import com.rmd.wms.service.PurchaseInInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/instock")
@Controller
public class InStockController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(InStockController.class);

    @Autowired
    private InStockBillService inStockBillService;
    @Autowired
    private GroundingBillService groundingBillService;
    @Autowired
    private PurchaseInInfoService purchaseInInfoService;

    /**
     * 获取入库单基本详情
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/show_instock_bill", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object getInStock(HttpServletRequest request, HttpServletResponse response, @RequestBody InStockBill param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getInStockNo())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        try {
            InStockBill inStockBill = inStockBillService.selectByInStockNo(param.getInStockNo());
            GroundingBill groundingBill = groundingBillService.selectByInStockNo(param.getInStockNo());
            if (inStockBill == null || groundingBill == null || !currentWare.getId().equals(inStockBill.getWareId()) || !currentWare.getId().equals(groundingBill.getWareId())) {
                json.put("status", "102");
                json.put("message", "上架任务不存在");
                return json;
            }
            if (Constant.GroundingBillStatus.FINISH == groundingBill.getStatus()) {
                json.put("status", "103");
                json.put("message", "上架任务已完成");
                return json;
            }
            if (groundingBill.getOuserId() != null && !currentUser.getId().equals(groundingBill.getOuserId())) {
                json.put("status", "104");
                json.put("message", "任务已被领取");
                return json;
            }
            InStockBillInfo info = new InStockBillInfo();
            BeanUtils.copyProperties(inStockBill, info);
            info.setSkuNum(purchaseInInfoService.selectCountByInStockNo(param.getInStockNo()));
            json.put("status", "200");
            json.put("message", "查询成功");
            json.put("result", info);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "查询失败");
            logger.error("查询入库单异常", e);
        }
        return json;
    }

    /**
     * 领取上架单任务
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/get_instock_bill_task", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object getPurchaseBillTask(HttpServletRequest request, HttpServletResponse response, @RequestBody InStockBill param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getInStockNo())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        try {
            InStockBill inStockBill = inStockBillService.selectByInStockNo(param.getInStockNo());
            GroundingBill groundingBill = groundingBillService.selectByInStockNo(param.getInStockNo());
            if (inStockBill == null || groundingBill == null || !currentWare.getId().equals(inStockBill.getWareId()) ||
                    !currentWare.getId().equals(groundingBill.getWareId())) {
                json.put("status", "102");
                json.put("message", "上架任务不存在");
                return json;
            }
            if (Constant.GroundingBillStatus.FINISH == groundingBill.getStatus()) {
                json.put("status", "103");
                json.put("message", "上架任务已完成");
                return json;
            }
            if (groundingBill.getOuserId() != null && !currentUser.getId().equals(groundingBill.getOuserId())) {
                json.put("status", "104");
                json.put("message", "任务已被领取");
                return json;
            }
            groundingBill.setOuserId(currentUser.getId());
            groundingBill.setOuserName(super.getCurrentUserName());
            InStockBillInfo inBillTask = inStockBillService.getInBillTask(groundingBill);
            if (inBillTask == null) {
                json.put("status", "105");
                json.put("message", "任务错误");
            } else {
                json.put("result", inBillTask);
                json.put("status", "200");
                json.put("message", "操作成功");
            }
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
            logger.error("领取上架单任务异常", e);
        }
        return json;
    }

    /**
     * 上架
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/putaway", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object putaway(HttpServletRequest request, HttpServletResponse response, @RequestBody InStockBillInfo param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getInStockNo()) || param.getBindIns() == null) {
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
            ServerStatus serverStatus = inStockBillService.putaway(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (WMSException e) {
            json.put("status", e.getCode());
            json.put("message", e.getMsg());
            logger.error("上架失败", e);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "上架失败");
            logger.error("上架异常", e);
        }
        return json;
    }

}
