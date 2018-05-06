package com.rmd.thread.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wyf on 2017/7/27.
 */
public class MyService1 {
    private ReentrantLock lock=new ReentrantLock();
    private Condition condition=lock.newCondition();
    public void waitMethod(){
        try{
            lock.lock();
            System.out.println("A");
            condition.await();
            System.out.println("B");
        }catch (Exception e){
         e.printStackTrace();
        }finally {
            lock.unlock();
            System.out.println("锁释放了");
        }
    }

    public void server1Method(){
        try{
            lock.lock();
            System.out.println("method1 getHoldCount"+lock.getHoldCount());
            server2Method();
        }finally {
            lock.unlock();
        }
    }

    public void server2Method(){
        try{
            lock.lock();
            System.out.println("method2 getHoldCount"+lock.getHoldCount());
        }finally {
            lock.unlock();
        }
    }
}

