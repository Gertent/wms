package com.rmd.wms.app.controller.stockout;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rmd.bms.entity.User;
import com.rmd.commons.servutils.Notification;
import com.rmd.oms.constant.OrderStatus;
import com.rmd.oms.constant.OrderType;
import com.rmd.oms.service.OrderBaseService;
import com.rmd.wms.app.common.AbstractAjaxController;
import com.rmd.wms.app.common.LoginRequired;
import com.rmd.wms.bean.StockOutBill;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.app.StockOutBillInfo;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.StockOutBillService;
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
 * Created by liu on 2017/2/24.
 */
@RequestMapping("/stockout")
@Controller
public class StockOutController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(StockOutController.class);

    @Autowired
    private StockOutBillService stockOutBillService;
    @Autowired
    private OrderBaseService orderBaseService;

    /**
     * 查询拣货单基本信息
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/show_stockout_bill", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object showStockOutBill(HttpServletRequest request, HttpServletResponse response, @RequestBody StockOutBill param){
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getOrderNo()) || param.getStatus() == null) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        try {
            StockOutBillInfo info = stockOutBillService.selectSobillInfoByOrderNo(param.getOrderNo());
            if (info == null || !currentWare.getId().equals(info.getWareId())) {
                json.put("status", "102");
                json.put("message", "任务不存在");
                return json;
            }
            if (info.getFreeze() == Constant.TYPE_STATUS_YES) {
                json.put("status", "104");
                json.put("message", "该任务已冻结");
                return json;
            }
            if ((info.getPickingStatus() == null || Constant.PickingStatus.STOCKOUT == info.getPickingStatus() || Constant.PickingStatus.ERROR == info.getPickingStatus())) {
                json.put("status", "105");
                json.put("message", "该任务缺货或被取消");
                return json;
            }
            if ((Constant.BillTypeParam.PICKING == param.getStatus() && Constant.PickingStatus.FINISH == info.getPickingStatus()) ||
                    (Constant.BillTypeParam.RECHECK == param.getStatus() && Constant.RecheckStatus.FINISH == info.getRecheckStatus())) {
                json.put("status", "106");
                json.put("message", "该任务已完成");
                return json;
            }
            if ((Constant.BillTypeParam.PICKING == param.getStatus() && info.getPickingUser() != null && !currentUser.getId().equals(info.getPickingUser())) ||
                    (Constant.BillTypeParam.RECHECK == param.getStatus() && info.getRecheckUser() != null && !currentUser.getId().equals(info.getRecheckUser()))) {
                json.put("status", "107");
                json.put("message", "该任务已被领取");
                return json;
            }
//            if (Constant.TYPE_STATUS_YES == info.getFreeze()) {
//                json.put("status", "108");
//                json.put("message", "该任务已冻结");
//                return json;
//            }
            // 验证订单状态是否可用，验证状态为103或104
            Integer otv = Integer.valueOf(param.getOrderNo().substring(0, 1));
            OrderType orderType = OrderType.getOrderType(otv);
            Notification<OrderStatus> noti = orderBaseService.selectOrderStatus(orderType, param.getOrderNo());
            logger.info(Constant.LINE + JSON.toJSONString(noti));
            if (!(noti != null && noti.getResponseData() != null && (OrderStatus.ORDER_CONFIRM.getValue() == noti.getResponseData().getValue()) || OrderStatus.ORDER_FINISH_PREPARE.getValue() == noti.getResponseData().getValue())) {
                json.put("status", "109");
                json.put("message", "订单状态错误");
                return json;
            }
            json.put("status", "200");
            json.put("message", "查询成功");
            json.put("result", info);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "查询失败");
            logger.error("查询拣货单异常", e);
        }
        return json;
    }

    /**
     * 领取拣货单任务
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping("/get_picking_bill_task")
    @LoginRequired
    @ResponseBody
    public Object getPickingBillTask(HttpServletRequest request, HttpServletResponse response, @RequestBody StockOutBill param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getOrderNo())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        try{
            StockOutBill stockOutBill = stockOutBillService.selectByOrderNo(param.getOrderNo());
            if (stockOutBill == null || !currentWare.getId().equals(stockOutBill.getWareId())) {
                json.put("status", "102");
                json.put("message", "任务不存在");
                return json;
            }
            if (stockOutBill.getWareId() != super.getCurrentWare().getId()) {
                json.put("status", "103");
                json.put("message", "非本仓库任务");
                return json;
            }
            if (Constant.PickingStatus.FINISH ==  stockOutBill.getPickingStatus()) {
                json.put("status", "104");
                json.put("message", "该任务已完成");
                return json;
            }
            if (Constant.PickingStatus.STOCKOUT ==  stockOutBill.getPickingStatus()) {
                json.put("status", "105");
                json.put("message", "该任务缺货");
                return json;
            }
//            if (Constant.PickingStatus.ERROR ==  stockOutBill.getPickingStatus()) {
//                json.put("status", "106");
//                json.put("message", "该任已被取消");
//                return json;
//            }
            if (stockOutBill.getPickingUser() != null && !currentUser.getId().equals(stockOutBill.getPickingUser())) {
                json.put("status", "107");
                json.put("message", "该任务已被领取");
                return json;
            }
            if (Constant.TYPE_STATUS_YES == stockOutBill.getFreeze()) {
                json.put("status", "108");
                json.put("message", "该任务已冻结");
                return json;
            }
            Integer otv = Integer.valueOf(param.getOrderNo().substring(0, 1));
            OrderType orderType = OrderType.getOrderType(otv);
            logger.debug(Constant.LINE + "param:" + orderType + "---" + param.getOrderNo());
            Notification<OrderStatus> noti = orderBaseService.selectOrderStatus(orderType, param.getOrderNo());
            logger.info(Constant.LINE + JSON.toJSONString(noti));
            if (noti == null || noti.getResponseData() == null) {
                json.put("status", "109");
                json.put("message", "订单状态服务异常");
                return json;
            }
            int orderStatus = noti.getResponseData().getValue();
            // 验证订单状态是否可用，验证状态为103 或者 出库单为拣货中并且订单状态为104正在出库或等待状态
            if (!(OrderStatus.ORDER_CONFIRM.getValue() == orderStatus ||
                    (OrderStatus.ORDER_FINISH_PREPARE.getValue() == orderStatus &&
                            (Constant.PickingStatus.PICKING == stockOutBill.getPickingStatus().intValue() ||
                                    Constant.PickingStatus.WATTING == stockOutBill.getPickingStatus().intValue())
                    )
                )
            ) {
                json.put("status", "110");
                json.put("message", "订单状态错误");
                return json;
            }
            stockOutBill.setPickingUser(currentUser.getId());
            stockOutBill.setPickingUserName(super.getCurrentUserName());
            StockOutBillInfo stockOutBillInfo = stockOutBillService.getPinkingTask(stockOutBill, currentUser.getId(), currentUser.getRealname(), orderStatus);
            if (stockOutBillInfo == null || stockOutBillInfo.getBindOutInfos() == null) {
                json.put("status", "111");
                json.put("message", "领取失败");
            } else {
                json.put("status", "200");
                json.put("message", "领取成功");
                json.put("result", stockOutBillInfo);
            }
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
            logger.error("领取拣货单任务异常", e);
        }
        return json;
    }

    /**
     * 拣货
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/picking", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object picking(HttpServletRequest request, HttpServletResponse response, @RequestBody StockOutBillInfo param) {
        JSONObject json = new JSONObject();
        Warehouse currentWare = super.getCurrentWare();
        param.setWareId(currentWare.getId());
        param.setWareName(currentWare.getWareName());
        try {
            ServerStatus serverStatus = stockOutBillService.doPicking(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (WMSException e) {
            json.put("status", e.getCode());
            json.put("message", e.getMsg());
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
            logger.error("拣货异常", e);
        }
        return json;
    }

    /**
     * 领取打包复检单任务
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping("/get_recheck_bill_task")
    @LoginRequired
    @ResponseBody
    public Object getRecheckBillTask(HttpServletRequest request, HttpServletResponse response, @RequestBody StockOutBill param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getOrderNo())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        try{
            StockOutBill stockOutBill = stockOutBillService.selectByOrderNo(param.getOrderNo());
            if (stockOutBill == null || !currentWare.getId().equals(stockOutBill.getWareId())) {
                json.put("status", "102");
                json.put("message", "任务不存在");
                return json;
            }
            if (Constant.RecheckStatus.FINISH ==  stockOutBill.getRecheckStatus()) {
                json.put("status", "103");
                json.put("message", "该任务已完成");
                return json;
            }
            if (Constant.PickingStatus.ERROR ==  stockOutBill.getPickingStatus()) {
                json.put("status", "104");
                json.put("message", "该任务已被取消");
                return json;
            }
            if (stockOutBill.getRecheckUser() != null && !currentUser.getId().equals(stockOutBill.getRecheckUser())) {
                json.put("status", "105");
                json.put("message", "该任务已被领取");
                return json;
            }
            if (Constant.TYPE_STATUS_YES == stockOutBill.getFreeze()) {
                json.put("status", "106");
                json.put("message", "该任务已冻结");
                return json;
            }
            // 验证订单状态是否可用，验证状态为104
            Integer otv = Integer.valueOf(param.getOrderNo().substring(0, 1));
            OrderType orderType = OrderType.getOrderType(otv);
            Notification<OrderStatus> noti = orderBaseService.selectOrderStatus(orderType, param.getOrderNo());
            logger.info(Constant.LINE + JSON.toJSONString(noti));
            if (!(noti != null && noti.getResponseData() != null && OrderStatus.ORDER_FINISH_PREPARE.getValue() == noti.getResponseData().getValue())) {
                json.put("status", "107");
                json.put("message", "订单状态错误");
                return json;
            }
            stockOutBill.setRecheckUser(currentUser.getId());
            stockOutBill.setRecheckUserName(super.getCurrentUserName());
            StockOutBillInfo stockOutBillInfo = stockOutBillService.getRecheckTask(stockOutBill);
            if (stockOutBillInfo == null || stockOutBillInfo.getSoInfos() == null) {
                json.put("status", "108");
                json.put("message", "领取错误");
            } else {
                json.put("status", "200");
                json.put("message", "领取成功");
                json.put("result", stockOutBillInfo);
            }
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
            logger.error("领取打包复检单任务异常", e);
        }
        return json;
    }

    /**
     * 打包复检
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping("/recheck")
    @LoginRequired
    @ResponseBody
    public Object recheck(HttpServletRequest request, HttpServletResponse response, @RequestBody StockOutBill param) {
        JSONObject json = new JSONObject();
        Warehouse currentWare = super.getCurrentWare();
        param.setWareId(currentWare.getId());
        param.setWareName(currentWare.getWareName());
        try{
            ServerStatus serverStatus = stockOutBillService.doRechecking(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
            logger.error("打包复检异常", e);
        }
        return json;
    }

}
