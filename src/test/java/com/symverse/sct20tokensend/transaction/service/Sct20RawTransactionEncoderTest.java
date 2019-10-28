package com.symverse.sct20tokensend.transaction.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import com.symverse.common.CommUtil;
import com.symverse.sct20tokensend.transaction.domain.Sct20SendRawTransactionTest;


public class Sct20RawTransactionEncoderTest {
	
	static final Logger logger = LoggerFactory.getLogger(Sct20RawTransactionEncoderTest.class);
	
	public static final byte[] TRUE = {0x01};
	public static final byte[] FALSE = {};
	
	

	
    public static byte[] signMessage(Sct20SendRawTransactionTest sct20SendRawTransaction, byte chainId, Credentials credentials ) {
        byte[] encodedTransaction = hashEncode(sct20SendRawTransaction, chainId  );
        Sign.SignatureData signatureData = Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
        Sign.SignatureData signSignatureData = new Sign.SignatureData(signatureData.getV(), signatureData.getR(), signatureData.getS());
        return encode(sct20SendRawTransaction, signSignatureData );
    }

    public static byte[] hashEncode(Sct20SendRawTransactionTest sct20SendRawTransaction, byte chainId  ) {
        Sign.SignatureData signatureData = new Sign.SignatureData(chainId, new byte[] {}, new byte[] {});
        return hashEncode(sct20SendRawTransaction, signatureData );
    }

    private static byte[] hashEncode(Sct20SendRawTransactionTest sct20SendRawTransaction, Sign.SignatureData signatureData) {
		List<RlpType> values = hashAsRlpValues(sct20SendRawTransaction, signatureData );        
		RlpList rlpList = new RlpList(values);
		return RlpEncoder.encode(rlpList);
    }
    
    public static byte[] encode(Sct20SendRawTransactionTest sct20SendRawTransaction, byte chainId) {
        Sign.SignatureData signatureData = new Sign.SignatureData(chainId, new byte[] {}, new byte[] {});
        return encode(sct20SendRawTransaction, signatureData);
    }

    private static byte[] encode(Sct20SendRawTransactionTest sct20SendRawTransaction, Sign.SignatureData signatureData ) {
		List<RlpType> values = hashAsRlpValues(sct20SendRawTransaction, signatureData);
		RlpList rlpList = new RlpList(values);
		return RlpEncoder.encode(rlpList);
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
    	   contents.add( RlpString.create(sct20SendRawTransaction.getInput().getParams().get(0)));  // 계약 이름 영뮨 20자 이내
    	   contents.add( RlpString.create(sct20SendRawTransaction.getInput().getParams().get(1)));  // 계약 심볼 영문 정확히 3글자
    	   contents.add( RlpString.create(new BigInteger( sct20SendRawTransaction.getInput().getParams().get(2)) ));  // 토큰  총 발행량
    	   contents.add( RlpString.create(Numeric.hexStringToByteArray( sct20SendRawTransaction.getInput().getParams().get(3)) ));  // 계약 소유자
    	   setValue.add( new RlpList(contents));	   
    	   return  RlpEncoder.encode( new RlpList(setValue));
    	   
    }
   
    
    /**
     * @param Sct20SendRawTransactionTest methodType 1 , 3 , 4, 5  ( 1 - 토큰 송금 , 3 - 제 3자 토큰 승인 ( 스펜더 지정 ) , 4 - 토큰 추가 발행 , 5 - 토큰 태움 ) 
     * @return 
     */
    static byte[] hashInputRlpMethodType1and3and4and5_Values(Sct20SendRawTransactionTest sct20SendRawTransaction) {  
    	   List<RlpType> setValue = new ArrayList<>();
    	   setValue.add( RlpString.create( Numeric.hexStringToByteArray( CommUtil.strintDecimaltoHexValueConvert ( sct20SendRawTransaction.getInput().getSctType() )))  );
           setValue.add( RlpString.create( Numeric.toBigInt( sct20SendRawTransaction.getInput().getMethod() ) ));
    	   List<RlpType> contents = new ArrayList<RlpType>();
    	   contents.add( RlpString.create(Numeric.hexStringToByteArray(  sct20SendRawTransaction.getInput().getParams().get(0)) ));  // 수신자 SymId
    	   contents.add( RlpString.create(new BigInteger( sct20SendRawTransaction.getInput().getParams().get(1)) ));  // 토큰 양  
    	   setValue.add( new RlpList(contents));	   
    	   return  RlpEncoder.encode(new RlpList(setValue));
    }
    
