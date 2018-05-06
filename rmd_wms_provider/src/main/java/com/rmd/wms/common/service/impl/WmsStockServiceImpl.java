package com.rmd.wms.common.service.impl;

import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.Stock;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.WmsStockService;
import com.rmd.wms.dao.LocationGoodsBindMapper;
import com.rmd.wms.dao.StockMapper;
import com.rmd.wms.exception.WMSException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 修改库存实现类
 *
 * @author : liu
 * Date : 2017/3/27
 */
@Service("wmsStockService")
public class WmsStockServiceImpl extends BaseService implements WmsStockService {

    @Override
    public void alterLocaStock(String locationNo, String goodsCode, Integer wareId, Integer areaId, Integer saleType, Integer alterRealNum, Integer alterValidNum) throws WMSException {
        if (StringUtils.isBlank(locationNo) || StringUtils.isBlank(goodsCode) || wareId == null || areaId == null || saleType == null || (alterRealNum == null && alterValidNum == null)) {
            throw new WMSException("修改货位库存，参数错误");
        }
        LocationGoodsBind bind = new LocationGoodsBind(locationNo, goodsCode, wareId, areaId, saleType);
        bind = this.getMapper(LocationGoodsBindMapper.class).selectUnionPrimaryKey(bind);
        if (bind == null) {
            throw new WMSException("货位库存不存在");
        }
        bind.setLocationNum(bind.getLocationNum() == null ? null : bind.getLocationNum() + (alterRealNum == null ? 0 : alterRealNum));
        bind.setValidityNum(bind.getValidityNum() == null ? null : bind.getValidityNum() + (alterValidNum == null ? 0 : alterValidNum));
        this.getMapper(LocationGoodsBindMapper.class).updateByPrimaryKeySelective(bind);
    }

    @Override
    public void alterWareStock(String goodsCode, Integer wareId, Integer saleType, Integer alterRealNum, Integer alterValidNum) throws WMSException {
        if (StringUtils.isBlank(goodsCode) || wareId == null || saleType == null || (alterRealNum == null && alterValidNum == null)) {
            throw new WMSException("修改仓库存库，参数错误");
        }
        Stock stock = new Stock(goodsCode, wareId, saleType);
        stock = this.getMapper(StockMapper.class).selectUnionPrimaryKey(stock);
        if (stock == null) {
            throw new WMSException("仓库库存不存在");
        }
        stock.setStockNum(stock.getStockNum() == null ? null : stock.getStockNum() + (alterRealNum == null ? 0 : alterRealNum));
        stock.setValidityNum(stock.getValidityNum() == null ? null : stock.getValidityNum() + (alterValidNum == null ? 0 : alterValidNum));
        this.getMapper(StockMapper.class).updateByPrimaryKeySelective(stock);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void alterWmsStock(String locationNo, String goodsCode, Integer wareId, Integer areaId, Integer saleType, Integer alterRealNum, Integer alterValidNum) throws WMSException {
        this.alterLocaStock(locationNo, goodsCode, wareId, areaId, saleType, alterRealNum, alterValidNum);
        this.alterWareStock(goodsCode, wareId, saleType, alterRealNum, alterValidNum);
    }
}
