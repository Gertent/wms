package org.rmd.wms.provider.service;

import com.alibaba.fastjson.JSON;
import com.rmd.bms.bean.MessageWraper;
import com.rmd.bms.mq.service.MessageConsumerService;
import com.rmd.wms.bean.LogisticsFreightTemplate;
import com.rmd.wms.bean.PurchaseInInfo;
import com.rmd.wms.bean.vo.LogisticsFreightTemplateVo;
import com.rmd.wms.bean.vo.app.PurchaseBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.oss.OssService;
import com.rmd.wms.oss.bean.OssFile;
import com.rmd.wms.service.PurchaseBillService;
import com.rmd.wms.util.BarcodeUtil;
import com.rmd.wms.util.WmsUtil;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestPurchaseBillService extends ICommonServiceTest {

    @Resource
    private PurchaseBillService purchaseBillService;

    @Qualifier("purchaseBillService")
    @Resource
    private MessageConsumerService messageConsumerService;


    @Test
    public void testPurInStock() {
//        PurchaseBillInfo pinfo = new PurchaseBillInfo();
//        pinfo.setPurchaseNo("CG1601090001");
//        List<PurchaseInInfo> infos = new ArrayList<>();
//        PurchaseInInfo info1 = new PurchaseInInfo();
//        info1.setPurchaseNo(pinfo.getPurchaseNo());
//        info1.setInStockNum(10);
//        info1.setGoodsCode("A07090202450");
//        infos.add(info1);
//        PurchaseInInfo info2 = new PurchaseInInfo();
//        info2.setPurchaseNo(pinfo.getPurchaseNo());
//        info2.setInStockNum(20);
//        info2.setGoodsCode("A07090202451");
//        infos.add(info2);
//        pinfo.setInfos(infos);

        String s = "{\"infos\":[{\"goodsCode\":\"A07090202450\",\"inStockNum\":1}],\"ouserId\":2,\"ouserName\":\"001\",\"purchaseNo\":\"CG160109000001\"}";
        PurchaseBillInfo purchaseBillInfo = JSON.parseObject(s, PurchaseBillInfo.class);

        try {
            ServerStatus serverStatus = purchaseBillService.purInStock(purchaseBillInfo);
//            System.out.println("---------------------" + JSON.toJSONString(pinfo));
            System.out.println(serverStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOSS() {
        try {
//            String path=getClass().getResource("/").getFile().toString();
//            if(path.indexOf("//")==0){
//                path=path.substring(1,path.length());
//            }
//            path+= UUID.randomUUID().toString()+".png";
//            System.out.println("*******oss*********"+path);
//            String msg = "CG001170228013";
//            //String path = "C:\\Target.PNG";
//            BarcodeUtil.generateFile(msg, path);
//            File f=new File(path);
////            File f=new File("F:\\project_code_idea\\rmd_wms_parent\\rmd_wms_web\\target\\rmd_wms_web\\CG001170228013.png");
//            OssService oss=new OssService();
//            OssFile of=oss.Ossupload(f);
//            System.out.println("*******oss*********"+of.getUrl());
//            f.deleteOnExit();
            for(int i=0;i<100;i++) {
                String str = "{\"createDate\":1494849383000,\"department\":\"abc1234\",\"financeCode\":\"fas12312312\",\"goodsAmount\":1000,\"goodsSum\":44000.00,\"ouserId\":1,\"ouserName\":\"yuyang\",\"purchaseInfoVOs\":[{\"goodsBarCode\":\"测试首页\",\"goodsCode\":\"A00000000001\",\"goodsName\":\"测试首页\",\"goodspriceSum\":44000.00,\"packageNum\":\"22\",\"purchaseNo\":\""+i+"AACG1011705150149\",\"purchaseNum\":1000,\"purchasePrice\":44.00,\"spec\":\"test首页\",\"unit\":\"个\",\"wareId\":1,\"wareName\":\"雷蒙德北京仓库01\"}],\"purchaseNo\":\""+i+"AACG1011705150149\",\"supplierCode\":\"CN00045\",\"supplierName\":\"测试供应商\",\"wareId\":1,\"wareName\":\"雷蒙德北京仓库01\"}";
                MessageWraper m = new MessageWraper();
                m.setAppName("aa");
                m.setBody(str);
                m.setTags("Purchase");
                m.setTopic("Scm2Wm");
                m.setBusinessName("orderService");
                messageConsumerService.messageExecute(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testvo() {
        LogisticsFreightTemplateVo vo=new LogisticsFreightTemplateVo();
        vo.setLogisticsId(111);
        vo.setCreateTime(new Date());
        LogisticsFreightTemplate l=new LogisticsFreightTemplate();
        WmsUtil.copyPropertiesIgnoreNull(vo, l);
        System.out.println(l.getId());
    }

}
