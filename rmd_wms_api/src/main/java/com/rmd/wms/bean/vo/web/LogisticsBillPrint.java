package com.rmd.wms.bean.vo.web;

import java.math.BigDecimal;


/**
 * 运单
 * */
public class LogisticsBillPrint {
	//*********************Begin 寄件人信息***************************
	//寄件人姓名
	private String senderFromName;
	//始发地
	private String senderOrignAddr;
	//寄件人单位名称
	private String senderCompanyName;
	//寄件地址
	private String senderAddr;
	//寄件地址-省
	private String senderProvince;
	//寄件地址-市
	private String senderCity;
	//寄件地址-县/区
	private String senderDistrict;
	//寄件地址-乡镇
	private String senderTown;
	//寄件人手机
	private String senderMobile;
	//寄件人固定电话
	private String senderTel;
	//*********************END 寄件人信息***************************
	//*********************BEGIN 物品信息***************************
	//文件
	private String document;
	//物品
	private String parcel;
	//数量
	private Integer quanlity;
	//件数
	private Integer pieces;
	//重量
	private BigDecimal weight;
	//体积
	private BigDecimal dimensionl;
	//长
	private BigDecimal dimensionlLong;
	//宽
	private BigDecimal dimensionlWidth;
	//高
	private BigDecimal dimensionlHeight;
	//*********************END 物品信息***************************
	
	//*********************Begin 收件人信息***************************
	//收件人姓名
	private String toName;
	//目的地
	private String toDestination;
	//收件人单位名称
	private String toCompanyName;
	//收件地址
	private String toAddr;
	//收件地址-省
	private String toProvince;
	//收件地址-市
	private String toCity;
	//收件地址-县/区
	private String toDistrict;
	//收件地址-乡镇
	private String toTown;
	//收件人手机
	private String toMobile;
	//收件人固定电话
	private String toTel;
	
	//*********************END 收件人信息***************************
	
	//*********************BEGIN 费用信息***************************
	//保价  0:非保价 1：保价
	private String insurance;
	//保价金额
	private BigDecimal insuredValue;
	//保价费
	private BigDecimal insuranceCharge;
	//资费倍数
	private Integer timesCharge;
	//付费方式 1:寄方付 2：收方付
	private Integer paymentCharge;
	//资费方式 1:现金 2：协议结算
	private Integer paymentOptions;
	//运费
	private BigDecimal freight;
	//费用总计
	private BigDecimal totalCharge;
	
	//*********************END 费用信息***************************
	
	public String getSenderFromName() {
		return senderFromName;
	}
	public void setSenderFromName(String senderFromName) {
		this.senderFromName = senderFromName;
	}
	public String getSenderOrignAddr() {
		return senderOrignAddr;
	}
	public void setSenderOrignAddr(String senderOrignAddr) {
		this.senderOrignAddr = senderOrignAddr;
	}
	public String getSenderCompanyName() {
		return senderCompanyName;
	}
	public void setSenderCompanyName(String senderCompanyName) {
		this.senderCompanyName = senderCompanyName;
	}
	public String getSenderAddr() {
		return senderAddr;
	}
	public void setSenderAddr(String senderAddr) {
		this.senderAddr = senderAddr;
	}
	public String getSenderProvince() {
		return senderProvince;
	}
	public void setSenderProvince(String senderProvince) {
		this.senderProvince = senderProvince;
	}
	public String getSenderCity() {
		return senderCity;
	}
	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}
	public String getSenderDistrict() {
		return senderDistrict;
	}
	public void setSenderDistrict(String senderDistrict) {
		this.senderDistrict = senderDistrict;
	}
	public String getSenderTown() {
		return senderTown;
	}
	public void setSenderTown(String senderTown) {
		this.senderTown = senderTown;
	}
	public String getSenderMobile() {
		return senderMobile;
	}
	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}
	public String getSenderTel() {
		return senderTel;
	}
	public void setSenderTel(String senderTel) {
		this.senderTel = senderTel;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public String getParcel() {
		return parcel;
	}
	public void setParcel(String parcel) {
		this.parcel = parcel;
	}
	public Integer getQuanlity() {
		return quanlity;
	}
	public void setQuanlity(Integer quanlity) {
		this.quanlity = quanlity;
	}
	public Integer getPieces() {
		return pieces;
	}
	public void setPieces(Integer pieces) {
		this.pieces = pieces;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getDimensionl() {
		return dimensionl;
	}
	public void setDimensionl(BigDecimal dimensionl) {
		this.dimensionl = dimensionl;
	}
	public BigDecimal getDimensionlLong() {
		return dimensionlLong;
	}
	public void setDimensionlLong(BigDecimal dimensionlLong) {
		this.dimensionlLong = dimensionlLong;
	}
	public BigDecimal getDimensionlWidth() {
		return dimensionlWidth;
	}
	public void setDimensionlWidth(BigDecimal dimensionlWidth) {
		this.dimensionlWidth = dimensionlWidth;
	}
	public BigDecimal getDimensionlHeight() {
		return dimensionlHeight;
	}
	public void setDimensionlHeight(BigDecimal dimensionlHeight) {
		this.dimensionlHeight = dimensionlHeight;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getToDestination() {
		return toDestination;
	}
	public void setToDestination(String toDestination) {
		this.toDestination = toDestination;
	}
	public String getToCompanyName() {
		return toCompanyName;
	}
	public void setToCompanyName(String toCompanyName) {
		this.toCompanyName = toCompanyName;
	}
	public String getToAddr() {
		return toAddr;
	}
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}
	public String getToProvince() {
		return toProvince;
	}
	public void setToProvince(String toProvince) {
		this.toProvince = toProvince;
	}
	public String getToCity() {
		return toCity;
	}
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}
	public String getToDistrict() {
		return toDistrict;
	}
	public void setToDistrict(String toDistrict) {
		this.toDistrict = toDistrict;
	}
	public String getToTown() {
		return toTown;
	}
	public void setToTown(String toTown) {
		this.toTown = toTown;
	}
	public String getToMobile() {
		return toMobile;
	}
	public void setToMobile(String toMobile) {
		this.toMobile = toMobile;
	}
	public String getToTel() {
		return toTel;
	}
	public void setToTel(String toTel) {
		this.toTel = toTel;
	}
	public String getInsurance() {
		return insurance;
	}
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	public BigDecimal getInsuredValue() {
		return insuredValue;
	}
	public void setInsuredValue(BigDecimal insuredValue) {
		this.insuredValue = insuredValue;
	}
	public BigDecimal getInsuranceCharge() {
		return insuranceCharge;
	}
	public void setInsuranceCharge(BigDecimal insuranceCharge) {
		this.insuranceCharge = insuranceCharge;
	}
	public Integer getTimesCharge() {
		return timesCharge;
	}
	public void setTimesCharge(Integer timesCharge) {
		this.timesCharge = timesCharge;
	}
	public Integer getPaymentCharge() {
		return paymentCharge;
	}
	public void setPaymentCharge(Integer paymentCharge) {
		this.paymentCharge = paymentCharge;
	}
	public Integer getPaymentOptions() {
		return paymentOptions;
	}
	public void setPaymentOptions(Integer paymentOptions) {
		this.paymentOptions = paymentOptions;
	}
	public BigDecimal getFreight() {
		return freight;
	}
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	public BigDecimal getTotalCharge() {
		return totalCharge;
	}
	public void setTotalCharge(BigDecimal totalCharge) {
		this.totalCharge = totalCharge;
	}
		
	
	
}
