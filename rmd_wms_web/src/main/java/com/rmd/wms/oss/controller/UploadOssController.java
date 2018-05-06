package com.rmd.wms.oss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rmd.wms.oss.OssService;
import com.rmd.wms.oss.bean.OssFile;


@Controller
@RequestMapping("/base/upload")
public class UploadOssController {

	
	@RequestMapping("/uploadPic")
	@ResponseBody
	public OssFile ossupload(@RequestParam MultipartFile file){
//		String filename1 = request.getParameter("licenceName");
		Map<String, Object> map = new HashMap<String, Object>();
//		String fileData = request.getParameter("fileData");// Base64编码过的图片数据信息，对字节数组字符串进行Base64解码
		OssFile filename= null;
		try {
			OssService ossService =new OssService();
			filename=ossService.Ossupload(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filename;
	}
	/**
	 * 多个文件上传
	 * */
	@RequestMapping(value="/uploadPics",method = RequestMethod.POST)
	@ResponseBody
	public List<OssFile> ossuploads(HttpServletRequest request, HttpServletResponse response,@RequestParam MultipartFile[] files){
		String desc = request.getParameter("description");
		Map<String, Object> map = new HashMap<String, Object>();
//		String fileData = request.getParameter("fileData");// Base64编码过的图片数据信息，对字节数组字符串进行Base64解码
		OssFile filename= null;
		List<OssFile> fileNames=new LinkedList<>();
		try {
			OssService ossService =new OssService();
			for(MultipartFile file:files){
			  filename=ossService.Ossupload(file);
			  fileNames.add(filename);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileNames;
	}
}
