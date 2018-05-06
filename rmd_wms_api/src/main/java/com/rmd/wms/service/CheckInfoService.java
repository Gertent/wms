package com.rmd.wms.service;

import com.rmd.wms.bean.CheckInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.bean.vo.web.SubmitChecksVo;
import com.rmd.wms.exception.WMSException;

import java.util.List;
import java.util.Map;

/**
 * 盘点单详情服务
 *
 * @author : liu
 * @Date : 2017/4/12
 */
public interface CheckInfoService {

    int deleteByPrimaryKey(Integer id);

    int insert(CheckInfo record);

    int insertSelective(CheckInfo record);

    CheckInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CheckInfo record);

    int updateByPrimaryKey(CheckInfo record);

    List<CheckInfo> selectByCriteria(Map<String, Object> map);

    PageBean<CheckInfo> listCheckInfo(Integer page, Integer rows, Map<String, Object> map);

    /**
     * 提交盘点报告
     * @param param
     * @return
     * @throws WMSException
     */
    ServerStatus submitChecksReport(SubmitChecksVo param) throws WMSException;

    /**
     * 修改提报信息：开放/关闭订单
     * @param wareId
     * @param operateStatus 操作标识 0：关闭订单 1：开放订单
     * @return
     * */
    int updateCheckInfoStatus(Integer wareId,String operateStatus);

}
