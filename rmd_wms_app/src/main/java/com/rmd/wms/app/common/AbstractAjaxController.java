package com.rmd.wms.app.common;

import com.rmd.bms.entity.User;
import com.rmd.wms.app.model.CurrentUser;
import com.rmd.wms.bean.Warehouse;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: AbstractAjaxController
 * @Description:AbstractAjaxController抽象类
 */
public abstract class AbstractAjaxController implements AbstractController {

    private static Logger logger = Logger.getLogger(AbstractAjaxController.class);

    protected CurrentUser currentUser;

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        /**
         * 自动转换日期类型的字段格式
         */
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

    }

    /**
     * 获取当前用户
     *
     * @return
     */
    protected User getCurrentUser() {
        return this.currentUser == null ? null : this.currentUser.getUser();
    }

    /**
     * 获取当前用户名称 如：工号＋账号，例如(001)zhangsan
     */
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
        return this.currentUser == null ? null : this.currentUser.getWarehouse();
    }

    /**
     * aop动态设置当前用户
     *
     * @param user
     */
    @CurrentUserAnno
    public void setCurrentUser(CurrentUser user) {
        this.currentUser = user;
    }

}
