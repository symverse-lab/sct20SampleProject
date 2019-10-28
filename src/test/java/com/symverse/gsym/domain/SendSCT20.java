package com.symverse.gsym.domain;

import org.web3j.protocol.core.Response;

public class SendSCT20 extends Response<String>{

	public String getHash() {
        return getResult();
    }

}
