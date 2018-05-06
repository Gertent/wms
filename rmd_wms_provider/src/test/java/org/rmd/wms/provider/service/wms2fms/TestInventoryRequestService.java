package org.rmd.wms.provider.service.wms2fms;

import com.rmd.wms.bean.po.InventoryRequestMqVo;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.wms2fms.InventoryRequestService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class TestInventoryRequestService extends ICommonServiceTest {

    @Resource
    private InventoryRequestService inventoryRequestService;

    @Test
    public void testPushPurchaseInStock() {
        try {
            List<InventoryRequestMqVo> mqVoList = new ArrayList<>();
            // 测试添加提报数据
            boolean flag = inventoryRequestService.pushInventoryReport(mqVoList);
            System.out.println(Constant.LINE + flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
