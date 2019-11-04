package com.symverse.sct20.transaction.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.symverse.core.config.systemenv.SystemEnvFactory;
import com.symverse.sct20.common.util.GetKeyStoreJson;
import com.symverse.sct20.common.util.KeyStoreManagement;
import com.symverse.sct20.common.util.SymGetAPI;
import com.symverse.sct20.transaction.domain.Sct20SendRawTransaction;
import com.symverse.sct20.transaction.domain.Sct20TempleteVO;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;


@Slf4j
@Component
public class Sct20Factory {
	static final Logger logger = LoggerFactory.getLogger(Sct20Factory.class);
	/*
	 *  application properties 설정
		ca.keystore.passwored=userpassword -> wallet지갑에서 생성한 해당 SYMID의  패스워드를 넣습니다.
		ca.node.url=http://1.234.16.211:8545 -> 심버스 블록체인 jsonrpc 주소
		ca.chain.id=3 -> 체인아이디
	*/
	
	
	// -DSERVICE_MODE=testnet 
	// -DKEYSTORE_PASSWORD=Hf/7/UqxJD0UFg70IAoS5stN+fPyI4Rv4jRH8EbzKJchdsRxkG9WW1IGYgLXHziX 
	// -DKEYSTORE_FILENAME=testnet-keystore.json 
	// -DCHAIN_ID=2 
	// -DsystemEnvFactory.NODE_URL=http://1.234.16.211:8545
	
	
	@Autowired private SystemEnvFactory systemEnvFactory; 
	@Autowired private KeyStoreManagement keyStoreManagement; 
	@Autowired private GetKeyStoreJson KeyStoreJson;
	@Autowired private Sct20SendRawTransactionService sct20SendRawTransactionService;
	
	
	
    
	
    
	
