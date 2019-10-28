package com.symverse.common;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.web3j.utils.Numeric;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;



public class GetKeyStoreJsonTest {

	public static String getKeyStoreValue(String Key) throws ParseException, IOException {
		JSONParser parser = new JSONParser();
		ObjectMapper objectMapper = new ObjectMapper();
		File file = new ClassPathResource("keystore/keystore.json").getFile();
		Map<String, String> jsonFile = objectMapper.readValue(file, Map.class);
		String value =  jsonFile.get(Key);
		return Numeric.prependHexPrefix(value);
	}

}
