package com.rmd.wms.common.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.LocationGoodsBindOut;
import com.rmd.wms.bean.Stock;
import com.rmd.wms.bean.StockOutInfo;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.StockOutService;
import com.rmd.wms.common.service.WmsStockService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.LocationGoodsBindMapper;
import com.rmd.wms.dao.LocationGoodsBindOutMapper;
import com.rmd.wms.dao.StockMapper;
import com.rmd.wms.dao.StockOutInfoMapper;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.util.WmsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liu on 2017/3/16.
 */
@Service("stockOutService")
public class StockOutServiceImpl extends BaseService implements StockOutService {

    private Logger logger = Logger.getLogger(StockOutServiceImpl.class);

    @Autowired
    private WmsStockService wmsStockService;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean lockedOneSku(String orderNo, Integer ginfoId, String goodsCode, Integer lockedNum, Integer wareId) throws WMSException {
        if (StringUtils.isBlank(orderNo) || ginfoId == null || StringUtils.isBlank(goodsCode) || lockedNum == null || wareId == null ) {
            throw new WMSException("参数错误");
        }
        Date now = new Date();
        boolean oosFlag = false; // 是否是缺货订单
        int waitLockedNum = lockedNum;// 等待处理的数据
        List<LocationGoodsBindOut> newOuts = new ArrayList<>();
        // 查询商品的所有货位库存信息
        LocationGoodsBind bindParam = new LocationGoodsBind(goodsCode, wareId, Constant.TYPE_STATUS_YES);
        List<LocationGoodsBind> binds = this.getMapper(LocationGoodsBindMapper.class).selectByCriteria(bindParam);
        // 货位不存在则记录缺货数据
        if (binds == null || binds.isEmpty()) {
            LocationGoodsBindOut newOut = new LocationGoodsBindOut();
            newOut.setGoodsCode(goodsCode);
            newOut.setWareId(wareId);
            newOut.setLocationNo(Constant.OOS_FLAG);
            newOut.setLockedNum(lockedNum);
            newOut.setOrderNo(orderNo);
            newOut.setGinfoId(ginfoId);
            newOut.setCreateTime(now);
            newOuts.add(newOut);
            // 缺货需要修改异常订单
            oosFlag = true;
        } else {
            // 货位存在进行锁库
            List<LocationGoodsBind> updateBinds = new ArrayList<>();
            List<Stock> updateStocks = new ArrayList<>();
            for (LocationGoodsBind bindTemp : binds) {
                if (bindTemp.getValidityNum() <= 0) {
                    continue;
                }
                // 判断货位库存表中的每条数据是否满足订单新锁定数量
                LocationGoodsBindOut newOut = new LocationGoodsBindOut();
                WmsUtil.copyPropertiesIgnoreNull(bindTemp, newOut);
                newOut.setId(null);// 去除货位表的id
                newOut.setOrderNo(orderNo);
                newOut.setGinfoId(ginfoId);
                newOut.setCreateTime(now);
                if (bindTemp.getValidityNum() >= waitLockedNum) {
                    newOut.setLockedNum(waitLockedNum);
                    waitLockedNum = 0;// 库存够去锁定
                } else {
                    newOut.setLockedNum(bindTemp.getValidityNum());
                    waitLockedNum -= bindTemp.getValidityNum();
                }
                newOuts.add(newOut);
                // 把锁掉的库存更新可用库存
                bindTemp.setValidityNum(bindTemp.getValidityNum() - newOut.getLockedNum()); // 货位可用库存减去锁定数量
                // 更新货位库存
                updateBinds.add(bindTemp);
                // 判断仓库库存
                Stock stock = new Stock(newOut.getGoodsCode(), newOut.getWareId(), Constant.TYPE_STATUS_YES);
                stock = this.getMapper(StockMapper.class).selectUnionPrimaryKey(stock);
                // 仓库不存在库存信息，则跳过进行负卖
                if (stock == null) {
                    throw new WMSException(Constant.LINE + "仓库库存和货位库存不一致");
                } else {
                    // 仓库可用库存减少锁定数量
                    stock.setValidityNum(stock.getValidityNum() - newOut.getLockedNum());
                    // 修改库存数量
                    updateStocks.add(stock);
                }
                // 如果待锁定数量为0了，跳出循环
                if (waitLockedNum == 0) {
                    break;
                }
            }
            // 批量更新货位库存
            if (updateBinds.size() > 0) {
                this.getMapper(LocationGoodsBindMapper.class).updateBatchByPrimaryKey(updateBinds);
            }
            // 批量修改库存信息
            if (updateStocks.size() > 0) {
                this.getMapper(StockMapper.class).updateBatchByPrimaryKey(updateStocks);
            }

            // 如果等待锁定数量大于0，说明缺货。最后货位号为“-1”
            if (waitLockedNum > 0) {
                LocationGoodsBindOut newOut = new LocationGoodsBindOut();
                WmsUtil.copyPropertiesIgnoreNull(binds.get(0), newOut);
                newOut.setId(null);
                newOut.setLocationId(null);
                newOut.setValidityTime(null);
                newOut.setLocationNo(Constant.OOS_FLAG);
                newOut.setLockedNum(waitLockedNum);
                newOut.setOrderNo(orderNo);
                newOut.setGinfoId(ginfoId);
                newOut.setCreateTime(now);
                newOuts.add(newOut);
                // 缺货需要修改异常订单
                oosFlag = true;
            }
        }
        this.getMapper(LocationGoodsBindOutMapper.class).insertBatch(newOuts);
        return oosFlag;
    }

