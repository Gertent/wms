package com.rmd.wms.web.common;

import com.rmd.bms.entity.User;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.web.constant.Constant;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName: AbstractAjaxController
 * @Description:AbstractAjaxController抽象类
 * @author: hanguoshuai
 * @date: 2015年11月3日 下午1:31:06
 */
public abstract class AbstractAjaxController implements AbstractController {

    private static Logger logger = Logger.getLogger(AbstractAjaxController.class);

    protected void writeOutWebMessage(WebMessageCode code, HttpServletRequest request, HttpServletResponse response) {
        try {
            WebMessages webMessages = new WebMessages(request);
            webMessages.addMessageCode(code);
            ResponseUtils.renderJson(response, mapToObjString(webMessages.getMessagesMap()));
        } catch (Exception e) {
            logger.error("AbstractAjaxController.writeOutWebMessage(WebMessageCode code,HttpServletRequest request,HttpServletResponse response) is error", e);
        }
    }

    /**
     * @throws
     * @Title:mapToObjString
     * @Description:将map类型转化为对象型json字符串</br> 对象中也有可能含有collection
     * @param:@param data
     * @param:@return
     * @return:String
     */
    protected String mapToObjString(Map<?, ?> data) {
        JSONObject object = JSONObject.fromObject(data);
        return object.toString();
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    protected User getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

    /**
     * 获取当前用户id
     *
     * @return
     */
    protected Integer getCurrentUserId() {
        User user = getCurrentUser();
        if (user != null) {
            return user.getId();
        }
        return null;
    }
    /**
     * 获取当前用户名称 如：工号＋账号，例如(001)zhangsan
     * */
    protected String getCurrentUserName() {
        User user = getCurrentUser();
        if (user != null) {
            return "(" + user.getJobNum() + ")" + user.getLoginname();
        }
        return null;
    }
    /**
     * 获取当前仓库
     *
     * @return
     */
    protected Warehouse getCurrentWare() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return (Warehouse)session.getAttribute(Constant.CURRENT_WARE);
    }

    /**
     * 获取当前仓库
     *
     * @return
     */
    protected Integer getCurrentWareId() {
        Warehouse ware = getCurrentWare();
        if (ware != null) {
            return ware.getId();
        }
        return null;
    }

    /**
     * 存放当前仓库
     * @param ware
     */
    protected void setCurrentWare(Warehouse ware) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute(Constant.CURRENT_WARE, ware);
    }
    /**
     * 获取当前shiro session
     * */
    protected Session getSessionOfShiro() {
    	Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return session;
	}

}
