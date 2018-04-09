package com.bdcor.pip.client.util.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdcor.pip.client.core.configInfo.constants.Constants;
import com.bdcor.pip.client.util.DatLogger;
import com.bdcor.pip.client.util.IProgress;

public class HttpClientCore {

	private static Logger log = LoggerFactory.getLogger(HttpClientCore.class);
	boolean isLogin = false;
	
	String loginName = "admin";
	String loginPwd = "111111";
	
	static String loginUrl = "http://192.168.1.105:8080/BDCOR-web/login";
	
	
	public void login(){
		
	}
	

	/**
	 * get方法,下载文件
	 * @param url
	 * @param params
	 * @return
	 */
	protected static boolean getLoadFile(String url, String params, final String path,final String name,IProgress progress) throws Exception{
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		boolean returnV = true;
		url = url + "?" + params;
		InputStream instream = null;
		OutputStream out  = null;
		HttpClientAthentication jetHttpClientUtils = HttpClientAthentication.getInstance();
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpEntity entity = jetHttpClientUtils.getLoadFile(url, nvps);
			//HttpGet httpget = new HttpGet(url);
			//setHttpConfig(httpget);
			try {
				//HttpEntity entity = httpclient.execute(httpget).getEntity();
				
				File wdFile = new File(path + File.separator+ name);  
	            //文件已存在  
	            if(!wdFile.exists()){  
	                wdFile.createNewFile();
	            }  
	            out = new FileOutputStream(wdFile);  
	            if (entity != null) {  
	                instream = entity.getContent();  
	                long contentLength = entity.getContentLength();
	                
	                long loadLength = 0;
	                int l;  
	                byte[] tmp = new byte[2048];  
	                while ((l = instream.read(tmp)) != -1) {  
	                    out.write(tmp, 0, l);
	                    loadLength += l;
	                    if(progress!=null){
	                    	int percent = (int)((loadLength*100)/contentLength);
	                    	progress.setPercentage(percent);
	                    }
	                }  
	            }  
	            if(out!=null){  
	                try {  
	                    out.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                    
	                    DatLogger.loggError(e);
	    				throw new RuntimeException("文件下载发生错误", e);
	                }  
	            }
			} catch (ClientProtocolException e) {
				returnV = false;
				DatLogger.loggError(e);
				throw new RuntimeException("文件下载发生错误", e);
			} catch (IOException e1) {
				returnV = false;
				DatLogger.loggError(e1);
				System.out.println(e1);
				throw new RuntimeException("文件下载发生错误", e1);
			}
			return returnV;
		} finally {
			instream.close();
			out.close();
			/*try {
				//httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		
		}
	}
	/**
	 * get方法
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 */
	protected static String getRequest(String url, Map params) throws IOException {
		StringBuffer sb = new StringBuffer();
		for (Object key : params.keySet()) {
			String keyStr = key.toString();
			String valueStr = params.get(keyStr).toString();
			sb.append(keyStr + "=" + valueStr + "&");
		}
		sb.deleteCharAt(sb.length() - 1);

		return getRequest(url,sb.toString());
	}
	
	/**
	 * get方法
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 */
	protected static String getRequest(String url, String params) throws IOException {
		String responseBody = null;
		url = url + "?" + params;
		log.debug("url:"+url);
		try{
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//url = url + "?" + params;
		HttpClientAthentication jetHttpClientUtils = HttpClientAthentication.getInstance();
		responseBody = jetHttpClientUtils.getAndPost(url, nvps);
		}catch(Exception e){
			DatLogger.loggError(e);
			throw new IOException("http连接错误"+e.getMessage(),e);
		}
			return responseBody;
		/*CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);
			
			httpget.setHeader(new BasicHeader("Connection","close"));
			
			setHttpConfig(httpget);
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
			
			try {
				responseBody = httpclient.execute(httpget, responseHandler);
			} catch (ClientProtocolException e) {
				DatLogger.loggError(e);
				throw new IOException("http连接错误"+e.getMessage(),e);
			}
			log.debug("url load end");
			return responseBody;
		} finally {
			httpclient.close();
		}*/
	}
	
	/**
	 * post 方法
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 */
	public static String postRequest(String url, Map params) throws IOException {
		String responseBody = null;
		try{
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Object key : params.keySet()) {
			String keyStr = key.toString();
			String valueStr = params.get(keyStr).toString();
			nvps.add(new BasicNameValuePair(keyStr, valueStr));
		}
		HttpClientAthentication jetHttpClientUtils = HttpClientAthentication.getInstance();
		responseBody = jetHttpClientUtils.getAndPost(url, nvps);
		}catch (Exception e){
			DatLogger.loggError(e);
			throw new IOException("http连接错误"+e.getMessage(),e);
		}
		return responseBody;
		/*CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			setHttpConfig(httpPost);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Object key : params.keySet()) {
				String keyStr = key.toString();
				String valueStr = params.get(keyStr).toString();

				
				
				nvps.add(new BasicNameValuePair(keyStr, valueStr));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			}

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
			responseBody = httpclient.execute(httpPost, responseHandler);
		} catch (ClientProtocolException e) {
			DatLogger.loggError(e);
			throw new RuntimeException("ftp连接错误"+e.getMessage(),e);
		} catch (IOException e) {
			DatLogger.loggError(e);
			throw new RuntimeException("ftp连接错误"+e.getMessage(),e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {DatLogger.loggError(e);
				e.printStackTrace();
			}
		}
		return responseBody;*/
	}


