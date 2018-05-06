package com.rmd.test;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.rmd.wms.web.enums.SystemMenuFlag;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by win7 on 2017/5/3.
 */
public class test {
    public static void  main(String[] args){
        Map map=new HashMap<>();
        Optional<Map> o = Optional.of(map);
        System.out.println(o.isPresent());
        int i=-1;
//        Preconditions.checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);
        Objects.equal("a", "a");
        System.out.println(Thread.currentThread().getName());

    }

    public static void test(){
//        StringBuffer sb=new StringBuffer();
//        char[] buf=new char[1024];
//        try {
//            FileReader f=new FileReader("d://tmp//aa.txt");
//            while(f.read(buf)>0){
//                sb.append(buf);
//            }
//            System.out.println(sb.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    for(int i=0;i<50;i++) {
       System.out.println(UUID.randomUUID().toString());
    }
    }
}
