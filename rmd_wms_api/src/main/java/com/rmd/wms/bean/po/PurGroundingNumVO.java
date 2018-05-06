package com.rmd.wms.bean.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * wms通知scm采购单上架数量
 * @author : liu
 * @Date : 2017/4/28
 */
public class PurGroundingNumVO implements Serializable {
	
	private static final long serialVersionUID = -2029396927902721022L;
    // 采购单号
    private String purchaseNo;
    // 该订单商品总上架数量
    private Integer groundingNum;
    //操作人
    private Integer userId;
    //操作时间
    private Date operatingTime;
    //商品信息（存放商品编码和商品上架数量）
    private List<GoodsGroundingNumVO> goodsGroundingNumVOs = new ArrayList<GoodsGroundingNumVO>();

    public PurGroundingNumVO() {
    }

    public PurGroundingNumVO(String purchaseNo, Integer groundingNum) {
        this.purchaseNo = purchaseNo;
        this.groundingNum = groundingNum;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Integer getGroundingNum() {
        return groundingNum;
    }

    public void setGroundingNum(Integer groundingNum) {
        this.groundingNum = groundingNum;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getOperatingTime() {
		return operatingTime;
	}

	public void setOperatingTime(Date operatingTime) {
		this.operatingTime = operatingTime;
	}

	public List<GoodsGroundingNumVO> getGoodsGroundingNumVOs() {
		return goodsGroundingNumVOs;
	}

	public void setGoodsGroundingNumVOs(
			List<GoodsGroundingNumVO> goodsGroundingNumVOs) {
		this.goodsGroundingNumVOs = goodsGroundingNumVOs;
	}
}
