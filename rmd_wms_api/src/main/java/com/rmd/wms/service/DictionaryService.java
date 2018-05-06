package com.rmd.wms.service;

/**
 * Created by liu on 2017/2/22.
 */
public interface DictionaryService {

    /**
     * 生成单号
     * @param billFlag
     * @param wareCode
     * @return
     */
    String generateBillNo(String billFlag, String wareCode);
}
