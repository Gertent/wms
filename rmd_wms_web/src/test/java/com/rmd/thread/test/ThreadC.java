package com.rmd.thread.test;

/**
 * Created by wyf on 2017/7/25.
 */
public class ThreadC extends Thread {
    private Service service;
    public ThreadC(Service service){
        super();
        this.service=service;
    }
    @Override
    public void run(){
        service.printC();
    }
}
