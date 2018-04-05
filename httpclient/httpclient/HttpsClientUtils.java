package com.bdcor.pip.client.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpsClientUtils {
	public static String name = null;
	public static String password = null;
	public static String protocols = null;
	
	public static HttpClient httpClient = null;
	public static HttpClient  getHttpClient() {
		try {
            CookieStore cookieStore = new BasicCookieStore();
            //HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
            RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
            ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
            registryBuilder.register("http", plainSF);  
            try {  
            	//String certName= "C:\\Users\\luodan\\Documents\\Tencent Files\\1074673969\\FileRecv\\ca_public.crt";
            	String path =Thread.currentThread().getContextClassLoader().getResource("").getPath(); 
        		String file = path+"ca/ca_public.crt";
            	InputStream certInput = new BufferedInputStream(new FileInputStream(new File(file)));
            	CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            	final X509Certificate serverCert = (X509Certificate)certificateFactory.generateCertificate(certInput);
                TrustManager[] tm = new TrustManager[]{
                	new X509TrustManager() {
						@Override
						public X509Certificate[] getAcceptedIssuers() {
							return null;
						}
						@Override
						public void checkServerTrusted(X509Certificate[] chain, String authType) {
							
							for(X509Certificate cert:chain){
								try {
									cert.checkValidity();
									cert.verify(serverCert.getPublicKey());
								} catch (CertificateExpiredException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CertificateNotYetValidException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InvalidKeyException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (java.security.cert.CertificateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoSuchAlgorithmException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoSuchProviderException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SignatureException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						
						@Override
						public void checkClientTrusted(X509Certificate[] chain, String authType) {
							
						}
					}
                };
            	
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tm, null);
                //LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
                // 验证数字证书，等于 ALLOW_ALL_HOSTNAME_VERIFIER
				HostnameVerifier hostnameVerifier = new HostnameVerifier(){
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						/*
						HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
						Boolean result = hv.verify("*", arg1);
						return result;
						*/
						return true;
					}
                };
                LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);  
                registryBuilder.register("https", sslSF);  
            } catch (KeyManagementException e) {  
                throw new RuntimeException(e);  
            } catch (NoSuchAlgorithmException e) {  
                throw new RuntimeException(e);  
            }  
            Registry<ConnectionSocketFactory> registry = registryBuilder.build();  
            //设置连接管理器  
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);  
            //构建客户端  
            httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();  
		} catch (Exception e) {
            e.printStackTrace();
        }
		return httpClient;
	}

}

