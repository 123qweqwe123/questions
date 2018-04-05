package com.bdcor.pip.client.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdcor.pip.client.core.configInfo.constants.Constants;


public class HttpClientUtils3 {
	private static final Logger log = LoggerFactory.getLogger(HttpClientUtils3.class);
    private static HttpClient httpClient;
    private static HttpClientUtils3 instance;  
    private HttpClientUtils3 (){}  
    //HttpClientUtils3设置单例
    public static synchronized HttpClientUtils3 getInstance() {  
    if (instance == null) {  
        instance = new HttpClientUtils3();  
        instance.login();
    }  
    return instance;  
    }
    //服务关闭后执行关闭HttpClient
    public static void closeHttpClient(){
    	try {
    		if(httpClient!=null){
			((InputStream) httpClient).close();
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
  
	public  void gethttpclict(){
    	httpClient = HttpsClientUtils.getHttpClient();
    }
	
	/*@Scheduled(fixedRate = 1000 * 60 * 60 * 12)*/
    public boolean keepSessionAlive() {
        // 是否启动数据同步服务
        /*boolean startSync = Boolean.parseBoolean(env.getProperty("third-service.data-sync.active", "true"));
        if (!startSync) {
            return true;
        }*/
        if (httpClient == null) {
            return this.login();
        }
        if (!isSessionAlive()) {
            return this.login();
        }
        return true;
    }

    public boolean isSessionAlive() {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(HttpsClientUtils.protocols).setHost(Constants.getPropertyValue("dat_connection_ip",null));
        try {
            HttpResponse getResponse = httpClient.execute(new HttpGet(builder.build()));
            if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean login() {
    	this.gethttpclict();
        try {
            CookieStore cookieStore = new BasicCookieStore();
            //HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
            URIBuilder builder = new URIBuilder();
            builder.setScheme(HttpsClientUtils.protocols).setHost(Constants.getPropertyValue("dat_connection_ip",null)).setPath("/datserver/login");
            builder.addParameter("username", HttpsClientUtils.name);
            builder.addParameter("password", HttpsClientUtils.password);
            HttpPost post= new HttpPost(builder.build());
            HttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                //this.httpClient = httpClient;
                log.info("数据同步服务器连接成功！");
                return true;
            } else {
            	log.error("连接数据同步服务器失败，{}", statusCode);
                return false;
            }
        } catch (Exception e) {
        	log.error("连接数据同步服务器失败", e);
            e.printStackTrace();
            return false;
        }
    }
    
	public String getAndPost(String url,List<NameValuePair> list) throws Exception{
		if("https".equals(HttpsClientUtils.protocols)){
			url = url.replace("http", "https");
		} 
    	byte[] responseContent = null;
    	String responsebody = null;
    	if(keepSessionAlive()){
    		try{
	            URIBuilder builder = new URIBuilder(url);
	            //builder.setScheme("http").setHost(this.getproperties("hostAndPort")).setPath(url);
	            builder.addParameters(list);
	            HttpGet post= new HttpGet(builder.build());
	            HttpClientUtils2.setHttpConfig(post);
	            HttpResponse response = httpClient.execute(post);
	            HttpEntity entity=response.getEntity();
                responseContent = EntityUtils.toByteArray(entity);
                responsebody = new String(responseContent);
        	}catch(Exception e ){
        		log.error("访问资源失败", e);
                e.printStackTrace();
        	}
    	}else{
    		throw new Exception("连接数据同步服务器失败");
    	}
    	return responsebody;
    }
	
	public HttpEntity getLoadFile(String url,List<NameValuePair> list) throws Exception{
    	HttpEntity entity = null;
    	if("https".equals(HttpsClientUtils.protocols)){
			url = url.replace("http", "https");
		}
    	if(keepSessionAlive()){
    		try{
	            URIBuilder builder = new URIBuilder(url);
	            //builder.setScheme("http").setHost(this.getproperties("hostAndPort")).setPath(url);
	            builder.addParameters(list);
	            HttpPost post= new HttpPost(builder.build());
	            HttpClientUtils2.setHttpConfig(post);
	            HttpResponse response = httpClient.execute(post);
	            entity=response.getEntity();
        	}catch(Exception e ){
        		log.error("访问资源失败", e);
                e.printStackTrace();
        	}
    	}else{
    		throw new Exception("连接数据同步服务器失败");
    	}
    	return entity;
    }
	
	public HttpEntity UploadResult(String url,File file) throws Exception{
		HttpEntity entity = null;
		if("https".equals(HttpsClientUtils.protocols)){
			url = url.replace("http", "https");
		}
		if(keepSessionAlive()){
		try{ 
			URIBuilder builder = new URIBuilder(url);
			//builder.setScheme("http").setHost(this.getproperties("hostAndPort")).setPath(url);
	         //builder.addParameters(list);
	         HttpPost post= new HttpPost(builder.build());
	         HttpClientUtils2.setHttpConfig(post);
	         HttpEntity reqEntity = MultipartEntityBuilder.create()
	 				.addBinaryBody("file", file)
	 				.addTextBody("fileName", "test.zip")
	 				.build();
	         post.setEntity(reqEntity);
	         HttpResponse response = httpClient.execute(post);
	         entity=response.getEntity();
		}catch(Exception e){
			log.error("访问资源失败", e);
            e.printStackTrace();
		}
		}else{
    		throw new Exception("连接数据同步服务器失败");
    	}
		 return entity;
	}
}
