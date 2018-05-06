package com.rmd.wms.bean.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 销售出库单
 * @author zuoguodong
 */
public class SalesSlipVO implements Serializable{

	private static final long serialVersionUID = -7105836628560908821L;
	
	//订单号
	private String orderNo;
	//发货单号
	private String deliveryNo;
	//下单时间
	private Date orderDate; 
	//出库时间	
	private Date deliveryEndTime; 
	//入库时时间
	private Date inStockTime;
	//仓库ID	
	private Integer wareId;
	//仓库名称
	private String wareName;
	//物流快递	
	private String logisComName;
	//运单号	
	private String logisticsNo;
	//销售数量
	private Integer goodsAmount;
	//销售订单出库明细列表
	private List<SalesSlipDetailVO> salesSlipDetailList = new ArrayList<SalesSlipDetailVO>();
	
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
	public String getLogisComName() {
		return logisComName;
	}
	public void setLogisComName(String logisComName) {
		this.logisComName = logisComName;
	}
	public String getLogisticsNo() {
		return logisticsNo;
	}
	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}
	public Integer getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(Integer goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public List<SalesSlipDetailVO> getSalesSlipDetailList() {
		return salesSlipDetailList;
	}
	public void setSalesSlipDetailList(List<SalesSlipDetailVO> salesSlipDetailList) {
		this.salesSlipDetailList = salesSlipDetailList;
	}
	
}
