package com.bdcor.pip.client.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdcor.pip.client.core.configInfo.constants.Constants;
import com.bdcor.pip.client.util.httpclient.HttpClientCore;

public class HttpClientUtils2 extends HttpClientCore{

	private static Logger log = LoggerFactory.getLogger(HttpClientUtils2.class);
	boolean isLogin = false;
	
	String loginName = "admin";
	String loginPwd = "111111";
	
	static String loginUrl = "http://192.168.1.105:8080/BDCOR-web/login";
	
	private static int count = Integer.parseInt(Constants.getPropertyValue("retry_ftp_count"));

	
	/**
	 * get方法
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 */
	public static String getRequest(String url, Map params,int count) throws IOException {
		StringBuffer sb = new StringBuffer();
		for (Object key : params.keySet()) {
			String keyStr = key.toString();
			String valueStr = params.get(keyStr).toString();
			sb.append(keyStr + "=" + valueStr + "&");
		}
		sb.deleteCharAt(sb.length() - 1);

		return getRequest(url,sb.toString(),count);
	}
	
	/**
	 * get方法
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 */
	public static String getRequest(String url, String params,int count) throws IOException {
		String returnStr = null;
		int correntCount = 1;
		for(int index=1; index<=correntCount; index++){
			try
			{
				CloseableHttpClient httpclient = HttpClients.createDefault();
				try {
					HttpGet httpget = new HttpGet(url+"?"+params);
					httpget.setHeader(new BasicHeader("Connection","close"));
					ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
						public String handleResponse(final HttpResponse response)
								throws ClientProtocolException, IOException {
							int status = response.getStatusLine().getStatusCode();
							if (status >= 200 && status < 300) {
								HttpEntity entity = response.getEntity();
								return entity != null ? EntityUtils.toString(entity)
										: null;
							} else {
								throw new ClientProtocolException(
										"Unexpected response status: " + status);
							}
						}
					};
					String responseBody = httpclient.execute(httpget, responseHandler);
					return responseBody;
				} finally {
					httpclient.close();
				}
			}catch(Exception e){
				if(correntCount == index){
					throw new IOException("http get 请求失败。");
				}
			}
		}
		return returnStr;
	}

	/**
	 * post 方法
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 */
	public static String postRequest(String url, String params,int count) throws IOException {
		return getRequest(url, params,count);
	}
	
	/**
	 * post 方法
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postRequest(String url, Map params, int count) throws IOException{
		String returnStr = null;
		int correntCount = 1;
		for(int index=1; index<=correntCount; index++){
			try {
				returnStr = HttpClientCore.postRequest(url, params);
				break;
			} catch(Exception e) {
//				ProgressHandle.addProgress(new TransferProgress("http第"+index+"次请求失败:"+e.getMessage(), TransferProgress.STATE_SHOWMESSAGE));
//				DatLogger.logSysCommun(HttpClientUtils.class, ITransferWay.logTypeFlag+""+
//						DatLogger.midlleFlag+"http第"+index+"次请求失败:"+e.getMessage());
				if(correntCount == index){
					throw new IOException("http post 请求失败");
				}
			}
		}
		
		return returnStr;

	}

	
	
}
