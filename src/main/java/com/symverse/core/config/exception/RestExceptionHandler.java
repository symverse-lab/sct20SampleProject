package com.symverse.core.config.exception;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.symverse.common.model.TimeUtil;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {



	// 500
    @ExceptionHandler({ ExecutionException.class })
    @ResponseBody
    public String  executionException(final Exception ex, HttpServletRequest req) throws Exception {
    	String dateTime = TimeUtil.currentDateFormat("yyyymmdd");
        logger.info(ex.getClass().getName());
        logger.error("ExecutionException error", ex);
        //
     //   final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , ex.getLocalizedMessage(),  Arrays.asList("error occurred") ,   Arrays.asList("error occurred") , request.getLocale().toString() );
     //   return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getHttpStatusCode(), request);
        final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , "error",  ex.getClass().getName() ,   ex.getMessage() ,req.getRequestURL().toString() );
        String result = new ObjectMapper().writeValueAsString(apiError);
        return result;
    }

    
    // 500
    @ExceptionHandler({ BadRequestException.class })
    @ResponseBody
    public String  badRequestException(final Exception ex, HttpServletRequest req) throws Exception {
    	String dateTime = TimeUtil.currentDateFormat("yyyymmdd");
        logger.info(ex.getClass().getName());
        logger.info(ex.getMessage());
        logger.info(req.getRequestURL().toString());
        logger.error("BadRequestException error", ex);
        
        
     //   final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , ex.getLocalizedMessage(),  Arrays.asList("error occurred") ,   Arrays.asList("error occurred") , request.getLocale().toString() );
     //   return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getHttpStatusCode(), request);
        final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , "error", ex.getClass().getName() ,   ex.getMessage() ,req.getRequestURL().toString() );
        String result = new ObjectMapper().writeValueAsString(apiError);
        return result;
    }

    // 500
    @ExceptionHandler({ RuntimeException.class })
    @ResponseBody
    public String httpMessageNotReadableException(final Exception ex, HttpServletRequest req) throws Exception {
    	String dateTime = TimeUtil.currentDateFormat("yyyymmdd");
        logger.info(ex.getClass().getName());
        logger.error("RuntimeException error:real", ex);
        //
     //   final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , ex.getLocalizedMessage(),  Arrays.asList("error occurred") ,   Arrays.asList("error occurred") , request.getLocale().toString() );
     //   return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getHttpStatusCode(), request);
        final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , "error", ex.getClass().getName(),   ex.getMessage() ,req.getRequestURL().toString() );
        String result = new ObjectMapper().writeValueAsString(apiError);
        return result;
    }
    

    // 500
    @ExceptionHandler({ NullPointerException.class })
    @ResponseBody
    public String nullPointException(final Exception ex, HttpServletRequest req) throws Exception {
    	String dateTime = TimeUtil.currentDateFormat("yyyymmdd");
        logger.info(ex.getClass().getName());
        logger.error("NullPointerException error:real", ex);
        //
     //   final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , ex.getLocalizedMessage(),  Arrays.asList("error occurred") ,   Arrays.asList("error occurred") , request.getLocale().toString() );
     //   return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getHttpStatusCode(), request);
        final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , "error", ex.getClass().getName(),   ex.getMessage() ,req.getRequestURL().toString() );
        String result = new ObjectMapper().writeValueAsString(apiError);
        return result;
    }
    
    @ExceptionHandler({ JsonMappingException.class })
    @ResponseBody
    public String jsonMappingException(final Exception ex, HttpServletRequest req) throws Exception {
    	String dateTime = TimeUtil.currentDateFormat("yyyymmdd");
        logger.info(ex.getClass().getName());
        logger.error("JsonMappingException error:real", ex);
        //
     //   final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , ex.getLocalizedMessage(),  Arrays.asList("error occurred") ,   Arrays.asList("error occurred") , request.getLocale().toString() );
     //   return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getHttpStatusCode(), request);
        final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , "error", ex.getClass().getName(),   ex.getMessage() ,req.getRequestURL().toString() );
        String result = new ObjectMapper().writeValueAsString(apiError);
        return result;
    }
	
    
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String exception(final Exception ex, HttpServletRequest req) throws Exception {
    	String dateTime = TimeUtil.currentDateFormat("yyyymmdd");
        logger.info(ex.getClass().getName());
        logger.error("Exception error:real", ex);
        //
     //   final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , ex.getLocalizedMessage(),  Arrays.asList("error occurred") ,   Arrays.asList("error occurred") , request.getLocale().toString() );
     //   return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getHttpStatusCode(), request);
        final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , "error",  ex.getClass().getName() ,   ex.getMessage() ,req.getRequestURL().toString() );
        String result = new ObjectMapper().writeValueAsString(apiError);
        return result;
    }
    
    
    
    
    
    
 // 500
    @ExceptionHandler({ ServerErrorException.class })
    @ResponseBody
    public String serverErrorException(final Exception ex,HttpServletRequest req) throws Exception {
    	String dateTime = TimeUtil.currentDateFormat("yyyymmdd");
        logger.info(ex.getClass().getName());
        logger.error("ServerErrorException error:real", ex);
        //
     //   final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , ex.getLocalizedMessage(),  Arrays.asList("error occurred") ,   Arrays.asList("error occurred") , request.getLocale().toString() );
     //   return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getHttpStatusCode(), request);
        final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , "error", ex.getClass().getName() ,   ex.getMessage() ,req.getRequestURL().toString() );
        String result = new ObjectMapper().writeValueAsString(apiError);
        return result;
    }
    
    
    // 500
    @ExceptionHandler({ SQLException.class })
    public String sqlException(final Exception ex,HttpServletRequest req) throws Exception{
    	String dateTime = TimeUtil.currentDateFormat("yyyymmdd");
        logger.info(ex.getClass().getName());
        logger.error("SQLException error:real", ex);
        //
     //   final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , ex.getLocalizedMessage(),  Arrays.asList("error occurred") ,   Arrays.asList("error occurred") , request.getLocale().toString() );
     //   return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getHttpStatusCode(), request);
        final APIResponseException apiError = new APIResponseException(dateTime , HttpStatus.BAD_REQUEST , "error", ex.getClass().getName(),   ex.getMessage() ,req.getRequestURL().toString() );
        String result = new ObjectMapper().writeValueAsString(apiError);
        return result;
    }
    
    




}
