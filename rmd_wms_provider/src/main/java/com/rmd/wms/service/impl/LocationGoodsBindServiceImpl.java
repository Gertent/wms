package com.rmd.wms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.Notifications;
import com.rmd.lygp.back.model.GoodsSaleInfo;
import com.rmd.lygp.back.service.GoodsBaseService;
import com.rmd.wms.bean.vo.LocationGoodsBindVo;
import com.rmd.wms.bean.vo.web.SearchBindsParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.rmd.wms.bean.LocationGoodsBind;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.LocationGoodsBindMapper;
import com.rmd.wms.service.LocationGoodsBindService;
import yao.util.collection.CollectionUtil;

/**
 * @author ZXLEI
 * @ClassName: LocationGoodsBindServiceImpl
 * @Description: TODO(库区货位可用数量)
 * @date Mar 3, 2017 6:00:22 PM
 */
@Service("locationGoodsBindService")
public class LocationGoodsBindServiceImpl extends BaseService implements LocationGoodsBindService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GoodsBaseService goodsBaseService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getMapper(LocationGoodsBindMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int insert(LocationGoodsBind record) {
        return this.getMapper(LocationGoodsBindMapper.class).insert(record);
    }

    @Override
    public int insertSelective(LocationGoodsBind record) {
        return this.getMapper(LocationGoodsBindMapper.class).insertSelective(record);
    }

    @Override
    public LocationGoodsBind selectByPrimaryKey(Integer id) {
        return this.getMapper(LocationGoodsBindMapper.class).selectByPrimaryKey(id);
    }

    @Override
    public LocationGoodsBind selectUnionPrimaryKey(LocationGoodsBind record) {
        return this.getMapper(LocationGoodsBindMapper.class).selectUnionPrimaryKey(record);
    }

    @Override
    public List<LocationGoodsBind> selectByCriteria(LocationGoodsBind record) {
        return this.getMapper(LocationGoodsBindMapper.class).selectByCriteria(record);
    }

    @Override
    public List<LocationGoodsBindVo> selectByParmMap(Map<String,Object> map){
        return this.getMapper(LocationGoodsBindMapper.class).selectByParmMap(map);
    }

    @Override
    public List<LocationGoodsBind> searchBindsByCreateCheck(SearchBindsParam param) {
        // 如果通过商品名称或规格搜货位，需要依赖商品系统搜出所有商品编码然后去获取
        if (param != null && (StringUtils.isNotEmpty(param.getGoodsName()) || StringUtils.isNotEmpty(param.getSpec()))) {
            // 通过商品名称或规格去查询所有的商品编码
            // 需要添加请求其他接口的逻辑
            Notification<List<GoodsSaleInfo>> noti = goodsBaseService.selectSkuInfoByName(param.getGoodsName(), param.getSpec());
            if (noti != null || Notifications.OK.getNotifCode() == noti.getNotifCode()) {
                List<GoodsSaleInfo> goodsSaleInfos = noti.getResponseData();
                if (goodsSaleInfos != null && !goodsSaleInfos.isEmpty()) {
                    Set<String> codeSet = (Set<String>) CollectionUtil.toFieldSet(goodsSaleInfos, "code");
                    param.setGoodsCodeList(new ArrayList<>(codeSet));
                } else {
                    return null;
                }
            }
        }
        return this.getMapper(LocationGoodsBindMapper.class).searchBindsByCreateCheck(param);
    }

    @Override
    public int updateByPrimaryKeySelective(LocationGoodsBind record) {
        return this.getMapper(LocationGoodsBindMapper.class).updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKey(LocationGoodsBind record) {
        return this.getMapper(LocationGoodsBindMapper.class).updateByPrimaryKey(record);
    }


    @Override
    public PageBean<LocationGoodsBind> listLocationGoodsBind(Integer page, Integer rows, LocationGoodsBind locationGoodsBind) {
        PageHelper.startPage(page, rows);
        return new PageBean<LocationGoodsBind>(this.getMapper(LocationGoodsBindMapper.class).selectByCriteria(locationGoodsBind));
    }
    @Override
    public PageBean<LocationGoodsBindVo> selectByParmMapAdPage(Integer page,
                                                      Integer rows,Map<String,Object> map){
        PageHelper.startPage(page, rows);
        return new PageBean<LocationGoodsBindVo>(this.getMapper(LocationGoodsBindMapper.class).selectByParmMap(map));
    }

}
