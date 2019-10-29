package com.symverse.sct20.gsym.domain;

import org.web3j.protocol.core.Response;

public class SendSct20 extends Response<String>{

	public String getHash() {
        return getResult();
    }

}
