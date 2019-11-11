package com.symverse.core.config.systemenv;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.symverse.sct20.transaction.service.Sct20SendRawTransactionService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SystemEnvFactory {

	
	public String SERVICE_MODE ;
	public String KEYSTORE_PASSWORD ;
	public String KEYSTORE_FILENAME ;
	public String CHAIN_ID ;
	public String NODE_URL ;
	public String ENGINE_VERSION ;
				
	
	@PostConstruct
	public void systemEnvInitParameter() {
		this.SERVICE_MODE = Optional.ofNullable(System.getProperty("SERVICE_MODE")).orElse("docker").toLowerCase();
		this.KEYSTORE_PASSWORD = Optional.ofNullable(System.getProperty("KEYSTORE_PASSWORD")).orElse("INSERT_PASSWORD").toLowerCase();
		this.KEYSTORE_FILENAME = Optional.ofNullable(System.getProperty("KEYSTORE_FILENAME")).orElse("keystore.json").toLowerCase();
		this.CHAIN_ID = Optional.ofNullable(System.getProperty("CHAIN_ID")).orElse("1").toLowerCase();
		this.NODE_URL = Optional.ofNullable(System.getProperty("NODE_URL")).orElse("http://58.227.193.175:8545").toLowerCase();
		this.ENGINE_VERSION = Optional.ofNullable(System.getProperty("ENGINE_VERSION")).orElse("1.0.14").toLowerCase();
		
		log.debug("");
		log.debug("");
		log.debug(" [[ ↓↓ SystemEnvFactory ENV Argument ↓↓ ]] ");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
		log.debug(" [ ENGINE_VERSION : " + this.ENGINE_VERSION + " ]");
		log.debug(" [ KEYSTORE_FILENAME : " + this.KEYSTORE_FILENAME + " ]");
		log.debug(" [ KEYSTORE_PASSWORD : " + this.KEYSTORE_PASSWORD + " ]");
		log.debug(" [ CHAIN_ID : " + this.CHAIN_ID + " ]");
		log.debug(" [ NODE_URL : " + this.NODE_URL + " ]");
		log.debug(" [ ENGINE_VERSION : " + this.ENGINE_VERSION + " ]");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★ ");
		log.debug("");
		log.debug("");
	}

}
