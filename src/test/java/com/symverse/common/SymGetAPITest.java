package com.symverse.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.utils.Numeric;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.minidev.json.JSONArray;


public class SymGetAPITest {

	
	static final Logger logger = LoggerFactory.getLogger(SymGetAPITest.class);
	
	
	public static String getSymAPIConnection(String RequestMethod , String urlAddress ,  String methodName ,List<String> param , String resultKey ) {
		
		String resultValue = "0";
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
			 
			JsonObject job = new JsonObject();
			job.addProperty("jsonrpc", "2.0");
			job.addProperty("id", "2");
			job.addProperty("method", methodName);
			Gson json = new Gson();
			job.add("params", json.toJsonTree(param));
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
			    System.out.println("response : "+response);
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
			    		long v = Long.parseLong(Numeric.cleanHexPrefix( result ), 16);
			    		resultValue = String.valueOf(v);
			    	}
			    	
			    	
			    }catch (Exception e) {
			    	logger.debug("Fail");
			    	logger.debug(e.getLocalizedMessage());
			    	resultValue = "Fail";
			    	return resultValue;
				}
			    
			    
			}


			conn.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return resultValue;
	}
	
}
