package com.bdcor.pip.client.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdcor.pip.client.core.configInfo.constants.Constants;
import com.bdcor.pip.client.core.dataTransfer.assist.progress.ProgressHandle;
import com.bdcor.pip.client.core.dataTransfer.assist.progress.TransferProgress;
import com.bdcor.pip.client.core.dataTransfer.assist.progress.TransferProgressNormal;
import com.bdcor.pip.client.core.dataTransfer.logic.ITransfer;
import com.bdcor.pip.client.core.dataTransfer.manner.ITransferWay;
import com.bdcor.pip.client.util.httpclient.HttpClientCore;

public class HttpClientUtils extends HttpClientCore{

	private static Logger log = LoggerFactory.getLogger(HttpClientUtils.class);
	boolean isLogin = false;
	
	String loginName = "admin";
	String loginPwd = "111111";
	
	static String loginUrl = "http://192.168.1.105:8080/BDCOR-web/login";
	
	private static int count = Integer.parseInt(Constants.getPropertyValue("retry_ftp_count"));

	/**
	 * get方法,下载文件
	 * @param url
	 * @param params
	 * @return
	 */
	public static boolean getLoadFile(String url,
			Map params,
			final String path,
			final String name,
			IProgress progress,
			int count)throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("1=1&");
		if(params!=null){
			for (Object key : params.entrySet()) {
				String keyStr = key.toString();
				String valueStr = params.get(keyStr).toString();
				sb.append(keyStr + "=" + valueStr + "&");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return getLoadFile(url, sb.toString(),path,name,progress,count);
	}
	
	/**
	 * get方法,下载文件
	 * @param url
	 * @param params
	 * @return
	 */
	public static boolean getLoadFile(String url, 
			String params, 
			final String path,
			final String name,
			IProgress progress,
			int count) throws IOException {
		boolean returnValue = false;
		int correntCount = getCount(count);
		int index=1;
		for(; index<=correntCount; index++){
			try
			{
				HttpClientCore.getLoadFile(url, params, path, name, progress);
				returnValue = true;
				break;
			}catch(Exception e){
//				ProgressHandle.addProgress(new TransferProgress("http 第"+index+"次下载文件失败:"+e.getMessage(), TransferProgress.STATE_SHOWMESSAGE));
//				DatLogger.logSysCommun(HttpClientUtils.class, ITransferWay.logTypeFlag+""+
//						DatLogger.midlleFlag+"http第"+index+"次下载文件失败:"+e.getMessage());
				if(correntCount == index){
					throw new IOException("下载文件失败");
				}
			}
		}
		
		return returnValue;
		
	}
	
	
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
		int correntCount = getCount(count);
		for(int index=1; index<=correntCount; index++){
			try
			{
				returnStr = HttpClientCore.getRequest(url, params);
				break;
			}catch(Exception e){
//				ProgressHandle.addProgress(new TransferProgress("http第"+index+"次get请求失败:"+e.getMessage(), TransferProgress.STATE_SHOWMESSAGE));
//				DatLogger.logSysCommun(HttpClientUtils.class, ITransferWay.logTypeFlag+""+
//						DatLogger.midlleFlag+"http第"+index+"次get请求失败:"+e.getMessage());
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
		int correntCount = getCount(count);
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

	public static int getCount(int count){
		int correntCount = count;
		if(correntCount==0){
			correntCount= HttpClientUtils.count;
		}
		return correntCount;
	}
	
	
	public static boolean uploadfile(File file){
		
		return true;
	}
	
}
