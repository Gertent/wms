package com.rmd.wms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rmd.wms.bean.PurchaseBill;
import com.rmd.wms.bean.PurchaseInInfo;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.PurchaseInInfoMapper;
import com.rmd.wms.service.PurchaseInInfoService;
@Service("purchaseInInfoService")
public class PurchaseInInfoServiceImpl extends BaseService implements PurchaseInInfoService{

	@Override
	public List<PurchaseInInfo> ListPurchaseInInfo(Map<String, Object> parmaMap) {
		return this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(parmaMap);
	}

	@Override
	public List<Map> ListAllByParmMap(Map<String, Object> parmaMap) {
		return this.getMapper(PurchaseInInfoMapper.class).selectAllByParmMap(parmaMap);
	}

	@Override
	public PurchaseInInfo selectByPrimaryKey(Integer id) {
		
		return this.getMapper(PurchaseInInfoMapper.class).selectByPrimaryKey(id);
	}

	//后期引用
	@Override
	public Map<String, Object> ListPurchaseInfos(Map<String, Object> parmaMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		PurchaseBill purchaseBill=new PurchaseBill();
  	        List<PurchaseInInfo> pInInfos=new ArrayList<PurchaseInInfo>(10);
  	        List<PurchaseInInfo> newPInInfos=new ArrayList<PurchaseInInfo>(10);
		try {
	    	  pInInfos=this.getMapper(PurchaseInInfoMapper.class).selectAllByWhere(parmaMap);
	    	  for (PurchaseInInfo purchaseInInfo : pInInfos) {
	    	    Map<String, Object> whereParams=new HashMap<String, Object>();//动态条件
	    	    whereParams.put("wareId", 1);
	    	    whereParams.put("purchaseNo",purchaseInInfo.getInStockNo());
				  whereParams.put("goodsCode",purchaseInInfo.getGoodsCode());
	    	    newPInInfos.add(purchaseInInfo);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("purchaseBill", purchaseBill);
		map.put("newPInInfos", newPInInfos);
		return map;
	}


	@Override
	public int insertBatch(List<PurchaseInInfo> list) {
		
		return this.getMapper(PurchaseInInfoMapper.class).insertBatch(list);
	}
	

	@Override
	public int selectCountByInStockNo(String inStockNo) {
		return this.getMapper(PurchaseInInfoMapper.class).selectCountByInStockNo(inStockNo);
	}

}
