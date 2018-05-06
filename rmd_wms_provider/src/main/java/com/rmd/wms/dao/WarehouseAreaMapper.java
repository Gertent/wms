package com.rmd.wms.dao;

import java.util.List;

import com.rmd.wms.bean.WarehouseArea;

public interface WarehouseAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarehouseArea record);

    int insertSelective(WarehouseArea record);

    WarehouseArea selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarehouseArea record);

    int updateByPrimaryKey(WarehouseArea record);

	List<WarehouseArea> selectByCriteria(WarehouseArea record);

	int selectByName(WarehouseArea warehouseArea);

	int selectByCode(WarehouseArea warehouseArea);
}