package com.rmd.wms.service;

import com.rmd.wms.bean.CheckUser;

import java.util.List;
import java.util.Map;

/**
 * 盘点人服务
 * @author : liu
 * @Date : 2017/4/18
 */
public interface CheckUserService {

    List<CheckUser> selectByCheckNo(String checkNo);

    List<CheckUser> selectByCriteria(Map<String,Object> map);
}
