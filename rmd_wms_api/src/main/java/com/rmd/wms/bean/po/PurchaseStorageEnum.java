package com.rmd.wms.bean.po;

import java.util.HashMap;
import java.util.Map;

public enum PurchaseStorageEnum {

    采购入库(1), 采购退货(2);

    private int code;

    PurchaseStorageEnum(int code) {
        this.code = code;
    }

    private static Map<Integer, String> PurchaseStorageMap = new HashMap<Integer, String>();

    static {
        PurchaseStorageMap.put(PurchaseStorageEnum.采购入库.code, "采购入库");
        PurchaseStorageMap.put(PurchaseStorageEnum.采购退货.code, "采购退货");
    }
    
    public static String getInfo(int code) {
        for (PurchaseStorageEnum enumInfo : PurchaseStorageEnum.values()) {
            if (enumInfo.code == code) {
                return PurchaseStorageMap.get(code);
            }
        }
        return "";
    }
}
