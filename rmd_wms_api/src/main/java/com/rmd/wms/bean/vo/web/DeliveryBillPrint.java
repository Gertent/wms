package com.rmd.wms.bean.vo.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
* @ClassName: DeliveryPrint 
* @Description: TODO(发运交接单实体) 
* @author ZXLEI
* @date Mar 31, 2017 10:40:14 AM 
*
 */
public class DeliveryBillPrint {

	//索引编号
	private Integer index;

	//发货单号
	private String deliveryNo;

	//运单件数
	private Integer orderSum;

	//承运商id
	private Integer logisComId;

	//承运商名称
	private String logisComName;

	//仓库ID
	private Integer wareId;

	//仓库名称
	private String wareName;

	//录入运单号时间
	private Date inLogisticsTime;

	//订单商品总价
	private BigDecimal goodsSum;

	//承运重量
	private Double parcelWeight;

	//订单商品总数
	private Integer goodsAmount;

	//承运单号
	private String logisticsNo;

	//销售价格
	private BigDecimal salesPrice;

	//商品名称
	private String goodsName;

	//商品规格属性
	private String spec;

	//有效期
	private Date validityTime;
	
	//订单单一商品数量
	private Integer stockOutNum;

	//订单单一商品金额汇总
    private BigDecimal stockOutSum;

	//承运单订单商品总价
	private BigDecimal deliveryGoodsSum;

	//承运单订单商品总数
	private Integer deliveryGoodsAmount;

	//承运单商品信息
	private List<DeliveryGoodsInfo> deliveryGoodsInfoList;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getStockOutNum() {
		return stockOutNum;
	}

	public void setStockOutNum(Integer stockOutNum) {
		this.stockOutNum = stockOutNum;
	}

	public BigDecimal getStockOutSum() {
		return stockOutSum;
	}

	public void setStockOutSum(BigDecimal stockOutSum) {
		this.stockOutSum = stockOutSum;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public Integer getOrderSum() {
		return orderSum;
	}

	public void setOrderSum(Integer orderSum) {
		this.orderSum = orderSum;
	}

	public Integer getLogisComId() {
		return logisComId;
	}

	public void setLogisComId(Integer logisComId) {
		this.logisComId = logisComId;
	}

	public String getLogisComName() {
		return logisComName;
	}

	public void setLogisComName(String logisComName) {
		this.logisComName = logisComName;
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

	public Date getInLogisticsTime() {
		return inLogisticsTime;
	}

	public void setInLogisticsTime(Date inLogisticsTime) {
		this.inLogisticsTime = inLogisticsTime;
	}

	public BigDecimal getGoodsSum() {
		return goodsSum;
	}

	public void setGoodsSum(BigDecimal goodsSum) {
		this.goodsSum = goodsSum;
	}

	public Double getParcelWeight() {
		return parcelWeight;
	}

	public void setParcelWeight(Double parcelWeight) {
		this.parcelWeight = parcelWeight;
	}

	public Integer getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(Integer goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Date getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(Date validityTime) {
		this.validityTime = validityTime;
	}

	public BigDecimal getDeliveryGoodsSum() {
		return deliveryGoodsSum;
	}

	public void setDeliveryGoodsSum(BigDecimal deliveryGoodsSum) {
		this.deliveryGoodsSum = deliveryGoodsSum;
	}

	public Integer getDeliveryGoodsAmount() {
		return deliveryGoodsAmount;
	}

	public void setDeliveryGoodsAmount(Integer deliveryGoodsAmount) {
		this.deliveryGoodsAmount = deliveryGoodsAmount;
	}

	public List<DeliveryGoodsInfo> getDeliveryGoodsInfoList() {
		return deliveryGoodsInfoList;
	}

	public void setDeliveryGoodsInfoList(List<DeliveryGoodsInfo> deliveryGoodsInfoList) {
		this.deliveryGoodsInfoList = deliveryGoodsInfoList;
	}
}
