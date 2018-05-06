package com.rmd.wms.dao;

import java.util.List;

import com.rmd.wms.bean.Location;

public interface LocationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Location record);

    int insertBatch(List<Location> list);

    int insertSelective(Location record);

    Location selectByPrimaryKey(Integer id);

    Location selectByLocaNoAndWareId(Location record);

    int updateByPrimaryKeySelective(Location record);

    int updateByPrimaryKey(Location record);

	List<Location> selectByCriteria(Location record);

	int selectByLocationNo(Location location);
	
	int updateByType(Location record);

    int updateStatusByCriteria(Location record);
}