package org.rmd.wms.provider.service;

import com.alibaba.fastjson.JSON;
import com.rmd.wms.bean.LocationGoodsBindIn;
import com.rmd.wms.bean.OrderLogisticsInfo;
import com.rmd.wms.bean.PurchaseInInfo;
import com.rmd.wms.bean.vo.InStockInfo;
import com.rmd.wms.bean.vo.ServerInStockParam;
import com.rmd.wms.bean.vo.app.InStockBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.InStockBillService;
import com.rmd.wms.service.MovementService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestInStockBillService extends ICommonServiceTest {

    @Resource
    private InStockBillService inStockBillService;
    @Autowired
    private MovementService movementService;

    @Test
    public void testPutaway() {
        InStockBillInfo info = new InStockBillInfo();
        info.setInStockNo("RK00117222005");
        List<LocationGoodsBindIn> inList = new ArrayList<>();
        LocationGoodsBindIn in1 = new LocationGoodsBindIn();
        in1.setGinfoId(14);
        in1.setGoodsCode("A07090202451");
        in1.setLocationNo("B01-02-03");
        in1.setGroundingNum(20);
        inList.add(in1);
        info.setBindIns(inList);
        try {
            System.out.println("----------参数-----------" + JSON.toJSONString(info));
            ServerStatus serverStatus = inStockBillService.putaway(info);
            System.out.println("--------结果-------------" + JSON.toJSONString(serverStatus));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testServerInStock() {
        ServerInStockParam param = new ServerInStockParam();
        param.setServerNo("31703253456880");
        param.setOrderdate(new Date());
        param.setAgree(Constant.TYPE_STATUS_YES);
        param.setWareId(1);
        param.setWareName("测试");
        param.setUserId(1);
        param.setUserName("liu");
        param.setRemark("同意");
        List<InStockInfo> params = new ArrayList<>();
        // 入库详情
        InStockInfo in1 = new InStockInfo();
        in1.setInGoodsAmount(100);
        in1.setInGoodsSum(new BigDecimal(500));
        in1.setStatus(Constant.TYPE_STATUS_YES);

        List<PurchaseInInfo> inInfos = new ArrayList<>();
        PurchaseInInfo inInfo1 = new PurchaseInInfo();
        inInfo1.setInStockNum(100);
        inInfo1.setPurchasePrice(new BigDecimal(5));
        inInfo1.setInStockSum(new BigDecimal(500));
        inInfo1.setGoodsCode("A00000000001");
        inInfo1.setGoodsBarCode("A00000000001");
        inInfo1.setGoodsName("test");
        inInfo1.setSpec("test");
        inInfo1.setPackageNum("test");
        inInfo1.setUnit("test");
        inInfos.add(inInfo1);
        in1.setInInfos(inInfos);
        params.add(in1);

        // 收货地址详情
        OrderLogisticsInfo orderLogiInfo = new OrderLogisticsInfo();
        orderLogiInfo.setOrderNo(param.getServerNo());
        orderLogiInfo.setReceivername("收货人");
        orderLogiInfo.setDetailedAddress("收货地址");
        orderLogiInfo.setProvCode(1);
        orderLogiInfo.setProvName("北京市");
        orderLogiInfo.setReceiveTel("010-88888888");
        orderLogiInfo.setReceiveMobile("18899996666");


        param.setParams(params);
        param.setOrderLogiInfo(orderLogiInfo);

//        ServerInStockParam serverInStockParam = JSON.parseObject("{\"agree\":0,\"remark\":\"1111\",\"serverNo\":\"31703206768698\",\"userId\":1}", ServerInStockParam.class);

        try {
            System.out.println("----------参数-----------" + JSON.toJSONString(param));
            ServerStatus serverStatus = inStockBillService.serverInStock(param);
            System.out.println("--------结果-------------" + JSON.toJSONString(serverStatus));
        } catch (WMSException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testRelockedStock() {
        try {
            ServerStatus serverStatus = movementService.relockedStock(1);
            System.out.println(Constant.LINE + JSON.toJSONString(serverStatus));
        } catch (WMSException e) {
            e.printStackTrace();
        }
    }

}
