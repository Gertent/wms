package com.rmd.wms.auth.client;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * httpclient 工具类
 * 
 */
public class HttpClientUtil {
	public static final int SOCKET_TIMEOUT = 180000;
	public static final int CONNECT_TIMEOUT = 10000;
	public static final int CONNECTION_REQUEST_TIMEOUT = 60000;

	/**
	 * 默认的HTTP响应实体编码 = "UTF-8"
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";

	private HttpClientUtil() {
	}

	private static CloseableHttpClient httpClient = null;

	private static PoolingHttpClientConnectionManager connManager = null;

	static {
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.build();
			connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
			connManager.setDefaultSocketConfig(socketConfig);
			MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
					.setMaxLineLength(2000).build();
			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
					.setMessageConstraints(messageConstraints).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(200);
			connManager.setDefaultMaxPerRoute(200);
			httpClient = HttpClients.custom().setConnectionManager(connManager).build();
		
	}

	/**
	 * HTTP Get
	 * <p/>
	 * 响应内容实体采用<code>UTF-8</code>字符集
	 *
	 * @param url
	 *            请求url
	 * @return 响应内容实体
	 */
	public static String get(String url) {
		return get(url, DEFAULT_CHARSET);
	}

	/**
	 * 获取请求返回byte数组
	 *
	 * @param url
	 *            请求url
	 * @return 响应内容实体
	 */
	public static byte[] get2Bytes(String url) {
		HttpGet getMethod = null;
		try {
			getMethod = new HttpGet(url);
			HttpResponse response = httpClient.execute(getMethod);
			return consumeResponseEntity(response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}
	}

	/**
	 * HTTP Get
	 *
	 * @param url
	 *            请求url
	 * @param responseCharset
	 *            响应内容字符集
	 * @return 响应内容实体
	 */
	public static String get(String url, String responseCharset) {
		HttpGet getMethod = null;
		try {
			getMethod = new HttpGet(url);
			HttpResponse response = httpClient.execute(getMethod);
			return consumeResponseEntity(response, responseCharset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// <<Post>>

	/**
	 * HTTP Post
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @return 响应内容实体
	 */
	public static String post(String url, Map<String, String> params) {
		return post(url, params, DEFAULT_CHARSET, DEFAULT_CHARSET);
	}


	/**
	 * HTTP Post XML（使用默认字符集）
	 *
	 * @param url
	 *            请求的URL
	 * @param xml
	 *            XML格式请求内容
	 * @return 响应内容实体
	 */
	public static String postXml(String url, String xml) {
		return postXml(url, xml, DEFAULT_CHARSET, DEFAULT_CHARSET);
	}

	/**
	 * HTTP Post XML
	 *
	 * @param url
	 *            请求的URL
	 * @param xml
	 *            XML格式请求内容
	 * @param requestCharset
	 *            请求内容字符集
	 * @param responseCharset
	 *            响应内容字符集
	 * @return 响应内容实体
	 */
	public static String postXml(String url, String xml, String requestCharset, String responseCharset) {
		return post(url, xml, "text/xml; charset=" + requestCharset, "text/xml", requestCharset, responseCharset);
	}

	/**
	 * HTTP Post JSON（使用默认字符集）
	 *
	 * @param url
	 *            请求的URL
	 * @param json
	 *            JSON格式请求内容
	 * @return 响应内容实体
	 */
	public static String postJson(String url, String json) {
		return postJson(url, json, DEFAULT_CHARSET, DEFAULT_CHARSET);
	}

	/**
	 * HTTP Post JSON
	 *
	 * @param url
	 *            请求的URL
	 * @param json
	 *            JSON格式请求内容
	 * @param requestCharset
	 *            请求内容字符集
	 * @param responseCharset
	 *            响应内容字符集
	 * @return 响应内容实体
	 */
	public static String postJson(String url, String json, String requestCharset, String responseCharset) {
		return post(url, json, "application/json; charset=" + requestCharset, "application/json", requestCharset,
				responseCharset);
	}

	/**
	 * HTTP Post
	 *
	 * @param url
	 *            请求URL
	 * @param params
	 *            请求参数集合
	 * @param paramEncoding
	 *            请求参数编码
	 * @param responseCharset
	 *            响应内容字符集
	 * @return 响应内容实体
	 */
	public static String post(String url, Map<String, String> params, String paramEncoding, String responseCharset) {
		HttpPost post = null;
		try {
			post = new HttpPost(url);
			if (params != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (String key : params.keySet()) {
					paramList.add(new BasicNameValuePair(key, params.get(key)));
				}
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, paramEncoding);
				post.setEntity(formEntity);
			}

			// 设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).build();
			post.setConfig(requestConfig);

			// 执行请求
			HttpResponse response = httpClient.execute(post);
			return consumeResponseEntity(response, responseCharset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
	}

	/**
	 * HTTP Post
	 *
	 * @param url
	 *            请求的URL
	 * @param content
	 *            请求内容
	 * @param contentType
	 *            请求内容类型，HTTP Header中的<code>Content-type</code>
	 * @param mimeType
	 *            请求内容MIME类型
	 * @param requestCharset
	 *            请求内容字符集
	 * @param responseCharset
	 *            响应内容字符集
	 * @return 响应内容实体
	 */
	public static String post(String url, String content, String contentType, String mimeType, String requestCharset,
			String responseCharset) {
		HttpPost post = null;
		try {
			post = new HttpPost(url);
			post.setHeader("Content-Type", contentType);
			HttpEntity requestEntity = new StringEntity(content, ContentType.create(mimeType, requestCharset));
			post.setEntity(requestEntity);

			// 设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).build();
			post.setConfig(requestConfig);
			HttpResponse response = httpClient.execute(post);
			return consumeResponseEntity(response, responseCharset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
	}

	/**
	 * 安全的消耗（获取）响应内容实体
	 * <p/>
	 * 使用 {@link EntityUtils} 将响应内容实体转换为字符串，同时关闭输入流
	 * <p/>
	 *
	 * @param response
	 *            HttpResponse
	 * @param responseCharset
	 *            响应内容字符集
	 * @return 响应内容实体
	 * @throws IOException
	 *             IOException
	 */
	private static String consumeResponseEntity(HttpResponse response, String responseCharset) throws IOException {
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity responseEntity = response.getEntity();
			String responseBody = EntityUtils.toString(responseEntity, responseCharset);
			return responseBody;
		} else {
			return null;
		}
	}

	/**
	 * 安全的消耗（获取）响应内容实体
	 * <p/>
	 * 使用 {@link EntityUtils} 将响应内容实体转换为字符串，同时关闭输入流
	 * <p/>
	 *
	 * @param response
	 *            HttpResponse
	 * @return 响应内容实体
	 * @throws IOException
	 */
	private static byte[] consumeResponseEntity(HttpResponse response) throws IOException {
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity responseEntity = response.getEntity();
			return EntityUtils.toByteArray(responseEntity);
		} else {
			return null;
		}
	}

}
