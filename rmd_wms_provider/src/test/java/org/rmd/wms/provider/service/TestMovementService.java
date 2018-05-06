package org.rmd.wms.provider.service;

import com.alibaba.fastjson.JSON;
import com.rmd.wms.bean.MovementInfo;
import com.rmd.wms.bean.vo.app.MovementBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.MovementService;
import org.junit.Test;
import org.rmd.wms.provider.common.ICommonServiceTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/3/7.
 */
public class TestMovementService extends ICommonServiceTest {

    @Autowired
    private MovementService movementService;

    @Test
    public void testPickingBreakage() {
        MovementBillInfo param = new MovementBillInfo();
        param.setOrderNo("11703069434764");
        param.setType(Constant.MovementType.PICKING_BREAKAGE);
        param.setMoveAmount(55);
        param.setInUser(1);
        param.setOutUser(1);
        param.setWareId(1);
        List<MovementInfo> moveInfos = new ArrayList<>();
        MovementInfo info1 = new MovementInfo();
        info1.setGoodsCode("A00000000001");
        info1.setLocationNoOut("B02-01-01");
        info1.setOutNum(5);
//        MovementInfo info2 = new MovementInfo();
//        info2.setGoodsCode("");
//        info2.setLocationNoOut("");
//        info2.setOutNum(5);
        moveInfos.add(info1);
//        moveInfos.add(info2);
        param.setMoveInfos(moveInfos);
        try {
            ServerStatus serverStatus = movementService.pickingBreakage(param);
            System.out.println(Constant.LINE + JSON.toJSONString(serverStatus));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMovementTask(){
        MovementBillInfo param = new MovementBillInfo();
        param.setId(4);
        MovementBillInfo task = movementService.getMovementTask(param);
        System.out.println(JSON.toJSONString(task));
    }


    public static void main(String[] args) {
        MovementBillInfo param = new MovementBillInfo();
        param.setId(3);
        List<MovementInfo> moveInfos = new ArrayList<>();
        MovementInfo info1 = new MovementInfo();
        info1.setId(1);
        info1.setLocationNoOut("B02-01-01");
        info1.setInNum(2);
        moveInfos.add(info1);
        param.setMoveInfos(moveInfos);
        System.out.println(JSON.toJSONString(param));
    }


}
