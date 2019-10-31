package com.symverse.sct20.common.util;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KeyStoreManagement {

	public Credentials getCredentials(String keyStoreName , String password ) throws Exception {

        Credentials credentials;

		try {
			log.debug("[ca_log]load credential");
			log.debug("[ca_log] credential keystorename : "+keyStoreName+" credential password : "+password);
		    String os = System.getProperty("os.name").toLowerCase();
		    File keyStoreFile;
		    if(os.contains("win")) { //local
		    	 keyStoreFile = new ClassPathResource("keystore/"+keyStoreName).getFile();
		    }else{
		    	 keyStoreFile = new File("/webapp/keystore/"+keyStoreName);
		    }
			credentials = WalletUtils.loadCredentials(password, keyStoreFile);

		} catch (IOException e) {
			throw new Exception(e.getMessage());
		} catch (CipherException e) {
			throw new Exception(e.getMessage());
		}

		return credentials;

	}

}



