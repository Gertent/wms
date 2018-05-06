package com.rmd.wms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.web.LogisFreiBillVo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.*;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.LogisticsFreightBillService;
import com.rmd.wms.util.WmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 物流运费单服务实现
 *
 * @author : liu
 * @Date : 2017/6/22
 */
@Service("logisticsFreightBillService")
public class LogisticsFreightBillServiceImpl extends BaseService implements LogisticsFreightBillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void generateFreightBill(List<Integer> deliveryIds) throws WMSException {
        logger.debug(Constant.LINE + "发货单ids：" + JSON.toJSONString(deliveryIds));
        if (deliveryIds == null || deliveryIds.isEmpty()) {
            return;
        }
        List<DeliveryBill> deliveryBills = this.getMapper(DeliveryBillMapper.class).selectByPrimaryKeys(deliveryIds);
        if (deliveryBills == null || deliveryBills.isEmpty()) {
            logger.debug(Constant.LINE + "发货单不存在");
            return;
        }
        // 封装运费单对象List
        Date now = new Date();
        List<LogisticsFreightBill> lfBills = new ArrayList<>();
        for (DeliveryBill deliTemp : deliveryBills) {

            // 判断发货单是否已打印
            if (deliTemp.getDodeliveryPrint() != null && Constant.TYPE_STATUS_YES == deliTemp.getDodeliveryPrint()) {
                logger.debug(Constant.LINE + "发货单：" + deliTemp.getDeliveryNo() + "，已经打印并生成运费单。");
                continue;
            }
            // 获取所有发货单数据
            Map<String, Object> soParam = new HashMap<>();
            soParam.put("deliveryNo", deliTemp.getDeliveryNo());
            soParam.put("status", Constant.StockOutBillStatus.SHIPPING);
            List<StockOutBill> outBills = this.getMapper(StockOutBillMapper.class).selectAllByWhere(soParam);
            if (outBills == null || outBills.isEmpty()) {
                logger.debug(Constant.LINE + "发货单：" + deliTemp.getDeliveryNo() + "下无待生成运费单的出库单信息。");
                throw new WMSException("104", "该发货单无待生成运费单的出库单信息");
            }
            // 处理出库单的运费单数据
            for (StockOutBill soBillTemp : outBills) {
                if (StringUtils.isBlank(soBillTemp.getOrderNo()) || StringUtils.isBlank(soBillTemp.getLogisticsNo())) {
                    logger.debug(Constant.LINE + "出库单：" + soBillTemp.getOrderNo() + ",出库单数据异常，出库单号或物流单号不存在。");
                    throw new WMSException("105", "出库单号或物流单号不存在");
                }
                // 查看物流运费单是否存在
                LogisFreiBillVo param = new LogisFreiBillVo();
                param.setOrderNo(soBillTemp.getOrderNo());
                List<LogisticsFreightBill> logisticsFreightBills = this.getMapper(LogisticsFreightBillMapper.class).selectByCriteria(param);
                if (logisticsFreightBills != null && !logisticsFreightBills.isEmpty()) {
                    logger.debug(Constant.LINE + "出库单：" + soBillTemp.getOrderNo() + ",订单号已存在,不能重复。");
                    continue;
                }
                // 封装物流运费单
                LogisticsFreightBill bill = new LogisticsFreightBill();
                // 复制数据：logisticsNo，orderNo，logisComId，logisComName，code，wareId，wareName
                WmsUtil.copyPropertiesIgnoreNull(soBillTemp, bill);
                bill.setId(null);
                // 封装收货地址
                OrderLogisticsInfo logisticsInfo = this.getMapper(OrderLogisticsInfoMapper.class).selectByOrderNo(soBillTemp.getOrderNo());
                if (logisticsInfo == null) {
                    logger.debug(Constant.LINE + "出库单：" + soBillTemp.getOrderNo() + ",收货人信息数据错误。");
                    throw new WMSException("107", "收货人信息数据错误");
                }
                String addressStr = (StringUtils.isBlank(logisticsInfo.getProvName()) ? "" : logisticsInfo.getProvName()) +
                        (StringUtils.isBlank(logisticsInfo.getCityName()) ? "" : logisticsInfo.getCityName()) +
                        (StringUtils.isBlank(logisticsInfo.getAreaName()) ? "" : logisticsInfo.getAreaName()) +
                        (StringUtils.isBlank(logisticsInfo.getDetailedAddress()) ? "" : logisticsInfo.getDetailedAddress());
                bill.setReceiveAddress(addressStr);
                // 封装基础价格
                BigDecimal freightPrice;
                try {
                    freightPrice = getFreightPrice(bill.getLogisComId(), String.valueOf(logisticsInfo.getCityCode()), soBillTemp.getParcelWeight(), soBillTemp.getWareId());
                } catch (WMSException e) {
                    logger.error(Constant.LINE + e.getMsg(), e);
                    throw e;
                }
                if (freightPrice == null) {
                    logger.debug(Constant.LINE + "出库单：" + soBillTemp.getOrderNo() + ",运费价格数据错误。");
                    throw new WMSException("108", "运费价格数据错误");
                }
                bill.setBasePrice(freightPrice);
                // 设置其他字段默认值
                bill.setExtraCharges(new BigDecimal(0));
                bill.setDoChange(Constant.TYPE_STATUS_NO);
                bill.setUpdateTime(now);
                bill.setCreateTime(now);
                lfBills.add(bill);
            }
        }
        if (lfBills.isEmpty()) return;
        this.getMapper(LogisticsFreightBillMapper.class).insertBatch(lfBills);
    }

    /**
     * 获取运费，最后四舍五入保留两位小数
     * 运费＝首费＋(包裹重量－首重)*续费＋派送费
     *
     * @return
     */
    public BigDecimal getFreightPrice(Integer logisComId, String cityCode, Double parcelWeight, Integer wareId) throws WMSException {
        if (logisComId == null || StringUtils.isBlank(cityCode) || parcelWeight == null) {
            logger.info(Constant.LINE + "计算运费,参数错误");
            throw new WMSException("109", "计算运费,参数错误");
        }
        LogisticsFreightTemplate template;
        Map<String, Object> cityParam = new HashMap<>();
        cityParam.put("logisticsId", logisComId);
        cityParam.put("cityCode", cityCode);
        cityParam.put("wareId", wareId);
        List<LogisticsFreightCity> list = this.getMapper(LogisticsFreightCityMapper.class).selectByCriteria(cityParam);
        // 判断该城市是否有自己的物流费用模板，如果有用自己的，没有用默认的
        if (list == null || list.isEmpty()) {
            // 查询物流公司的默认费用模板
            Map<String, Object> tempParam = new HashMap<>();
            tempParam.put("wareId", wareId);
            tempParam.put("logisticsId", logisComId);
            tempParam.put("freightType", Constant.TYPE_STATUS_YES);
            List<LogisticsFreightTemplate> templates = this.getMapper(LogisticsFreightTemplateMapper.class).selectByCriteria(tempParam);
            if (templates == null || templates.isEmpty()) {
                logger.info(Constant.LINE + "计算运费,默认模板不存在");
                throw new WMSException("110", "计算运费,默认模板不存在");
            }
            template = templates.get(0);
        } else {
            template = this.getMapper(LogisticsFreightTemplateMapper.class).selectByPrimaryKey(list.get(0).getTemplateId());
        }
        if (template == null || template.getFirstPrice() == null || template.getFirstWeight() == null || template.getSecondPrice() == null || template.getSecondWeight() == null) {
            logger.info(Constant.LINE + "计算运费,模板数据错误");
            throw new WMSException("111", "计算运费,模板数据错误");
        }
        BigDecimal sum;// 总费用
        if (parcelWeight.compareTo(template.getFirstWeight().doubleValue()) > 0) {
            // 运费＝首费＋(包裹重量－首重)/续重(进一法取整)*续费＋派送费
            BigDecimal firstPrice = template.getFirstPrice();// 首费
            BigDecimal times = new BigDecimal(parcelWeight.toString()).subtract(template.getFirstWeight()).divide(template.getSecondWeight(), 0, BigDecimal.ROUND_UP);//续重次数
            BigDecimal secondPrice = times.multiply(template.getSecondPrice());// 续费
            BigDecimal deliveryPrice = template.getDeliveryPrice();// 派送费
            sum = firstPrice.add(secondPrice).add(deliveryPrice);
        } else {
            // 运费＝首费＋派送费
            sum = template.getFirstPrice().add(template.getDeliveryPrice());
        }
        return sum.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public PageBean<LogisticsFreightBill> getLFBillsByPage(int page, int rows, LogisFreiBillVo param) {
        PageHelper.startPage(page, rows);
        return new PageBean<>(this.getMapper(LogisticsFreightBillMapper.class).selectByCriteria(param));
    }

    @Override
    public List<LogisticsFreightBill> getLFBillsByCriteria(LogisFreiBillVo param) {
        return this.getMapper(LogisticsFreightBillMapper.class).selectByCriteria(param);
    }

    @Override
    public int updateByPrimaryKeySelective(LogisticsFreightBill record) {
        return this.getMapper(LogisticsFreightBillMapper.class).updateByPrimaryKeySelective(record);
    }

    @Override
    public LogisticsFreightBill selectByPrimaryKey(Integer id) {
        return this.getMapper(LogisticsFreightBillMapper.class).selectByPrimaryKey(id);
    }
}
