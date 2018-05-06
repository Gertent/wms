package com.rmd.wms.app.controller.user;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.rmd.bms.entity.User;
import com.rmd.bms.entity.Vo.UserRoleVo;
import com.rmd.bms.entity.Vo.UserVo;
import com.rmd.bms.service.BmsApiService;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.Notifications;
import com.rmd.wms.app.common.AbstractAjaxController;
import com.rmd.wms.app.common.LoginRequired;
import com.rmd.wms.app.model.CurrentUser;
import com.rmd.wms.app.util.AESCipher;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.WarehouseUser;
import com.rmd.wms.bean.vo.app.MoveInUsersParam;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.constant.ConstantEncryption;
import com.rmd.wms.service.WarehouseService;
import com.rmd.wms.service.WarehouseUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yao.util.collection.CollectionUtil;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequestMapping("/user")
@Controller
public class UserController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(UserController.class);

    @Resource(name = "bmsApiService")
    private BmsApiService bmsApiService;
    @Autowired
    private WarehouseUserService warehouseUserService;
    @Autowired
    private WarehouseService warehouseService;

    /**
     * 用户登录
     *
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response, @RequestBody CurrentUser user) {
        JSONObject json = new JSONObject();
        if (user == null || user.getUser() == null || StringUtils.isBlank(user.getUser().getLoginname()) ||
                StringUtils.isBlank(user.getUser().getPassword()) || user.getWarehouse() == null || user.getWarehouse().getId() == null) {
            json.put("status", "101");
            json.put("massage", "参数错误");
            return json;
        }
        try {
            Notification<UserVo> noti = bmsApiService.selectCodeByUser(user.getUser().getLoginname(), user.getUser().getPassword(), Constant.RF_APP_CODE);
            logger.info(Constant.LINE + "bmsApiService.selectCodeByUser : " + JSON.toJSONString(noti));
            if (noti == null) {
                json.put("status", "102");
                json.put("message", "登录服务异常");
                return json;
            }
            if (noti.getNotifCode() != Notifications.OK.getNotifCode()) {
                logger.info(Constant.LINE + "Bms登录失败：" + noti.getNotifInfo());
                json.put("status", "103");
                json.put("message", "Bms登录失败");
                return json;
            }
            UserVo userVo = noti.getResponseData();
            if (userVo == null || userVo.getId() == null || StringUtils.isBlank(userVo.getLoginname()) || StringUtils.isBlank(userVo.getJobNum())) {
                json.put("status", "104");
                json.put("message", "该用户不存在");
                return json;
            }
            AESCipher aes = new AESCipher(ConstantEncryption.APP_RANDOM);
            Cookie cookie = new Cookie("token", aes.encrypt(userVo.getId().toString() + "," + user.getWarehouse().getId().toString()));
            cookie.setMaxAge(Constant.LOGIN_VALID_TIME);
            cookie.setPath("/");
            response.addCookie(cookie);
            json.put("status", "200");
            json.put("message", "登录成功");
            json.put("result", userVo);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "登录失败");
            logger.error("登录异常", e);
        }
        return json;
    }

    /**
     * 获取用户登录的仓库
     *
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/get_user_wares", method = RequestMethod.POST)
    @ResponseBody
    public Object getUserWares(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {
        JSONObject json = new JSONObject();
        if (user == null || StringUtils.isBlank(user.getLoginname())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        try {
            // 从bms获取用户的信息
            Notification<User> noti = bmsApiService.selectUserByName(user.getLoginname());
            logger.info(Constant.LINE + "bmsApiService.selectUserByName : " + JSON.toJSONString(noti));
            if (noti == null || Notifications.OK.getNotifCode() != noti.getNotifCode() || noti.getResponseData() == null) {
                json.put("status", "102");
                json.put("message", "验证平台用户失败");
                return json;
            }
            // 查询用户的仓库信息
            user = noti.getResponseData();
            List<Warehouse> warehouses = warehouseService.selectByUserId(user.getId());
            json.put("status", "200");
            json.put("message", "查询成功");
            json.put("result", warehouses);
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "查询失败");
            logger.error(Constant.LINE + "查询仓库异常", e);
        }
        return json;
    }

    /**
     * 获取移库移入用户列表
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/get_move_in_users", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object getMoveInUsers(HttpServletRequest request, HttpServletResponse response, @RequestBody MoveInUsersParam param) {
        JSONObject json = new JSONObject();
        if (param == null || StringUtils.isBlank(param.getOperationCode())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        Warehouse currentWare = super.getCurrentWare();
        try {
            Notification<List<UserRoleVo>> noti = bmsApiService.selectUserByOperationCode(Constant.RF_APP_CODE, param.getOperationCode());
            logger.info(Constant.LINE + "bmsApiService.selectUserByOperationCode : " + JSON.toJSONString(noti));
            if (noti == null || Notifications.OK.getNotifCode() != noti.getNotifCode()) {
                json.put("status", "102");
                json.put("message", "获取用户列表失败");
                return json;
            }
            List<UserRoleVo> users = noti.getResponseData();
            if (users == null || users.isEmpty()) {
                json.put("status", "103");
                json.put("message", "本仓库无移库移入人员");
                return json;
            }
            Set<Integer> userIds = (Set<Integer>) CollectionUtil.toFieldSet(users, "id");
            param.setWareId(currentWare.getId());
            param.setUserIds(new ArrayList<>(userIds));
            List<WarehouseUser> wareUsers = warehouseUserService.selectByWareIdAndUserIds(param);
            if (wareUsers == null || wareUsers.isEmpty()) {
                json.put("status", "103");
                json.put("message", "本仓库无移库移入人员");
                return json;
            }
            List<UserRoleVo> vaildWareUsers = new ArrayList<>();
            for (WarehouseUser userTemp : wareUsers) {
                for (UserRoleVo voTemp : users) {
                    if (userTemp.getUserId().equals(voTemp.getId())) {
                        vaildWareUsers.add(voTemp);
                    }
                }
            }
            json.put("status", "200");
            json.put("message", "查询成功");
            json.put("result", vaildWareUsers);
        } catch (Exception e) {
            logger.error(Constant.LINE + "获取移库移入用户列表异常", e);
        }
        return json;
    }

    /**
     * 获取移库移入用户列表
     *
     * @param request
     * @param response
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public Object test(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        DefaultMQProducer defaultMQProducer;

        // 初始化
        defaultMQProducer = new DefaultMQProducer("testG");
        defaultMQProducer.setNamesrvAddr("mqns.saharabuy.alilocal:9876");
        defaultMQProducer.setInstanceName(String.valueOf(System.currentTimeMillis()));
        try {
            defaultMQProducer.start();
            Message msg = new Message("test111", "testtag111", "hello world!".getBytes());
            SendResult sendResult = defaultMQProducer.send(msg);
            logger.info(Constant.LINE + JSON.toJSONString(sendResult));
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 修改密码
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/alter_pwd", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public Object alterPwd(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {
        JSONObject json = new JSONObject();
        if (user == null || StringUtils.isBlank(user.getPassword())) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        User currentUser = super.getCurrentUser();
        try {
            Notification<Boolean> noti = bmsApiService.updatePasswordByUserId(currentUser.getId(), user.getPassword());
            if (noti == null || Notifications.OK.getNotifCode() != noti.getNotifCode()) {
                json.put("status", "102");
                json.put("message", "修改密码出错");
                return json;
            }
            if (noti.getResponseData()) {
                json.put("status", "200");
                json.put("message", "修改密码成功");
            } else {
                json.put("status", "102");
                json.put("message", "修改密码出错");
            }
        } catch (Exception e) {
            json.put("status", "-200");
            json.put("message", "修改密码失败");
            logger.error(Constant.LINE + "修改密码异常", e);
        }
        return json;
    }
}
