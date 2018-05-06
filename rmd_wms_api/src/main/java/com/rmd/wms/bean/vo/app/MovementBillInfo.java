package com.rmd.wms.bean.vo.app;

import com.rmd.wms.bean.LocationGoodsBindIn;
import com.rmd.wms.bean.Movement;
import com.rmd.wms.bean.MovementInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/3/2.
 */
public class MovementBillInfo extends Movement implements Serializable {

    private List<MovementInfo> moveInfos;
    private List<LocationGoodsBindIn> bindIns;

    public List<MovementInfo> getMoveInfos() {
        return moveInfos;
    }

    public void setMoveInfos(List<MovementInfo> moveInfos) {
        this.moveInfos = moveInfos;
    }

    public List<LocationGoodsBindIn> getBindIns() {
        return bindIns;
    }

    public void setBindIns(List<LocationGoodsBindIn> bindIns) {
        this.bindIns = bindIns;
    }
}
