package org.rmd.wms.provider.service.wms2scm;

import com.rmd.wms.service.wms2scm.PurGroundingService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;

public class TestPurGroundingService extends ICommonServiceTest {

    @Resource
    private PurGroundingService purGroundingService;

    @Test
    public void testPushPurGrounding() {
        try {
            purGroundingService.pushPurGrounding("RK001170502547");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
