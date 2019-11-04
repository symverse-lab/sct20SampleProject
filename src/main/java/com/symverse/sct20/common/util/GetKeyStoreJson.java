package com.symverse.sct20.common.util;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.web3j.utils.Numeric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.symverse.core.config.systemenv.SystemEnvFactory;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;


@Slf4j
@Component
public class GetKeyStoreJson {
	
	
	@Autowired SystemEnvFactory systemEvn;  // System.getProperty를 가져옵니다.
	
	public String getKeyStoreValue(String keystoreFileName , String Key) throws Exception {
		JSONParser parser = new JSONParser();
		ObjectMapper objectMapper = new ObjectMapper();
		log.debug("[ca_log]load credential");
		
		System.out.println("[ Loading KeyStore File : "+keystoreFileName +"]");
	    File keyStoreFile;
		if(systemEvn.SERVICE_MODE.contains("main")) { //local
			keyStoreFile = new ClassPathResource("keystore/"+keystoreFileName).getFile();
	    }else{
	    	keyStoreFile = new File("/webapp/keystore/"+keystoreFileName);
	    }
		
		// File file = new ClassPathResource(systemEvn.KEYSTORE_FILENAME).getFile();
		
		Map<String, String> jsonFile = objectMapper.readValue(keyStoreFile, Map.class);
		String value =  jsonFile.get(Key);
		return Numeric.prependHexPrefix(value);
	}

}
