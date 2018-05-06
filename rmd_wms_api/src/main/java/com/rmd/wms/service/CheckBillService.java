package com.rmd.wms.service;

import com.rmd.wms.bean.CheckBill;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.CheckInfos;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.bean.vo.web.SearchCheckBillVo;
import com.rmd.wms.exception.WMSException;

import java.util.List;
import java.util.Map;

/**
 * 盘点单服务
 * @author : liu
 * @Date : 2017/4/12
 */
public interface CheckBillService {

    /**
     * 根据盘点单号查询盘点单
     * @param checkNo
     * @return
     */
    CheckBill selectByCheckNo(String checkNo);

    /**
     * 领取盘点任务
     * @param param
     * @return
     * @throws WMSException
     */
    CheckInfos getCheckBillTask(CheckInfos param) throws WMSException;

    /**
     * 盘点商品
     * @param param
     * @return
     * @throws WMSException
     */
    ServerStatus doCheck(CheckInfos param) throws WMSException;

    /**
     * 生成盘点单
     * @param userId
     * @param userName
     * @param ware
     * @param idList
     * @param type
     * @return
     * @throws WMSException
     */
    ServerStatus createCheckBill(Integer userId, String userName, Warehouse ware, List<Integer> idList, Integer type) throws WMSException;

    /**
     * 搜索盘点单
     * @param param
     * @return
     * @throws WMSException
     */
    PageBean<CheckBill> searchCheckBills(Integer page, Integer rows, SearchCheckBillVo param) throws WMSException;

    /**
     * 搜索盘点单
     * @param mapParam
     * @return
     * @throws WMSException
     */
    PageBean<CheckBill> searchCheckBills(Integer page, Integer rows, Map<String, Object> mapParam) throws WMSException;

    /**
     * 根据条件查询
     * @param map
     * @return
     * */
	List<CheckBill> selectByCriteria(Map<String, Object> map);

	/**
	 * 查询分页
	 * @param page
	 * @param rows
	 * @param map
	 * @return
	 * */
	PageBean<CheckBill> listCheckBill(Integer page, Integer rows,
			Map<String, Object> map);
	
	/**
     * 根据主键查盘点单信息
     * @param id
     * @return
     */
    CheckBill selectByPrimaryKey(Integer id);
}
