package org.rmd.wms.provider.service;

import com.alibaba.fastjson.JSON;
import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.StockInfoVo;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.RepositoryDeliverrangeService;
import com.rmd.wms.service.StockService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;
import java.util.List;

public class TestStockService extends ICommonServiceTest {

    @Resource
    private StockService stockService;

    @Test
    public void testGetSkuStockNum() {
        try {
            Notification<Integer> notification = stockService.getSkuStockNum("A00000000001", 1);
            System.out.println(Constant.LINE + JSON.toJSONString(notification));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWareStockByCriteria() {
        try {
            StockInfoVo param = new StockInfoVo();
            param.setGoodsCode("A00000000003");
            param.setWareId(1);
            Notification<List<StockInfoVo>> noti = stockService.getWareStockByCriteria(param);
            System.out.println(Constant.LINE + JSON.toJSONString(noti));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
