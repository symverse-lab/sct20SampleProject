package com.symverse.core.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 5457020071021462699L;


	private Object invoke;
    private int errorCode;

    public BadRequestException(String message){
        super(message);
    }

    public BadRequestException(String message, Object invoke){
        super(message);
        this.invoke = invoke;
    }

    public BadRequestException(String message, Object invoke, Integer errorCode){
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
