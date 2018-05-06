package com.rmd.wms.bean.vo;

import com.rmd.wms.bean.OrderLogisticsInfo;
import com.rmd.wms.bean.StockOutInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单详情
 * @author : liu
 * Date : 2017/3/15
 */
public class OrderInfo implements Serializable {

    private String orderNo;//订单号
    private Integer orderType;//订单号
    private Date orderdate;//下单时间
    private Integer goodsAmount;//商品总数
    private BigDecimal goodsSum;//商品总额
    private Integer wareId;//仓库id
    private String wareName;//仓库名称
    private Double weight;//商品总重量

    private OrderLogisticsInfo orderLogiInfo; // 订单收货人信息
    private List<StockOutInfo> stockOutInfos;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public Integer getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Integer goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getGoodsSum() {
        return goodsSum;
    }

    public void setGoodsSum(BigDecimal goodsSum) {
        this.goodsSum = goodsSum;
    }

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public OrderLogisticsInfo getOrderLogiInfo() {
        return orderLogiInfo;
    }

    public void setOrderLogiInfo(OrderLogisticsInfo orderLogiInfo) {
        this.orderLogiInfo = orderLogiInfo;
    }

    public List<StockOutInfo> getStockOutInfos() {
        return stockOutInfos;
    }

    public void setStockOutInfos(List<StockOutInfo> stockOutInfos) {
        this.stockOutInfos = stockOutInfos;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
