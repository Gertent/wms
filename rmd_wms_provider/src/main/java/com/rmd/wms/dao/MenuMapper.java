package com.rmd.wms.dao;

import java.util.List;

import com.rmd.wms.bean.Menu;

public interface MenuMapper {
    int deleteByPrimaryKey(Integer mid);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer mid);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
    /**
     * 查询菜单列表
     * @param parentId
     * @return
     */
    List<Menu> selectAllMenu(Integer parentId);
}