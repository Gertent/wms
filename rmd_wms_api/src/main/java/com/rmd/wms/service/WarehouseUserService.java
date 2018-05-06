package com.rmd.wms.service;

import java.util.List;

import com.rmd.wms.bean.WarehouseUser;
import com.rmd.wms.bean.vo.app.MoveInUsersParam;

public interface WarehouseUserService {

    /**
     * 插入数据
     *
     * @param warehouseUser
     * @return
     */
    int insertSelective(WarehouseUser warehouseUser);

    /**
     * 批量插入
     *
     * @param wareId
     * @param list
     * @return
     */
    int insertBatchSelective(Integer wareId, List<WarehouseUser> list);

    /**
     * 通过仓库id查询信息
     *
     * @param wareId
     * @return
     */
    List<WarehouseUser> selectByWareId(Integer wareId);

    /**
     * 通过仓库id查询信息
     *
     * @param param
     * @return
     */
    List<WarehouseUser> selectByWareIdAndUserIds(MoveInUsersParam param);

}
