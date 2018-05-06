package org.rmd.wms.provider.service;

import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.DictionaryService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;

import javax.annotation.Resource;

public class TestDictionaryService extends ICommonServiceTest {

    @Resource
    private DictionaryService dictionaryService;

    @Test
    public void testGenerateBillNo() {
        try {
            System.out.println(Constant.LINE + dictionaryService.generateBillNo("FH", "001"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
