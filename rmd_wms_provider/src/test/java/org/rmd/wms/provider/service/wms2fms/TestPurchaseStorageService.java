package org.rmd.wms.provider.service.wms2fms;

import com.rmd.wms.service.wms2fms.PurchaseStorageService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;

public class TestPurchaseStorageService extends ICommonServiceTest {

    @Resource
    private PurchaseStorageService purchaseStorageService;

    @Test
    public void testPushPurchaseInStock() {
        try {
            purchaseStorageService.pushPurchaseInStock("RK001170405522");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
