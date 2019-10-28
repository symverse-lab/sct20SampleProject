package com.symverse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.symverse.sct20.transaction.service.Sct20Factory;


@RestController
@RequestMapping("/sct20")
public class Sct20Controller {

	
	@Value("${ca.node.url}")	private String engineNodeRequsetUrl;
	
	@Autowired
	private Sct20Factory sct40Factory;
	
	
	
	@RequestMapping(value="/method0", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method0(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_0  참조해서 구현
		return "method0";
	}
	
	@RequestMapping(value="/method1", method=RequestMethod.GET, consumes = { "application/json" }, produces = "application/json" )
	@ResponseBody
	public Object method1(  ) throws Exception {
		// Sct20SendRawTransactionServiceTest.class - sct20_MethodCode_1  참조해서 구현
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
