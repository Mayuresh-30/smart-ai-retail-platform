package com.smart_ai_retail_platform.backend.common.exception;

public class CustomerAlreadyExistException extends RuntimeException{
    public CustomerAlreadyExistException(Long customerId){
        super("Customer is Already Exist with id : " + customerId);
    }
}
