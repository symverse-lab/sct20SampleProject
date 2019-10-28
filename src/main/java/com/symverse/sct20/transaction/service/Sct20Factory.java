package com.symverse.sct20.transaction.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.symverse.common.KeyStoreManagement;
import com.symverse.common.SymGetAPITest;
import com.symverse.exception.ServerErrorException;
import com.symverse.gsym.core.Gsym;
import com.symverse.gsym.core.JsonRpc2_0Gsym;
import com.symverse.gsym.domain.SendSCT20;
import com.symverse.sct20tokensend.transaction.domain.Sct20SendRawTransactionTest;
import com.symverse.sct20tokensend.transaction.domain.Sct20TempleteVOTest;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Sct20Factory {
	static final Logger logger = LoggerFactory.getLogger(Sct20Factory.class);
	/*
	 *  application properties 설정
		ca.keystore.passwored=userpassword -> wallet지갑에서 생성한 해당 SYMID의  패스워드를 넣습니다.
		ca.node.url=http://1.234.16.211:8545 -> 심버스 블록체인 jsonrpc 주소
		ca.chain.id=3 -> 체인아이디
	*/
    private Sct20Factory() {
    }
 
    private static class LazyHolder {
        public static final Sct20Factory INSTANCE = new Sct20Factory();
    }
 
    public static Sct20Factory getInstance() {
        return LazyHolder.INSTANCE;
    }
	
	
	@Value("${spring.profiles.active}")
	private String serverEnv;
	
	
	// chian id  설정
	@Value("#{new Integer('${ca.chain.id}')}") // 3
	private int chianId;
	
    @Value("${ca.node.url}")
    private String nodeUrl; // Symverse BlockChain Engine DevNet httpUrl : 
	
    private JsonRpc2_0Gsym jsonRpc2_0Gsym;
    
	@Autowired
	KeyStoreManagement keyStoreManagement;

	@Autowired
    public void initJsonRpc2_0Sym(Environment env){
		/*## 운영 Prod ( dev 넷   )
		ca.id=0002
		ca.keystore.passwored=@devnet_dhvmsrkwmdk!
		ca.node.url=http://1.234.16.211:8545
		ca.chain.id=3

		## 셋팅값 test net ( 테스트 넷 )
		ca.test.id=0002
		ca.test.keystore.passwored=symverse321#@!_testnet
		ca.test.node.url=http://1.234.16.208:8545
		ca.test.chain.id=2

		## 셋팅 값 dev net  ( dev 전용 )
		ca.dev.id=0002
		ca.dev.keystore.passwored=@devnet_dhvmsrkwmdk!
		ca.dev.node.url=http://1.234.16.211:8545
		ca.dev.chain.id=3*/
		String getPropertyFileName = "";
		if("prod".equals(serverEnv)) {
			getPropertyFileName = "ca.test.node.url";
		}else if("local".equals(serverEnv)) {
			getPropertyFileName = "ca.local.node.url";
		}else if("dev".equals(serverEnv)) {
			getPropertyFileName = "ca.dev.node.url";
		}
		this.jsonRpc2_0Gsym = Gsym.build(new HttpService(env.getProperty(getPropertyFileName)));
    }
    
    /**
	 * citizen 등록
	 * @param citizenRawTransaction
	 * @return
     * @throws Exception 
	 */
	public String sct20SendRawTransaction (Sct20SendRawTransactionTest sct20SendRawTransaction ) throws Exception {
		log.debug("[coupon_log]sct20SendRawTransaction methodType ["+sct20SendRawTransaction.getInput().getSctType()+"]");
		SendSCT20 sendSct20 = new SendSCT20();
		//TODO sign값 value로 설정 넣어주기
		byte[] signedMessage = Sct20RawTransactionEncoderTest.signMessage(sct20SendRawTransaction, (byte) chianId, keyStoreManagement.getCredentials() );
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


	private static String getKeyStoreValue(String keyStoreFileName ,String Key) throws ParseException, IOException {
		JSONParser parser = new JSONParser();
		ObjectMapper objectMapper = new ObjectMapper();
		if(keyStoreFileName == null || "".equals(keyStoreFileName) ) {
			keyStoreFileName  = "keystore.json";
		}
		File file = new ClassPathResource("keystore/"+keyStoreFileName).getFile();
		Map<String, String> jsonFile = objectMapper.readValue(file, Map.class);
		String value =  jsonFile.get(Key);
		return Numeric.prependHexPrefix(value);
	}
	
	
	
	// sct 20 토큰 계약 생성
	public String sct20TokenCreate(String keyStoreFileName , String coinName , String coinSimpleName , String coinTotalSupply  ) throws Exception  {
		
		 List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		 String keyStroeAddress = Sct20Factory.getKeyStoreValue( keyStoreFileName, "address");
		 setSymAPIConnectionParam.add(keyStroeAddress);
		 setSymAPIConnectionParam.add("pending");
		 // 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		 String nonceValue =SymGetAPITest.getSymAPIConnection("GET", nodeUrl ,"sym_getTransactionCount",setSymAPIConnectionParam ,"");
		 
		 Sct20SendRawTransactionTest sct20SendRawTransaction	= new Sct20SendRawTransactionTest();                                          
		 sct20SendRawTransaction.setFrom(keyStroeAddress);                                                                          
		 sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));                                                              
		 sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정                                                
		 sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정                                                    
		 sct20SendRawTransaction.setTo("null");                                                                                     
		 sct20SendRawTransaction.setValue(new BigInteger("2"));                                                                     
		 Sct20TempleteVOTest inputParam = new Sct20TempleteVOTest();                                                                        
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
		 String getCouponTransactionHash = this.sct20SendRawTransaction(sct20SendRawTransaction);	    
		 return getCouponTransactionHash;
	}
	

	// sct 20  토큰 교환 ( 토큰 전송 )
	public String sct20ToeknSend(String keyStoreFileName, String contractAddress , String toSymId , String sendTokenAmt) throws Exception {
		List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = Sct20Factory.getKeyStoreValue( keyStoreFileName, "address");
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPITest.getSymAPIConnection("GET", nodeUrl ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransactionTest sct20SendRawTransaction	= new Sct20SendRawTransactionTest();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo(contractAddress); 
		sct20SendRawTransaction.setValue(new BigInteger("2"));
		Sct20TempleteVOTest inputParam = new Sct20TempleteVOTest();
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
  	    String getCouponTransactionHash = this.sct20SendRawTransaction(sct20SendRawTransaction);
  	    return getCouponTransactionHash;
	}

	
	// 제 3자 토큰 교환(송금) - ( 스펜더 계정에서 특정 SymId에게 송금하기 )  
	public String sct20SpenderSendToken(String keyStoreFileNameString , String contractAddress ,String spenderSymid , String toSymId , String sendTokenAmt) throws Exception {
		 List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		 String keyStroeAddress = Sct20Factory.getKeyStoreValue( keyStoreFileNameString, "address");
		 setSymAPIConnectionParam.add(keyStroeAddress);
		 setSymAPIConnectionParam.add("pending");
		 // 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		 String nonceValue =SymGetAPITest.getSymAPIConnection("GET", nodeUrl ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		 Sct20SendRawTransactionTest sct20SendRawTransaction	= new Sct20SendRawTransactionTest();
		 sct20SendRawTransaction.setFrom(keyStroeAddress); // 제 3자 스펜더의 SYMID
		 sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		 sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		 sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		 sct20SendRawTransaction.setTo(contractAddress); 
		 sct20SendRawTransaction.setValue(new BigInteger("0"));
		 Sct20TempleteVOTest inputParam = new Sct20TempleteVOTest();
		 inputParam.setSctType("20"); // 20 , 30 , 40
		 inputParam.setMethod("2");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		 ArrayList<String> paramsArray = new ArrayList<String>();
		 paramsArray.add(spenderSymid);  // 제 3자 토큰 위임자 symid ( 최초 토큰을 생성한 symid ) //위임한 SymId를 넣으면 안됨! // ★★서명은 제3자 스펜더 SymId 개인키로 서명 해야합니다.★★
		 paramsArray.add(toSymId);  // 제 3자 토큰 위임자 symid ( 수신자 symid ) 
		 paramsArray.add(sendTokenAmt); // 토큰 양
		 inputParam.setParams(paramsArray);
		 sct20SendRawTransaction.setInput(inputParam);
		 sct20SendRawTransaction.setType("1");
		 List<String> workNodesValue = new ArrayList<>();
		 workNodesValue.add("00022000000000270002");
		 sct20SendRawTransaction.setWorkNode(workNodesValue);
	     String getCouponTransactionHash = this.sct20SendRawTransaction(sct20SendRawTransaction);	  
	     return getCouponTransactionHash;
	}

	
	

	// 제 3자 토큰 승인 ( 스펜더 설정 )  - 제 3자 토큰 승인을 해야  제 3자 토큰 교환을 할 수 있습니다. 
	public String sct20SpenderAssign(String keyStoreFileNameString , String contractAddress ,String spenderSymid  , String sendTokenAmt) throws Exception {
		List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = Sct20Factory.getKeyStoreValue( keyStoreFileNameString, "address");
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔진에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPITest.getSymAPIConnection("GET", nodeUrl ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		
		Sct20SendRawTransactionTest sct20SendRawTransaction	= new Sct20SendRawTransactionTest();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo(contractAddress); 
		sct20SendRawTransaction.setValue(new BigInteger("2"));
		Sct20TempleteVOTest inputParam = new Sct20TempleteVOTest();
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
	    String getCouponTransactionHash = this.sct20SendRawTransaction(sct20SendRawTransaction);	   
	    return getCouponTransactionHash;
	}
	
	
	
	
	

	// sct 20 토큰 추가 발행 ( 토큰 총량 증가하기 )
	public String sct20TokenTotalSupplyAdd(String keyStoreFileName , String contractAddress,String addTotalTokenSupplyAmt) throws Exception {
		List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = Sct20Factory.getKeyStoreValue( keyStoreFileName, "address");
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPITest.getSymAPIConnection("GET", nodeUrl ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransactionTest sct20SendRawTransaction	= new Sct20SendRawTransactionTest();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo("0xf89448d8a643f0a26a34"); 
		sct20SendRawTransaction.setValue(new BigInteger("0"));
		Sct20TempleteVOTest inputParam = new Sct20TempleteVOTest();
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
	    String getCouponTransactionHash = this.sct20SendRawTransaction(sct20SendRawTransaction);	   
	    return getCouponTransactionHash;
	}

	
	

	// sct 20 토큰 태움 - ( 토큰 총량 감소하기 )
	public String sct20TokenTotalSupplyBurn(String keyStoreFileName,String contractAddress ,String bornTotalTokenSupplyAmt) throws Exception {
		List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = Sct20Factory.getKeyStoreValue(  keyStoreFileName, "address");
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPITest.getSymAPIConnection("GET", nodeUrl ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransactionTest sct20SendRawTransaction	= new Sct20SendRawTransactionTest();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo(contractAddress); 
		sct20SendRawTransaction.setValue(new BigInteger("0"));
		Sct20TempleteVOTest inputParam = new Sct20TempleteVOTest();
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
	    String getCouponTransactionHash = this.sct20SendRawTransaction(sct20SendRawTransaction);
	    return getCouponTransactionHash;
	}
	
	
	// sct 20 토큰 거래 일시 정지  
	public String sct20TokenTradingPause(String keyStoreFileName,String contractAddress) throws Exception {
	    List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = Sct20Factory.getKeyStoreValue( keyStoreFileName, "address");
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPITest.getSymAPIConnection("GET", nodeUrl ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransactionTest sct20SendRawTransaction	= new Sct20SendRawTransactionTest();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo("0xf89448d8a643f0a26a34"); 
		sct20SendRawTransaction.setValue(new BigInteger("0"));
		Sct20TempleteVOTest inputParam = new Sct20TempleteVOTest();
		inputParam.setSctType("20"); // 20 , 30 , 40
		inputParam.setMethod("6");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		ArrayList<String> paramsArray = new ArrayList<String>();
		inputParam.setParams(paramsArray);
		sct20SendRawTransaction.setInput(inputParam);
		sct20SendRawTransaction.setType("1");
		List<String> workNodesValue = new ArrayList<>();
		workNodesValue.add(keyStroeAddress);
		sct20SendRawTransaction.setWorkNode(workNodesValue);
	    String getCouponTransactionHash = this.sct20SendRawTransaction(sct20SendRawTransaction);
	    return getCouponTransactionHash;
	}
	
	// sct 20 토큰 거래 일시 정지  해제
	public String sct20TokenTradingPauseRelease(String keyStoreFileName, String contractAddress) throws Exception {
	    List<String> setSymAPIConnectionParam  = new ArrayList<String>();
		String keyStroeAddress = Sct20Factory.getKeyStoreValue( keyStoreFileName, "address");
		setSymAPIConnectionParam.add(keyStroeAddress);
		setSymAPIConnectionParam.add("pending");
		// 블록체인 엔인에서 현재의 블록 nonce값을 가져 옵니다.
		String nonceValue =SymGetAPITest.getSymAPIConnection("GET", nodeUrl ,"sym_getTransactionCount",setSymAPIConnectionParam ,""); 
		Sct20SendRawTransactionTest sct20SendRawTransaction	= new Sct20SendRawTransactionTest();
		sct20SendRawTransaction.setFrom(keyStroeAddress);
		sct20SendRawTransaction.setNonce(new BigInteger(nonceValue));
		sct20SendRawTransaction.setGasPrice(new BigInteger("18000000000")); // 값 고정
		sct20SendRawTransaction.setGasLimit(new BigInteger("2000000")); // 값 고정
		sct20SendRawTransaction.setTo("0xf89448d8a643f0a26a34"); 
		sct20SendRawTransaction.setValue(new BigInteger("0"));
		Sct20TempleteVOTest inputParam = new Sct20TempleteVOTest();
		inputParam.setSctType("20"); // 20 , 30 , 40
		inputParam.setMethod("7");  // 0 , 1 , 2, 3, 4, 5, 6 , 7
		ArrayList<String> paramsArray = new ArrayList<String>();
		inputParam.setParams(paramsArray);
		sct20SendRawTransaction.setInput(inputParam);
		sct20SendRawTransaction.setType("1");
		List<String> workNodesValue = new ArrayList<>();
		workNodesValue.add(keyStroeAddress);
		sct20SendRawTransaction.setWorkNode(workNodesValue);
	    String getCouponTransactionHash = this.sct20SendRawTransaction(sct20SendRawTransaction);	   	
	    return getCouponTransactionHash;
	}

	
	

	

	




}
