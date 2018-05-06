package com.rmd.wms.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;


/**
 * 
 * @ClassName: FileIO
 * @Description: 文档输出流
 * @author wangyf
 * 
 */
public class OutputIO {

	/**
	 * 输出文档流
	 * @param file
	 * @param output
	 * @throws IOException
	 */
	public static void write(File file, OutputStream os) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				file));
		try {
			if (file == null) {
				throw new IllegalArgumentException("file == null!");
			}
			if (os == null) {
				throw new IllegalArgumentException("output == null!");
			}
			commonWrite(in, os);
		} finally {
			in.close();
			os.flush();
		}
	}
	/**
	 * 输入流转输出流
	 * @param is
	 * @param os
	 * @throws IOException
	 */
	public static void write(InputStream is, OutputStream os) throws IOException {
		BufferedInputStream in = new BufferedInputStream(is);
		try {
			if (is == null) {
				throw new IllegalArgumentException("is == null!");
			}
			if (os == null) {
				throw new IllegalArgumentException("output == null!");
			}
			commonWrite(in, os);
		} finally {
			is.close();
			in.close();
			os.flush();
		}
	}
	/**
	 * 输出字符串流
	 * @param targetInformation
	 * @param os
	 * @throws IOException
	 */
	public static void write(String targetInformation, OutputStream os)
			throws IOException {
		if (targetInformation == null) {
			throw new IllegalArgumentException("targetInformation == null!");
		}
		if (os == null) {
			throw new IllegalArgumentException("os == null!");
		}
		byte[] bs = targetInformation.getBytes("UTF-8");
		os.write(bs);
		os.flush();
	}
	/**
	 * 输出字符串图片
	 * @param targetInformation
	 * @param imageType
	 * @param width
	 * @param height
	 * @param os
	 * @throws IOException
	 */
	public static void write(String targetInformation,String imageType,int width,int height, OutputStream os)
			throws IOException {
		if (targetInformation == null) {
			throw new IllegalArgumentException("targetInformation == null!");
		}
		if (os == null) {
			throw new IllegalArgumentException("os == null!");
		}
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.getGraphics();
		g.setColor(Color.gray);// 背景颜色要偏淡
		g.fillRect(0, 0, width, height);// 画背景
		g.setColor(Color.red);// 边框颜色
		g.drawRect(0, 0, width - 1, height - 1);// 画边框
		Font font = new Font("Times New Roman", Font.ITALIC, width/targetInformation.length()); // 创建字体，字体的大小应该根据图片的高度来定。
		g.setFont(font);// 设置字体
		g.setColor(Color.blue);
		g.drawString(targetInformation,width/3, height/2);
		g.dispose();// 图像生效
		write(image, imageType,os);
	}
	/**
	 * 输出图片
	 * @param image
	 * @param imageType gif,jpeg,png
	 * @param os
	 * @throws IOException
	 */
	public static void write(RenderedImage image, String imageType,OutputStream os)
			throws IOException {
		if (image == null) {
			throw new IllegalArgumentException("image == null!");
		}
		if (os == null) {
			throw new IllegalArgumentException("os == null!");
		}
		ImageIO.write(image, imageType, os);
		os.flush();
	}
	
	public static void commonWrite(InputStream is, OutputStream os) throws IOException{
		int size = 0;
		byte[] temp = new byte[1024];
		while ((size = is.read(temp)) != -1) {
			os.write(temp, 0, size);
		}
	}
}
