package com.rmd.wms.service;

import java.util.List;
import java.util.Map;

import com.rmd.wms.bean.Menu;
import com.rmd.wms.bean.PurchaseBill;
import com.rmd.wms.bean.PurchaseInInfo;

/**
 * 
* @ClassName: PurchaseInInfoService   采购，入库，上架 商品详情
* @Description: TODO(采购，入库，上架 商品详情) 
* @author ZXLEI
* @date Feb 21, 2017 10:19:35 AM 
*
 */
public interface PurchaseInInfoService {

	/**
	 * 根据 采购单，入库单，上架单 ，仓库id 查询商品列表 
	 * @param parmaMap
	 * @return
	 */
	List<PurchaseInInfo> ListPurchaseInInfo(Map<String, Object> parmaMap);

	/**
	 * 获取详情及其入库信息
	 * @param parmaMap
	 * @return
	 * */
	List<Map> ListAllByParmMap(Map<String, Object> parmaMap);
	
	PurchaseInInfo selectByPrimaryKey(Integer id);

	Map<String,Object> ListPurchaseInfos(Map<String, Object> parmaMap);

	
	int insertBatch(List<PurchaseInInfo> list);
   


	int selectCountByInStockNo(String inStockNo);


}
