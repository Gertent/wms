package com.rmd.wms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rmd.wms.bean.LocationGoodsBindOut;
import com.rmd.wms.bean.StockOutInfo;
import com.rmd.wms.bean.vo.web.StockOutGoodsInfo;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.LocationGoodsBindOutMapper;
import com.rmd.wms.dao.StockOutInfoMapper;
import com.rmd.wms.service.StockOutInfoService;
/**
 * 
* @ClassName: StockOutInfoServiceImpl 
* @Description: TODO(出库商品详情) 
* @author ZXLEI
* @date Feb 23, 2017 11:50:29 AM 
*
 */
@Service("stockOutInfoService")
public class StockOutInfoServiceImpl extends BaseService implements StockOutInfoService  {

	private static Logger logger=Logger.getLogger(StockOutInfoServiceImpl.class);

	@Override
	public int deleteByPrimaryKey(Integer id) {
		
		return this.getMapper(StockOutInfoMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(StockOutInfo record) {
		return this.getMapper(StockOutInfoMapper.class).insert(record);
	}

	@Override
	public int insertSelective(StockOutInfo record) {
		return this.getMapper(StockOutInfoMapper.class).insertSelective(record);
	}

	@Override
	public StockOutInfo selectByPrimaryKey(Integer id) {
		return this.getMapper(StockOutInfoMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(StockOutInfo record) {
		return this.getMapper(StockOutInfoMapper.class).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(StockOutInfo record) {
		return this.getMapper(StockOutInfoMapper.class).updateByPrimaryKey(record);
	}

	@Override
	public List<StockOutInfo> ListStockOutInfos(Map<String, Object> parmaMap) {
		return this.getMapper(StockOutInfoMapper.class).selectAllByWhere(parmaMap);
	}

	@Override
	public List<StockOutGoodsInfo> ListStockOutGoodsInfo(
			Map<String, Object> parmaMap) {
		//出库单详情
        List<StockOutInfo> stockOutInfos=new ArrayList<StockOutInfo>(10);
        //出库单绑定货位vo
		List<StockOutGoodsInfo> StockOutGoodsList=new ArrayList<StockOutGoodsInfo>(10);
		try {
			stockOutInfos=this.getMapper(StockOutInfoMapper.class).selectAllByWhere(parmaMap);
			for(StockOutInfo sInfo :stockOutInfos){
				StockOutGoodsInfo stockOutGoodsInfo=new StockOutGoodsInfo();
				parmaMap.put("goodsCode",sInfo.getGoodsCode());
				parmaMap.put("searchWhere"," and locked_num>0");
				List<LocationGoodsBindOut> locationGoodsBindOut= this.getMapper(LocationGoodsBindOutMapper.class).selectByOrderNoCode(parmaMap);
				for(LocationGoodsBindOut l :locationGoodsBindOut){
				    if("-1".equals(l.getLocationNo())){
					l.setLocationNo("缺货");
				    }
					logger.info("LocationNo:"+l.getLocationNo());
				}
				stockOutGoodsInfo.setlGoodsBindOuts(locationGoodsBindOut);
				stockOutGoodsInfo.setGoodsBarCode(sInfo.getGoodsBarCode());
				stockOutGoodsInfo.setGoodsCode(sInfo.getGoodsCode());
				stockOutGoodsInfo.setGoodsName(sInfo.getGoodsName());
				stockOutGoodsInfo.setSpec(sInfo.getSpec());
				stockOutGoodsInfo.setPackageNum(sInfo.getPackageNum());
				stockOutGoodsInfo.setSalesPrice(sInfo.getSalesPrice());
				stockOutGoodsInfo.setUnit(sInfo.getUnit());
				stockOutGoodsInfo.setValidityTime(sInfo.getValidityTime());
			    stockOutGoodsInfo.setStockOutNum(sInfo.getStockOutNum());
				StockOutGoodsList.add(stockOutGoodsInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StockOutGoodsList;
	}

	

}
