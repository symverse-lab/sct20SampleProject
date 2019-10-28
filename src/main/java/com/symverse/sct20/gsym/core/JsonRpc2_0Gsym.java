package com.symverse.sct20.gsym.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.utils.Async;

import com.symverse.sct20.gsym.domain.SendSct20;

public class JsonRpc2_0Gsym implements Gsym{

	public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

	protected final Web3jService web3jService;
	private final long blockTime;
	private final ScheduledExecutorService scheduledExecutorService;

	public JsonRpc2_0Gsym(Web3jService web3jService) {
        this(web3jService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

	public JsonRpc2_0Gsym(
            Web3jService web3jService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        this.web3jService = web3jService;
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

	

	@Override
	public void shutdown() {

		scheduledExecutorService.shutdown();
        try {
            web3jService.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close web3j service", e);
        }

	}

	@Override
	public Request<?, SendSct20> sct20SendRawTransaction(String signedTransactionData) {
		return new Request<>(
                "sym_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                web3jService,
                SendSct20.class);
	}

	
	

}


