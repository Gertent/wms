package com.rmd.thread.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wyf on 2017/7/28.
 */
public class Run1 {
    private static Timer timer=new Timer();
    static public class MyTask extends TimerTask{

        @Override
        public void run() {
            System.out.println("运行了，时间为:"+new Date());
        }
    }
    public static void main(String[] args) throws ParseException {
        MyTask task=new MyTask();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-HH-dd HH:mm:ss");
        Date dateDef=sdf.parse("2017-07-28 09:49:40");
        System.out.println("当前时间为:"+new Date().toLocaleString());
        timer.schedule(task,dateDef,3000);
    }
}
