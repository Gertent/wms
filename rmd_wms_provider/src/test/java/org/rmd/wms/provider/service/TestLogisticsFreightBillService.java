package org.rmd.wms.provider.service;

import com.alibaba.fastjson.JSON;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.LogisticsFreightBillService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author : liu
 * @Date : 2017/7/5
 */
public class TestLogisticsFreightBillService extends ICommonServiceTest {

    @Resource
    private LogisticsFreightBillService logisticsFreightBillService;

    @Test
    public void testSelectByProvCodeAndWeight() {
        try {
            BigDecimal freightPrice = logisticsFreightBillService.getFreightPrice(1, "110100", 2d, 1);
            System.out.println(Constant.LINE + JSON.toJSONString(freightPrice));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
