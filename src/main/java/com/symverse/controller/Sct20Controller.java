package com.symverse.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	
	
	@Autowired SystemEnvFactory systemEvn;  // System.getProperty를 가져옵니다.
	@Autowired GetKeyStoreJson getKeyStoreJson;  // keystore의 객체를 가져 옵니다.
	@Autowired private Sct20Factory sct20Factory; 

	@RequestMapping(value="/passswordEnc", method=RequestMethod.POST)
	@ResponseBody
	public Object passswordEnc(@RequestBody String password ) throws Exception {
		log.debug("parameter password : "+password);
		String encStr = JasyptUtil.stringEncryptor(password);
		log.debug("passwordEnc : "+encStr);  // 
		return encStr;
	}
	
	@RequestMapping(value="/passswordDec", method=RequestMethod.POST)
	@ResponseBody
	public Object passswordDec(@RequestBody String password ) throws Exception {
		log.debug("parameter password : "+password);
		String decStr = JasyptUtil.stringDecryptor(password);
		log.debug("passwordEnc : "+decStr);
		return decStr;
	}
	
	
	@RequestMapping(value="/getBalance", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object getBalance(@RequestParam String symId ) throws Exception {
		List<String> requestParam = new ArrayList<String>();
		requestParam.add(symId);
		requestParam.add("latest");
		String getBalance =SymGetAPI.getSymAPIConnection("GET", systemEvn.NODE_URL ,"sym_getBalance", requestParam ,"");
		BigDecimal  amountValue  =  new BigDecimal(  getBalance ).divide(new BigDecimal(  "1000000000000000000" )) ;
		DecimalFormat dfFormat = new DecimalFormat("#.####");
		String str =  dfFormat.format( amountValue.floatValue() );
		return str;
	}
	
	// 토큰 보내기 메소드
	@RequestMapping(value="/sendRawTransaction", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object sendRawTransaction(@RequestParam String toSymId , String sendAmount ) throws Exception {
		String resultHashValue = sct20Factory.sendRawTransaction(systemEvn.KEYSTORE_FILENAME,  toSymId, sendAmount );
		return resultHashValue;
	}
	
	
	
	// sct 20 토큰 계약 생성 - method 0
	@RequestMapping(value="/sct20TokenCreate", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method0(@RequestParam String coinName , @RequestParam String coinSimpleName , @RequestParam String coinTotalSupply  ) throws Exception {
		
		String transactionResultHashValue = sct20Factory.sct20TokenCreate(systemEvn.KEYSTORE_FILENAME, coinName, coinSimpleName, coinTotalSupply);
		return transactionResultHashValue;
	}
	
	
	// sct 20 토큰 전송  - method 1
	@RequestMapping(value="/sct20TokenSend", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method1( @RequestParam String contractAddress , @RequestParam String toSymId , @RequestParam String sendAmount  ) throws Exception {
		String transactionResultHashValue = sct20Factory.sct20ToeknSend(systemEvn.KEYSTORE_FILENAME, contractAddress, toSymId, sendAmount );
		return transactionResultHashValue;
	}
	
	// 제 3자 토큰 교환(송금) - ( 스펜더 계정에서 특정 SymId에게 송금하기 )  - method 2
	@RequestMapping(value="/sct20SpenderSendToken", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method2(  @RequestParam String contractAddress , @RequestParam String toSymId , @RequestParam String amountValue  ) throws Exception {
		String transactionResultHashValue = sct20Factory.sct20SpenderSendToken(systemEvn.KEYSTORE_FILENAME, contractAddress, toSymId, amountValue);
		return transactionResultHashValue;
	}
	
	// 제 3자 토큰 승인 ( 스펜더 설정 )  - 제 3자 토큰 승인을 해야  제 3자 토큰 교환을 할 수 있습니다.  - method 3
	@RequestMapping(value="/sct20SpenderAssign", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method3(  @RequestParam String contractAddress , @RequestParam String spenderSymId , @RequestParam String amountValue   ) throws Exception {
		String transactionResultHashValue = sct20Factory.sct20SpenderAssign(systemEvn.KEYSTORE_FILENAME, contractAddress, spenderSymId , amountValue );
		return transactionResultHashValue;
	}
	
	// sct 20 토큰 추가 발행 ( 토큰 총량 증가하기 )  - method 4
	@RequestMapping(value="/sct20TokenTotalSupplyAdd", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method4(  @RequestParam String contractAddress , @RequestParam String amountValue  ) throws Exception {
		String transactionResultHashValue = sct20Factory.sct20TokenTotalSupplyAdd(systemEvn.KEYSTORE_FILENAME, contractAddress, amountValue );
		return transactionResultHashValue;
	}
	
	// sct 20 토큰 태움 - ( 토큰 총량 감소하기 )  - method 5
	@RequestMapping(value="/sct20TokenTotalSupplyBurn", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method5(  @RequestParam String contractAddress , @RequestParam String toSymId , @RequestParam String amountValue  ) throws Exception {
		String transactionResultHashValue = sct20Factory.sct20TokenTotalSupplyBurn(systemEvn.KEYSTORE_FILENAME, contractAddress, amountValue );
		return transactionResultHashValue;
	}
	
	// sct 20 토큰 거래 일시 정지  - method 6
	@RequestMapping(value="/sct20TokenTradingPause", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method6(  @RequestParam String contractAddress , @RequestParam String toSymId , @RequestParam String amountValue  ) throws Exception {
		String transactionResultHashValue = sct20Factory.sct20TokenTradingPause(systemEvn.KEYSTORE_FILENAME, contractAddress);
		return transactionResultHashValue;
	}
	
	
	// sct 20 토큰 거래 일시 정지  해제  - method 7
	@RequestMapping(value="/sct20TokenTradingPauseRelease", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method7(  @RequestParam String contractAddress , @RequestParam String toSymId , @RequestParam String amountValue  ) throws Exception {
		String transactionResultHashValue = sct20Factory.sct20TokenTradingPauseRelease(systemEvn.KEYSTORE_FILENAME, contractAddress);
		return transactionResultHashValue;
	}
	
	
	
}
