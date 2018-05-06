package org.rmd.wms.provider.service;

import com.alibaba.fastjson.JSON;
import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.OrderLogisticsInfo;
import com.rmd.wms.bean.StockOutInfo;
import com.rmd.wms.bean.vo.AlterSOBillParam;
import com.rmd.wms.bean.vo.OrderInfo;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.StockOutBillService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liu on 2017/3/20.
 */

public class TestStockOutBillService extends ICommonServiceTest {

    @Resource
    private StockOutBillService stockOutBillService;

    @Test
    public void testOrderLock() {
        OrderInfo param = new OrderInfo();
        param.setOrderNo("11703069430716");
        param.setOrderType(Constant.StockOutBillOrderType.GENERAL);
        param.setOrderdate(new Date());
        param.setWareId(1);
        param.setWareName("雷蒙德北京仓库");
        param.setGoodsAmount(5);
        param.setGoodsSum(new BigDecimal(500));
        // 收货地址
        OrderLogisticsInfo olInfo = new OrderLogisticsInfo();
        olInfo.setOrderNo("11703069430716");
        olInfo.setReceivername("test_receiver");
        olInfo.setDetailedAddress("收货详细地址");
        olInfo.setProvCode(110000);
        olInfo.setProvName("北京市");
        olInfo.setReceiveMobile("18810880000");
        olInfo.setReceiveTel("010-12345678");
        param.setOrderLogiInfo(olInfo);
        // 商品数据
        List<StockOutInfo> soInfos = new ArrayList<>();

        StockOutInfo info1 = new StockOutInfo();
        info1.setGoodsName("测试数据test1");
        info1.setGoodsCode("A00000000003");
        info1.setGoodsBarCode("A00000000003");
        info1.setSalesPrice(new BigDecimal(1));
        info1.setStockOutNum(10);
        info1.setSpec("AABBCCDD1");
        info1.setPackageNum("10/箱");
        info1.setUnit("箱");
        info1.setValidityTime(new Date());
        soInfos.add(info1);

        StockOutInfo info2 = new StockOutInfo();
        info2.setGoodsName("测试数据test2");
        info2.setGoodsCode("A00000000002");
        info2.setGoodsBarCode("A00000000002");
        info2.setSalesPrice(new BigDecimal(2));
        info2.setStockOutNum(20);
        info2.setSpec("AABBCCDD2");
        info2.setPackageNum("21/箱");
        info2.setUnit("箱");
        info2.setValidityTime(new Date());
        soInfos.add(info2);

        param.setStockOutInfos(soInfos);
        try {
//            System.out.println(Constant.LINE +JSON.toJSONString(param));
            Notification<Object> notification = stockOutBillService.orderLock(param);
            System.out.println(Constant.LINE + JSON.toJSONString(notification));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLockedStockOfOrder() {
        OrderInfo param = new OrderInfo();
        param.setOrderNo("11703069434703");
        param.setOrderType(Constant.StockOutBillOrderType.GENERAL);
        param.setOrderdate(new Date());
        param.setWareId(1);
        param.setWareName("雷蒙德北京仓库");
        param.setGoodsAmount(5);
        param.setGoodsSum(new BigDecimal(500));

        OrderLogisticsInfo olInfo = new OrderLogisticsInfo();
        olInfo.setOrderNo("11703069434703");
        olInfo.setReceivername("test_receiver");
        olInfo.setDetailedAddress("收货详细地址");
        olInfo.setProvCode(110000);
        olInfo.setProvName("北京市");
//        olInfo.setCityCode(1);
//        olInfo.setCityName();
        olInfo.setReceiveMobile("18810880000");
        olInfo.setReceiveTel("010-12345678");
        param.setOrderLogiInfo(olInfo);
        List<StockOutInfo> soInfos = new ArrayList<>();
        StockOutInfo info1 = new StockOutInfo();
        info1.setGoodsName("测试数据test");
        info1.setGoodsCode("A10000000001");
        info1.setGoodsBarCode("A10000000001");
        info1.setSalesPrice(new BigDecimal(100));
        info1.setStockOutNum(5);
        info1.setSpec("AABBCCDD");
        info1.setPackageNum("11/箱");
        info1.setUnit("箱");
        info1.setValidityTime(new Date());
        soInfos.add(info1);
        param.setStockOutInfos(soInfos);
        try {
            Notification<Object> notification = stockOutBillService.lockedStockOfOrder(param.getOrderNo());
            System.out.println(Constant.LINE + JSON.toJSONString(notification));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAlterStockOutBill() {
        AlterSOBillParam param = new AlterSOBillParam();
        param.setOrderNo("11705126260463");
        param.setOrderType(Constant.StockOutBillOrderType.GENERAL);
        param.setStatus(Constant.AlterSOBillStatusParam.CANTEL);
        try {
            Notification<Object> notification = stockOutBillService.alterStockOutBill(param);
            System.out.println(Constant.LINE + JSON.toJSONString(notification));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}