    /**
     * @param Sct20SendRawTransactionTest methodType 2  ( 제 3자 토큰 송금 - 제 3자 토큰 승인한 ( 스펜더 ) 계정에서 토큰 송금 ) 
     * @return 
     */
    static byte[] hashInputRlpMethodType2_Values(Sct20SendRawTransactionTest sct20SendRawTransaction) {  
    	   List<RlpType> setValue = new ArrayList<>();
    	   setValue.add( RlpString.create( Numeric.hexStringToByteArray( CommUtil.strintDecimaltoHexValueConvert ( sct20SendRawTransaction.getInput().getSctType() )))  );
           setValue.add( RlpString.create( Numeric.toBigInt( sct20SendRawTransaction.getInput().getMethod() ) ));
    	   List<RlpType> contents = new ArrayList<RlpType>();
    	   contents.add( RlpString.create(Numeric.hexStringToByteArray(  sct20SendRawTransaction.getInput().getParams().get(0)) ));  // sct20 토큰을 생성한 SymId
    	   contents.add( RlpString.create(Numeric.hexStringToByteArray(  sct20SendRawTransaction.getInput().getParams().get(1)) ));  // 제 3자 토큰 승인을 할  SymId
    	   contents.add( RlpString.create(new BigInteger( sct20SendRawTransaction.getInput().getParams().get(2)) ));  // 제 3자에게 승인할 토큰양 
    	   setValue.add( new RlpList(contents));	   
    	   return  RlpEncoder.encode(new RlpList(setValue));
    }
    
    /**
     * @param sct20SendRawTransaction
     * Sct20SendRawTransactionTest methodType 6 , 7  ( 6 - 토큰 거래 일시 정지 , 7 - 토큰 거래 일시 정지 해제 ) 
     * @return
     */
    static byte[] hashInputRlpMethodType6and7_Values(Sct20SendRawTransactionTest sct20SendRawTransaction) {  
    	   List<RlpType> setValue = new ArrayList<>();
    	   setValue.add( RlpString.create( Numeric.hexStringToByteArray( CommUtil.strintDecimaltoHexValueConvert ( sct20SendRawTransaction.getInput().getSctType() )))  );
           setValue.add( RlpString.create( Numeric.toBigInt( sct20SendRawTransaction.getInput().getMethod() ) ));
    	   List<RlpType> contents = new ArrayList<RlpType>();
    	   setValue.add( new RlpList(contents));	   
    	   return  RlpEncoder.encode(new RlpList(setValue));
    }
    
    /**
     * @param sct20SendRawTransaction
     * 
     * @return List<RlpType>
     */
    static List<RlpType> hashWorkNodeRlpValues(Sct20SendRawTransactionTest sct20SendRawTransaction) {
       List<RlpType> result = new ArrayList<>();
       result.add(RlpString.create(Numeric.hexStringToByteArray(sct20SendRawTransaction.getWorkNode().get(0))));  // worknode1
       //result.add(RlpString.create(Numeric.hexStringToByteArray(couponRawTransaction.getWorkNodesList().get(1))));  // worknode2
       //result.add(RlpString.create(Numeric.hexStringToByteArray(couponRawTransaction.getWorkNodesList().get(2))));  // worknode3
       return result;
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
  
    
    static List<RlpType> hashAsRlpValues(Sct20SendRawTransactionTest sct20SendRawTransaction, Sign.SignatureData signatureData ) {
    	List<RlpType> result = new ArrayList<>();
    	
        result.add(RlpString.create( Numeric.hexStringToByteArray(sct20SendRawTransaction.getFrom())));
        result.add(RlpString.create( sct20SendRawTransaction.getNonce()) );
        result.add(RlpString.create( sct20SendRawTransaction.getGasPrice()));
        result.add(RlpString.create( sct20SendRawTransaction.getGasLimit()));
        if(sct20SendRawTransaction.getTo().equals("null")) {
       	 	result.add(RlpString.create( FALSE ));  // to 
        }else {
        	result.add(RlpString.create( Numeric.hexStringToByteArray(sct20SendRawTransaction.getTo()) ));  // to  
        }
        result.add(RlpString.create( sct20SendRawTransaction.getValue() )); //value
        String methodType =sct20SendRawTransaction.getInput().getMethod();
        if( methodType.equals("0") ) {
        	System.out.println("hashInputRlpMethodType0_Values start");
       	   result.add(RlpString.create( hashInputRlpMethodType0_Values(sct20SendRawTransaction) ) ); // input
        }else if(  methodType.equals("1") || methodType.equals("3") || methodType.equals("4")  || methodType.equals("5")  ) {
           result.add(RlpString.create( hashInputRlpMethodType1and3and4and5_Values(sct20SendRawTransaction) ) ); // input
        }else if(  methodType.equals("2") ) {
           result.add(RlpString.create( hashInputRlpMethodType2_Values(sct20SendRawTransaction) ) ); // input
        }else if(  methodType.equals("6") || methodType.equals("7") ) {
           result.add(RlpString.create( hashInputRlpMethodType6and7_Values(sct20SendRawTransaction) ) ); // input
        }
        result.add(RlpString.create( sct20SendRawTransaction.getType().equals("1") ? TRUE : FALSE ));
        result.add(new RlpList(hashWorkNodeRlpValues(sct20SendRawTransaction))); // worknodes
        if (signatureData.getV() == 0) {  // v
          result.add(RlpString.create(FALSE)); 
        } else {
       	  result.add(RlpString.create(BigInteger.valueOf(signatureData.getV())));
        }
        
        result.add(RlpString.create( Bytes.trimLeadingZeroes(signatureData.getR()) ));
        result.add(RlpString.create( Bytes.trimLeadingZeroes(signatureData.getS()) ));
        logger.debug("asRlpValues Finish");
        return result;
        
    }
    
   

}
