package com.symverse.sct20.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.web3j.utils.Numeric;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;


@Slf4j
public class GetKeyStoreJson {
	
	private static final String SERVICE_MODE = Optional.ofNullable(System.getProperty("SERVICE_MODE")).orElse("main").toLowerCase();
	
	public static String getKeyStoreValue(String Key) throws ParseException, IOException {
		JSONParser parser = new JSONParser();
		ObjectMapper objectMapper = new ObjectMapper();
		log.debug("[ca_log]load credential");
		String keyStoreFileName = "";
		if(SERVICE_MODE.contains("dev")) {
			keyStoreFileName = "keystore/devnet-keystore.json";
		}else if(SERVICE_MODE.contains("test")){
			keyStoreFileName = "keystore/testnet-keystore.json";
		}else if(SERVICE_MODE.contains("main")){
			keyStoreFileName = "keystore/mainnet-keystore.json";
		}else if(SERVICE_MODE.contains("wise")){
			keyStoreFileName = "keystore/wisenet-keystore.json";
		}else {
			keyStoreFileName = "keystore/local-keystore.json";
		}
		System.out.println("[ Loading KeyStore File : "+keyStoreFileName+"]");
		File file = new ClassPathResource(keyStoreFileName).getFile();
		
		Map<String, String> jsonFile = objectMapper.readValue(file, Map.class);
		String value =  jsonFile.get(Key);
		return Numeric.prependHexPrefix(value);
	}

}
