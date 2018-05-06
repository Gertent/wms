package org.rmd.wms.provider.service;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.JSON;
import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.LogisticsFreightTemplate;
import com.rmd.wms.bean.vo.LogisticsFreightTemplateVo;
import com.rmd.wms.bean.vo.StockInfoVo;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.StockService;
import com.rmd.wms.util.WmsUtil;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;
import java.io.*;
import java.lang.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Date;
import java.util.List;

public class TestAll  {


    @Test
    public void testvo() {
        LogisticsFreightTemplateVo vo=new LogisticsFreightTemplateVo();
        vo.setLogisticsId(111);
        vo.setCreateTime(new Date());
        LogisticsFreightTemplate l=new LogisticsFreightTemplate();
        WmsUtil.copyPropertiesIgnoreNull(vo, l);
        System.out.println(l.getId());
    }

    @Test
    public void testio() throws IOException {
        String file="d:\\a.txt";
        String charset="UTF-8";
        //写字符转换成字节流
        FileOutputStream outputStream=new FileOutputStream(file);
        OutputStreamWriter writer=new OutputStreamWriter(outputStream,charset);
        try{
            writer.write("这是要保存的中文！");
        }finally {
            writer.close();
        }
        //读字节转换成字符
//        FileInputStream inputStream=new FileInputStream(file);
//        InputStreamReader reader=new InputStreamReader(inputStream,charset);
//        StringBuffer buffer=new StringBuffer();
//        Char[] buf=new Char[64];
//        int count=0;
//        try{
//            while ((count=reader.read(buf))!=-1) {
//                buffer.append(buffer,0,count);
//            }
//        }finally {
//            reader.close();
//        }

        String s="中文字符串";
        byte[] b=s.getBytes();
        Charset charset1=Charset.forName("utf-8");
    }


}
