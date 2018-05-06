package com.rmd.wms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.NotificationBuilder;
import com.rmd.commons.servutils.Notifications;
import com.rmd.wms.bean.WareDeliverRange;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.WareDeliverRangeMapper;
import com.rmd.wms.dao.WarehouseMapper;
import com.rmd.wms.service.RepositoryDeliverrangeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liu on 2017/3/17.
 */
@Service("repositoryDeliverrangeService")
public class RepositoryDeliverrangeServiceImpl extends BaseService implements RepositoryDeliverrangeService {

    @Override
    public List<WareDeliverRange> selectByCriteria(WareDeliverRange record) {
        return this.getMapper(WareDeliverRangeMapper.class).selectByCriteria(record);
    }

    @Override
    public Notification<Warehouse> selectWareByProvCode(String provCode) {
        Notification<Warehouse> noti = new Notification<>();
        if (StringUtils.isBlank(provCode)) {
            noti = NotificationBuilder.buildOne(Notifications.PARAMETER_ERROR);
            noti.setNotifInfo("参数错误");
            return noti;
        }
        WareDeliverRange record = new WareDeliverRange();
        record.setProvCode(provCode);
        record.setStatus(Constant.TYPE_STATUS_YES);
        List<WareDeliverRange> deliverranges = this.getMapper(WareDeliverRangeMapper.class).selectByCriteria(record);
        if (deliverranges == null || deliverranges.size() < 1) {
            noti.setNotifCode(101);
            noti.setNotifInfo("该地区不支持配送");
            return noti;
        }
        Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(deliverranges.get(0).getWareId());
        if (warehouse == null || warehouse.getStatus() == Constant.WarehouseStatus.DISABLE) {
            noti.setNotifCode(102);
            noti.setNotifInfo("数据错误，仓库被停用或不存在");
            return noti;
        }
        noti = NotificationBuilder.buildOne(Notifications.OK);
        noti.setResponseData(warehouse);
        return noti;
    }
}
