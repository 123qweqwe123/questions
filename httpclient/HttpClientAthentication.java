package com.bdcor.pip.client.util.httpclient;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdcor.pip.client.core.configInfo.constants.Constants;



public class HttpClientAthentication {
	public CookieStore cookiestore = null;
	private static final Logger log = LoggerFactory.getLogger(HttpClientAthentication.class);
    private static HttpClient httpClient;
    private static HttpClientAthentication instance;  
    private HttpClientAthentication (){}  
    //HttpClientUtils3设置单例
    public static synchronized HttpClientAthentication getInstance() {  
    if (instance == null) {  
        instance = new HttpClientAthentication();  
        try {
			instance.login();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
    }  
    	return instance;  
    }
    //服务关闭后执行关闭HttpClient
    public static void closeHttpClient(){
    	try {
    		if(httpClient!=null){
    			((CloseableHttpClient)httpClient).close();
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
  
	public  void gethttpclict(){
    	httpClient = HttpsClientRegisterHttps.getHttpClient();
    }
	
	/*@Scheduled(fixedRate = 1000 * 60 * 60 * 12)*/
    public boolean keepSessionAlive() throws IOException {
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
        builder.setScheme(HttpsClientRegisterHttps.protocols).setHost(Constants.getPropertyValue("dat_connection_ip",null)).setPath("/bioserver");
        HttpResponse getResponse = null;
        try {
            getResponse = httpClient.execute(new HttpGet(builder.build()));
            if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	 HttpEntity entity=getResponse.getEntity();
                 byte[] responseContent = EntityUtils.toByteArray(entity);
                 String responsebody = new String(responseContent);
            	if(responsebody!=null && responsebody.contains("401")){
            		return false;
            	}
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	try {
        		if(getResponse!=null){
        			getResponse.getEntity().getContent().close();
        		}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return false;
    }
    
    public boolean login() throws IOException {
    	this.gethttpclict();
    	HttpPost post = null;
        HttpResponse response = null;
        try {
        	//CookieStore cookieStore = new BasicCookieStore();
        	//httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
            URIBuilder builder = new URIBuilder();
            builder.setScheme(HttpsClientRegisterHttps.protocols).setHost(Constants.getPropertyValue("dat_connection_ip",null)).setPath("/bioserver/login");
            builder.addParameter("username", HttpsClientRegisterHttps.name);
            builder.addParameter("password", HttpsClientRegisterHttps.password);
            post = new HttpPost(builder.build());
            response = httpClient.execute(post);
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
        }finally {
        	if(response!=null){
            	response.getEntity().getContent().close();
        	}
    		/*post.releaseConnection();
    		post.abort(); */
		}
    }
    
	public String getAndPost(String url,List<NameValuePair> list)  throws Exception{
		if("https".equals(HttpsClientRegisterHttps.protocols)){
			url = url.replace("http", "https");
		} 
    	byte[] responseContent = null;
    	String responsebody = null;
    	HttpPost post = null;
    	HttpResponse response  = null;
    	if(keepSessionAlive()){
    		try{
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
    			
	            URIBuilder builder = new URIBuilder(url);
	            log.debug(url);
	            //builder.setScheme("http").setHost(this.getproperties("hostAndPort")).setPath(url);
	            builder.addParameters(list);
	            post= new HttpPost(builder.build());
	            HttpClientCore.setHttpConfig(post);
	            System.out.println(Thread.currentThread());
	            response = httpClient.execute(post);
	            HttpEntity entity=response.getEntity();
                responseContent = EntityUtils.toByteArray(entity);
                responsebody = new String(responseContent);
        	}catch(Exception e ){
        		log.error("访问资源失败", e);
                e.printStackTrace();
        	}finally{
        		/*post.releaseConnection();
        		post.abort(); */
        		response.getEntity().getContent().close();
        	}
    		
    	}else{
    		throw new Exception("连接数据同步服务器失败");
    	}
    	return responsebody;
    }
	
	
	
	public HttpEntity getLoadFile(String url,List<NameValuePair> list) throws Exception{
    	HttpEntity entity = null;
    	HttpResponse response = null;
    	if("https".equals(HttpsClientRegisterHttps.protocols)){
			url = url.replace("http", "https");
		}
    	if(keepSessionAlive()){
    		HttpPost post = null;
    		try{
	            URIBuilder builder = new URIBuilder(url);
	            post= new HttpPost(builder.build());
	            HttpClientCore.setHttpConfig(post);
	            try{
	            response = httpClient.execute(post);
	            entity=response.getEntity();
	            }catch(Exception e){
	            	System.out.println(e);
	            }
        	}catch(Exception e ){
        		log.error("访问资源失败", e);
                e.printStackTrace();
        	}finally {
        		/*post.releaseConnection();
        		post.abort(); */
        		//response.getEntity().getContent().close();
			}
    	}else{
    		throw new Exception("连接数据同步服务器失败");
    	} 
    	return entity;
    }
	
	public HttpEntity UploadResult(String url,File file) throws Exception{
		HttpEntity entity = null;
		HttpResponse response  = null;
		if("https".equals(HttpsClientRegisterHttps.protocols)){
			url = url.replace("http", "https");
		}
		if(keepSessionAlive()){
		try{ 
			URIBuilder builder = new URIBuilder(url);
			//builder.setScheme("http").setHost(this.getproperties("hostAndPort")).setPath(url);
	         //builder.addParameters(list);
	         HttpPost post= new HttpPost(builder.build());
	         HttpClientCore.setHttpConfig(post);
	         HttpEntity reqEntity = MultipartEntityBuilder.create()
	 				.addBinaryBody("file", file)
	 				.addTextBody("fileName", "test.zip")
	 				.build();
	         post.setEntity(reqEntity);
	         response = httpClient.execute(post);
	         entity=response.getEntity();
		}catch(Exception e){
			log.error("访问资源失败", e);
            e.printStackTrace();
		}finally {
    		//response.getEntity().getContent().close();
		}
		}else{
    		throw new Exception("连接数据同步服务器失败");
    	}
		 return entity;
	}
}
