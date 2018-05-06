package com.rmd.design.decorator;

/**
 * Created by win7 on 2017/5/10.
 */
public class Source implements Sourceable {
    @Override
    public void method() {
        System.out.println("the original method!");
    }
}
