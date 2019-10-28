package com.symverse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerErrorException extends RuntimeException {

	private static final long serialVersionUID = 5457020071021462699L;


	private Object invoke;
    private int errorCode;

    public ServerErrorException(String message){
        super(message);
    }

    public ServerErrorException(String message, Object invoke){
        super(message);
        this.invoke = invoke;
    }

    public ServerErrorException(String message, Object invoke, Integer errorCode){
        super(message);
        this.invoke = invoke;
        this.errorCode = errorCode;
    }

    public Object getInvoke() {
        return invoke;
    }

    public Object getErrorCode() {
        return errorCode;
    }

}
