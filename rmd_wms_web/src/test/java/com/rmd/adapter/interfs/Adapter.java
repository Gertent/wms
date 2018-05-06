package com.rmd.adapter.interfs;

/**
 * Created by win7 on 2017/5/10.
 */
public class Adapter extends Source implements Targetable {
    @Override
    public void method2() {
        System.out.println("this is the targetable method!");
    }
}
