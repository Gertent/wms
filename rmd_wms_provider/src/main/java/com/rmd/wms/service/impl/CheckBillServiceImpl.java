package com.rmd.wms.service.impl;

import com.github.pagehelper.PageHelper;
import com.rmd.commons.servutils.Notification;
import com.rmd.commons.servutils.Notifications;
import com.rmd.lygp.back.entity.GoodsSaleInfoVo;
import com.rmd.lygp.back.service.GoodsBaseService;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.CheckInfos;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.bean.vo.web.SearchCheckBillVo;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.*;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.CheckBillService;
import com.rmd.wms.service.DictionaryService;
import com.rmd.wms.util.WmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 盘点单服务实现类
 *
 * @author : liu
 * @Date : 2017/4/12
 */
@Service("checkBillService")
public class CheckBillServiceImpl extends BaseService implements CheckBillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private GoodsBaseService goodsBaseService;

    @Override
    public CheckBill selectByCheckNo(String checkNo) {
        return this.getMapper(CheckBillMapper.class).selectByCheckNo(checkNo);
    }

    @Transactional
    @Override
    public CheckInfos getCheckBillTask(CheckInfos param) throws WMSException {
        // 如果是盘点人盘点中意外退出等，重新领取不用添加盘点人和修改盘点单信息
        Date now = new Date();
        if (Constant.TYPE_STATUS_NO == param.getRegetTask()) {
            // 修改盘点单状态和最后盘点人
            if (Constant.CheckBillTimes.FIRST == param.getCheckTimes().intValue()) {
                param.setFirstStartTime(now);
            } else if (Constant.CheckBillTimes.SECOND == param.getCheckTimes().intValue()) {
                param.setSecondStartTime(now);
            }
            param.setStatus(Constant.CheckBillStatus.CHECKING);
            this.getMapper(CheckBillMapper.class).updateByPrimaryKeySelective(param);
            // 添加盘人信息
            List<CheckUser> users = param.getUsers();
            if (users == null || users.size() < 1) {
                throw new WMSException("盘点人不能为空");
            }
            CheckUser checkUser = users.get(0);
            checkUser.setCreateTime(now);
            checkUser.setCheckNo(param.getCheckNo());
            checkUser.setCheckTimes(param.getCheckTimes());
            this.getMapper(CheckUserMapper.class).insertSelective(checkUser);
        }
        List<CheckInfo> infos = this.getMapper(CheckInfoMapper.class).selectByCheckNo(param.getCheckNo());
        param.setInfos(infos);
        // 前端不需要盘点人信息
        param.setUsers(null);
        return param;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus doCheck(CheckInfos param) throws WMSException {
        ServerStatus ss = new ServerStatus();
        Date now = new Date();
        // 运算盘点数量
        List<CheckInfo> infosParam = param.getInfos();
        for (CheckInfo paramTemp : infosParam) {
            // 如果库存数量未传跳过
            if (paramTemp.getLocationNum() == null) {
                continue;
            }
            // 计算盘点差异
            if (Constant.CheckBillTimes.FIRST == param.getCheckTimes()) {
                if (paramTemp.getFirstCheckNum() == null) {
                    continue;
                }
                paramTemp.setSecondCheckNum(null);// 初盘时，复盘数量应该为空
                // 计算实际差异数量
                Integer deffNum = paramTemp.getFirstCheckNum() - paramTemp.getLocationNum();
                // 判断是盘盈还是盘亏
                if (deffNum > 0) {
                    // 如果盘盈了，有效差异添加
                    paramTemp.setFirstCheckValidDiff(deffNum);
                    paramTemp.setFirstCheckLockDiff(0);
                } else if (deffNum < 0) {
                    // 如果盘亏了，首先减有效差异，当有效差异 >= 有效数量时，进行减锁库库存
                    if (Constant.WareAreaType.USABLE_SALE == paramTemp.getSaleType()) {
                        int deffNumTemp = -deffNum;//当前缺了数量
                        int validDiffNum = deffNumTemp - paramTemp.getValidityNum();// 当前有效的库存报却的数量
                        // 判断有效的数量是否可满足报缺的数量
                        if (validDiffNum >= 0) {
                            paramTemp.setFirstCheckValidDiff(-paramTemp.getValidityNum());
                            paramTemp.setFirstCheckLockDiff(-validDiffNum);
                        } else {
                            paramTemp.setFirstCheckValidDiff(deffNum);
                            paramTemp.setFirstCheckLockDiff(0);
                        }
                    } else {
                        paramTemp.setFirstCheckValidDiff(deffNum);
                        paramTemp.setFirstCheckLockDiff(0);
                    }
                } else {
                    paramTemp.setFirstCheckValidDiff(0);
                    paramTemp.setFirstCheckLockDiff(0);
                }
            } else if (Constant.CheckBillTimes.SECOND == param.getCheckTimes()) {
                if (paramTemp.getSecondCheckNum() == null) {
                    continue;
                }
                paramTemp.setFirstCheckNum(null);//复盘时，初盘数量应该为空
                // 计算实际差异数量
                Integer deffNum = paramTemp.getSecondCheckNum() - paramTemp.getLocationNum();
                // 判断是盘盈还是盘亏
                if (deffNum > 0) {
                    // 如果盘盈了，有效差异添加
                    paramTemp.setSecondCheckValidDiff(deffNum);
                    paramTemp.setSecondCheckLockDiff(0);
                } else if (deffNum < 0) {
                    // 如果盘亏了，首先减有效差异，当有效差异 >= 有效数量时，进行减锁库库存
                    if (Constant.WareAreaType.USABLE_SALE == paramTemp.getSaleType()) {
                        int deffNumTemp = -deffNum;//当前缺了数量
                        int validDiffNum = deffNumTemp - paramTemp.getValidityNum();// 当前有效的库存报却的数量
                        // 判断有效的数量是否可满足报缺的数量
                        if (validDiffNum >= 0) {
                            paramTemp.setSecondCheckValidDiff(-paramTemp.getValidityNum());
                            paramTemp.setSecondCheckLockDiff(-validDiffNum);
                        } else {
                            paramTemp.setSecondCheckValidDiff(deffNum);
                            paramTemp.setSecondCheckLockDiff(0);
                        }
                    } else {
                        paramTemp.setSecondCheckValidDiff(deffNum);
                        paramTemp.setSecondCheckLockDiff(0);
                    }
                }
            } else {
                paramTemp.setSecondCheckLockDiff(0);
                paramTemp.setSecondCheckValidDiff(0);
            }
            if (paramTemp.getFirstCheckNum() != null) {
                paramTemp.setDoChecked(Constant.TYPE_STATUS_YES);
            }
        }
        // 批量更新盘点详情数据
        this.getMapper(CheckInfoMapper.class).updateBatchByPrimaryKeySelective(infosParam);
        // 如果是第一次盘点完成改为第二次等待状态，否则为完成状态
        if (Constant.CheckBillTimes.FIRST == param.getCheckTimes()) {
            // 如果是中断提交，盘点单为部分盘点状态
            if (Constant.TYPE_STATUS_YES == param.getInterrupt()) {
                param.setStatus(Constant.CheckBillStatus.CHECK_PART);
            } else {
                param.setCheckTimes(Constant.CheckBillTimes.SECOND);
                param.setStatus(Constant.CheckBillStatus.WAITTING);
                param.setFirstEndTime(now);
            }
        } else if (Constant.CheckBillTimes.SECOND == param.getCheckTimes()) {
            if (Constant.TYPE_STATUS_YES == param.getInterrupt()) {
                param.setStatus(Constant.CheckBillStatus.CHECK_PART);
            } else {
                param.setStatus(Constant.CheckBillStatus.FINISH);
                param.setSecondEndTime(now);
            }
        }
        // 修改盘点单状态
        this.getMapper(CheckBillMapper.class).updateByPrimaryKeySelective(param);
        ss.setStatus("200");
        ss.setMessage("盘点成功");
        return ss;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus createCheckBill(Integer userId, String userName, Warehouse ware, List<Integer> idList, Integer type) throws WMSException {
        ServerStatus ss = new ServerStatus();
        Date now = new Date();
        List<LocationGoodsBind> binds = this.getMapper(LocationGoodsBindMapper.class).selectByIds(idList);
        if (binds == null || binds.size() < 1) {
            ss.setStatus("102");
            ss.setMessage("货位为空");
            return ss;
        }
        ware = this.getMapper(WarehouseMapper.class).selectByPrimaryKey(ware.getId());
        // 封装盘点单的主表数据
        String billNo = dictionaryService.generateBillNo(Constant.BillNoPreFlag.PD, ware.getCode());
        CheckBill checkBill = new CheckBill();
        checkBill.setCheckNo(billNo);
        checkBill.setType(type);
        checkBill.setStatus(Constant.CheckBillStatus.WAITTING);
        checkBill.setCheckTimes(Constant.CheckBillTimes.FIRST);
        checkBill.setCreaterId(userId);
        checkBill.setCreaterName(userName);
        checkBill.setWareId(ware.getId());
        checkBill.setWareName(ware.getWareName());
        checkBill.setCreateTime(now);
        this.getMapper(CheckBillMapper.class).insertSelective(checkBill);
        // 封装盘点单详情数据
        List<CheckInfo> infos = new ArrayList<>();
        for (LocationGoodsBind bindTemp : binds) {
            CheckInfo info = new CheckInfo();
            info.setCheckNo(billNo);
            info.setType(type);
            // 详情中的：商品编码，商品条形码，仓库id，仓库名称，有效期，货位号，货位库存数量，售卖类型
            WmsUtil.copyPropertiesIgnoreNull(bindTemp, info);
            info.setBindId(bindTemp.getId());
            try {
                Notification<GoodsSaleInfoVo> noti = goodsBaseService.selectGoodsSaleInfoVoByCode(bindTemp.getGoodsCode());
                if (noti != null && Notifications.OK.getNotifCode() == noti.getNotifCode()) {
                    GoodsSaleInfoVo data = noti.getResponseData();
                    info.setGoodsName(data.getGoodsBase().getName());
                    info.setSpec(data.getGoodsSaleInfo().getSpec());
                    info.setPackageNum(data.getGoodsSaleInfo().getPacknum() + "");
                    info.setUnit(data.getGoodsSaleInfo().getSaleunit());
                }
            } catch (Exception e) {
                logger.error(Constant.LINE + "获取商品信息异常，不影响业务", e);
            }
            // 根绝盘点类型设置是否可以提报
            if (Constant.CheckBillType.USUAL_CHECK == type) {
                info.setSubmitStatus(Constant.CheckInfoSubmitStatus.UN_SUBMIT);
            } else if (Constant.CheckBillType.LARGE_CHECK == type) {
                info.setSubmitStatus(Constant.CheckInfoSubmitStatus.NO_SUBMIT);
            }
            info.setDoChecked(Constant.TYPE_STATUS_NO);
            info.setDoAudit(Constant.TYPE_STATUS_NO);
            info.setCreateTime(now);
            info.setCreaterId(userId);
            info.setCreaterName(userName);
            infos.add(info);
        }
        this.getMapper(CheckInfoMapper.class).insertBatch(infos);
        ss.setStatus("200");
        ss.setMessage("操作成功");
        return ss;
    }

    @Override
    public List<CheckBill> selectByCriteria(Map<String, Object> map) {
        return this.getMapper(CheckBillMapper.class).selectByCriteria(map);
    }

    @Override
    public PageBean<CheckBill> listCheckBill(Integer page, Integer rows,
                                             Map<String, Object> map) {
        PageHelper.startPage(page, rows);
        return new PageBean<CheckBill>(this.getMapper(CheckBillMapper.class).selectByCriteria(map));
    }

    @Override
    public PageBean<CheckBill> searchCheckBills(Integer page, Integer rows, SearchCheckBillVo param) throws WMSException {
        // 查询盘点单数据
        PageHelper.startPage(page, rows);
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("checkNo", param.getCheckNo());
        mapParam.put("startTime", param.getStarTime());
        mapParam.put("endTime", param.getEndTime());
        mapParam.put("status", param.getStatus());
        mapParam.put("type", param.getType());
        mapParam.put("wareId", param.getWareId());
        List<CheckBill> checkBills = this.getMapper(CheckBillMapper.class).selectByCriteriaAndPage(mapParam);
        return new PageBean<>(checkBills);
    }

    @Override
    public PageBean<CheckBill> searchCheckBills(Integer page, Integer rows, Map<String, Object> mapParam) throws WMSException {
        // 查询盘点单数据
        PageHelper.startPage(page, rows);
        List<CheckBill> checkBills = this.getMapper(CheckBillMapper.class).selectByCriteriaAndPage(mapParam);
        return new PageBean<>(checkBills);
    }

    @Override
    public CheckBill selectByPrimaryKey(Integer id) {
        CheckBill checkBill = this.getMapper(CheckBillMapper.class).selectByPrimaryKey(id);
        return checkBill;
    }
}
