package com.symverse.core.config.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class APIResponseException  {
	// {
	//     "timestamp": "0000-00-00",
	//     "status": 500,
	//     "httpStatus": "error message",
	//     "message": "error",
	//     "message": "/~~"
	// }
	
	//    기존 오류날때 
	//    "httpStatus": "INTERNAL_SERVER_ERROR",
	//    "message": "error",
	//    "result": "Exception"
	
	private static final long serialVersionUID =  5457020071021462699L; 
	
	//private String  result;
	private String  timestamp; 
	private HttpStatus httpStatus; // 200 http코드 // 숫자
	private String  message ; // error message 어떠한 에러가 났는지
	private String  cssCode ; // 추후에  method 어떤 종류의 에러가 났는지 ? db error 인지 , 디테일한 에러 
	private String   cssMessage ;  // csscode 에러가
	private String  path ;  // 
	
	
	
	
}
