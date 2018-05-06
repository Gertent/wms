package com.rmd.thread.test;

/**
 * Created by wyf on 2017/7/25.
 */
public class Service {

    public void testMethod(Object object){
        try {
            synchronized (object){
                System.out.println("begin wait()");
                object.wait();
                System.out.println("end wait()");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    synchronized public static void printA(){
        try {
            System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printA");
            Thread.sleep(3000);
            System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开PrintA");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    synchronized public static void printB(){
        try {
            System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printB");
            Thread.sleep(3000);
            System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printB");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    synchronized public static void printC(){
        try {
            System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printC");
            Thread.sleep(3000);
            System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printC");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void print(Object object){
        synchronized (object){
            while (true){
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
