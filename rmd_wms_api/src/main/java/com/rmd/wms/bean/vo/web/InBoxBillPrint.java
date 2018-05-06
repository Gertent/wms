package com.rmd.wms.bean.vo.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class InBoxBillPrint {

	private String orderNo;
	
	private String orderDate;
	//商品总费用
	private BigDecimal goodsSum;
	//配送费用
	private BigDecimal deliveryFee;
	//优惠费用
	private BigDecimal discount;
	//支付费用
	private BigDecimal payFee;
	//保价费用
	private BigDecimal returnFee;
	//应付总金额
	private BigDecimal totalFee;
		
	private String receiveName;
	
	private String receiveMobile;
	
	private List<InBoxBill> stockOutInfos;

	private String logoPth;
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public BigDecimal getGoodsSum() {
		return goodsSum;
	}

	public void setGoodsSum(BigDecimal goodsSum) {
		this.goodsSum = goodsSum;
	}
	
	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getPayFee() {
		return payFee;
	}

	public void setPayFee(BigDecimal payFee) {
		this.payFee = payFee;
	}

	public BigDecimal getReturnFee() {
		return returnFee;
	}

	public void setReturnFee(BigDecimal returnFee) {
		this.returnFee = returnFee;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getReceiveMobile() {
		return receiveMobile;
	}

	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}

	public List<InBoxBill> getStockOutInfos() {
		return stockOutInfos;
	}

	public void setStockOutInfos(List<InBoxBill> stockOutInfos) {
		this.stockOutInfos = stockOutInfos;
	}


	public String getLogoPth() {
		return logoPth;
	}

	public void setLogoPth(String logoPth) {
		this.logoPth = logoPth;
	}



}
