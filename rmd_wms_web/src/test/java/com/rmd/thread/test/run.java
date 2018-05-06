package com.rmd.thread.test;

import com.rmd.wms.enums.YesAdNoFlag;
import org.junit.Test;

import static java.lang.Thread.currentThread;

/**
 * Created by wyf on 2017/7/12.
 */
public class run {
    public static void main(String[] args) {

        theadLocalTest test=new theadLocalTest();
        System.out.println(test.get());

//        try {
//            MyThread myThread = new MyThread();
//            myThread.start();
////            myThread.join();
//            System.out.println("我想当mythead执行完后在执行");
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//       Service service=new Service();
//        Service serviceB=new Service();
//
//        ThreadA threadA=new ThreadA(service);
//        threadA.setName("A");
//        threadA.start();
//
//        ThreadB threadB=new ThreadB(service);
//        threadB.setName("B");
//        threadB.start();

//        ThreadC threadc=new ThreadC(service);
//        threadc.setName("C");
//        threadc.start();

    }

    @Test
    public void t1() {
        MyService myService=new MyService();
        MyThread a1=new MyThread(myService);
        MyThread a2=new MyThread(myService);
        MyThread a3=new MyThread(myService);
        MyThread a4=new MyThread(myService);
        MyThread a5=new MyThread(myService);
        a1.start();
        a2.start();
        a3.start();
        a4.start();
        a5.start();
//        System.out.println(YesAdNoFlag.A001.getValue());
//        try {
//            MyThread myThread = new MyThread();
//            myThread.setName("AAAA");
//
//            myThread.start();
//        }catch (Exception e){
//            System.out.println("捕获异常");
//        }


    }
    @Test
    public void t2() {
       final Service1 service1=new Service1();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                service1.waitMethod();
            }
        };

    }
}
