package com.rmd.wms.oss.builder;

import com.rmd.wms.oss.OssConstant;

import java.util.UUID;

public class UuidFileNameBuilder implements FileNameBuilder {

	
	public String build(String fileName) {
		String suffix = OssConstant.EMPTY;
		if (fileName.contains(OssConstant.DOT)) {
			suffix = fileName.substring(fileName.lastIndexOf(OssConstant.DOT));
		}
		return UUID.randomUUID().toString() + suffix;
	}
}
