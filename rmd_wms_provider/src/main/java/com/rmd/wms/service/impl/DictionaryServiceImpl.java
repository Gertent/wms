package com.rmd.wms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.rmd.wms.bean.Dictionary;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.DictionaryMapper;
import com.rmd.wms.service.DictionaryService;
import org.springframework.stereotype.Service;
import yao.util.date.DateUtil;

/**
 * Created by liu on 2017/2/22.
 */
@Service("dictionaryService")
public class DictionaryServiceImpl extends BaseService implements DictionaryService {

    @Override
    public String generateBillNo(String billFlag, String wareCode) {
        if (StringUtils.isBlank(billFlag) || StringUtils.isBlank(wareCode)) return "";
        // 单号规则：操作名称+库房编号+下单时间+三位数 例如：CG001170113001
        String billNo = billFlag.trim() + wareCode.trim() + (DateUtil.year() + "").substring(2, 4);
        // 月和日如果是一位前补0
        String monthStr = DateUtil.month() + "";
        if (monthStr.length() == 1)
            billNo = billNo + "0" + monthStr;
        else
            billNo = billNo + monthStr;
        String dayStr = DateUtil.day() + "";
        if (dayStr.length() == 1)
            billNo = billNo + "0" + dayStr;
        else
            billNo = billNo + dayStr;
        Dictionary dic = this.getMapper(DictionaryMapper.class).selectByCode(billFlag + Constant.BILL_NO_VAL);
        if (dic == null) return "";
        // 如果随后三位数到999则从1开始
        if (dic.getSort() >= 999) dic.setSort(1);
        else dic.setSort(dic.getSort() + 1);
        this.getMapper(DictionaryMapper.class).updateByPrimaryKeySelective(dic);
        // 判断最后三位数字是否满足，否则补0
        String sortStr = dic.getSort() + "";
        if (sortStr.trim().length() == 1)
            billNo = billNo + "00" + dic.getSort();
        else if (sortStr.trim().length() == 2)
            billNo = billNo + "0" + dic.getSort();
        else
            billNo = billNo + dic.getSort();
        return billNo;
    }
}
