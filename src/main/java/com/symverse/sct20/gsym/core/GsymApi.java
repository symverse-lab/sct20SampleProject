package com.symverse.sct20.gsym.core;

import org.web3j.protocol.core.Request;

import com.symverse.sct20.gsym.domain.SendSct20;

public interface GsymApi {

    //추가 20190426
    public Request<?, SendSct20> sct20SendRawTransaction (String signedTransactionData);

    
    /*
     * 
     * 

    public Request<?, Response> symGetRawCitizenByHash(String hash);

    public Request<?, Response> symGetRawCitizenBySymID(String symId);

    public Request<?, Response> symGetCitizens();

    public Request<?, Response> symClientVerseion();*/

}
