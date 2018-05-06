package com.rmd.wms.service.impl;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.rmd.bms.bean.MessageWraper;
import com.rmd.bms.mq.service.MessageConsumerService;
import com.rmd.wms.bean.CheckInfo;
import com.rmd.wms.bean.LocationGoodsBindIn;
import com.rmd.wms.bean.LocationGoodsBindOut;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.po.InventoryRequestMqVo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.bean.vo.web.SubmitChecksVo;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.InStockService;
import com.rmd.wms.common.service.WmsCheckService;
import com.rmd.wms.common.service.WmsStockService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.CheckInfoMapper;
import com.rmd.wms.dao.LocationGoodsBindInMapper;
import com.rmd.wms.dao.LocationGoodsBindOutMapper;
import com.rmd.wms.dao.WarehouseMapper;
import com.rmd.wms.enums.WarehouseStatus;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.CheckInfoService;
import com.rmd.wms.service.wms2fms.InventoryRequestService;
import com.rmd.wms.util.AsyncExcutor;
import com.rmd.wms.util.WmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yao.util.collection.CollectionUtil;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 盘点详情服务实现类
 *
 * @author : liu
 * @Date : 2017/4/12
 */
@Service("checkInfoService")
public class CheckInfoServiceImpl extends BaseService implements CheckInfoService, MessageConsumerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InventoryRequestService inventoryRequestService;
    @Autowired
    private WmsCheckService wmsCheckService;
    @Autowired
    private InStockService inStockService;
    @Autowired
    private WmsStockService wmsStockService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getMapper(CheckInfoMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int insert(CheckInfo record) {
        this.getMapper(CheckInfoMapper.class).insert(record);
        int id = record.getId();
        return id;
    }


    @Override
    public int insertSelective(CheckInfo record) {
        return this.getMapper(CheckInfoMapper.class).insertSelective(record);
    }

    @Override
    public CheckInfo selectByPrimaryKey(Integer id) {
        return this.getMapper(CheckInfoMapper.class).selectByPrimaryKey(id);
    }


    @Override
    public int updateByPrimaryKeySelective(CheckInfo record) {
        return this.getMapper(CheckInfoMapper.class).updateByPrimaryKeySelective(record);
    }


    @Override
    public int updateByPrimaryKey(CheckInfo record) {
        return this.getMapper(CheckInfoMapper.class).updateByPrimaryKey(record);
    }

    @Override
    public List<CheckInfo> selectByCriteria(Map<String, Object> map) {
        return this.getMapper(CheckInfoMapper.class).selectByCriteria(map);
    }

    @Override
    public PageBean<CheckInfo> listCheckInfo(Integer page, Integer rows,
                                             Map<String, Object> map) {
        PageHelper.startPage(page, rows);
        return new PageBean<CheckInfo>(this.getMapper(CheckInfoMapper.class).selectByCriteria(map));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus submitChecksReport(SubmitChecksVo param) throws WMSException {
        // 第一步准备出所有待盘点锁库的商品
        ServerStatus ss = new ServerStatus();
        List<CheckInfo> checkInfos = this.getMapper(CheckInfoMapper.class).selectByIds(param.getIds());
        if (checkInfos == null || checkInfos.isEmpty()) {
            ss.setStatus("102");
            ss.setMessage("数据不存在");
            return ss;
        }
        Date now = new Date();
        // 消息推送集合
        List<InventoryRequestMqVo> mqVoList = new ArrayList<>();
        // 提报的盘点详情集合
        List<CheckInfo> isReportList = new ArrayList<>();
        // 进行锁库集合
        List<CheckInfo> lockedList = new ArrayList<>();
        Warehouse warehouse = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(checkInfos.get(0).getWareId());
        for (CheckInfo infoTemp : checkInfos) {
            // 如果盘点详情不是大盘或者不是未盘点或已提报的跳过
            if (Constant.CheckBillType.LARGE_CHECK != infoTemp.getType() || Constant.CheckInfoSubmitStatus.NO_SUBMIT != infoTemp.getSubmitStatus() || Constant.TYPE_STATUS_NO == infoTemp.getDoChecked()) {
                continue;
            }
            InventoryRequestMqVo voTemp = new InventoryRequestMqVo();
            // 复制字段：wareName，goodsCode，goodsBarCode，goodsName，spec，unit，
            WmsUtil.copyPropertiesIgnoreNull(infoTemp, voTemp);
            voTemp.setCheckInfoId(infoTemp.getId());
            voTemp.setWareCode(warehouse.getCode());
            voTemp.setSubmitTime(now);
            voTemp.setSubmitUser(param.getUserName());
            // 判断盘点差异数(如果复盘数为空则取初盘差异数)
            Integer checkDuff = infoTemp.getSecondCheckValidDiff() != null ? infoTemp.getSecondCheckValidDiff() : infoTemp.getFirstCheckValidDiff();
            voTemp.setCheckDiffNum(checkDuff);
            voTemp.setRemark(param.getDesc());
            voTemp.setImgsUrl(param.getFileUrls());

            mqVoList.add(voTemp);
            // 设置为已提报，最后更新状态
            infoTemp.setSubmitStatus(Constant.TYPE_STATUS_YES);
            isReportList.add(infoTemp);
            // 判断如果是盘亏了，进行锁库操作
            if (checkDuff < 0) {
                lockedList.add(infoTemp);
            }
        }
        if (isReportList.isEmpty()) {
            ss.setStatus("103");
            ss.setMessage("提报数据为空");
            return ss;
        }
        // 批量修改盘点详情为已提报
        this.getMapper(CheckInfoMapper.class).updateBatchByPrimaryKeySelective(isReportList);
        // 第二步把数据提交到消息队列中，异步放入消息队列中
        Future future = pushCheckReport(mqVoList);
        // 第三步把盘亏的商品进行锁库操作
        try {
            wmsCheckService.checkLockedStock(lockedList);
        } catch (WMSException e) {
            logger.error(Constant.LINE + "盘点锁库失败", e);
            e.setMsg("盘点锁库失败");
            throw e;
        }
        // 第四步判断异步处理结果，如果失败，抛出异常，回滚数据
        try {
            Boolean flag = (Boolean) AsyncExcutor.getExcuteResult(future);
            if (flag != null && flag) {
                ss.setMessage("操作成功");
            } else {
                logger.info(Constant.LINE + "盘点发送消息失败");
                throw new WMSException("盘点发送消息失败");
            }
        } catch (WMSException e) {
            logger.error(Constant.LINE + "异步处理发送消息队列异常", e);
            throw e;
        }
        return ss;
    }

    /*
     * 执行异步提交到消息队列
     */
    private Future pushCheckReport(final List<InventoryRequestMqVo> mqVoList) throws WMSException {
        return AsyncExcutor.excute(new Callable() {
            @Override
            public Boolean call() throws Exception {
                // 执行异步提交到消息队列逻辑
                boolean flag = inventoryRequestService.pushInventoryReport(mqVoList);
                logger.debug(Constant.LINE + "异步推送结果 : " + flag);
                return flag;
            }
        });
    }

    @Transactional
    @Override
    public int updateCheckInfoStatus(Integer wareId,String operateStatus) throws WMSException {
    	int result=0;
    	if("0".equals(operateStatus)){//关闭订单
    		Warehouse warehouse= this.getMapper(WarehouseMapper.class).selectByPrimaryKey(wareId);
    		Warehouse wh=new Warehouse();
    		wh.setId(warehouse.getId());
    		wh.setStatus(WarehouseStatus.A002.getValue());//大盘中
    		result=this.getMapper(WarehouseMapper.class).updateByPrimaryKeySelective(wh);
    	}else if("1".equals(operateStatus)){//开放订单 		
    		Warehouse warehouse= this.getMapper(WarehouseMapper.class).selectByPrimaryKey(wareId);
    		Map<String, Object> paraMap=new HashMap<String, Object>();
        	paraMap.put("wareId", wareId);
        	paraMap.put("type", WarehouseStatus.A002.getValue());//大盘
        	paraMap.put("submitStatus", 0);//提报状态，-1：不可提交，0：未提交，1：已提交
    		List<CheckInfo> list= this.getMapper(CheckInfoMapper.class).selectByCriteria(paraMap);
    		CheckInfo ck=null;
    		List<CheckInfo> listNewCk=new ArrayList<>();
    		for(CheckInfo o:list){
    			ck=new CheckInfo();
    			ck.setId(o.getId());
    			ck.setSubmitStatus(-1);
    			listNewCk.add(ck);
    		}
    		if(listNewCk.size()>0){
    			this.getMapper(CheckInfoMapper.class).updateBatchByPrimaryKeySelective(listNewCk);
    		}
    		Warehouse wh=new Warehouse();
    		wh.setId(warehouse.getId());
    		wh.setStatus(WarehouseStatus.A001.getValue());//启用
    		result=this.getMapper(WarehouseMapper.class).updateByPrimaryKeySelective(wh);
    	}
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean messageExecute(MessageWraper messageWraper) {
        if (messageWraper == null) {
            return true;
        }
        logger.info(Constant.LINE + "checkInfoService messageWraper : " + JSON.toJSONString(messageWraper));
        List<Integer> ids = JSON.parseArray(messageWraper.getBody(), Integer.class);
        List<CheckInfo> checkInfos = this.getMapper(CheckInfoMapper.class).selectByIds(ids);
        List<CheckInfo> checkedUps = new ArrayList<>(); //盘盈集合
        List<CheckInfo> checkedDowns = new ArrayList<>(); //盘亏集合
        // 如果没有该盘点记录，处理消息并放行业务
        if (checkInfos == null) {
            return true;
        }
        // 根据盘点记录进行分类处理
        for (CheckInfo infoTemp : checkInfos) {
            Integer checkDiff = infoTemp.getSecondCheckValidDiff() != null ? infoTemp.getSecondCheckValidDiff() : infoTemp.getFirstCheckValidDiff();
            if (checkDiff != null && checkDiff > 0) {
                checkedUps.add(infoTemp);
            } else if (checkDiff != null && checkDiff < 0) {
                checkedDowns.add(infoTemp);
            }
            // 设置盘点记录为已审核
            infoTemp.setDoAudit(Constant.TYPE_STATUS_YES);
        }
        Date now = new Date();
        try {
            // 处理盘盈业务(盘盈需要把盘盈的数量上架入库)
            if (!checkedUps.isEmpty()) {
                List<LocationGoodsBindIn> bindInList = new ArrayList<>();
                for (CheckInfo infoTemp : checkedUps) {
                    // 盘盈多出来的数量，需要入库入库上架
                    Integer checkDiff = infoTemp.getSecondCheckValidDiff() != null ? infoTemp.getSecondCheckValidDiff() : infoTemp.getFirstCheckValidDiff();
                    LocationGoodsBindIn bindInTemp = new LocationGoodsBindIn();
                    // 复制字段：locationNo，goodsCode，goodsBarCode，validityTime，wareId，wareName，
                    WmsUtil.copyPropertiesIgnoreNull(infoTemp, bindInTemp);
                    bindInTemp.setGinfoId(infoTemp.getId());
                    bindInTemp.setId(null);
                    bindInTemp.setGroundingNo(infoTemp.getCheckNo());
                    bindInTemp.setGroundingNum(checkDiff);
                    bindInTemp.setCreateTime(now);
                    // 下面三个字段暂时没有
                    bindInTemp.setLocationId(null);
                    bindInTemp.setAreaId(null);
                    bindInTemp.setAreaName(null);
                    bindInList.add(bindInTemp);
                    // 上架修改库存信息
                    try {
                        inStockService.putawayAlterStock(bindInTemp, bindInTemp.getWareId());
                    } catch (WMSException e) {
                        throw new WMSException(null, e.getMsg(), "上架修改库存失败", e);
                    }
                }
                // 批量插入上架记录
                if (!bindInList.isEmpty()) {
                    this.getMapper(LocationGoodsBindInMapper.class).insertBatch(bindInList);
                }
            }
            // 处理盘亏业务（盘亏需要把有效盘亏数进行拣货操作）
            if (!checkedDowns.isEmpty()) {
                String checkNo = checkedDowns.get(0).getCheckNo();
                // 获取现在的所有盘点单的锁库数据
                Set<Integer> infoIds = (Set<Integer>) CollectionUtil.toFieldSet(checkedDowns, "id");
                List<LocationGoodsBindOut> bindOuts = this.getMapper(LocationGoodsBindOutMapper.class).selectValidOutByGinfoIds(new ArrayList<>(infoIds));
                List<LocationGoodsBindOut> alterBindOuts = new ArrayList<>(); // 待修改的出库记录
                // 修改已拣数量为锁库数量，下面进行修改库存数量
                for (LocationGoodsBindOut outTemp : bindOuts) {
                    if (!outTemp.getOrderNo().equals(checkNo)) {
                        continue;
                    }
                    outTemp.setPickedNum(outTemp.getLockedNum());
                    // 这里需要判断盘点上库存是否是可卖库存
                    for (CheckInfo infoTemp : checkedDowns) {
                        if (infoTemp.getId().equals(outTemp.getGinfoId())) {
                            // 修改货位和仓库的库存数
                            try {
                                wmsStockService.alterWmsStock(outTemp.getLocationNo(), outTemp.getGoodsCode(), outTemp.getWareId(), outTemp.getAreaId(), infoTemp.getSaleType(), -(outTemp.getPickedNum() == null ? 0 : outTemp.getPickedNum()), null);
                            } catch (WMSException e) {
                                logger.error(Constant.LINE + "出库拣货修改库存错误： " + e.getMsg(), e);
                                throw e;
                            }
                        }
                    }
                    alterBindOuts.add(outTemp);
                }
                // 批量修改已拣数量
                if (!alterBindOuts.isEmpty()) {
                    this.getMapper(LocationGoodsBindOutMapper.class).updateBatchByPrimaryKey(alterBindOuts);
                }
            }
            // 最后修改盘点记录为已审核
            if (!checkInfos.isEmpty()) {
                this.getMapper(CheckInfoMapper.class).updateBatchByPrimaryKeySelective(checkInfos);
            }
        } catch (Exception e) {
            logger.error(Constant.LINE + "盘点审核通过处理业务异常", e);
            return false;
        }
        return true;
    }
}
