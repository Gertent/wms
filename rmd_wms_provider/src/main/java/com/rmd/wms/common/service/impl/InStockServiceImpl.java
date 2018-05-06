package com.rmd.wms.common.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.rmd.oms.constant.OrderType;
import com.rmd.wms.bean.*;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.InStockService;
import com.rmd.wms.common.service.WmsStockService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.*;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.DictionaryService;
import com.rmd.wms.util.WmsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by liu on 2017/3/9.
 */
@Service("inStockService")
public class InStockServiceImpl extends BaseService implements InStockService {

    private Logger logger = Logger.getLogger(StockOutServiceImpl.class);

    @Autowired
    private WmsStockService wmsStockService;

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 上架封装上架记录的货位信息和修改仓库和货位库存
     *
     * @param bindIn
     * @param wareId
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void putawayAlterStock(LocationGoodsBindIn bindIn, Integer wareId) throws WMSException {
        // 通过仓库id和货位号查询到当前货位信息
        Location location = new Location(bindIn.getLocationNo(), wareId);
        location = this.getMapper(LocationMapper.class).selectByLocaNoAndWareId(location);
        // 处理空指针
        if (location == null) {
            throw new WMSException("货位不存在");
        }
        // 封装上架记录的货位信息
        bindIn.setLocationId(location.getId());
        bindIn.setWareId(location.getWareId());
        bindIn.setWareName(location.getWareName());
        bindIn.setAreaId(location.getAreaId());
        bindIn.setAreaName(location.getAreaName());
        bindIn.setCreateTime(new Date());
        // 封装必要条件
        LocationGoodsBind bind = new LocationGoodsBind(bindIn.getLocationNo(), bindIn.getGoodsCode(), bindIn.getWareId(), bindIn.getAreaId(), location.getType());
        bind = this.getMapper(LocationGoodsBindMapper.class).selectUnionPrimaryKey(bind);
        // 查看是否该货位信息是否存在,如果库中不存在，则创建，存在，更新库存数据
        if (bind != null && bind.getId() != null) {
            if (bind.getLocationNum() > 0 && bind.getValidityTime().compareTo(bindIn.getValidityTime()) != 0) {
                throw new WMSException("不同效期不可以上架到相同货位上");
            }
            bind.setLocationNum(bind.getLocationNum() + bindIn.getGroundingNum());// 累加货位库存的实际数量
            bind.setValidityNum(bind.getValidityNum() != null && Constant.TYPE_STATUS_YES == bind.getSaleType() ? bind.getValidityNum() + bindIn.getGroundingNum() : null);// 累加货位上的可买数量
            bind.setValidityTime(bindIn.getValidityTime());
            this.getMapper(LocationGoodsBindMapper.class).updateByPrimaryKeySelective(bind);
        } else {
            bind = new LocationGoodsBind();
            WmsUtil.copyPropertiesIgnoreNull(bindIn, bind);
            bind.setValidityNum(Constant.TYPE_STATUS_YES == location.getType() ? bindIn.getGroundingNum() : null);
            bind.setLocationNum(bindIn.getGroundingNum());
            bind.setSaleType(location.getType());
            bind.setCreateTime(new Date());
            this.getMapper(LocationGoodsBindMapper.class).insertSelective(bind);
        }
        // 封装仓库库存必要条件
        Stock stock = new Stock(bind.getGoodsCode(), bind.getWareId(), bind.getSaleType());
        stock = this.getMapper(StockMapper.class).selectUnionPrimaryKey(stock);
        // 查看仓库库存是否存在，存在更新，否则添加
        if (stock != null && stock.getId() != null) {
            stock.setStockNum(stock.getStockNum() + bindIn.getGroundingNum());
            stock.setValidityNum(stock.getValidityNum() != null && Constant.TYPE_STATUS_YES == stock.getSaleType() ? stock.getValidityNum() + bindIn.getGroundingNum() : null);
            stock.setAlterTime(new Date());
            this.getMapper(StockMapper.class).updateByPrimaryKeySelective(stock);
        } else {
            stock = new Stock();
            stock.setWareId(bind.getWareId());
            stock.setGoodsCode(bind.getGoodsCode());
            stock.setSaleType(bind.getSaleType());
            stock.setStockNum(bindIn.getGroundingNum());
            stock.setValidityNum(Constant.TYPE_STATUS_YES == stock.getSaleType() ? bindIn.getGroundingNum() : null);
            stock.setWareName(bind.getWareName());
            Date temp = new Date();
            stock.setAlterTime(temp);
            stock.setCreateTime(temp);
            this.getMapper(StockMapper.class).insertSelective(stock);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String generateInStockBill(InStockBill inBill, List<PurchaseInInfo> inInfosParam) throws WMSException {
        // 生成入库单
        Date now = new Date();
        inBill.setCreateTime(now);
        inBill.setInStockTime(now);
        int goodsAmount = 0; //入库总数量
        BigDecimal inGoodsSum = new BigDecimal(0);// 入库总金额
        PurchaseBill purBill1; // 采购单
//        StockOutBill soutBill; // 出库单
        // 单子类型：1-采购单入库，2-服务单入库，3-取消订单入库
        if (Constant.InStockBillType.PURCHASE_BILL == inBill.getType()) {
            // 采购入库
            purBill1 = this.getMapper(PurchaseBillMapper.class).selectByPurchaseNo(inBill.getPurchaseNo());
            inBill.setWareId(purBill1.getWareId());
            inBill.setWareName(purBill1.getWareName());
            // 新添加供应商名称字段
            inBill.setSupplierName(purBill1.getSupplierName());
            Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(purBill1.getWareId());
            inBill.setInStockNo(dictionaryService.generateBillNo(Constant.BillNoPreFlag.RK, warehouse.getCode()));
            // 查询所有采购单的详情数据
            Map<String, Object> map = new HashMap<>();
            map.put("purchaseNo", inBill.getPurchaseNo());
            List<PurchaseInInfo> purInfos = this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(map);
            // 计算入库总数量和封装入库单详情数据
            for (PurchaseInInfo temp : inInfosParam) {
                //设置入库单详情
                for (PurchaseInInfo info : purInfos) {
                    if (temp.getGoodsCode().equals(info.getGoodsCode())) {
                        WmsUtil.copyPropertiesIgnoreNull(info, temp);
                    }
                }
                temp.setPurchaseNo(null);
                temp.setInStockNo(inBill.getInStockNo());
                temp.setInStockSum(temp.getPurchasePrice().multiply(new BigDecimal(temp.getInStockNum())));//计算单sku上架总金额（采购价乘以入库数量）
                goodsAmount += temp.getInStockNum();
                inGoodsSum = inGoodsSum.add(temp.getInStockSum());
            }
        } else if (Constant.InStockBillType.SERVER_BILL == inBill.getType()) {
            // 服务单入库
            Integer otv = Integer.valueOf(inBill.getServerNo().substring(0, 1));
            OrderType orderType = OrderType.getOrderType(otv);
            inBill.setType(orderType.getValue());
            Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(inBill.getWareId());
            inBill.setInStockNo(dictionaryService.generateBillNo(Constant.BillNoPreFlag.RK, warehouse.getCode()));
            inGoodsSum = null; // 服务单入库的总价格为空
            // 计算入库总数量和封装入库单详情数据
            for (PurchaseInInfo temp : inInfosParam) {
                goodsAmount += temp.getInStockNum();
                //设置入库单详情
                temp.setPurchaseNo(null);
                temp.setInStockSum(null);// 服务单入库的商品价格为空
                temp.setInStockNo(inBill.getInStockNo());
                temp.setValidityTime(Constant.DEFULT_VALIDITY_TIME);
                temp.setWareId(warehouse.getId());
                temp.setWareName(warehouse.getWareName());
            }
        } else if (Constant.InStockBillType.CANCEL_BILL == inBill.getType()) {
            // 取消订单入库（出库已经拣了部分货或已经拣完货）
            String billNo = inBill.getOrderNo();
            if (StringUtils.isBlank(billNo)) {
                throw new WMSException("参数错误");
            }
            StockOutBill soutBill = this.getMapper(StockOutBillMapper.class).selectByOrderNo(billNo);
            inBill.setWareId(soutBill.getWareId());
            inBill.setWareName(soutBill.getWareName());
            Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(soutBill.getWareId());
            inBill.setInStockNo(dictionaryService.generateBillNo(Constant.BillNoPreFlag.RK, warehouse.getCode()));
            inBill.setPurchaseNo(inBill.getOrderNo());// 取消订单入库，采购单默认为订单号
            // 新建入库单记录
            inInfosParam = new ArrayList<>();
            // 查询出库单的所有商品
            List<StockOutInfo> stockOutInfos = this.getMapper(StockOutInfoMapper.class).selectByOrderNo(billNo);
            if (stockOutInfos == null || stockOutInfos.isEmpty()) {
                logger.debug(Constant.LINE + "出库单无商品详情");
                throw new WMSException("出库单无商品详情");
            }
            // 查询所有已经锁掉的库存（包含未锁定的）
            LocationGoodsBindOut outParam = new LocationGoodsBindOut(billNo, null, soutBill.getWareId());
            List<LocationGoodsBindOut> bindOuts = this.getMapper(LocationGoodsBindOutMapper.class).selectValidOutByOrderNo(outParam);
            if (bindOuts.isEmpty()) {
                logger.debug(Constant.LINE + "出库单无锁定商品");
                throw new WMSException("出库单无锁定商品");
            }
            inGoodsSum = null; // 取消订单入库的总价格为空
            // 计算入库总数量和封装入库单详情数据(入库总数量需要从实际拣了的计算)
            for (StockOutInfo infoTemp : stockOutInfos) {
                //入库单详情
                PurchaseInInfo inInfo = new PurchaseInInfo();
                WmsUtil.copyPropertiesIgnoreNull(infoTemp, inInfo);

                // 单个商品的总数
                int oneGoodsAmount = 0;
                for (LocationGoodsBindOut outTemp : bindOuts) {
                    // 如果缺货跳过或未拣的数据
                    if (Constant.OOS_FLAG.equals(outTemp.getLocationNo()) || outTemp.getPickedNum() == null || outTemp.getPickedNum() == 0) {
                        continue;
                    }
                    // 如果该锁库记录是商品详情的则进行操作
                    if (outTemp.getGinfoId().equals(infoTemp.getId())) {
                        // 计算单商品下所有出库对应已拣的数量
                        oneGoodsAmount += outTemp.getPickedNum();
                    }
                }
                //  如果拣货数量大于0，需要生成入库单
                if (oneGoodsAmount > 0) {
                    goodsAmount += oneGoodsAmount;
                    inInfo.setInStockNo(inBill.getInStockNo());
                    inInfo.setInStockNum(oneGoodsAmount);
                    inInfo.setInStockSum(null);
                    inInfo.setValidityTime(Constant.DEFULT_VALIDITY_TIME);
                    inInfosParam.add(inInfo);
                }
            }
        }
        inBill.setInGoodsAmount(goodsAmount);
        inBill.setInGoodsSum(inGoodsSum);
        // 如果操作人为空，设置默认操作人名称
        if (StringUtils.isBlank(inBill.getOuserName())) {
            inBill.setOuserName(Constant.DEFAULT_USER_NAME);
        }
        //插入入库单主子表数据
        if (!inInfosParam.isEmpty()) {
            this.getMapper(InStockBillMapper.class).insertSelective(inBill);
            this.getMapper(PurchaseInInfoMapper.class).insertBatch(inInfosParam);
            // 插入上架单数据
            GroundingBill gbill = new GroundingBill();
            WmsUtil.copyPropertiesIgnoreNull(inBill, gbill);
            gbill.setStatus(Constant.GroundingBillStatus.WAITTING);
            gbill.setType(inBill.getType());
            gbill.setGroundingAmount(inBill.getInGoodsAmount());
            // 默认上架单是没有操作人的
            gbill.setOuserId(null);
            gbill.setOuserName(null);
            this.getMapper(GroundingBillMapper.class).insertSelective(gbill);
        }
        return inBill.getInStockNo();
    }


}
