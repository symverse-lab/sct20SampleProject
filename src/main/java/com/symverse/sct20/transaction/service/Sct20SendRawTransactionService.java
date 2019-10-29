package com.symverse.sct20.transaction.service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import com.symverse.exception.ServerErrorException;
import com.symverse.sct20.common.util.KeyStoreManagement;
import com.symverse.sct20.gsym.core.Gsym;
import com.symverse.sct20.gsym.core.JsonRpc2_0Gsym;
import com.symverse.sct20.gsym.domain.SendSct20;
import com.symverse.sct20.transaction.domain.Sct20SendRawTransaction;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service("sct20SendRawTransactionService")
public class Sct20SendRawTransactionService {
	static final Logger logger = LoggerFactory.getLogger(Sct20SendRawTransactionService.class);
	/*
	 *  application properties 설정
		ca.keystore.passwored=userpassword -> wallet지갑에서 생성한 해당 SYMID의  패스워드를 넣습니다.
		ca.node.url=http://1.234.16.211:8545 -> 심버스 블록체인 jsonrpc 주소
		ca.chain.id=3 -> 체인아이디
	*/
	
	private static final String CHAIN_ID = Optional.ofNullable(System.getProperty("CHAIN_ID")).orElse("1").toLowerCase();
	private static final String NODE_URL = Optional.ofNullable(System.getProperty("NODE_URL")).orElse("http://1.234.16.211:8545").toLowerCase();
	
	@Value("${spring.profiles.active}")
	private String serverEnv;
	
	
	// chian id  설정
	@Value("#{new Integer('${ca.chain.id}')}") // 3
	private int chianId;
	
    private JsonRpc2_0Gsym jsonRpc2_0Gsym;
    
	@Autowired
	KeyStoreManagement keyStoreManagement;

	@Autowired
    public void initJsonRpc2_0Sym(Environment env){
		this.jsonRpc2_0Gsym = Gsym.build(new HttpService(NODE_URL));
    }
    
    /**
	 * citizen 등록
	 * @param citizenRawTransaction
	 * @return
     * @throws Exception 
	 */
	public String sct20SendRawTransaction (Sct20SendRawTransaction sct20SendRawTransaction ) throws Exception {
		log.debug("[coupon_log]sct20SendRawTransaction methodType ["+sct20SendRawTransaction.getInput().getSctType()+"]");
		SendSct20 sendSct20 = new SendSct20();
		//TODO sign값 value로 설정 넣어주기
		byte[] signedMessage = Sct20RawTransactionEncoder.signMessage(sct20SendRawTransaction, (byte) Integer.parseInt(CHAIN_ID), keyStoreManagement.getCredentials() );
		String hexValue = Numeric.toHexString(signedMessage);
		logger.debug("hexvalue: " + hexValue);
		
		try {
			// 블록체인 엔진으로 sym_sendrawtransaction 을 보냅니다.
			sendSct20 = jsonRpc2_0Gsym.sct20SendRawTransaction(hexValue).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new Exception(e.getMessage());
		}
		
		if(sendSct20.hasError()) {
			throw new ServerErrorException(sendSct20.getError().getMessage());
		}
		
		log.debug("[ca_log]sct20SendRawTransaction hash: " + sendSct20.getHash());
		return sendSct20.getHash();
	}


	

	

	




}
