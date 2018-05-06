package com.rmd.wms.app.controller.movement;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.rmd.bms.entity.User;
import com.rmd.wms.app.common.AbstractAjaxController;
import com.rmd.wms.app.common.LoginRequired;
import com.rmd.wms.bean.Movement;
import com.rmd.wms.bean.MovementInfo;
import com.rmd.wms.bean.StockOutBill;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.app.MovementBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.MovementService;
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
import java.util.List;

/**
 * Created by liu on 2017/3/2.
 */
@RequestMapping("/movement")
@Controller
public class MovementController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(MovementController.class);

    @Autowired
    private MovementService movementService;
    @Autowired
    private StockOutBillService stockOutBillService;

    /**
     * 拣货报损
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/picking_breakage", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object pickingBreakage(HttpServletRequest request, HttpServletResponse response, @RequestBody MovementBillInfo param) {
        JSONObject json = new JSONObject();
        if (param == null || param.getInUser() == null || param.getMoveInfos() == null || param.getMoveAmount() <= 0 || param.getType() != Constant.MovementType.PICKING_BREAKAGE ||
                !(param.getBreakType() == Constant.MovementBreakType.BREAK_DOWN || param.getBreakType() == Constant.MovementBreakType.LOSE)) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        param.setOutUser(currentUser.getId());
        param.setOutUserName(super.getCurrentUserName());
        param.setWareId(currentWare.getId());
        param.setWareName(currentWare.getWareName());
        try {
            StockOutBill outBill = stockOutBillService.selectByOrderNo(param.getOrderNo());
            if (outBill == null || !currentWare.getId().equals(outBill.getWareId())) {
                json.put("status", "102");
                json.put("message", "订单不存在");
                return json;
            }
            ServerStatus serverStatus = movementService.pickingBreakage(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (WMSException e) {
            json.put("status", e.getCode());
            json.put("message", e.getMsg());
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "拣货报损失败");
            logger.error("拣货报损异常", e);
        }
        return json;
    }

    /**
     * 普通拣货和普通移库校验货位和数量
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/verify_moving_and_breakage", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object verifyMovingAndBreakage(HttpServletRequest request, HttpServletResponse response, @RequestBody MovementInfo param) {
        JSONObject json = new JSONObject();
        if (param == null || param.getOutNum() == null || StringUtils.isBlank(param.getGoodsCode()) || StringUtils.isBlank(param.getLocationNoOut())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        try {
            ServerStatus serverStatus = movementService.verifyMovingAndBreakage(param, super.getCurrentWare().getId());
            if (serverStatus == null) {
                json.put("status", "102");
                json.put("message", "数据错误");
            } else {
                json.put("status", serverStatus.getStatus());
                json.put("message", serverStatus.getMessage());
                json.put("result", serverStatus.getResult());
            }
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "报损失败");
            logger.error("报损异常", e);
        }
        return json;
    }

    /**
     * 普通移库和报损
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/moving_and_breakage", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object movingAndBreakage(HttpServletRequest request, HttpServletResponse response, @RequestBody MovementBillInfo param) {
        JSONObject json = new JSONObject();

        if (param == null || param.getInUser() == null || param.getMoveInfos() == null || param.getMoveAmount() <= 0 ||
                !(param.getType() == Constant.MovementType.GENERAL_BREAKAGE || param.getType() == Constant.MovementType.GENERAL_MOVEMENT) ||
                ((param.getType() == Constant.MovementType.GENERAL_BREAKAGE && (param.getBreakType() != Constant.MovementBreakType.BREAK_DOWN &&
                        param.getBreakType() != Constant.MovementBreakType.LOSE)))) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        param.setOutUser(currentUser.getId());
        param.setOutUserName(super.getCurrentUserName());
        param.setWareId(currentWare.getId());
        param.setWareName(currentWare.getWareName());
        try {
            ServerStatus serverStatus = movementService.movingAndBreakage(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (WMSException e) {
            json.put("status", e.getCode());
            json.put("message", e.getMsg());
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "报损失败");
            logger.error("报损异常", e);
        }
        return json;
    }

    /**
     * 查询移库任务列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/show_movement_tasks", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object getMovementList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Movement movement = new MovementBillInfo();
        movement.setInUser(super.getCurrentUser().getId());
        movement.setWareId(super.getCurrentWare().getId());
        try {
            List<Movement> movements = movementService.getMovementList(movement);
            json.put("status", "200");
            json.put("message", "查询成功");
            json.put("result", movements);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "报损失败");
            logger.error("报损异常", e);
        }
        return json;
    }

    /**
     * 领取移库任务
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/get_movement_task", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object getMovementTask(HttpServletRequest request, HttpServletResponse response, @RequestBody MovementBillInfo param) {
        JSONObject json = new JSONObject();
        if (param == null || param.getId() == null) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        Warehouse currentWare = super.getCurrentWare();
        try {
            Movement movement = movementService.selectByPrimaryKey(param.getId());
            if (movement == null || !currentWare.getId().equals(movement.getWareId())) {
                json.put("status", "102");
                json.put("message", "移库单不存在");
                return json;
            }
            MovementBillInfo task = movementService.getMovementTask(param);
            json.put("status", "200");
            json.put("message", "查询成功");
            json.put("result", task);
        } catch (WMSException e) {
            json.put("status", e.getCode());
            json.put("message", e.getMsg());
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "报损失败");
            logger.error("报损异常", e);
        }
        return json;
    }

    /**
     * 移库入库操作
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/move_putaway", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object moveInStock(HttpServletRequest request, HttpServletResponse response, @RequestBody MovementBillInfo param) {
        JSONObject json = new JSONObject();
        if (param == null || param.getId() == null || param.getBindIns() == null || param.getBindIns().size() <= 0) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        param.setInUser(currentUser.getId());
        param.setInUserName(super.getCurrentUserName());
        param.setWareId(super.getCurrentWare().getId());
        try {
            ServerStatus serverStatus = movementService.movePutaway(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (WMSException e) {
            json.put("status", e.getCode());
            json.put("message", e.getMsg());
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "操作失败");
            logger.error("移入异常", e);
        }
        return json;
    }


}
