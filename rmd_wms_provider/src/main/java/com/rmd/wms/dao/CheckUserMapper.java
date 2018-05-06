package com.rmd.wms.dao;

import com.rmd.wms.bean.CheckUser;

import java.util.List;
import java.util.Map;

public interface CheckUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CheckUser record);

    int insertSelective(CheckUser record);

    CheckUser selectByPrimaryKey(Integer id);

    List<CheckUser> selectByCheckNo(String checkNo);

    int updateByPrimaryKeySelective(CheckUser record);

    int updateByPrimaryKey(CheckUser record);

    List<CheckUser> selectByCriteria(Map<String,Object> map);
}