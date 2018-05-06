package com.rmd.thread.test;

/**
 * Created by wyf on 2017/7/12.
 */
public class MyThread extends Thread{

    private MyService myService;
    public MyThread(MyService myService){
        super();
        this.myService=myService;
    }
    @Override
    public void run(){
       myService.testMethod();
    }
}
