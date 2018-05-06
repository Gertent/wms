package com.rmd.wms.bean.vo.web;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
* @ClassName: DeliveryGoodsBase
* @Description: TODO(发运交接单商品基本信息)
*
 */
public class DeliveryGoodsBase {

	//商品名称
	private String goodsName;

	//商品规格属性
	private String spec;

	//销售价格
	private BigDecimal salesPrice;

	//有效期
	private Date validityTime;
	
	//订单单一商品数量
	private Integer stockOutNum;

	//订单单一商品金额汇总
    private BigDecimal stockOutSum;

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
}
