package com.symverse.gsym.domain;

import org.web3j.protocol.core.Response;

public class SendCoupon extends Response<String>{

	public String getCitieznHash() {
        return getResult();
    }

}
