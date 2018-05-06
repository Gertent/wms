package com.rmd.thread.test;

/**
 * Created by wyf on 2017/7/25.
 */
public class ThreadA extends Thread {
    private Service service;
    public ThreadA(Service service){
        super();
        this.service=service;
    }
    @Override
    public void run(){
        service.print(new Object());
    }
}
