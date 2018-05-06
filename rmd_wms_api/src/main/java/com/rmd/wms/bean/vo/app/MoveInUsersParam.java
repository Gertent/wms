package com.rmd.wms.bean.vo.app;

import java.io.Serializable;
import java.util.List;

/**
 * 获取移入用户列表参数
 * @author : liu
 * @Date : 2017/4/28
 */
public class MoveInUsersParam implements Serializable {

    private static final long serialVersionUID = 4338593559191012064L;

    private String operationCode;

    private Integer wareId;

    private List<Integer> userIds;

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
