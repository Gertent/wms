package org.rmd.wms.provider.service;

import com.alibaba.fastjson.JSON;
import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.RepositoryDeliverrangeService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;

public class TestRepositoryDeliverrangeService extends ICommonServiceTest {

    @Resource
    private RepositoryDeliverrangeService repositoryDeliverrangeService;

    @Test
    public void testSelectWareByProvCode() {
        try {
            Notification<Warehouse> notification = repositoryDeliverrangeService.selectWareByProvCode("130000");
            System.out.println(Constant.LINE + JSON.toJSONString(notification));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
