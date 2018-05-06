package org.rmd.wms.provider.service.wms2fms;

import com.rmd.wms.service.wms2fms.SalesSlipService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;

public class TestSalesSlipService extends ICommonServiceTest {

    @Resource
    private SalesSlipService salesSlipService;

    @Test
    public void testPushSalesSlipIn() {
        try {
            salesSlipService.pushSalesSlipIn("21705118536772");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPushSalesSlipOut() {
        try {
            salesSlipService.pushSalesSlipOut("FH001170327049");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
