package com.rmd.wms.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.rmd.wms.bean.CheckInfo;
import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.LocationGoodsBindOut;
import com.rmd.wms.bean.Stock;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.WmsCheckService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.LocationGoodsBindMapper;
import com.rmd.wms.dao.LocationGoodsBindOutMapper;
import com.rmd.wms.dao.StockMapper;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.util.WmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import yao.util.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 仓库盘点公共逻辑实现
 * @author : liu
 * @Date : 2017/4/26
 */
@Service("wmsCheckService")
public class WmsCheckServiceImpl extends BaseService implements WmsCheckService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void checkLockedStock(List<CheckInfo> lockedList) throws WMSException {
        logger.info(Constant.LINE + "待锁定的盘点记录：" + JSON.toJSONString(lockedList));
        if (lockedList == null || lockedList.isEmpty()) {
            return;
        }
        Set<Integer> bindIdSet = (Set<Integer>)CollectionUtil.toFieldSet(lockedList, "bindId");
        List<LocationGoodsBind> binds = this.getMapper(LocationGoodsBindMapper.class).selectByIds(new ArrayList<>(bindIdSet));
        if (binds == null || binds.isEmpty()) {
            throw new WMSException("货位库存数据错误");
        }
        Date now = new Date();
        List<LocationGoodsBindOut> newOuts = new ArrayList<>();
        List<Stock> updateStocks = new ArrayList<>();
        // 锁库的记录集合
        for (CheckInfo infoTemp : lockedList) {
            LocationGoodsBindOut newOut = new LocationGoodsBindOut();
            // 复制：locationNo，goodsCode，goodsBarCode，validityTime，wareId，wareName
            WmsUtil.copyPropertiesIgnoreNull(infoTemp, newOut);
            newOut.setId(null);
            newOut.setGinfoId(infoTemp.getId());
            newOut.setOrderNo(infoTemp.getCheckNo());
            Integer checkDuff = infoTemp.getSecondCheckValidDiff() != null ? infoTemp.getSecondCheckValidDiff() : infoTemp.getFirstCheckValidDiff();
            newOut.setLockedNum(-checkDuff);// 盘亏数为负数，这里取锁的正数
            newOut.setCreateTime(now);
            for (LocationGoodsBind bindTemp : binds) {
                if (bindTemp.getId().equals(infoTemp.getBindId())) {
                    // 设置货位id和库区内容
                    newOut.setLocationId(bindTemp.getLocationId());
                    newOut.setAreaId(bindTemp.getAreaId());
                    newOut.setAreaName(bindTemp.getAreaName());
                    // 仓库可用库存减少锁定数量
                    Stock stock = new Stock(newOut.getGoodsCode(), newOut.getWareId(), Constant.TYPE_STATUS_YES);
                    stock = this.getMapper(StockMapper.class).selectUnionPrimaryKey(stock);
                    if (stock == null || stock.getValidityNum() == null) {
                        logger.info(Constant.LINE + "仓库库存数据错误");
                        throw new WMSException("仓库库存数据错误");
                    }
                    // 当可卖数量大于盘亏数量，直接减去盘点数量即可，否则仓库库存减去货位可卖数量，货位库存可卖为0
                    if (bindTemp.getValidityNum() > newOut.getLockedNum()) {
                        stock.setValidityNum(stock.getValidityNum() - newOut.getLockedNum());
                        bindTemp.setValidityNum(bindTemp.getValidityNum() - newOut.getLockedNum());
                    } else {
                        stock.setValidityNum(stock.getValidityNum() - bindTemp.getValidityNum());
                        bindTemp.setValidityNum(0);
                    }
                    updateStocks.add(stock);
                }
            }
            newOuts.add(newOut);
        }
        // 批量更新货位库存
        this.getMapper(LocationGoodsBindMapper.class).updateBatchByPrimaryKey(binds);
        // 批量修改库存信息
        this.getMapper(StockMapper.class).updateBatchByPrimaryKey(updateStocks);
        // 批量插入锁库记录
        this.getMapper(LocationGoodsBindOutMapper.class).insertBatch(newOuts);
    }
}