	/**
	 * 设置连接参数
	 * @param httpRequest
	 */
	public static void setHttpConfig(HttpRequestBase httpRequest){
		long timeout = Long.parseLong(Constants.getPropertyValue("dat_http_timeout", "60000"));
		log.debug("time out:"+timeout); 
		RequestConfig config = RequestConfig.custom()
		        .setSocketTimeout(120 * 1000)// 等待数据超时时间
		        .setConnectTimeout((int)timeout) // 请求超时时间
		        .setConnectionRequestTimeout(500)  // 连接超时时间
		        .build();
		httpRequest.setConfig(config);
		
	}
	
	/**
	 * 是否可联网
	 * @return
	 */
	@Deprecated
	public static boolean isInInternet2(){
		boolean isInInternet = true;
		try{
			
			URL url = new URL("http://"+Constants.getPropertyValue("time_server","123.127.249.125"));
//			url=new URL("http://123.124.148.47");//取得资源对象
		    URLConnection uc=url.openConnection();//生成连接对象
		    int timeout = Integer.parseInt(Constants.getPropertyValue("time_server_timeout","2000"));
		    uc.setConnectTimeout(timeout);
		    uc.connect(); //发出连接
		}catch(Exception e){
			DatLogger.loggError(e);
			isInInternet = false;
			DatLogger.logSysCommun(HttpClientCore.class, "是否可联网，发生错误");
		}
		
		//强制使用联网模式
		boolean isOnlineByConfig = Boolean.parseBoolean(Constants.getPropertyValue("is_online_by_user","false"));
		if(isOnlineByConfig){
			isInInternet = true;
		}
		
		return isInInternet;
	}
	
	
	/**
	 * 是否可联网
	 * @return
	 */
	public static boolean isInInternet(String ipAndPort){
		boolean isInInternet = true;
		try{
			
			URL url = new URL("http://"+ipAndPort);
		    URLConnection uc=url.openConnection();//生成连接对象
		    int timeout = Integer.parseInt(Constants.getPropertyValue("time_server_timeout","2000"));
		    uc.setConnectTimeout(timeout);
		    uc.connect(); //发出连接
		}catch(Exception e){
			DatLogger.loggError(e);
			isInInternet = false;
			DatLogger.logSysCommun(HttpClientCore.class, "是否可联网，发生错误");
		}finally{
			
		}
		
		return isInInternet;
	}
	
	
	/**
	 * 获取http协议头中的时间
	 * @param url
	 * @return
	 */
	public static Date getServerTimeByProtocol(URL url){
		
		
		Date date = null;
		try {
			URLConnection uc=url.openConnection();//生成连接对象
			uc.setConnectTimeout(2000);
		    uc.connect(); //发出连接
		    long ld=uc.getDate(); //取得网站日期时间
		    date=new Date(ld); //转换为标准时间对象
		} catch (Exception e) {
			DatLogger.loggError(e);
		}//取得资源对象
	    return date;
	}
	
	/**
	 * 文件上传
	 * @param url
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	public static UploadResult uploadFile(String url,File file) throws Exception{
		UploadResult result = new UploadResult();
		HttpEntity respEntity  = null;
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			setHttpConfig(httpPost);
			
			//FileBody bin = new FileBody(file);
			//StringBody sb = new StringBody("fileName", ContentType.TEXT_PLAIN);
			
			/*HttpEntity reqEntity = MultipartEntityBuilder.create()
					.addBinaryBody("file", file)
					.addTextBody("fileName", "test.zip")
					.build();
			
			httpPost.setEntity(reqEntity);
			
			 
			CloseableHttpResponse response = httpclient.execute(httpPost);*/
			
			
				respEntity = HttpClientAthentication.getInstance().UploadResult(url, file);
				//DatLogger.logSysCommun(HttpClientUtils2.class, "upload file status:"+response.getStatusLine());
				//HttpEntity respEntity = response.getEntity();
				if(respEntity!=null){
					System.out.println("Response content length: " + respEntity.getContentLength());
				}
				String resultStr = EntityUtils.toString(respEntity);
				DatLogger.logSysCommun(HttpClientCore.class, "upload file result:"+resultStr);
				ObjectMapper objMap = new ObjectMapper();
				result = objMap.readValue(resultStr, UploadResult.class);
		} catch (ClientProtocolException e) {
			DatLogger.loggError(e);
			throw new RuntimeException("上传文件发送错误"+e.getMessage(),e);
		} catch (IOException e) {
			DatLogger.loggError(e);
			throw new RuntimeException("上传文件发送错误"+e.getMessage(),e);
		} finally {
			respEntity.getContent().close();
		}
		return result;
	}
	
	public static class UploadResult{
		public Boolean suc = false;
		public String msg = "";
	}
}
