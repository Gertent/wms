package com.rmd.adapter.interfs;

/**
 * Created by win7 on 2017/5/10.
 */
public class WrapperText {

    public static void main(String[] args) {
        Sourceable sourceable=new SourceSub1();
        sourceable.method1();

        Targetable targetable=new Adapter();
        targetable.method2();
    }

}