    @Override
    public void unlockedAllLockSku(String orderNo, Integer wareId) throws WMSException {
        if (StringUtils.isBlank(orderNo) || wareId == null ) {
            throw new WMSException("参数错误");
        }
        // 查询所有订单商品详情
        List<StockOutInfo> stockOutInfos = this.getMapper(StockOutInfoMapper.class).selectByOrderNo(orderNo);
        if (stockOutInfos == null || stockOutInfos.isEmpty()) {
            logger.debug(Constant.LINE + "出库单无商品详情");
            throw new WMSException("出库单无商品详情");
        }
        // 查询所有已经锁掉的库存（包含未锁定的）
        LocationGoodsBindOut outParam = new LocationGoodsBindOut(orderNo, null, wareId);
        List<LocationGoodsBindOut> bindOuts = this.getMapper(LocationGoodsBindOutMapper.class).selectValidOutByOrderNo(outParam);
        if (bindOuts == null || bindOuts.isEmpty()) {
            logger.debug(Constant.LINE + "出库单无锁定商品");
            throw new WMSException("出库单无锁定商品");
        }
        for (StockOutInfo infoTemp : stockOutInfos) {
            int alterValidNumSum = 0;
            for (LocationGoodsBindOut outTemp : bindOuts) {
                // 解(已锁定-已拣)的数据
                if (infoTemp.getGoodsCode().equals(outTemp.getGoodsCode())) {
                    // 解锁所有货位上的有效库存
                    int alterValidNum = outTemp.getLockedNum() - (outTemp.getPickedNum() == null ? 0 : outTemp.getPickedNum());
                    try {
                        wmsStockService.alterLocaStock(outTemp.getLocationNo(), outTemp.getGoodsCode(), outTemp.getWareId(), outTemp.getAreaId(), Constant.TYPE_STATUS_YES, null, alterValidNum);
                    } catch (WMSException e) {
                        logger.error(Constant.LINE + "解锁货位库存异常 ：" + e.getMsg(), e);
                        e.setMsg("解锁货位库存失败");
                        throw e;
                    }
                    // 累计待还原的仓库库存
                    alterValidNumSum += alterValidNum;
                }
            }
            // 还原仓库库存
            try {
                wmsStockService.alterWareStock(infoTemp.getGoodsCode(), infoTemp.getWareId(), Constant.TYPE_STATUS_YES, null, alterValidNumSum);
            } catch (WMSException e) {
                logger.error(Constant.LINE + "解锁仓库库存异常 ：" + e.getMsg(), e);
                e.setMsg("解锁仓库库存失败");
                throw e;
            }
        }
        // 批量修改库存出库记录的锁库数量（归0）
        LocationGoodsBindOut outBatchParam = new LocationGoodsBindOut();
        outBatchParam.setOrderNo(orderNo);
        outBatchParam.setLockedNum(0);
        this.getMapper(LocationGoodsBindOutMapper.class).updateByOrderNo(outBatchParam);
    }
}
