package com.rmd.wms.service;

import java.util.List;

import com.rmd.wms.bean.Location;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;

public interface LocationService {

	PageBean<Location> listLocation(Integer page, Integer rows,
			Location location);

	void updateByPrimaryKeySelective(Location location);

	int selectByLocationNo(Location location);

	void insertSelective(Location location);

	ServerStatus insertBatch(List<Location> list);

	Location selectByPrimaryKey(Integer id);

	void updateByType(Location location);

	List<Location> selectByCriteria(Location location);

	int updateByStatus(String ids, Integer status);

}
