package com.rmd.wms.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.rmd.bms.bean.MessageWraper;
import com.rmd.bms.mq.service.MessageConsumerService;
import com.rmd.wms.bean.InStockBill;
import com.rmd.wms.bean.PurchaseBill;
import com.rmd.wms.bean.PurchaseInInfo;
import com.rmd.wms.bean.po.PurchaseBillVO;
import com.rmd.wms.bean.po.PurchaseInfoVO;
import com.rmd.wms.bean.vo.app.PurchaseBillInfo;
import com.rmd.wms.bean.vo.app.ServerStatus;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.common.service.InStockService;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.dao.PurchaseBillMapper;
import com.rmd.wms.dao.PurchaseInInfoMapper;
import com.rmd.wms.enums.WmsLockBusiness;
import com.rmd.wms.enums.WmsLockName;
import com.rmd.wms.exception.WMSException;
import com.rmd.wms.oss.OssService;
import com.rmd.wms.oss.bean.OssFile;
import com.rmd.wms.service.PurchaseBillService;
import com.rmd.wms.service.wms2fms.PurchaseStorageService;
import com.rmd.wms.util.WmsLock;
import com.rmd.wms.util.BarcodeUtil;
import com.rmd.wms.util.WmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service("purchaseBillService")
public class PurchaseBillServiceImpl extends BaseService implements PurchaseBillService, MessageConsumerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InStockService inStockService;
    @Autowired
    private PurchaseStorageService purchaseStorageService;

    @Override
    public List<PurchaseBill> ListPurchaseBills(Map<String, Object> parmaMap) {
        return this.getMapper(PurchaseBillMapper.class).selectAllByWhere(parmaMap);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getMapper(PurchaseBillMapper.class).deleteByPrimaryKey(id);
    }

    @Override
    public int insert(PurchaseBill record) {
        return this.getMapper(PurchaseBillMapper.class).insert(record);
    }

    @Override
    public int insertSelective(PurchaseBill record) {
        return this.getMapper(PurchaseBillMapper.class).insertSelective(record);
    }

    @Override
    public PurchaseBill selectByPrimaryKey(Integer id) {

        return this.getMapper(PurchaseBillMapper.class).selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PurchaseBill record) {
        return this.getMapper(PurchaseBillMapper.class).updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PurchaseBill record) {
        return this.getMapper(PurchaseBillMapper.class).updateByPrimaryKey(record);
    }

    @Override
    public PurchaseBill selectByPurchaseNo(String purchaseNo) {
        return this.getMapper(PurchaseBillMapper.class).selectByPurchaseNo(purchaseNo);
    }

    @Override
    public PurchaseBillInfo getPurBillTask(PurchaseBill purchaseBill) throws InvocationTargetException, IllegalAccessException {
        if (purchaseBill == null || StringUtils.isBlank(purchaseBill.getPurchaseNo())) return null;
        // 查询采购单的详情数据
        Map<String, Object> param = new HashMap<>();
        param.put("purchaseNo", purchaseBill.getPurchaseNo());
        List<PurchaseInInfo> infos = this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(param);
        // 删除待入库数量为0的商品信息
        List<PurchaseInInfo> infosTemp = new ArrayList<>();
        for (PurchaseInInfo temp : infos) {
            if (temp.getPurchaseWaitNum() != 0)
                infosTemp.add(temp);
        }
        PurchaseBillInfo pinfo = new PurchaseBillInfo();
        WmsUtil.copyPropertiesIgnoreNull(purchaseBill, pinfo);
        pinfo.setInfos(infosTemp);
        return pinfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerStatus purInStock(PurchaseBillInfo pinfoParam) throws WMSException {
        ServerStatus ss = new ServerStatus();
        /*
         * 1、校验数据
		 */
        // 校验参数
        if (pinfoParam == null || StringUtils.isBlank(pinfoParam.getPurchaseNo()) || pinfoParam.getInfos() == null) {
            ss.setStatus("101");
            ss.setMessage("参数错误");
            return ss;
        }
//        try {
//            WmsLock wmsLock = new WmsLock(WmsLockBusiness.PURCHASE_BILL_SERVICE, WmsLockName.PUR_IN_STOCK);
//            wmsLock.acquire();
            String purchaseNo = pinfoParam.getPurchaseNo();
            PurchaseBill purchaseBill1 = this.getMapper(PurchaseBillMapper.class).selectByPurchaseNo(purchaseNo);
            if (purchaseBill1 == null || !pinfoParam.getWareId().equals(purchaseBill1.getWareId())) {
                ss.setStatus("102");
                ss.setMessage("采购单不存在");
                return ss;
            }
            // 校验入库数量
            List<PurchaseInInfo> inInfosParam = pinfoParam.getInfos();
            Map<String, Object> map = new HashMap<>();
            map.put("purchaseNo", purchaseNo);
            // 查询采购单的所有商品
            List<PurchaseInInfo> purInfos = this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(map);
            for (int i = 0; i < purInfos.size(); i++) {
                for (int j = 0; j < inInfosParam.size(); j++) {
                    // 校验是否是同一商品
                    if (inInfosParam.get(j).getGoodsCode().equals(purInfos.get(i).getGoodsCode())) {
                        // 校验数量是否出错
                        if (inInfosParam.get(j).getInStockNum() > purInfos.get(i).getPurchaseWaitNum()) {
                            ss.setStatus("103");
                            ss.setMessage("入库数量错误");
                            return ss;
                        }
                        // 每入库一条采购单中的待入库数量减少
                        if (inInfosParam.get(j).getInStockNum() <= purInfos.get(i).getPurchaseWaitNum()) {
                            purInfos.get(i).setPurchaseWaitNum(purInfos.get(i).getPurchaseWaitNum() - inInfosParam.get(j).getInStockNum());
                        }
                        // 如果商品没有效期则添加默认效期
                        if (inInfosParam.get(j).getValidityTime() == null) {
                            inInfosParam.get(j).setValidityTime(Constant.DEFULT_VALIDITY_TIME);
                        }
                    }
                }
            }
            /*
             * 2、生成入库单和入库详情
             */
            final String inStockNo;
            // 生成入库单操作
            try {
                InStockBill inStockBill = new InStockBill(purchaseNo, null, Constant.InStockBillType.PURCHASE_BILL, Constant.TYPE_STATUS_YES, pinfoParam.getOuserId(), pinfoParam.getOuserName());
                inStockNo = inStockService.generateInStockBill(inStockBill, pinfoParam.getInfos());
            } catch (WMSException e) {
                throw new WMSException("103", "生成入库单失败", "生成入库单异常", e);
            }
            /*
             * 3、修改采购单状态和详情的待入库数量
             */
            // 修改采购单详情中的待入库数量
            this.getMapper(PurchaseInInfoMapper.class).updateBatchByPrimaryKey(purInfos);
            int count = this.getMapper(PurchaseInInfoMapper.class).selectWaitCountByPurchaseNo(purchaseNo);
            PurchaseBill purchaseBill = new PurchaseBillInfo();
            purchaseBill.setPurchaseNo(purchaseNo);
            if (count > 0) {
                purchaseBill.setStatus(Constant.PurchaseBillStatus.PART);
            } else {
                purchaseBill.setStatus(Constant.PurchaseBillStatus.FINISH);
            }
            this.getMapper(PurchaseBillMapper.class).updateByPurchaseNoSelective(purchaseBill);
            /*
             * 4、如果采购单入库完成时，推动到财务系统
             */
            logger.info(Constant.LINE + "采购单入库完成，待推送到财务系统...");
            purchaseStorageService.pushPurchaseInStock(inStockNo);
            ss.setStatus("200");
            ss.setMessage("操作成功");
//            wmsLock.release();
//        } catch (WMSException e) {
//            logger.error(Constant.LINE + "采购入库异常", e);
//            throw e;
//        } catch (Exception e) {
//            logger.error(Constant.LINE + "采购入库锁异常", e);
//            throw new WMSException("104", "采购入库锁异常", "采购入库锁异常", e);
//        }
        return ss;
    }

    @Override
    public PageBean<PurchaseBill> ListPurchaseBills(Integer page, Integer rows,
                                                    Map<String, Object> parmaMap) {

        PageHelper.startPage(page, rows);
        return new PageBean<PurchaseBill>(this.getMapper(PurchaseBillMapper.class).selectAllByWhere(parmaMap));
    }

    @Override
    public boolean messageExecute(MessageWraper messageWraper) {
        if (messageWraper == null) {
            return true;
        }
        logger.info(Constant.LINE + "purchaseBillService messageWraper : " + JSON.toJSONString(messageWraper));
        PurchaseBillVO billVO = JSON.parseObject(messageWraper.getBody(), PurchaseBillVO.class);
        if (billVO == null || StringUtils.isBlank(billVO.getPurchaseNo()) || billVO.getPurchaseInfoVOs() == null || billVO.getPurchaseInfoVOs().isEmpty()) {
            return true;
        }
        // 接收到采购单，存入到数据库中
        PurchaseBill purBill = this.getMapper(PurchaseBillMapper.class).selectByPurchaseNo(billVO.getPurchaseNo().trim());
        if (purBill != null) {
            return true;
        } else {
            purBill = new PurchaseBill();
        }
        // 复制字段：purchaseNo，supplierCode，supplierName，wareId，wareName，goodsAmount，goodsSum，ouserId，ouserName，financeCode，department
        WmsUtil.copyPropertiesIgnoreNull(billVO, purBill);
        purBill.setStatus(Constant.PurchaseBillStatus.WAITTING);
        purBill.setInType(Constant.TYPE_STATUS_YES);
        purBill.setInDbData(null);// 暂时没用

//        String path = getClass().getResource("/").getFile().toString();
//        if (path.indexOf("/") == 0) {
//            path = path.substring(1, path.length());
//        }
//        path+= UUID.randomUUID().toString()+".png";
//        logger.info(Constant.LINE+"生成图片路径："+path);
//        try {
//            BarcodeUtil.generateFile(purBill.getPurchaseNo(), path);
//            File f=new File(path);
//            if(!f.exists()){
//                logger.info("生成条码文件失败"+Constant.LINE);
//            }else {
//                OssService oss = new OssService();
//                OssFile of = oss.Ossupload(f);
//                purBill.setBarCodeUrl(of.getUrl());// 采购单条形码地址
//                f.deleteOnExit();
//            }
//        } catch (IOException e) {
//            logger.error("生成条码地址失败"+Constant.LINE,e);
//            return false;
//        }catch (Throwable t){
//            logger.error("生成文件失败"+Constant.LINE,t);
//            return false;
//        }

        this.getMapper(PurchaseBillMapper.class).insertSelective(purBill);
        List<PurchaseInInfo> infos = new ArrayList<>();
        for (PurchaseInfoVO voTemp : billVO.getPurchaseInfoVOs()) {
            PurchaseInInfo info = new PurchaseInInfo();
            // 复制字段：purchaseNo，goodsCode，goodsBarCode，goodsName，spec，packageNum，unit，purchaseNum，purchasePrice，goodspriceSum，wareId，wareName
            WmsUtil.copyPropertiesIgnoreNull(voTemp, info);
            info.setPurchaseWaitNum(voTemp.getPurchaseNum());
            infos.add(info);
        }
        this.getMapper(PurchaseInInfoMapper.class).insertBatch(infos);
        return true;
    }
}
