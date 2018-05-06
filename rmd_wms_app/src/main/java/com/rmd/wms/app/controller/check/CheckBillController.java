package com.rmd.wms.app.controller.check;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.rmd.bms.entity.User;
import com.rmd.wms.app.common.AbstractAjaxController;
import com.rmd.wms.app.common.LoginRequired;
import com.rmd.wms.bean.CheckBill;
import com.rmd.wms.bean.CheckUser;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.CheckInfos;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.CheckBillService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author : liu
 * @Date : 2017/4/13
 */
@RequestMapping("/check")
@Controller
public class CheckBillController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(CheckBillController.class);

    @Autowired
    private CheckBillService checkBillService;

    /**
     * 领取拣货任务
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/get_check_task", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object getCheckTask(HttpServletRequest request, HttpServletResponse response, @RequestBody CheckInfos param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getCheckNo())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        Warehouse currentWare = super.getCurrentWare();
        CheckBill checkBill = checkBillService.selectByCheckNo(param.getCheckNo());
        if (checkBill == null || !currentWare.getId().equals(checkBill.getWareId())) {
            json.put("status", "102");
            json.put("message", "该盘点单不存在");
            return json;
        }
        if (!currentWare.getId().equals(checkBill.getWareId())) {
            json.put("status", "103");
            json.put("message", "该盘点单非本仓库");
            return json;
        }
        if (Constant.CheckBillTimes.SECOND == checkBill.getCheckTimes().intValue() && Constant.CheckBillStatus.FINISH == checkBill.getStatus().intValue()) {
            json.put("status", "104");
            json.put("message", "该盘点单已盘点完成");
            return json;
        }
        if (!currentUser.getId().equals(checkBill.getLastChecker()) && Constant.CheckBillStatus.CHECKING == checkBill.getStatus().intValue()) {
            json.put("status", "105");
            json.put("message", "该盘点单正在盘点中");
            return json;
        }
        if (currentUser.getId().equals(checkBill.getLastChecker()) && Constant.CheckBillStatus.CHECKING == checkBill.getStatus().intValue()) {
            param.setRegetTask(Constant.TYPE_STATUS_YES);
        } else {
            param.setRegetTask(Constant.TYPE_STATUS_NO);
        }
        CheckUser cUser = new CheckUser();
        cUser.setUserId(currentUser.getId());
        cUser.setUserName(super.getCurrentUserName());
        List<CheckUser> checkUsers = new ArrayList<>();
        checkUsers.add(cUser);
        param.setUsers(checkUsers);
        BeanUtils.copyProperties(checkBill, param);
        param.setLastChecker(currentUser.getId());
        try {
            CheckInfos checkBillTask = checkBillService.getCheckBillTask(param);
            json.put("status", "200");
            json.put("message", "领取成功");
            json.put("result", checkBillTask);
        } catch (WMSException e) {
            json.put("status", "106");
            json.put("message", e.getMsg());
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "领取失败");
        }
        return json;
    }

    /**
     * 盘点
     * @param request
     * @param response
     * @param param
     * 1、盘点单id，2、盘点单号，3、盘点详情，4、是否中断，5、盘点次数
     * 详情中：1、详情id，2、第一次盘点数量或第二次盘点数量，3、货位库存数量，
     * @return
     */
    @RequestMapping(value = "/do_check", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object doCheck(HttpServletRequest request, HttpServletResponse response, @RequestBody CheckInfos param) {
        JSONObject json = new JSONObject();
        if (param == null || param.getId() == null || StringUtils.isBlank(param.getCheckNo()) || param.getInterrupt() == null || param.getCheckTimes() == null ||
                param.getInfos() == null || param.getInfos().size() < 1) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        Warehouse currentWare = super.getCurrentWare();
        CheckBill checkBill = checkBillService.selectByCheckNo(param.getCheckNo());
        if (checkBill == null || !currentWare.getId().equals(checkBill.getWareId())) {
            json.put("status", "102");
            json.put("message", "该盘点单不存在");
            return json;
        }
        if (!currentWare.getId().equals(checkBill.getWareId())) {
            json.put("status", "103");
            json.put("message", "该盘点单非本仓库");
            return json;
        }
        if (Constant.CheckBillTimes.SECOND == checkBill.getCheckTimes() && Constant.CheckBillStatus.FINISH == checkBill.getStatus()) {
            json.put("status", "104");
            json.put("message", "该盘点单已盘点完成");
            return json;
        }
        try {
            ServerStatus serverStatus = checkBillService.doCheck(param);
            json.put("status", serverStatus.getStatus());
            json.put("message", serverStatus.getMessage());
        } catch (WMSException e) {
            json.put("status", "106");
            json.put("message", e.getMsg());
            logger.error(e);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "盘点失败");
            logger.error("盘点操作异常", e);
        }
        return json;
    }



}
