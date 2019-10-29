package com.symverse.sct20.common.util;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.symverse.common.model.CommUtil;

import net.minidev.json.JSONArray;


public class SymGetAPI {

	
	static final Logger logger = LoggerFactory.getLogger(SymGetAPI.class);
	
	public static String getSymAPIConnection(String RequestMethod , String urlAddress ,  String methodName ,List<String> param , String resultKey ) {
		String resultValue = "0";
		/*
		 * if(!blockchainRecodingActivation) {
		 * logger.debug("SymGetAPI Connection Disable "); return resultValue ; }
		 */		
		
		try {
			
			URL url = new URL (urlAddress);
			HttpURLConnection   conn    = null;
			OutputStream          os   = null;
			InputStream           is   = null;
			ByteArrayOutputStream baos = null;
			 
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(200000);
			conn.setReadTimeout(200000);
			conn.setRequestMethod(RequestMethod);
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			 
			JSONObject job = new JSONObject();
			job.put("jsonrpc", "2.0");
			job.put("id", "2");
			job.put("method", methodName);
			JSONArray arr = new JSONArray();
			for ( int i = 0 ; i < param.size() ; i++) {
				arr.add(param.get(i));
			}
			job.put("params", arr);
			os = conn.getOutputStream();
			os.write(job.toString().getBytes());
			os.flush();
			 
			String response;
			int responseCode = conn.getResponseCode();
			 
			if(responseCode == HttpURLConnection.HTTP_OK) {
			 
			    is = conn.getInputStream();
			    baos = new ByteArrayOutputStream();
			    byte[] byteBuffer = new byte[1024];
			    byte[] byteData = null;
			    int nLength = 0;
			    while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
			        baos.write(byteBuffer, 0, nLength);
			    }
			    byteData = baos.toByteArray();
			     
			    response = new String(byteData);
			     
			    JSONObject responseJSON = new JSONObject(response);
			    logger.debug(responseJSON.toString());
			    
			    try {
			    	String result = (String) responseJSON.get("result").toString();
			    	
			    	if(result.indexOf("{")  > -1 || result.indexOf("[") > -1 ) { //json 객체라면 
			    		Gson gson = new Gson();
			    		Map<String,String> map = new HashMap<String,String>();
			    		Map<String,String> obj = (Map<String,String>)  gson.fromJson(result, map.getClass());
			    		String getValue = (String) obj.get(resultKey);
			    		resultValue = getValue;
			    	}else {
			    		System.out.println("result : "+ CommUtil.preHexRemove ( result ));
			    		
			    	    BigInteger bigInt = new BigInteger( CommUtil.preHexRemove ( result ), 16);
			    	    
			    	    // 0x39f9049492a9babaa
			    		resultValue = String.valueOf(bigInt);
			    	}
			    	
			    	
			    }catch (Exception e) {
			    	logger.debug("Fail");
			    	logger.debug(e.getMessage());
			    	resultValue = "Fail";
			    	return resultValue;
				}
			    
			    
			}


			conn.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultValue;
	}
	
}
