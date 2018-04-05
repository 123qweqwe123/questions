package com.jet.datasync.bdcor2.config;


import java.math.BigInteger;
import java.security.MessageDigest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class Pmain {
	public static final String KEY_MD = "MD"; 
	public static void main(String[] args) throws Exception {
		
		System.out.println(Pmain.encryptBASE64("BASE加密测试!".getBytes()));
		String s = new String(Pmain.decryptBASE64("QkFTReWKoOWvhua1i+ivlSE="));
		System.out.println(s);
		System.out.println(Pmain.MD5("jiami"));
		System.out.println(Pmain.SHA("jiami"));
		
		
		
	}
	
	public static String encryptBASE64(byte[] key) throws Exception {   
        return (new BASE64Encoder()).encodeBuffer(key);   
    }  

	public static byte[] decryptBASE64(String key) throws Exception {   
        return (new BASE64Decoder()).decodeBuffer(key);   
    }
	
	
	 public static  String  MD5(String inputStr)
	 
	    {
		 
	        System.out.println("=======加密前的数据:"+inputStr);
	        BigInteger bigInteger=null;
	        try {
	         MessageDigest md = MessageDigest.getInstance("MD5");   
	         byte[] inputData = inputStr.getBytes(); 
	         md.update(inputData);   
	         bigInteger = new BigInteger(md.digest());   
	        } catch (Exception e) {e.printStackTrace();}
	        System.out.println("MD5加密后:" + bigInteger.toString(16));   
	        return bigInteger.toString(16);
	    }
	 
	 
	 public static  String  SHA(String inputStr)
	    {
	        BigInteger sha =null;
	        System.out.println("=======加密前的数据:"+inputStr);
	        byte[] inputData = inputStr.getBytes();   
	        try {
	             MessageDigest messageDigest = MessageDigest.getInstance("SHA");  
	             messageDigest.update(inputData);
	             sha = new BigInteger(messageDigest.digest());   
	             System.out.println("SHA加密后:" + sha.toString(32));   
	        } catch (Exception e) {e.printStackTrace();}
	        return sha.toString(32);
	    }
	 
	 
	 
	 /*public abstract class HMAC {   
		    public static final String KEY_MAC = "HmacMD5";   

		    *//**  
		     * 初始化HMAC密钥  
		     *   
		     * @return  
		     * @throws Exception  
		     *//*  
		    public static String initMacKey() throws Exception {   
		        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);   
		        SecretKey secretKey = keyGenerator.generateKey();   
		        return BASE64.encryptBASE64(secretKey.getEncoded());// BASE64加密  
		    }   

		    *//**  
		     * HMAC加密  ：主要方法
		     *   
		     * @param data  
		     * @param key  
		     * @return  
		     * @throws Exception  
		     *//*  
		    public static String encryptHMAC(byte[] data, String key) throws Exception {   

		        SecretKey secretKey = new SecretKeySpec(BASE64.decryptBASE64(key), KEY_MAC); //先base64解密  
		        Mac mac = Mac.getInstance(secretKey.getAlgorithm());   
		        mac.init(secretKey);   
		        return new String(mac.doFinal(data));   

		    }   

		    public static  String  getResult1(String inputStr)
		    {
		        String path=Tools.getClassPath();
		        String fileSource=path+"/file/HMAC_key.txt";
		        System.out.println("=======加密前的数据:"+inputStr);
		        String  result=null;
		        try {
		            byte[] inputData = inputStr.getBytes(); 
		            String key = HMAC.initMacKey(); 产生密钥  
		            System.out.println("Mac密钥:===" + key);  
		            将密钥写文件
		            Tools.WriteMyFile(fileSource,key);
		            result= HMAC.encryptHMAC(inputData, key);
		            System.out.println("HMAC加密后:===" + result); 
		        } catch (Exception e) {e.printStackTrace();}  
		       return result.toString();
		    }

		    public static  String  getResult2(String inputStr)
		    {
		        System.out.println("=======加密前的数据:"+inputStr);
		         String path=Tools.getClassPath();
		         String fileSource=path+"/file/HMAC_key.txt";
		         String key=null;;
		        try {
		             将密钥从文件中读取
		             key=Tools.ReadMyFile(fileSource);
		             System.out.println("getResult2密钥:===" + key);  
		        } catch (Exception e1) {
		            e1.printStackTrace();}
		        String  result=null;
		        try {
		            byte[] inputData = inputStr.getBytes();  
		            对数据进行加密
		            result= HMAC.encryptHMAC(inputData, key);
		            System.out.println("HMAC加密后:===" + result); 
		        } catch (Exception e) {e.printStackTrace();}  
		       return result.toString();
		    }

		    public static void main(String args[])
		    {
		        try {
		             String inputStr = "简单加密"; 
		             使用同一密钥：对数据进行加密：查看两次加密的结果是否一样
		             getResult1(inputStr); 
		             getResult2(inputStr);

		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		    }

		}*/
	 
 
}
