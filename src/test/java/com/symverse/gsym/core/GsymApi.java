package com.symverse.gsym.core;

import org.web3j.protocol.core.Request;

import com.symverse.gsym.domain.SendCoupon;
import com.symverse.gsym.domain.SendSCT20;

public interface GsymApi {

    
    public Request<?, SendSCT20> sct20SendRawTransaction (String signedTransactionData);
    /*
     * 
     * 

    public Request<?, Response> symGetRawCitizenByHash(String hash);

    public Request<?, Response> symGetRawCitizenBySymID(String symId);

    public Request<?, Response> symGetCitizens();

    public Request<?, Response> symClientVerseion();*/

}
