package com.rmd.wms.bean.vo.web;

import java.util.List;

import com.rmd.wms.bean.CheckInfo;

public class CheckInfoPrint {

	private String checkNo;
	
	private List<CheckInfo> checkInfos;
	
	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public List<CheckInfo> getCheckInfos() {
		return checkInfos;
	}

	public void setCheckInfos(List<CheckInfo> checkInfos) {
		this.checkInfos = checkInfos;
	}
}
