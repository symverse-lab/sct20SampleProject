package com.symverse.sct20.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KeyStoreManagement {

	
	private static final String KEYSTORE_PASSWORD = Optional.ofNullable(System.getProperty("KEYSTORE_PASSWORD")).orElse("0000").toLowerCase();
	private static final String KEYSTORE_FILENAME = Optional.ofNullable(System.getProperty("KEYSTORE_FILENAME")).orElse("keystore.json").toLowerCase();

	private Credentials credentials;

	
	@PostConstruct
	public void loadCredentials() throws Exception {

        Credentials credentials;

		try {
			log.debug("[ca_log]load credential");
			File keyStoreFile = new ClassPathResource("keystore/"+KEYSTORE_FILENAME).getFile();
			credentials = WalletUtils.loadCredentials(KEYSTORE_PASSWORD, keyStoreFile);

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



