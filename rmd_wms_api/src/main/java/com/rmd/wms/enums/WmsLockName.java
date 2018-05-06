package com.rmd.wms.enums;

/**
 * @author : liu
 * @Date : 2017/5/15
 */
public enum WmsLockName {
    PUR_IN_STOCK(1, "PUR_IN_STOCK"),
    GENERATE_BILL_NO(2, "GENERATE_BILL_NO");

    private final String name;
    private final int value;

    WmsLockName(int value, String name) {
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

    public static WmsLockName getWmsLockBusiness(int id) {
        WmsLockName[] oss = WmsLockName.values();
        for (int i = 0; i < oss.length; i++) {
            if (oss[i].compare(id))
                return oss[i];
        }
        return null;
    }
}
