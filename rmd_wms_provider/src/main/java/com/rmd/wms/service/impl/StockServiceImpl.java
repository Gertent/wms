package com.rmd.wms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.NotificationBuilder;
import com.rmd.commons.servutils.Notifications;
import com.rmd.wms.bean.Stock;
import com.rmd.wms.bean.vo.StockInfoVo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.StockMapper;
import com.rmd.wms.service.StockService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by liu on 2017/3/3.
 */
@Service("stockService")
public class StockServiceImpl extends BaseService implements StockService {

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getMapper(StockMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Stock record) {
        return this.getMapper(StockMapper.class).insert(record);
    }

    @Override
    public int insertSelective(Stock record) {
        return this.getMapper(StockMapper.class).insertSelective(record);
    }

    @Override
    public Stock selectByPrimaryKey(Integer id) {
        return this.getMapper(StockMapper.class).selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Stock record) {
        return this.getMapper(StockMapper.class).updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Stock record) {
        return this.getMapper(StockMapper.class).updateByPrimaryKey(record);
    }

    @Override
    public Notification<Integer> getSkuStockNum(String goodsCode, Integer wareId) {
        Notification<Integer> noti = new Notification<>();
        if (StringUtils.isBlank(goodsCode) || wareId == null || wareId <= 0) {
            noti = NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
            noti.setNotifInfo("参数错误");
            return noti;
        }
        Stock stock = new Stock(goodsCode, wareId, Constant.TYPE_STATUS_YES);
        stock = this.getMapper(StockMapper.class).selectUnionPrimaryKey(stock);
        if (stock == null || stock.getValidityNum() == null || stock.getValidityNum() <= 0) {
            noti.setNotifCode(101);
            noti.setNotifInfo("仓库数据错误");
            return noti;
        }
        noti = NotificationBuilder.buildOne(Notifications.OK);
        noti.setResponseData(stock.getValidityNum());
        return noti;
    }

    @Override
    public Notification<List<StockInfoVo>> getWareStockByCriteria(StockInfoVo param) {
        Notification<List<StockInfoVo>> noti = NotificationBuilder.buildOne(Notifications.OK);
        if (param == null) {
            param = new StockInfoVo();
        }
        param.setSaleType(Constant.TYPE_STATUS_YES);
        List<StockInfoVo> stockInfoVos = this.getMapper(StockMapper.class).selectWareStockByCriteria(param);
        noti.setResponseData(stockInfoVos);
        return noti;
    }

	@Override
	public PageBean<StockInfoVo> getStockInfoVoByParm(int page, int rows,
			Map<String, Object> param) {
		 PageHelper.startPage(page, rows);
		 PageBean<StockInfoVo> pageBean=new PageBean<StockInfoVo>(this.getMapper(StockMapper.class).selectWareStockByParmMap(param));
		 return pageBean;
	}
}
