package com.rmd.wms.bean.vo.web;

import java.io.Serializable;
import java.util.List;

import com.rmd.wms.bean.LocationGoodsBindOut;
import com.rmd.wms.bean.StockOutInfo;

/**
 * 
* @ClassName: StockOutGoodsInfo 
* @Description: TODO(出库单详情商品货位绑定) 
* @author ZXLEI
* @date Feb 23, 2017 1:28:26 PM 
*
 */
public class StockOutGoodsInfo extends StockOutInfo implements Serializable {
	
	private List<LocationGoodsBindOut> lGoodsBindOuts;

	public List<LocationGoodsBindOut> getlGoodsBindOuts() {
		return lGoodsBindOuts;
	}

	public void setlGoodsBindOuts(List<LocationGoodsBindOut> lGoodsBindOuts) {
		this.lGoodsBindOuts = lGoodsBindOuts;
	}


}
