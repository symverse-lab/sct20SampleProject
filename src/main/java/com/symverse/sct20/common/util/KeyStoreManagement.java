package com.symverse.sct20.common.util;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import com.symverse.core.config.systemenv.SystemEnvFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KeyStoreManagement {


	@Autowired SystemEnvFactory systemEvn;  // System.getProperty를 가져옵니다.
	
	public Credentials getCredentials(String keyStoreName , String password ) throws Exception {

        Credentials credentials;

		try {
			log.debug("[ca_log]load credential");
			log.debug("[ca_log] credential keystorename : "+keyStoreName+" credential password : "+password);
		    File keyStoreFile;
			if(systemEvn.SERVICE_MODE.contains("docker")) { //local1
				keyStoreFile = new File("/webapp/keystore/"+keyStoreName);
		    }else{
		    	keyStoreFile = new ClassPathResource("keystore/"+keyStoreName).getFile();
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



