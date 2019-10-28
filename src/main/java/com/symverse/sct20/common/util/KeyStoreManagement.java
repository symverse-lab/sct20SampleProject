package com.symverse.sct20.common.util;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KeyStoreManagement {

	@Value("${ca.keystore.passwored}")
	private String keyStorePassword;
	
	@Value("${spring.profiles.active}")
	private String serverEnv;
	

	private Credentials credentials;

	
	
	@PostConstruct
	public void loadCredentials() throws Exception {

        Credentials credentials;

		try {
			log.debug("[ca_log]load credential");
			String getPropertyFileName = "";
			if("prod".equals(serverEnv)) {
				getPropertyFileName = "keystore.json";
			}else if("local".equals(serverEnv)) {
				getPropertyFileName = "_devNet.json";
			}else if("dev".equals(serverEnv)) {
				getPropertyFileName = "_devNet.json";
			}
			File keyStoreFile = new ClassPathResource("keystore/"+getPropertyFileName).getFile();
			credentials = WalletUtils.loadCredentials(keyStorePassword, keyStoreFile);

		} catch (IOException e) {
			throw new Exception(e.getMessage());
		} catch (CipherException e) {
			throw new Exception(e.getMessage());
		}

		this.credentials = credentials;

	}

	public Credentials getCredentials() {
		return this.credentials;
	}

}



