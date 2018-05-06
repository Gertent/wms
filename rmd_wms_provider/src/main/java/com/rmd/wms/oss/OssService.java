package com.rmd.wms.oss;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.rmd.wms.oss.bean.OssFile;
import com.rmd.wms.oss.builder.FileNameBuilder;
import com.rmd.wms.oss.builder.UuidFileNameBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OssService {
	
	private static final Logger LOG = LoggerFactory.getLogger(OssService.class);

	private String bucket;
	private String traget;  //上传目录
	private String host;    //上传主机地址
	//图片命名规则
	private FileNameBuilder fileNameBuilder=new UuidFileNameBuilder();
	private OSSClient ossClient ;

	/**
	 * 上传方法，返回上传图片路径
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public OssFile Ossupload(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		return ossUpload(file.getName(),in, createDefaultObjectMetadata(file));
	}
	private OssFile ossUpload(String fileName, InputStream inputStream, ObjectMetadata metadata) {
		OssFile result = null;
		try {
			ossClient= initOSSClient();
			//oss上传路径设定规则：对应的目录+文件名=新的上传文件名 例如：development/saharabuy/pccn/123.jpg
			String newfileName=traget+fileName;
			//图片上传路径
			PutObjectResult putObjectResult = ossClient.putObject(bucket, newfileName, inputStream, metadata);
			result = new OssFile(putObjectResult.getETag());
			result.setUrl(host + OssConstant.SLASH +newfileName);
			result.setSize(metadata.getContentLength());
			result.setContentType(metadata.getContentType());
			result.setName(fileName);
		} catch (OSSException e) {
			result = new OssFile(e.getErrorCode(), e.getMessage());
			LOG.debug("OSSException"+result.getErrorMessage()+result.getErrorCode());
			e.printStackTrace();
		} catch (ClientException e) {
			result = new OssFile(null, e.getMessage());
			LOG.debug("ClientException "+result.getErrorMessage());
			e.printStackTrace();
		}
		LOG.info(result.getUrl());
		return result;
	}
	
	/**
	 * 初始化OSSClient
	 * @return
	 */
	private OSSClient initOSSClient(){
		 Map<String, String> proMap=initProperties();
		 OSSClient  ossClient= new OSSClient(proMap.get("endpoint"), proMap.get("accessId"), proMap.get("accessKey"));
		 return ossClient;
	}
	/**
	 * 读取对应的properties文件,保存到MAP中
	 * @return
	 */
	private Map<String, String> initProperties(){
		InputStream is = getClass().getResourceAsStream("/oss.properties");
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (Exception e) {
			LOG.debug("不能读取属性文件. " + "请确保db.properties在CLASSPATH指定的路径中");
		}
        Map<String, String> propertiesMap=new  HashMap<String, String>();
		String id = properties.getProperty("ACCESSID").trim();
		String key = properties.getProperty("ACCESSKEY").trim();
		String hostName = properties.getProperty("HOST").trim();
		String endpoint = properties.getProperty("ENDPOINT").trim();
		String bucketName = properties.getProperty("BUCKET").trim();
		String tragetName=properties.getProperty("TARGET").trim();
		bucket=bucketName;
		traget=tragetName;
		host=hostName;
		propertiesMap.put("accessId", id);
		propertiesMap.put("accessKey", key);
		propertiesMap.put("host", host);
		propertiesMap.put("endpoint", endpoint);
		propertiesMap.put("bucketName", bucketName);
		propertiesMap.put("traget",tragetName);
		propertiesMap.put("host",hostName);
		return propertiesMap;
	}

	private ObjectMetadata createDefaultObjectMetadata(File file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.length());
		metadata.setContentType(new MimetypesFileTypeMap().getContentType(file));
		metadata.setCacheControl("no-cache");
		metadata.setHeader("Pragma", "no-cache");
		metadata.setContentEncoding("utf-8");
		return metadata;
	}







	
}
