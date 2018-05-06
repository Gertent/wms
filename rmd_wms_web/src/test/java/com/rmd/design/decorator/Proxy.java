package com.rmd.design.decorator;

/**
 * Created by win7 on 2017/5/10.
 */
public class Proxy implements Sourceable {

    private Source source;

    public Proxy(){
        super();
        this.source=new Source();
    }

    @Override
    public void method() {
        before();
        source.method();
        atfer();
    }

    private void atfer() {
        System.out.println("after proxy!");
    }
    private void before() {
        System.out.println("before proxy!");
    }
}
