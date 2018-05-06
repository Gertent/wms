package com.rmd.thread.test;

/**
 * Created by wyf on 2017/7/24.
 */
public class threadOne extends Thread {
    @Override
    public void run(){
        long beginTime=System.currentTimeMillis();
        int count=0;
        for (int i = 0; i < 50000000; i++) {
            Thread.yield();
            count=count+(i+1);
        }
        long endTime=System.currentTimeMillis();
        System.out.println("用时："+(endTime-beginTime)+"毫秒");
    }

    public static void main(String[] args){
        threadOne t=new threadOne();
        t.start();
    }
}
