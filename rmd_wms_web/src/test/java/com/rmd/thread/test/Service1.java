package com.rmd.thread.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wyf on 2017/7/28.
 */
public class Service1 {
    public ReentrantLock lock=new ReentrantLock();
    public Condition condition=lock.newCondition();
    public void waitMethod(){
        try{
            lock.lock();
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
