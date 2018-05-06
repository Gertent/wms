package com.rmd.wms.dao;

import com.rmd.wms.bean.User;

import java.util.List;

@Deprecated
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectBySelective(User record);
}