	public String sendRawTransaction(String keyStoreFileName,String toAddress,String amount) throws Exception{
		List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = KeyStoreJson.getKeyStoreValue(keyStoreFileName,"address");
		Credentials credential = keyStoreManagement.getCredentials(keyStoreFileName , systemEnvFactory.KEYSTORE_PASSWORD );
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPI.getSymAPIConnection("GET", systemEnvFactory.NODE_URL ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransaction sct20SendRawTransaction	= new Sct20SendRawTransaction();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo(toAddress); //old phone
		sct20SendRawTransaction.setValue(new BigInteger(amount+"000000000000000000"));
		Sct20TempleteVO inputParam = new Sct20TempleteVO();  // 그냥 sym 송금은 input값 없어도됨.
		inputParam.setSctType("-1"); // 20 , 30 , 40
		inputParam.setMethod("-1");  //
		ArrayList<String> paramsArray = new ArrayList<String>();
		paramsArray.add(sct20SendRawTransaction.getTo());  // 수신자
		paramsArray.add(amount+"000000000000000000"); // 토큰 양
		inputParam.setParams(paramsArray);
		sct20SendRawTransaction.setInput(inputParam);
		sct20SendRawTransaction.setType("0");
		List<String> workNodesValue = new ArrayList<>();
		workNodesValue.add("0002db0859e72aad0002"); // mainnet worknode - 2  0x0002b103ddaae9780002		1.234.16.207	8545
		sct20SendRawTransaction.setWorkNode(workNodesValue);
		String getCouponTransactionHash = sct20SendRawTransactionService.sct20SendRawTransaction(sct20SendRawTransaction , credential);
		return getCouponTransactionHash;
	}
	
	
	// sct 20 토큰 계약 생성
	public String sct20TokenCreate(String keyStoreFileName , String coinName , String coinSimpleName , String coinTotalSupply  ) throws Exception  {
		
		 List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		 String keyStroeAddress = KeyStoreJson.getKeyStoreValue( keyStoreFileName,"address");
		Credentials credential = keyStoreManagement.getCredentials(keyStoreFileName , systemEnvFactory.KEYSTORE_PASSWORD );
		 setSymAPIConnectionParam.add(keyStroeAddress);
		 setSymAPIConnectionParam.add("pending");
		 // 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		 String nonceValue =SymGetAPI.getSymAPIConnection("GET", systemEnvFactory.NODE_URL ,"sym_getTransactionCount",setSymAPIConnectionParam ,"");
		 
		 Sct20SendRawTransaction sct20SendRawTransaction	= new Sct20SendRawTransaction();                                          
		 sct20SendRawTransaction.setFrom(keyStroeAddress);                                                                          
		 sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));                                                              
		 sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정                                                
		 sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정                                                    
		 sct20SendRawTransaction.setTo("null");                                                                                     
		 sct20SendRawTransaction.setValue(new BigInteger("2"));                                                                     
		 Sct20TempleteVO inputParam = new Sct20TempleteVO();                                                                        
		 inputParam.setSctType("20"); // 20 , 30 , 40  -> Symverse Engine에서는 SCT20 , SCT30 , SCT40이 있으며                                                                               
		 inputParam.setMethod("0");  // 0 , 1 , 2, 3, 4, 5, 6 , 7                                                                   
		 ArrayList<String> paramsArray = new ArrayList<String>();                                                                   
		 // input name , symbol , totalsupply , ownersymid                                                                          
		 paramsArray.add(coinName);  // 코인 이름                                                                                    
		 paramsArray.add(coinSimpleName);  // 코인이름 약어   - 무조건 3자리의 영문으로 표시합니다.                                                                                      
		 paramsArray.add(coinTotalSupply); // 총 코인 수량                                                                                       
		 paramsArray.add(keyStroeAddress); // 소유주 symId  //00022000000000270002                                                     
		 inputParam.setParams(paramsArray);                                                                                         
		 sct20SendRawTransaction.setInput(inputParam);                                                                              
		 sct20SendRawTransaction.setType("1");                                                                                      
		 List<String> workNodesValue = new ArrayList<>();                                                                           
		 workNodesValue.add(keyStroeAddress);                                                                                       
		 sct20SendRawTransaction.setWorkNode(workNodesValue);                                                                       
		 String getCouponTransactionHash = sct20SendRawTransactionService.sct20SendRawTransaction(sct20SendRawTransaction , credential);	    
		 return getCouponTransactionHash;
	}
	

	// sct 20  토큰 교환 ( 토큰 전송 )
	public String sct20ToeknSend(String keyStoreFileName, String contractAddress , String toSymId , String sendTokenAmt) throws Exception {
		List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = KeyStoreJson.getKeyStoreValue( keyStoreFileName,"address");
		Credentials credential = keyStoreManagement.getCredentials(keyStoreFileName , systemEnvFactory.KEYSTORE_PASSWORD );
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPI.getSymAPIConnection("GET", systemEnvFactory.NODE_URL ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransaction sct20SendRawTransaction	= new Sct20SendRawTransaction();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo(contractAddress); 
		sct20SendRawTransaction.setValue(new BigInteger("2"));
		Sct20TempleteVO inputParam = new Sct20TempleteVO();
		inputParam.setSctType("20"); // 20 , 30 , 40
		inputParam.setMethod("1");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		ArrayList<String> paramsArray = new ArrayList<String>();
		paramsArray.add(toSymId);  // 수신자
		paramsArray.add(sendTokenAmt); // 토큰 양
		inputParam.setParams(paramsArray);
		sct20SendRawTransaction.setInput(inputParam);
		sct20SendRawTransaction.setType("1");
		List<String> workNodesValue = new ArrayList<>();
		workNodesValue.add(keyStroeAddress);
		sct20SendRawTransaction.setWorkNode(workNodesValue);
  	    String getCouponTransactionHash = sct20SendRawTransactionService.sct20SendRawTransaction(sct20SendRawTransaction , credential);
  	    return getCouponTransactionHash;
	}

	
	// 제 3자 토큰 교환(송금) - ( 스펜더 계정에서 특정 SymId에게 송금하기 )  
	public String sct20SpenderSendToken(String keyStoreFileName , String contractAddress , String toSymId , String sendTokenAmt) throws Exception {
		 List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		 String keyStroeAddress = KeyStoreJson.getKeyStoreValue( keyStoreFileName ,  "address");
		 Credentials credential = keyStoreManagement.getCredentials(keyStoreFileName , systemEnvFactory.KEYSTORE_PASSWORD );
		 setSymAPIConnectionParam.add(keyStroeAddress);
		 setSymAPIConnectionParam.add("pending");
		 // 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		 String nonceValue =SymGetAPI.getSymAPIConnection("GET", systemEnvFactory.NODE_URL ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		 Sct20SendRawTransaction sct20SendRawTransaction	= new Sct20SendRawTransaction();
		 sct20SendRawTransaction.setFrom(keyStroeAddress); // 제 3자 스펜더의 SYMID
		 sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		 sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		 sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		 sct20SendRawTransaction.setTo(contractAddress); 
		 sct20SendRawTransaction.setValue(new BigInteger("0"));
		 Sct20TempleteVO inputParam = new Sct20TempleteVO();
		 inputParam.setSctType("20"); // 20 , 30 , 40
		 inputParam.setMethod("2");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		 ArrayList<String> paramsArray = new ArrayList<String>();
		 paramsArray.add( keyStroeAddress );  // 제 3자 토큰 위임자 symid ( 최초 토큰을 생성한 symid ) //위임한 SymId를 넣으면 안됨! // ★★서명은 제3자 스펜더 SymId 개인키로 서명 해야합니다.★★
		 paramsArray.add( toSymId );  // 제 3자 토큰 위임자 symid ( 수신자 symid ) 
		 paramsArray.add( sendTokenAmt ); // 토큰 양
		 inputParam.setParams(paramsArray);
		 sct20SendRawTransaction.setInput(inputParam);
		 sct20SendRawTransaction.setType("1");
		 List<String> workNodesValue = new ArrayList<>();
		 workNodesValue.add("00022000000000270002");
		 sct20SendRawTransaction.setWorkNode(workNodesValue);
	     String getCouponTransactionHash = sct20SendRawTransactionService.sct20SendRawTransaction(sct20SendRawTransaction , credential);	  
	     return getCouponTransactionHash;
	}

	
	

	// 제 3자 토큰 승인 ( 스펜더 설정 )  - 제 3자 토큰 승인을 해야  제 3자 토큰 교환을 할 수 있습니다. 
	public String sct20SpenderAssign(String keyStoreFileName , String contractAddress ,String spenderSymid  , String sendTokenAmt) throws Exception {
		List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = KeyStoreJson.getKeyStoreValue( keyStoreFileName , "address");
		Credentials credential = keyStoreManagement.getCredentials(keyStoreFileName , systemEnvFactory.KEYSTORE_PASSWORD);
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔진에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPI.getSymAPIConnection("GET", systemEnvFactory.NODE_URL ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		
		Sct20SendRawTransaction sct20SendRawTransaction	= new Sct20SendRawTransaction();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo(contractAddress); 
		sct20SendRawTransaction.setValue(new BigInteger("2"));
		Sct20TempleteVO inputParam = new Sct20TempleteVO();
		inputParam.setSctType("20"); // 20 , 30 , 40
		inputParam.setMethod("3");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		ArrayList<String> paramsArray = new ArrayList<String>();
		paramsArray.add(spenderSymid);  // 제 3자 토큰 위임자 symid ( 스펜더 지정 ) 
		paramsArray.add(sendTokenAmt); // 토큰 양
		inputParam.setParams(paramsArray);
		sct20SendRawTransaction.setInput(inputParam);
		sct20SendRawTransaction.setType("1");
		List<String> workNodesValue = new ArrayList<>();
		workNodesValue.add(keyStroeAddress);
		sct20SendRawTransaction.setWorkNode(workNodesValue);
	    String getCouponTransactionHash = sct20SendRawTransactionService.sct20SendRawTransaction(sct20SendRawTransaction , credential);	   
	    return getCouponTransactionHash;
	}
	
	
	
	
	

	// sct 20 토큰 추가 발행 ( 토큰 총량 증가하기 )
	public String sct20TokenTotalSupplyAdd(String keyStoreFileName , String contractAddress,String addTotalTokenSupplyAmt) throws Exception {
		List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = KeyStoreJson.getKeyStoreValue( keyStoreFileName,"address");
		Credentials credential = keyStoreManagement.getCredentials(keyStoreFileName , systemEnvFactory.KEYSTORE_PASSWORD );
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPI.getSymAPIConnection("GET", systemEnvFactory.NODE_URL ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransaction sct20SendRawTransaction	= new Sct20SendRawTransaction();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo("0xf89448d8a643f0a26a34"); 
		sct20SendRawTransaction.setValue(new BigInteger("0"));
		Sct20TempleteVO inputParam = new Sct20TempleteVO();
		inputParam.setSctType("20"); // 20 , 30 , 40
		inputParam.setMethod("4");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		ArrayList<String> paramsArray = new ArrayList<String>();
		paramsArray.add(keyStroeAddress);  // 토큰을 최초 만든  
		paramsArray.add(addTotalTokenSupplyAmt); // 추가 발행할 토큰 양
		inputParam.setParams(paramsArray);
		sct20SendRawTransaction.setInput(inputParam);
		sct20SendRawTransaction.setType("1");
		List<String> workNodesValue = new ArrayList<>();
		workNodesValue.add(keyStroeAddress);
		sct20SendRawTransaction.setWorkNode(workNodesValue);
	    String getCouponTransactionHash = sct20SendRawTransactionService.sct20SendRawTransaction(sct20SendRawTransaction , credential);	   
	    return getCouponTransactionHash;
	}

	
	

	// sct 20 토큰 태움 - ( 토큰 총량 감소하기 )
	public String sct20TokenTotalSupplyBurn(String keyStoreFileName,String contractAddress ,String bornTotalTokenSupplyAmt) throws Exception {
		List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = KeyStoreJson.getKeyStoreValue( keyStoreFileName , "address");
		Credentials credential = keyStoreManagement.getCredentials(keyStoreFileName , systemEnvFactory.KEYSTORE_PASSWORD );
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPI.getSymAPIConnection("GET", systemEnvFactory.NODE_URL ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransaction sct20SendRawTransaction	= new Sct20SendRawTransaction();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo(contractAddress); 
		sct20SendRawTransaction.setValue(new BigInteger("0"));
		Sct20TempleteVO inputParam = new Sct20TempleteVO();
		inputParam.setSctType("20"); // 20 , 30 , 40
		inputParam.setMethod("5");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		ArrayList<String> paramsArray = new ArrayList<String>();
		paramsArray.add(keyStroeAddress);  // 토큰을 최초 만든  
		paramsArray.add("20"); // 감소시킬 토큰 량 
		inputParam.setParams(paramsArray);
		sct20SendRawTransaction.setInput(inputParam);
		sct20SendRawTransaction.setType("1");
		List<String> workNodesValue = new ArrayList<>();
		workNodesValue.add(keyStroeAddress);
		sct20SendRawTransaction.setWorkNode(workNodesValue);
	    String getCouponTransactionHash = sct20SendRawTransactionService.sct20SendRawTransaction(sct20SendRawTransaction , credential);
	    return getCouponTransactionHash;
	}
	
	
	// sct 20 토큰 거래 일시 정지  
	public String sct20TokenTradingPause(String keyStoreFileName,String contractAddress) throws Exception {
	    List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = KeyStoreJson.getKeyStoreValue( keyStoreFileName,"address");
		Credentials credential = keyStoreManagement.getCredentials(keyStoreFileName , systemEnvFactory.KEYSTORE_PASSWORD );
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPI.getSymAPIConnection("GET", systemEnvFactory.NODE_URL ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransaction sct20SendRawTransaction	= new Sct20SendRawTransaction();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo("0xf89448d8a643f0a26a34"); 
		sct20SendRawTransaction.setValue(new BigInteger("0"));
		Sct20TempleteVO inputParam = new Sct20TempleteVO();
		inputParam.setSctType("20"); // 20 , 30 , 40
		inputParam.setMethod("6");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		ArrayList<String> paramsArray = new ArrayList<String>();
		inputParam.setParams(paramsArray);
		sct20SendRawTransaction.setInput(inputParam);
		sct20SendRawTransaction.setType("1");
		List<String> workNodesValue = new ArrayList<>();
		workNodesValue.add(keyStroeAddress);
		sct20SendRawTransaction.setWorkNode(workNodesValue);
	    String getCouponTransactionHash = sct20SendRawTransactionService.sct20SendRawTransaction(sct20SendRawTransaction , credential);
	    return getCouponTransactionHash;
	}
	
	// sct 20 토큰 거래 일시 정지  해제
	public String sct20TokenTradingPauseRelease(String keyStoreFileName, String contractAddress) throws Exception {
	    List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = KeyStoreJson.getKeyStoreValue( keyStoreFileName,"address");
		Credentials credential = keyStoreManagement.getCredentials(keyStoreFileName , systemEnvFactory.KEYSTORE_PASSWORD);
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPI.getSymAPIConnection("GET", systemEnvFactory.NODE_URL ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransaction sct20SendRawTransaction	= new Sct20SendRawTransaction();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo("0xf89448d8a643f0a26a34"); 
		sct20SendRawTransaction.setValue(new BigInteger("0"));
		Sct20TempleteVO inputParam = new Sct20TempleteVO();
		inputParam.setSctType("20"); // 20 , 30 , 40
		inputParam.setMethod("7");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		ArrayList<String> paramsArray = new ArrayList<String>();
		inputParam.setParams(paramsArray);
		sct20SendRawTransaction.setInput(inputParam);
		sct20SendRawTransaction.setType("1");
		List<String> workNodesValue = new ArrayList<>();
		workNodesValue.add(keyStroeAddress);
		sct20SendRawTransaction.setWorkNode(workNodesValue);
	    String getCouponTransactionHash = sct20SendRawTransactionService.sct20SendRawTransaction(sct20SendRawTransaction , credential);	   	
	    return getCouponTransactionHash;
	}

	
	

	

	




}
