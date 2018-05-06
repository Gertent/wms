package com.rmd.wms.bean.po;

import java.io.Serializable;

/**
 * 库存查询
 * @author zuoguodong
 */
public class InventoryQueryVO implements Serializable{
	private static final long serialVersionUID = 7048564236125585800L;
	
	//仓库
	private String wareName;
	//仓库ID	
	private Integer wareId;
	//商品编码
	private String code;
	//商品条码
	private String goodsBarCode;
	//当前库存量 真实库存
	private Integer locationNum;
	//可卖库存
	private Integer validityNum;
	public String getWareName() {
		return wareName;
	}
	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	public Integer getWareId() {
		return wareId;
	}
	public void setWareId(Integer wareId) {
		this.wareId = wareId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGoodsBarCode() {
		return goodsBarCode;
	}
	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}
	public Integer getLocationNum() {
		return locationNum;
	}
	public void setLocationNum(Integer locationNum) {
		this.locationNum = locationNum;
	}
	public Integer getValidityNum() {
		return validityNum;
	}
	public void setValidityNum(Integer validityNum) {
		this.validityNum = validityNum;
	}
	
}
