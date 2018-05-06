package com.rmd.wms.service;

import java.util.List;

import com.rmd.wms.bean.WarehouseArea;
import com.rmd.wms.bean.vo.web.PageBean;


public interface WarehouseAreaService {

	PageBean<WarehouseArea> listWarehouseArea(Integer page, Integer rows,
			WarehouseArea warehouseArea);

	int updateByPrimaryKeySelective(WarehouseArea warehouseArea);

	void insertSelective(WarehouseArea warehouseArea);

	WarehouseArea selectByPrimaryKey(Integer id);

	List<WarehouseArea> selectByCriteria(WarehouseArea warehouseArea);

	/**查询库区重复*/
	int selectByName(WarehouseArea warehouseArea);
	/**查询编号重复*/
	int selectByCode(WarehouseArea warehouseArea);

	int updateByStatus(String ids, Integer status, Integer wareId);

}
