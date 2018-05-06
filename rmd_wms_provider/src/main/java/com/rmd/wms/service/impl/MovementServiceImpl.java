package com.rmd.wms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.rmd.wms.bean.*;
import com.rmd.wms.bean.vo.app.MovementBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.app.StockOutBillInfo;
import com.rmd.wms.bean.vo.web.MovementLocation;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.InStockService;
import com.rmd.wms.common.service.StockOutService;
import com.rmd.wms.common.service.WmsStockService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.*;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.service.MovementService;
import com.rmd.wms.util.WmsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 移库服务实现
 * @author : liu
 * @Date : 2017/3/2
 */
@Service("movementService")
public class MovementServiceImpl extends BaseService implements MovementService {

    private Logger logger = Logger.getLogger(MovementServiceImpl.class);

    @Autowired
    private InStockService inStockService;
    @Autowired
    private StockOutService stockOutService;
    @Autowired
    private WmsStockService wmsStockService;

    @Override
    public Movement selectByPrimaryKey(Integer id) {
        return this.getMapper(MovementMapper.class).selectByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus pickingBreakage(MovementBillInfo param) throws WMSException {
        ServerStatus ss = new ServerStatus();
        /*
         * 1、校验数据
         */
        // 校验参数(订单号、入库交接人、移库类型和报损商品货位号以及数量和总数量)
        if (param == null || StringUtils.isBlank(param.getOrderNo()) || param.getInUser() == null || param.getMoveInfos() == null || param.getMoveAmount() <= 0 || param.getType() != Constant.MovementType.PICKING_BREAKAGE ||
                !(param.getBreakType() == Constant.MovementBreakType.BREAK_DOWN || param.getBreakType() == Constant.MovementBreakType.LOSE)) {
            ss.setStatus("101");
            ss.setMessage("参数错误");
            logger.info(Constant.LINE + "param ：" + param);
            return ss;
        }
        Date now = new Date();
        // 校验报损数据
        Map<String, Object> bindOutMap = new HashMap<>();
        bindOutMap.put("orderNo", param.getOrderNo());
        // 查询订单下的所有商品
        List<LocationGoodsBindOut> bindOuts = this.getMapper(LocationGoodsBindOutMapper.class).selectByOrderNoCode(bindOutMap);
        if (bindOuts == null || bindOuts.isEmpty()) {
            ss.setStatus("103");
            ss.setMessage("该订单无锁库记录");
            return ss;
        }
        List<MovementInfo> moveInfosParam = param.getMoveInfos();
        List<Integer> ids = new ArrayList<>();
        for (MovementInfo paramTemp : moveInfosParam) {
            if (paramTemp == null || paramTemp.getId() == null || paramTemp.getOutNum() == null) {
                logger.debug(Constant.LINE + "详情参数错误");
                throw new WMSException("101", "参数错误");
            }
            // 校验报损数据是否重复
            if (ids.contains(paramTemp.getId())) {
                ss.setStatus("104");
                ss.setMessage("拣货报损数据重复");
                return ss;
            } else {
                ids.add(paramTemp.getId());
            }
            // 校验数量是否错误
            for (LocationGoodsBindOut outTemp : bindOuts) {
                // 如果两个id相同，则为同一个数据
                if (paramTemp.getId().equals(outTemp.getId())) {
                    // 并且，报损数量 > 锁定数量 - 已拣数量 则返回数据错误
                    if (paramTemp.getOutNum() > outTemp.getLockedNum() - (outTemp.getPickedNum() == null ? 0 : outTemp.getPickedNum())) {
                        ss.setStatus("105");
                        ss.setMessage("报损数量错误");
                        return ss;
                    }
                    // 设置效期，移库移出详情中需要展示效期
                    paramTemp.setValidityTime(outTemp.getValidityTime());
                }
            }
        }

        /*
         * 2、添加移库记录
         */
        Movement movement = new Movement();
        WmsUtil.copyPropertiesIgnoreNull(param, movement);
        movement.setStatus(Constant.MovementStatus.WAITTING);
        movement.setMoveOutTime(now);
        movement.setCreateTime(now);
        this.getMapper(MovementMapper.class).insertSelective(movement);
        // 封装移库详情表数据（有效期需要前端传过来）
        for (MovementInfo temp : moveInfosParam) {
            temp.setMoveId(movement.getId());
            temp.setCreateTime(now);
        }
        this.getMapper(MovementInfoMapper.class).insertBatch(moveInfosParam);

        /*
         * 3、生成出库记录
         */
        boolean oosFlag = false; // 是否缺货标识符
        List<LocationGoodsBindOut> pickBreakageOuts = new ArrayList<>();
        List<LocationGoodsBindOut> alterLockBindOuts = new ArrayList<>();
        for (MovementInfo paramTemp : moveInfosParam) {
            // 封装拣货报损出库记录数据
            for (LocationGoodsBindOut outTemp : bindOuts) {
                // 如果两个id相同，则为同一个数据
                if (paramTemp.getId().equals(outTemp.getId())) {
                    // 拣货报损移库的出库记录
                    LocationGoodsBindOut pickingBreakageOut = new LocationGoodsBindOut();
                    WmsUtil.copyPropertiesIgnoreNull(outTemp, pickingBreakageOut);
                    pickingBreakageOut.setId(null);
                    pickingBreakageOut.setGinfoId(null);
                    pickingBreakageOut.setOrderNo(movement.getId()+"");
                    pickingBreakageOut.setCreateTime(now);
                    pickingBreakageOut.setPickedNum(paramTemp.getOutNum());
                    pickingBreakageOut.setLockedNum(paramTemp.getOutNum());
                    pickBreakageOuts.add(pickingBreakageOut);

                    // 修改报损移出的库存数量
                    try {
                        wmsStockService.alterWmsStock(outTemp.getLocationNo(), outTemp.getGoodsCode(), outTemp.getWareId(), outTemp.getAreaId(), Constant.TYPE_STATUS_YES, -paramTemp.getOutNum(), null);
                    } catch (WMSException e) {
                        logger.error(Constant.LINE + "出库拣货修改库存错误： " + e.getMsg(), e);
                        e.setCode("105");
                        throw e;
                    }

                    // 封装要更新之前的锁定数据
                    LocationGoodsBindOut alterOut = new LocationGoodsBindOut();
                    alterOut.setId(paramTemp.getId());
                    alterOut.setLockedNum(outTemp.getLockedNum() - paramTemp.getOutNum());// 原先的锁定数量减去报损的数量
                    alterLockBindOuts.add(alterOut);

                    // 对报损移出的商品进行锁库
                    boolean oosFlagLock =  stockOutService.lockedOneSku(outTemp.getOrderNo(), outTemp.getGinfoId(), outTemp.getGoodsCode(), paramTemp.getOutNum(), outTemp.getWareId());
                    oosFlag = oosFlag || oosFlagLock;
                }
            }
        }
        this.getMapper(LocationGoodsBindOutMapper.class).insertBatch(pickBreakageOuts);
        this.getMapper(LocationGoodsBindOutMapper.class).updateBatchByPrimaryKey(alterLockBindOuts);
        // 生成新的拣货任务，如果缺货，生成无货位的拣货任务，然后出库单改为缺货订单
        StockOutBill soBill = new StockOutBill();
        soBill.setOrderNo(param.getOrderNo());
        if (oosFlag) {
            soBill.setPickingStatus(Constant.PickingStatus.STOCKOUT);
        } else {
            soBill.setPickingStatus(Constant.PickingStatus.WATTING);
        }
        this.getMapper(StockOutBillMapper.class).updateByOrderNoSelective(soBill);
        ss.setStatus("200");
        ss.setMessage("操作成功");
        return ss;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus relockedStock(Integer wareId) throws WMSException {
        ServerStatus json = new ServerStatus();
        /*
         * 1、查询出所有需要重新锁库的数据（订单信息和对应的缺货锁定数据）
         */
        // 获取拣货节点中的所有缺货订单
        Map<String, Object> billMap = new HashMap<>();
        billMap.put("pickingStatus", Constant.PickingStatus.STOCKOUT);
        billMap.put("status", Constant.StockOutBillStatus.PICKING);
        billMap.put("wareId", wareId);
        List<StockOutBill> bills = this.getMapper(StockOutBillMapper.class).selectAllByWhere(billMap);
        if (bills == null || bills.size() < 1) {
            json.setStatus("101");
            json.setMessage("无缺货订单");
            return json;
        }
        // 存放订单及详情集合
        List<StockOutBillInfo> params = new ArrayList<>();
        List<StockOutBill> updateBills = new ArrayList<>();
        for (StockOutBill billTemp : bills) {
            // 查询所有缺货详情数据
            Map<String, Object> outMap = new HashMap<>();
            outMap.put("orderNo", billTemp.getOrderNo());
            outMap.put("locationNo", Constant.OOS_FLAG);
            outMap.put("wareId", wareId);
            List<LocationGoodsBindOut> bindOuts = this.getMapper(LocationGoodsBindOutMapper.class).selectByOrderNoCode(outMap);
            if (bindOuts == null || bindOuts.isEmpty()) {
                StockOutBill soBill = new StockOutBill();
                soBill.setId(billTemp.getId());
                soBill.setPickingStatus(Constant.PickingStatus.ERROR);
                updateBills.add(soBill);
                continue;
            }
            StockOutBillInfo param = new StockOutBillInfo();
            WmsUtil.copyPropertiesIgnoreNull(billTemp, param);
            param.setBindOutInfos(bindOuts);
            params.add(param);
            // 删除本仓库订单下的所有缺货锁库详情
            this.getMapper(LocationGoodsBindOutMapper.class).deleteBatchOrderStockOut(outMap);
        }

        /*
         * 2、进行重新锁库，锁完库，删除原先的数据，否则更新剩下待锁定的数量。
         */
        for (StockOutBillInfo paramTemp : params) {
            boolean oosFlag = false; // 是否缺货
            for (LocationGoodsBindOut outTemp : paramTemp.getBindOutInfos()) {
                boolean oosFlagLock = stockOutService.lockedOneSku(outTemp.getOrderNo(), outTemp.getGinfoId(), outTemp.getGoodsCode(), outTemp.getLockedNum(), outTemp.getWareId());
                oosFlag = oosFlag || oosFlagLock;
            }
            // 修改订单状态
            StockOutBill soBill = new StockOutBill();
            soBill.setId(paramTemp.getId());
            if (!oosFlag) {
                soBill.setPickingStatus(Constant.PickingStatus.WATTING);
                updateBills.add(soBill);
            }
        }
        if (!updateBills.isEmpty()) {
            this.getMapper(StockOutBillMapper.class).updateBatchByPrimaryKeySelective(updateBills);
            logger.info(Constant.LINE + "缺货还原订单：" + JSON.toJSONString(updateBills));
        }
        json.setStatus("200");
        json.setMessage("操作成功");
        return json;
    }

    @Override
    public ServerStatus<LocationGoodsBind> verifyMovingAndBreakage(MovementInfo param, Integer wareId) {
        ServerStatus<LocationGoodsBind> ss = new ServerStatus<>();
        if (param == null || StringUtils.isBlank(param.getGoodsCode()) || StringUtils.isBlank(param.getLocationNoOut())) return null;
        Location location = new Location(param.getLocationNoOut(), wareId);
        location = this.getMapper(LocationMapper.class).selectByLocaNoAndWareId(location);
        if (location == null) {
            ss.setStatus("103");
            ss.setMessage("货位不存在");
            return ss;
        }
        // 查询到当前扫到的货位库存信息
        LocationGoodsBind bind = new LocationGoodsBind(param.getLocationNoOut(), param.getGoodsCode(), wareId, location.getAreaId(), location.getType());
        bind = this.getMapper(LocationGoodsBindMapper.class).selectUnionPrimaryKey(bind);
        if (bind == null) {
            ss.setStatus("104");
            ss.setMessage("货位库存不存在");
            return ss;
        }
        // 检验可移库数量
        if (Constant.TYPE_STATUS_YES == location.getType()) {
            if (param.getOutNum() > bind.getValidityNum()) {
                ss.setStatus("105");
                ss.setMessage("报损数量超过货位库存数量");
                ss.setResult(bind);
                return ss;
            }
        } else if (Constant.TYPE_STATUS_NO == location.getType()) {
            if (param.getOutNum() > bind.getLocationNum()) {
                ss.setStatus("105");
                ss.setMessage("报损数量超过货位库存数量");
                ss.setResult(bind);
                return ss;
            }
        } else {
            ss.setStatus("106");
            ss.setMessage("货位信息错误");
            return ss;
        }
        ss.setStatus("200");
        ss.setMessage("校验成功");
        return ss;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public ServerStatus movingAndBreakage(MovementBillInfo param) throws WMSException {
        ServerStatus ss = new ServerStatus();
        /*
         * 1、校验参数
         */
        if (param == null || param.getInUser() == null || param.getMoveInfos() == null || param.getMoveAmount() <= 0 ||
                !(param.getType() == Constant.MovementType.GENERAL_BREAKAGE || param.getType() == Constant.MovementType.GENERAL_MOVEMENT) ||
                ((param.getType() == Constant.MovementType.GENERAL_BREAKAGE && (param.getBreakType() != Constant.MovementBreakType.BREAK_DOWN &&
                        param.getBreakType() != Constant.MovementBreakType.LOSE)))) {
            ss.setStatus("101");
            ss.setMessage("参数错误");
            return ss;
        }

        /*
         * 2、生成出库记录,修改货位及仓库库存的有效数据
         */
        List<MovementInfo> moveInfosParam = param.getMoveInfos();
        List<LocationGoodsBindOut> bindOutRecords = new ArrayList<>();
        Map<String, String> verifyParamMap = new HashMap<>();
        for (MovementInfo tempParam : moveInfosParam) {
            if (tempParam == null || StringUtils.isBlank(tempParam.getGoodsCode()) || StringUtils.isBlank(tempParam.getLocationNoOut()) || tempParam.getOutNum() == null) {
                logger.debug(Constant.LINE + "详情参数错误");
                throw new WMSException("101", "参数错误");
            }
            // 如果商品编码和货位号一样，说明是重复数据，返回数据异常
            if (verifyParamMap.containsKey(tempParam.getGoodsCode().trim()) && tempParam.getLocationNoOut().equals(verifyParamMap.get(tempParam.getGoodsCode().trim()))) {
                ss.setStatus("102");
                ss.setMessage("移库数据错误，不可重复");
                return ss;
            } else {
                verifyParamMap.put(tempParam.getGoodsCode(), tempParam.getLocationNoOut());
            }
            // 先查处货位信息，再封装查货位库存信息
            Location location = new Location(tempParam.getLocationNoOut(), param.getWareId());
            location = this.getMapper(LocationMapper.class).selectByLocaNoAndWareId(location);
            if (location == null) {
                throw new WMSException("103", "货位不存在");
            }
            // 查询到当前扫到的货位库存信息
            LocationGoodsBind bind = new LocationGoodsBind(tempParam.getLocationNoOut(), tempParam.getGoodsCode(), param.getWareId(), location.getAreaId(), location.getType());
            bind = this.getMapper(LocationGoodsBindMapper.class).selectUnionPrimaryKey(bind);
            if (bind == null) {
                throw new WMSException("104", "货位库存不存在");
            }
            // 检验可移库数量
            if (Constant.TYPE_STATUS_YES == location.getType()) {
                if (tempParam.getOutNum() > bind.getValidityNum()) {
                    throw new WMSException("105", "报损数量超过货位库存数量");
                }
            } else if (Constant.TYPE_STATUS_NO == location.getType()) {
                if (tempParam.getOutNum() > bind.getLocationNum()) {
                    throw new WMSException("105", "报损数量超过货位库存数量");
                }
            } else {
                throw new WMSException("106", "货位信息错误");
            }
            // 添加移库详情信息
            tempParam.setValidityTime(bind.getValidityTime());
            tempParam.setGoodsBarCode(bind.getGoodsBarCode());

            // 封装出库记录
            LocationGoodsBindOut out = new LocationGoodsBindOut();
            WmsUtil.copyPropertiesIgnoreNull(bind, out);
            out.setId(null);
            out.setPickedNum(tempParam.getOutNum());
            out.setLockedNum(tempParam.getOutNum());
            out.setCreateTime(new Date());
            bindOutRecords.add(out);
            //3、修改货位及仓库库存的有效数据
            try {
                wmsStockService.alterWmsStock(out.getLocationNo(), out.getGoodsCode(), out.getWareId(), out.getAreaId(), location.getType(), -tempParam.getOutNum(), -tempParam.getOutNum());
            } catch (WMSException e) {
                logger.error(Constant.LINE + "出库拣货修改库存错误： " + e.getMsg(), e);
                throw new WMSException("107", e.getMsg(), e.getMessage(), e);
            }
        }
        this.getMapper(LocationGoodsBindOutMapper.class).insertBatch(bindOutRecords);

        /*
         * 3、添加移库记录
         */
        Movement movement = new Movement();
        WmsUtil.copyPropertiesIgnoreNull(param, movement);
        movement.setStatus(Constant.MovementStatus.WAITTING);
        Date now = new Date();
        movement.setMoveOutTime(now);
        movement.setCreateTime(now);
        this.getMapper(MovementMapper.class).insertSelective(movement);
        // 封装移库详情表数据
        for (MovementInfo temp : moveInfosParam) {
            temp.setMoveId(movement.getId());
            temp.setCreateTime(now);
        }
        this.getMapper(MovementInfoMapper.class).insertBatch(moveInfosParam);
        ss.setStatus("200");
        ss.setMessage("操作成功");
        return ss;
    }

    @Override
    public List<Movement> getMovementList(Movement movement) {
        if (movement == null) return null;
        movement.setStatus(Constant.MovementStatus.WAITTING);
        return this.getMapper(MovementMapper.class).selectByCriteria(movement);
    }

    @Override
    public MovementBillInfo getMovementTask(MovementBillInfo param) throws WMSException {
        MovementInfo info = new MovementInfo();
        info.setMoveId(param.getId());
        List<MovementInfo> infos = this.getMapper(MovementInfoMapper.class).selectByCriteria(info);
        if (infos == null || infos.size() < 1) {
            throw new WMSException("103", "未查询到数据");
        }
        param.setMoveInfos(infos);
        return param;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus movePutaway(MovementBillInfo param) throws WMSException {
        ServerStatus ss = new ServerStatus();
        Date now = new Date();
        /*
         * 1、校验数据并把其他数据赋值到对应属性上
         */
        Movement movement = this.getMapper(MovementMapper.class).selectByPrimaryKey(param.getId());
        if (movement == null || !param.getWareId().equals(movement.getWareId()) || movement.getStatus() == null || Constant.MovementStatus.FINISH == movement.getStatus()) {
            ss.setStatus("102");
            ss.setMessage("数据错误");
            return ss;
        }
        List<LocationGoodsBindIn> paramBindIns = param.getBindIns();
        // 查询所有移库详情
        MovementInfo infoParam = new MovementInfo();
        infoParam.setMoveId(param.getId());
        List<MovementInfo> moveInfos = this.getMapper(MovementInfoMapper.class).selectByCriteria(infoParam);
        for (MovementInfo infoTemp : moveInfos) {
            int numTemp = 0;
            for (LocationGoodsBindIn paramTemp : paramBindIns) {
                // 是同一商品把货位上的数量相加
                if (infoTemp.getId().equals(paramTemp.getGinfoId())) {
                    numTemp += paramTemp.getGroundingNum();
                }
            }
            // 所有货位上的总数量等于入库数量置空，进行下一个商品比较，不相等，则返回错误
            if (infoTemp.getOutNum() == numTemp) {
                numTemp = 0;
            } else {
                ss.setStatus("103");
                ss.setMessage("上架数量错误");
                return ss;
            }

        }

        /*
         * 2、上架操作，绑定货位和绑定仓库库存
         */
        // 先上架，然后添加上架记录
        for (LocationGoodsBindIn bindIn : paramBindIns) {
            // 封装上架记录信息,然后批量插入
            for (MovementInfo info : moveInfos) {
                if (bindIn.getGoodsCode().equals(info.getGoodsCode())) {
                    bindIn.setGoodsBarCode(info.getGoodsBarCode());
                    bindIn.setValidityTime(info.getValidityTime());
                }
                // 下面是补全移库详情数据
                info.setInStockTime(now);
                // 入库数量必须和出库数量一致才能上架，所以取出库数量
                info.setInNum(info.getOutNum());
            }
            bindIn.setGroundingNo(param.getId() + "");// 上架单号改为存移库id
            // 上架修改库存信息
            try {
                inStockService.putawayAlterStock(bindIn, param.getWareId());
            } catch (WMSException e) {
                throw new WMSException("104", e.getMsg(), "上架修改库存失败", e);
            }
        }
        // 批量插入上架记录
        this.getMapper(LocationGoodsBindInMapper.class).insertBatch(paramBindIns);

        /*
         * 3、修改移库表和详情表
         */
        movement.setStatus(Constant.MovementStatus.FINISH);
        movement.setMoveInTime(now);
        // 新添加移入人员名称
        movement.setInUser(param.getInUser());
        movement.setInUserName(param.getInUserName());
        this.getMapper(MovementMapper.class).updateByPrimaryKeySelective(movement);
        this.getMapper(MovementInfoMapper.class).updateBatchByPrimaryKey(moveInfos);
        ss.setStatus("200");
        ss.setMessage("操作成功");
        return ss;
    }

    /**
     * zxl
     * 后台仓储作业查询
     */
    @Override
    public PageBean<MovementLocation> ListMovementBillInfo(int page, int rows,Map<String, Object> parmaMap) {
        List<MovementLocation> movementBillInfoList=new ArrayList<MovementLocation>();
        List<Movement> movementList=this.getMapper(MovementMapper.class).selectAllByWhere(parmaMap);
        Object locationNoOut=parmaMap.get("locationNoOut");//移出货位号
        Object locationNoIn=parmaMap.get("locationNoIn");//移入货位号
        Object goodsCode=parmaMap.get("goodsCode");//商品编码
        Map<String, Object> whereparma=new HashMap<String, Object>(10);
        String str="";
        if(locationNoIn!=null&&StringUtil.isNotEmpty(locationNoIn.toString())){
            Map<String,	Object> gbMap=new HashMap<>();
            gbMap.put("locationNo", locationNoIn);
            List<LocationGoodsBindIn> gbList=this.getMapper(LocationGoodsBindInMapper.class).selectAllByWhere(gbMap);
            StringBuilder sb=new StringBuilder();
            if(gbList.size()>0){
                sb.append("(");
                for(LocationGoodsBindIn o:gbList){
                    sb.append("'").append(o.getGinfoId()).append("',");
                }
                str=sb.toString();
                if(str.length()>0){
                    str=str.substring(0, str.length()-1)+")";
                }
            }else{
                whereparma.put("noData_Search", "1");
            }
        }
        StringBuilder moveIdBuf=new StringBuilder();
        for (Movement movement : movementList) {
            moveIdBuf.append(movement.getId()).append(",");
        }
        String moveIds=moveIdBuf.toString();//移库id集合
        if(StringUtil.isNotEmpty(moveIds)){
            moveIds="("+moveIds.substring(0,moveIds.length()-1)+")";
        }
        PageBean<MovementInfo> pageBeanMove=null;
        if(movementList.size()>0){

            whereparma.put("moveId_in", moveIds);
            if(locationNoOut!=null&&StringUtil.isNotEmpty(locationNoOut.toString())){
                whereparma.put("locationNoOut", locationNoOut);
            }
            if(goodsCode!=null&&StringUtil.isNotEmpty(goodsCode.toString())){
                whereparma.put("goodsCode", goodsCode);
            }
            if(str.length()>0){
                whereparma.put("id_in", str);
            }
            PageHelper.startPage(page, rows);
            List<MovementInfo> moveInfoList=this.getMapper(MovementInfoMapper.class).selectAllByWhere(whereparma);
            pageBeanMove=new PageBean<MovementInfo>(moveInfoList);
            for (MovementInfo movementInfo : moveInfoList) {
                MovementLocation movementLocation=new MovementLocation();
                for (Movement movement : movementList) {
                    if(movement.getId()==movementInfo.getMoveId()){
                        BeanUtils.copyProperties(movement,movementLocation);
                        break;
                    }
                }
                movementLocation.setMovementInfo(movementInfo);
                movementLocation.setLocationGoodsBindIn(new LocationGoodsBindIn());
                movementBillInfoList.add(movementLocation);
            }
        }

        PageBean<MovementLocation> pageBean=new PageBean<MovementLocation>(movementBillInfoList);
        pageBean.setList(movementBillInfoList);
        pageBean.setPageNum(pageBeanMove.getPageNum());
        pageBean.setPages(pageBeanMove.getPages());
        pageBean.setPageSize(pageBeanMove.getPageSize());
        pageBean.setSize(pageBeanMove.getSize());
        pageBean.setTotal(pageBeanMove.getTotal());
        return  pageBean;
    }

    @Override
    public List<Map<String, Object>> listAllByParmMap(Map<String, Object> parmaMap) {
        return this.getMapper(MovementMapper.class).selectAllByParmMap(parmaMap);
    }


}
