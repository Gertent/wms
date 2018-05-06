package com.rmd.wms.bean.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 销售订单出库明细
 * @author zuoguodong
 */
public class SalesSlipDetailVO implements Serializable{

	private static final long serialVersionUID = -7703769614377623248L;
	
	//订单号
	private String orderNo;
	//发货单号
	private String deliveryNo;
	//商品编码
	private String goodsCode;
	//商品条码
	private String goodsBarCode;
	//商品名称
	private String goodsName;
	//下单时间
	private Date orderDate;
	//出库时间
	private Date deliveryEndTime; 
	//入库时间
	private Date inStockTime;
	//仓库ID	
	private Integer wareId;
	//仓库名称
	private String wareName; 
	//销售数量
	private Integer stockOutNum;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsBarCode() {
		return goodsBarCode;
	}
	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getDeliveryEndTime() {
		return deliveryEndTime;
	}
	public void setDeliveryEndTime(Date deliveryEndTime) {
		this.deliveryEndTime = deliveryEndTime;
	}
	public Date getInStockTime() {
		return inStockTime;
	}
	public void setInStockTime(Date inStockTime) {
		this.inStockTime = inStockTime;
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
	public Integer getStockOutNum() {
		return stockOutNum;
	}
	public void setStockOutNum(Integer stockOutNum) {
		this.stockOutNum = stockOutNum;
	}
}
