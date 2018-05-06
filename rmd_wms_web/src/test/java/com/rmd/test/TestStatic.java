package com.rmd.test;

/**
 * Created by win7 on 2017/5/10.
 */
public class TestStatic {
    static {
        System.out.println("基类 Static 静态语句块");
    }

    public TestStatic() {
        System.out.println("基类  默认无参构造器");
    }

    {
        System.out.println("基类 非静态语句块");
    }

    public static void Iint() {
        System.out.println("基类 Static Iint");
    }

}
