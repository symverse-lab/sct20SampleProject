package com.symverse.sct20.gsym.core;

import java.util.concurrent.ScheduledExecutorService;

import org.web3j.protocol.Web3jService;

public interface Gsym extends GsymApi{

	/**
     * Construct a new Web3j instance.
     *
     * @param web3jService web3j service instance - i.e. HTTP or IPC
     * @return new Web3j instance
     */
    static JsonRpc2_0Gsym build(Web3jService web3jService) {
        return new JsonRpc2_0Gsym(web3jService);
    }

    /**
     * Construct a new Web3j instance.
     *
     * @param web3jService web3j service instance - i.e. HTTP or IPC
     * @param pollingInterval polling interval for responses from network nodes
     * @param scheduledExecutorService executor service to use for scheduled tasks.
     *                                 <strong>You are responsible for terminating this thread
     *                                 pool</strong>
     * @return new Web3j instance
     */
    static JsonRpc2_0Gsym build(
            Web3jService web3jService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Gsym(web3jService, pollingInterval, scheduledExecutorService);
    }

    /**
     * Shutdowns a Web3j instance and closes opened resources.
     */
    void shutdown();

}
