package com.rmd.wms.bean.vo.web;

import java.io.Serializable;

import com.rmd.wms.bean.LocationGoodsBindIn;
import com.rmd.wms.bean.Movement;
import com.rmd.wms.bean.MovementInfo;

public class MovementLocation extends Movement implements Serializable{
	
	 /** 
	* @Fields serialVersionUID 
	*/ 
	private static final long serialVersionUID = 1L;

	private MovementInfo movementInfo;
	 
	 private LocationGoodsBindIn locationGoodsBindIn;

	public MovementInfo getMovementInfo() {
		return movementInfo;
	}

	public void setMovementInfo(MovementInfo movementInfo) {
		this.movementInfo = movementInfo;
	}

	public LocationGoodsBindIn getLocationGoodsBindIn() {
		return locationGoodsBindIn;
	}

	public void setLocationGoodsBindIn(LocationGoodsBindIn locationGoodsBindIn) {
		this.locationGoodsBindIn = locationGoodsBindIn;
	}
	 
	 
	 

}
