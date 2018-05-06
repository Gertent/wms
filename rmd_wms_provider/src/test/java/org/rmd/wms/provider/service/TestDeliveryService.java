package org.rmd.wms.provider.service;

import com.alibaba.fastjson.JSON;
import com.rmd.wms.bean.DeliveryBill;
import com.rmd.wms.bean.vo.app.DeliveryBillInfo;
import com.rmd.wms.service.DeliveryBillService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;

public class TestDeliveryService extends ICommonServiceTest {

    @Resource
    private DeliveryBillService deliveryBillService;

    @Test
    public void testDelivery() {
        try {
            String s = "{\"id\":3,\"deliveryNo\":\"FH0011702003\",\"soBillList\":[{\"id\":1}]}";
            DeliveryBillInfo info = JSON.parseObject(s, DeliveryBillInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        try {
            DeliveryBill bill = new DeliveryBill();
            bill.setDeliveryNo("teste111");
            bill.setDodeliveryPrint(1);
            deliveryBillService.insertSelective(bill);
            System.out.println("---------------------------" + bill.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
