package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.commons.servutils.Notification;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.web.PageBean;

/**
 * Created by liu on 2017/2/27.
 */
public interface WarehouseService {

    int deleteByPrimaryKey(Integer id);

    int insert(Warehouse record);
    
    int insert(Warehouse record,String areaCode,String areaName);

    int insertSelective(Warehouse record);

    Warehouse selectByPrimaryKey(Integer id);

    Notification<Warehouse> selectByWareId(Integer id);

    Notification<Warehouse> selectByWareCode(String wareCode);

    Notification<List<Warehouse>> selectAllWarehouse();

    int updateByPrimaryKeySelective(Warehouse record);
    
    int updateByPrimaryKeySelective(Warehouse record,String areaCode,String areaName);

    int updateByPrimaryKey(Warehouse record);

    List<Warehouse> selectByCriteria(Warehouse record);

    List<Warehouse> selectByParam(Map<String, Object> paraMap);

    List<Warehouse> selectByUserId(Integer userId);

	PageBean<Warehouse> listWarehouse(Integer page, Integer rows,
			Warehouse record);
	/**查询名称是否重复*/
	int selectByName(Warehouse warehouse);
	/**查询编号是否重复*/
	int selectByCode(Warehouse warehouse);

	int updateByStatus(String ids, Integer status);


}
