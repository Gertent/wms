package org.rmd.wms.provider.common;

import com.rmd.wms.common.service.TaskService;
import com.rmd.wms.constant.Constant;
import org.junit.Test;

import javax.annotation.Resource;

public class TestTaskService extends ICommonServiceTest {

    @Resource
    private TaskService taskService;

    @Test
    public void testErrorOrderRelock() {
        try {
            taskService.errorOrderRelock();
            System.out.println(Constant.LINE + "错误订单重新锁库完成...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
