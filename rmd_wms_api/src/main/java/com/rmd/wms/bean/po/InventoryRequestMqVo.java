package com.rmd.wms.bean.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : liu
 * @Date : 2017/4/26
 */
public class InventoryRequestMqVo implements Serializable {

    private static final long serialVersionUID = -7007514015611497580L;

    // 盘点详情id
    private Integer checkInfoId;
    //仓库名称
    private String wareName;
    //仓库编号
    private String wareCode;
    //商品编码
    private String goodsCode;
    //商品条码
    private String goodsBarCode;
    //商品名称
    private String goodsName;
    //规格
    private String spec;
    //单位
    private String unit;
    //提报时间
    private Date submitTime;
    //提报人
    private String submitUser;
    //盘点差异数量
    private int checkDiffNum;
    //备注
    private String remark;
    //图片路径
    private String imgsUrl;

    public Integer getCheckInfoId() {
        return checkInfoId;
    }

    public void setCheckInfoId(Integer checkInfoId) {
        this.checkInfoId = checkInfoId;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getWareCode() {
        return wareCode;
    }

    public void setWareCode(String wareCode) {
        this.wareCode = wareCode;
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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
    }

    public int getCheckDiffNum() {
        return checkDiffNum;
    }

    public void setCheckDiffNum(int checkDiffNum) {
        this.checkDiffNum = checkDiffNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImgsUrl() {
        return imgsUrl;
    }

    public void setImgsUrl(String imgsUrl) {
        this.imgsUrl = imgsUrl;
    }
}
