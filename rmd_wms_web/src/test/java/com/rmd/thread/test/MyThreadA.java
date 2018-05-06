package com.rmd.thread.test;

/**
 * Created by wyf on 2017/7/27.
 */
public class MyThreadA extends Thread{
    private MyService1 myService1;
    public MyThreadA(MyService1 myService1){
        super();
        this.myService1=myService1;
    }
    @Override
    public void run(){
        myService1.waitMethod();
    }

}
