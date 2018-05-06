package com.rmd.wms.dao;

import java.util.List;

import com.rmd.wms.bean.WarehouseUser;
import com.rmd.wms.bean.vo.app.MoveInUsersParam;

public interface WarehouseUserMapper {
    int deleteByPrimaryKey(Integer id);
    
    int deleteByWareId(Integer wareId);

    int insert(WarehouseUser record);

    int insertSelective(WarehouseUser record);

    WarehouseUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarehouseUser record);

    int updateByPrimaryKey(WarehouseUser record);
    
    List<WarehouseUser> selectByWareId(Integer wareId);

    List<WarehouseUser> selectByWareIdAndUserIds(MoveInUsersParam param);

}