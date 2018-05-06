package org.rmd.wms.provider.service;

import com.alibaba.fastjson.JSON;
import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.LogisticsCompany;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.LogisticsCompanyService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;

public class TestLogisticsCompanyService extends ICommonServiceTest {

    @Resource
    private LogisticsCompanyService logisticsCompanyService;

    @Test
    public void testSelectByProvCodeAndWeight() {
        try {
            Notification<LogisticsCompany> notification = logisticsCompanyService.selectByProvCodeAndWeight("110000", 50.0);
            System.out.println(Constant.LINE + JSON.toJSONString(notification));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
