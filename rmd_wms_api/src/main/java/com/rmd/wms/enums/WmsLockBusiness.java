package com.rmd.wms.enums;

/**
 * wms业务锁
 *
 * @author : liu
 * @Date : 2017/5/15
 */
public enum WmsLockBusiness {

    PURCHASE_BILL_SERVICE(1, "PURCHASE_BILL_SERVICE"),
    DICTIONARY_SERVICE(2, "DICTIONARY_SERVICE");

    private final String name;
    private final int value;

    WmsLockBusiness(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public boolean compare(int i) {
        return value == i;
    }

    public static WmsLockBusiness getWmsLockBusiness(int id) {
        WmsLockBusiness[] oss = WmsLockBusiness.values();
        for (int i = 0; i < oss.length; i++) {
            if (oss[i].compare(id))
                return oss[i];
        }
        return null;
    }
}
