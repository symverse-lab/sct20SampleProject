package com.symverse.sct20tokensend.transaction.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import com.symverse.common.CommUtil;
import com.symverse.gsym.domain.SendSCT20;
import com.symverse.sct20tokensend.transaction.domain.Sct20SendRawTransactionTest;
import com.symverse.sct20tokensend.transaction.domain.Sct20TempleteVOTest;


public class RlpEncodingTestSample {
	
	static final Logger logger = LoggerFactory.getLogger(RlpEncodingTestSample.class);
	
	public static final byte[] TRUE = {0x01};
	public static final byte[] FALSE = {};
	
	
	public static void main(String[] args) {
		Sct20SendRawTransactionTest sc = new Sct20SendRawTransactionTest();
	  new RlpEncodingTestSample().sct20SendRawTransaction(sc);
	}

	
	@Test // sct 20 토큰 계약 생성
	public void sct20_MethodCode_TEST()  {
		 
		 Sct20SendRawTransactionTest sct20SendRawTransaction	= new Sct20SendRawTransactionTest();                                          
		 this.sct20SendRawTransaction(sct20SendRawTransaction);	    
		 
		 
		 
	}
   /**
	 * citizen 등록
	 * @param citizenRawTransaction
	 * @return
     * @throws Exception 
	 */
	public void sct20SendRawTransaction (Sct20SendRawTransactionTest sct20SendRawTransaction ) {
		SendSCT20 sendSct20 = new SendSCT20();
		byte[] rlpText = RlpEncoder.encode(this.hashAsRlpValuesTest(sct20SendRawTransaction) );
		String rlpTextValue = Numeric.toHexString(rlpText);
		System.out.println("rlpEncoding:"+rlpTextValue);
		//TODO sign값 value로 설정 넣어주기
	}

    /**
     * @param sct20SendRawTransaction
     * 
     * @return List<RlpType>
     */
    static List<RlpType> hashWorkNodeRlpValuesTest(Sct20SendRawTransactionTest sct20SendRawTransaction) {
       List<RlpType> result = new ArrayList<>();
       result.add(RlpString.create(Numeric.hexStringToByteArray("0x00021000000000010002")));  // worknode1
       result.add(RlpString.create(Numeric.hexStringToByteArray("0x00021000000000080002")));  // worknode1
       result.add(RlpString.create(Numeric.hexStringToByteArray("0x00021000000000090002")));  // worknode1
       //result.add(RlpString.create(Numeric.hexStringToByteArray(couponRawTransaction.getWorkNodesList().get(1))));  // worknode2
       //result.add(RlpString.create(Numeric.hexStringToByteArray(couponRawTransaction.getWorkNodesList().get(2))));  // worknode3
       return result;
    }
  
    
    
    /**
     * @param Sct20SendRawTransactionTest methodType 0 ( SCT 20 생성 - 토큰 생성 ) 
     * @return 
     */
    static byte[] hashInputRlpMethodType0_Values(Sct20SendRawTransactionTest sct20SendRawTransaction) {
    	   List<RlpType> setValue = new ArrayList<>();
           setValue.add( RlpString.create( Numeric.hexStringToByteArray( CommUtil.strintDecimaltoHexValueConvert ( sct20SendRawTransaction.getInput().getSctType() )))  );
           setValue.add( RlpString.create( Numeric.toBigInt( sct20SendRawTransaction.getInput().getMethod() ) ));
    	   List<RlpType> contents = new ArrayList<RlpType>();
    	   contents.add( RlpString.create( sct20SendRawTransaction.getInput().getParams().get(0)));  // 계약 이름 영뮨 20자 이내
    	   contents.add( RlpString.create( sct20SendRawTransaction.getInput().getParams().get(1)));  // 계약 심볼 영문 정확히 3글자
    	   contents.add( RlpString.create( new BigInteger( sct20SendRawTransaction.getInput().getParams().get(2)) ));  // 토큰  총 발행량
    	   contents.add( RlpString.create( Numeric.hexStringToByteArray( sct20SendRawTransaction.getInput().getParams().get(3)) ));  // 계약 소유자
    	   setValue.add( new RlpList(contents));	   
    	   return  RlpEncoder.encode( new RlpList(setValue));
    }

    
    static RlpList hashAsRlpValuesTest(Sct20SendRawTransactionTest sct20SendRawTransaction ) {

        
        
    	 List<RlpType> result = new ArrayList<>();
		 Sct20TempleteVOTest inputParam = new Sct20TempleteVOTest();                                                                        
		 inputParam.setSctType("20"); // 20 , 30 , 40  -> Symverse Engine에서는 SCT20 , SCT30 , SCT40이 있으며                                                                               
		 inputParam.setMethod("0");  // 0 , 1 , 2, 3, 4, 5, 6 , 7                                                                   
		 ArrayList<String> paramsArray = new ArrayList<String>();                                                                   
		 // input name , symbol , totalsupply , ownersymid                                                                          
		 paramsArray.add("TestToken");  // 코인 이름                                                                                    
		 paramsArray.add("TXT");  // 코인이름 약어   - 무조건 3자리의 영문으로 표시합니다.                                                                                      
		 paramsArray.add("10000"); // 총 코인 수량                                                                                       
		 paramsArray.add("0002c7daf74c6c120002"); // 소유주 symId  //00022000000000270002                                                     
		 inputParam.setParams(paramsArray);                                                                                         
		 sct20SendRawTransaction.setInput(inputParam);                              
         result.add(RlpString.create( hashInputRlpMethodType0_Values(sct20SendRawTransaction) ) ); // input
         
         return  new RlpList(result);
         

 		/*
 		 * List<RlpType> result = new ArrayList<>(); result.add(RlpString.create(
 		 * Numeric.hexStringToByteArray("0x00028f373ec3b7800002")) );
 		 * result.add(RlpString.create( Numeric.hexStringToByteArray("0x01")) );
 		 * result.add(RlpString.create( Numeric.hexStringToByteArray("0x03f5476a00")) );
 		 * result.add(RlpString.create( Numeric.hexStringToByteArray("0x027f4b")) );
 		 * result.add(RlpString.create(
 		 * Numeric.hexStringToByteArray("0x0002c7daf74c6c120002")) );
 		 * result.add(RlpString.create(
 		 * Numeric.hexStringToByteArray("0x2a45907d1bef7c00")) );
 		 * result.add(RlpString.create( FALSE )); // to result.add(RlpString.create( new
 		 * BigInteger("0")) ); // result.add(RlpString.create(
 		 * sct20SendRawTransaction.getType().equals("1") ? TRUE : FALSE ));
 		 * result.add(new RlpList(hashWorkNodeRlpValuesTest(sct20SendRawTransaction)));
 		 * // worknodes result.add(RlpString.create( new BigInteger("2")) ); //v
 		 * result.add(RlpString.create("")); result.add(RlpString.create(""));
 		 * logger.debug("asRlpValues Finish");
 		 */
         
         //0xf8518a00028f373ec3b7800002018503f5476a0083027f4b8a0002c7daf74c6c120002882a45907d1bef7c008080e18a000210000000000100028a000210000000000800028a00021000000000090002028080
         //0xf8518a00028f373ec3b7800002018503f5476a0083027f4b8a0002c7daf74c6c120002882a45907d1bef7c008030e18a000210000000000100028a000210000000000800028a00021000000000090002023030
        
        
        
    }

	 



}
