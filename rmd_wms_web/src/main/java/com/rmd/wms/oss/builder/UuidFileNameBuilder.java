package com.rmd.wms.oss.builder;

import java.util.UUID;

import com.rmd.wms.oss.OssConstant;

public class UuidFileNameBuilder implements FileNameBuilder {

	
	public String build(String fileName) {
		String suffix = OssConstant.EMPTY;
		if (fileName.contains(OssConstant.DOT)) {
			suffix = fileName.substring(fileName.lastIndexOf(OssConstant.DOT));
		}
		return UUID.randomUUID().toString() + suffix;
	}
}
