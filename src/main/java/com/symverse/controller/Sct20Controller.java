package com.symverse.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.symverse.core.config.jasypt.JasyptUtil;
import com.symverse.core.config.systemenv.SystemEnvFactory;
import com.symverse.sct20.common.util.GetKeyStoreJson;
import com.symverse.sct20.common.util.SymGetAPI;
import com.symverse.sct20.transaction.service.Sct20Factory;

import lombok.extern.slf4j.Slf4j;




@RestController
@RequestMapping("/sct20")
@Slf4j
public class Sct20Controller {
	
	
	// -DSERVICE_MODE=testnet 
	// -DKEYSTORE_PASSWORD=Hf/7/UqxJD0UFg70IAoS5stN+fPyI4Rv4jRH8EbzKJchdsRxkG9WW1IGYgLXHziX 
	// -DKEYSTORE_FILENAME=testnet-keystore.json 
	// -DCHAIN_ID=2 
	// -DNODE_URL=http://1.234.16.211:8545
	
	
	@Autowired SystemEnvFactory systemEvn;  // System.getProperty를 가져옵니다.
	@Autowired GetKeyStoreJson getKeyStoreJson;  // keystore의 객체를 가져 옵니다.
	@Autowired private Sct20Factory sct20Factory; 

	
	
	
	@RequestMapping(value="/passswordEnc", method=RequestMethod.POST)
	@ResponseBody
	public Object passswordEnc(@RequestBody String password ) throws Exception {
		System.out.println("parameter password : "+password);
		String encStr = JasyptUtil.stringEncryptor(password);
		System.out.println("passwordEnc : "+encStr);  // IpjPTWz8BCm6VUQmjOyrQ58LyCPJ/A150fds0Gcl4fY=
		return encStr;
	}
	
	@RequestMapping(value="/passswordDec", method=RequestMethod.POST)
	@ResponseBody
	public Object passswordDec(@RequestBody String password ) throws Exception {
		System.out.println("parameter password : "+password);
		String decStr = JasyptUtil.stringDecryptor(password);
		System.out.println("passwordEnc : "+decStr);
		return decStr;
	}
	
	
	@RequestMapping(value="/getBalance", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object getBalance(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_0  참조해서 구현
		// ## main net 
		// ca.id=0002
		// ca.keystore.passwored=ENC(R3m9tR8VULnT8yw7FrKVTY8o0zUfRkyyrZuhng87jlS6/i417KRHGg==)
		// ca.node.url=http://58.227.193.177:8545
		System.out.println("chainId : "+systemEvn.CHAIN_ID);
		
		
		List<String> requestParam = new ArrayList<String>();
		requestParam.add("0x00028530e81e13060002");
		requestParam.add("latest");
		String getBalance =SymGetAPI.getSymAPIConnection("GET", systemEvn.NODE_URL ,"sym_getBalance", requestParam ,"");
		// getBalance는 0을 뒤에 18개 붙이므로 정수로 표현할때는 1000000000000000000으로 나눠 준다 .난 소수점 2째자리까지 표현하고싶으므로~~~ 0 2개 뺌! 
		BigDecimal  amountValue  =  new BigDecimal(  getBalance ).divide(new BigDecimal(  "1000000000000000000" )) ;
		DecimalFormat dfFormat = new DecimalFormat("#.####");
		String str =  dfFormat.format( amountValue.floatValue() );
		
		//	System.out.println(amountValue);
		//	System.out.println(getBalance);
		//	System.out.println(str);
		return str;
	}
	
	// 토큰 보내기 메소드
	@RequestMapping(value="/sendRawTransaction", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object sendRawTransaction( String amountValue ) throws Exception {
		// String address = GetKeyStoreJson.getKeyStoreValue("address");
		String jaehyunAddress = "0x00026159665b7e620002";  //old폰
		System.out.println(amountValue);
		String resultHashValue = sct20Factory.sendRawTransaction(systemEvn.KEYSTORE_FILENAME, jaehyunAddress, amountValue);
		return resultHashValue;
	}
	
	
	
	// sct 20 토큰 계약 생성
	@RequestMapping(value="/method0", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method0(  ) throws Exception {
		
		// sct20Factory.sct20TokenCreate(keyStoreFileName, coinName, coinSimpleName, coinTotalSupply);
		return "method0";
	}
	
	
	// sct 20 토큰 전송
	@RequestMapping(value="/method1", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method1(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_1  참조해서 구현
		// sct20Factory.sct20ToeknSend(keyStoreFileName, contractAddress, toSymId, sendTokenAmt);
		return "method1";
	}
	
	@RequestMapping(value="/method2", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method2(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_2 참조해서 구현
		return "method2";
	}
	
	@RequestMapping(value="/method3", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method3(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_3 참조해서 구현
		return "method3";
	}
	
	@RequestMapping(value="/method4", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method4(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_4 참조해서 구현
		return "method4";
	}
	
	@RequestMapping(value="/method5", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method5(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_5 참조해서 구현
		return "method5";
	}
	
	@RequestMapping(value="/method6", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method6(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_6 참조해서 구현
		return "method6";
	}
	
	@RequestMapping(value="/method7", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method7(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_7 참조해서 구현
		return "method7";
	}
}
