package com.rmd.wms.web.util;

import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * web输出流	下载功能实现
 * @author wangyf
 *
 */
public class WebDownload {
	/**
	 * 通过字符串生成文件
	 * 
	 * @param response
	 * @param expandedName
	 * @param targetInformation
	 * @throws IOException
	 */
	public void export(HttpServletRequest request,HttpServletResponse response, String expandedName,
			String targetInformation) throws IOException {
		ServletOutputStream sos = configure(request,response, expandedName);
		OutputIO.write(targetInformation, sos);
		sos.close();
	}

	/**
	 * 通过字符串生成图片
	 * 
	 * @param response
	 * @param expandedName
	 * @param width
	 * @param height
	 * @param targetInformation
	 * @throws IOException
	 */
	public void export(HttpServletRequest request,HttpServletResponse response, String expandedName,
			int width, int height, String targetInformation) throws IOException {
		ServletOutputStream sos = configure(request,response, expandedName);
		String type = expandedName.substring(expandedName.indexOf(".") + 1);
		OutputIO.write(targetInformation, type, width, height, sos);
		sos.close();
	}

	/**
	 * 导出文件
	 * 
	 * @param response
	 * @param file
	 * @throws IOException
	 */
	public void export(HttpServletRequest request,HttpServletResponse response, String expandedName,File file)
			throws IOException {
		ServletOutputStream sos = configure(request,response, expandedName);
		OutputIO.write(file, sos);
		sos.close();
	}

	public void export(HttpServletRequest request,HttpServletResponse response, String expandedName,
			InputStream is) throws IOException {
		ServletOutputStream sos = configure(request,response, expandedName);
		OutputIO.write(is, sos);
		sos.close();
	}

	/**
	 * 程序内部生成图片，并导出
	 * 
	 * @param response
	 * @param expandedName
	 * @param image
	 * @throws IOException
	 */
	public void export(HttpServletRequest request,HttpServletResponse response, String expandedName,
			RenderedImage image) throws IOException {
		ServletOutputStream sos = configure(request,response, expandedName);
		String type = expandedName.substring(expandedName.indexOf(".") + 1);
		OutputIO.write(image, type, sos);
		sos.close();
	}
	public void exportZipEx(HttpServletRequest request,HttpServletResponse response,String expandedName,Map<String,InputStream> isMap) throws IOException{
		ZipOutputStream zos = new ZipOutputStream(configure(request,response, expandedName));
		Set<String> iss = isMap.keySet();
		for(String name:iss){
			zos.putNextEntry(new ZipEntry(name));
			InputStream bis = isMap.get(name);
			OutputIO.write(bis,zos);
			bis.close();
		}
		zos.close();
	}
	public void exportZipEx2(HttpServletRequest request,HttpServletResponse response,String expandedName,Map<String,XWPFDocument> isMap) throws IOException{
		ZipOutputStream zos = new ZipOutputStream(configure(request,response, expandedName));
		Set<String> iss = isMap.keySet();
		for(String name:iss){
			zos.putNextEntry(new ZipEntry(name));
			XWPFDocument xdoc = isMap.get(name);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xdoc.write(out);
			byte[] byteArray = out.toByteArray();
			out.close();
			zos.write(byteArray);
		}
		zos.close();
	}
	/**
	 * 
	 * @param response
	 * @param expandedName	文件导出名称
	 * @param files	创建压缩文件
	 * @throws IOException
	 */
	public void exportZip(HttpServletRequest request,HttpServletResponse response,String expandedName,List<File> files) throws IOException{
		boolean isZipEx = expandedName.toUpperCase().trim().endsWith(".ZIP");
		if (!isZipEx)expandedName += ".zip";
		exportZip(configure(request,response, expandedName), files);
		
	}
	/**
	 * 创建ZIP
	 * @param os
	 * @param files
	 * @throws IOException
	 */
	public void exportZip(OutputStream os,List<File> files) throws IOException{
		ZipOutputStream zos = new ZipOutputStream(os);
		for(File sourcefile:files){
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
					sourcefile));
			zos.putNextEntry(new ZipEntry(sourcefile.getName()));
			OutputIO.write(bis,zos);
			bis.close();
		}
		zos.close();
	}
	/**
	 * 下载时response设置
	 * 
	 * @param response
	 * @param expandedName
	 * @return
	 * @throws IOException
	 */
	public ServletOutputStream configure(HttpServletRequest request,HttpServletResponse response,
			String expandedName) throws IOException {
		response.reset();
		String agent = request.getHeader("USER-AGENT");
		if(agent!=null&&agent.indexOf("MSIE")==-1){
			//FF
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(expandedName.getBytes("UTF-8"),"ISO8859-1"));
		}else{
			response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(expandedName,"UTF-8"));
		}
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Connection","close");
		response.setDateHeader("Expires", 0);
		response.setContentType("application/octet-stream");
//		response.setContentType("application/x-download");
//		response.setContentLength(len);
//		response.setBufferSize(size);
//		response.setCharacterEncoding(charset);
//		response.setIntHeader(name, value);
//		response.setLocale(loc);
//		response.setStatus(sc);
		return response.getOutputStream();
	}
}
