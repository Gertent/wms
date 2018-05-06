package com.rmd.wms.bean.vo.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rmd.wms.bean.PurchaseInInfo;


/**
 * 
* @ClassName: InStockPrint 
* @Description: TODO(入库单打印实体) 
* @author ZXLEI
* @date Mar 31, 2017 10:15:12 AM 
*
 */
public class InStockBillPrint   {

	    
	private String supplierName;//供应商
	
	private Date inStockTime;//入库时间

	private String inStockUserName;//收货员

	private String purchaseNo;//采购单号

	private String inStockNo;//入库单号

	private String wareName;//收货单位名称

	private Integer purchaseNum;//采购数量

	private Integer inStockNum;//入库数量

	private List<PurchaseInInfo> purchaseInInfoList;

	public Date getInStockTime() {
		return inStockTime;
	}

	public void setInStockTime(Date inStockTime) {
		this.inStockTime = inStockTime;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getInStockUserName() {
		return inStockUserName;
	}

	public void setInStockUserName(String inStockUserName) {
		this.inStockUserName = inStockUserName;
	}

	public String getPurchaseNo() {
		return purchaseNo;
	}

	public void setPurchaseNo(String purchaseNo) {
		this.purchaseNo = purchaseNo;
	}

	public String getInStockNo() {
		return inStockNo;
	}

	public void setInStockNo(String inStockNo) {
		this.inStockNo = inStockNo;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public List<PurchaseInInfo> getPurchaseInInfoList() {
		return purchaseInInfoList;
	}

	public void setPurchaseInInfoList(List<PurchaseInInfo> purchaseInInfoList) {
		this.purchaseInInfoList = purchaseInInfoList;
	}

	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public Integer getInStockNum() {
		return inStockNum;
	}

	public void setInStockNum(Integer inStockNum) {
		this.inStockNum = inStockNum;
	}

	
}
