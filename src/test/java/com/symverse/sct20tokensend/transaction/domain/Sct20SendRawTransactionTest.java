package com.symverse.sct20tokensend.transaction.domain;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sct20SendRawTransactionTest {

   // 이더리움 TX 구조
   // [
   //     "from" : "" , 0
   //     "nonce" : "" , 1
   //     "gasPrice" : "" , 2
   //     "gasLimit" : "" , 3
   //     "to" : "" ,  4
   //     "value" : "" , 5
   //     "input" : "" ,  6
   //     "type" : "" , 7
   //     "workNode" : "" , 8
   //     "v" : "" , 9
   //     "r" : "" , 10 
   //     "s" : "" 11
   // ]
	                           

   private String from        ; 
   private BigInteger nonce    ;
   private BigInteger gasPrice   ; 
   private BigInteger gasLimit   ; 
   private String to     ; 
   private BigInteger value      ;
   private Sct20TempleteVOTest input ;
   private String type;
   private List<String> workNode;
   
   
   // input의 파라미터는 다음과 같다.
   // template = [ 
   //   “type”: “0x0”, 
   // 	“method”: “0x0”,
   //  	“params = ["SymverseCoin", "SYM", "0x5000000000000000", "0x0000000001” ]
   // ]
   
    @SuppressWarnings("unchecked")
	private static String readFrom() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();

		File file;
		Map<String, String> jsonFile = new HashMap<>();

		try {
			file = new ClassPathResource("keystore/keystore.json").getFile();
			jsonFile = objectMapper.readValue(file, Map.class);
		} catch (IOException e) {
			throw new Exception("key store file을 찾을 수 없습니다.");
		}

		return jsonFile.get("address");
	}

}
