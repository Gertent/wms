package com.rmd.wms.common.vo;

import com.rmd.oms.constant.OrderStatus;
import com.rmd.oms.constant.OrderType;
import com.rmd.oms.entity.vo.OperateUserVo;

import java.io.Serializable;

/**
 * 修改订单状态参数集合
 * @author : liu
 * Date : 2017/3/24
 */
public class UpdateOrderStatusParam implements Serializable {

    public UpdateOrderStatusParam() {
    }

    public UpdateOrderStatusParam(OrderType var1, String var2, OperateUserVo var3, OrderStatus var4) {
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.var4 = var4;
    }

    OrderType var1;
    String var2;
    OperateUserVo var3;
    OrderStatus var4;

    public OrderType getVar1() {
        return var1;
    }

    public void setVar1(OrderType var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public OperateUserVo getVar3() {
        return var3;
    }

    public void setVar3(OperateUserVo var3) {
        this.var3 = var3;
    }

    public OrderStatus getVar4() {
        return var4;
    }

    public void setVar4(OrderStatus var4) {
        this.var4 = var4;
    }
}
