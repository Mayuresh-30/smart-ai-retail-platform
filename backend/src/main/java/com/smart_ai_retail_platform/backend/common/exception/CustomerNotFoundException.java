package com.smart_ai_retail_platform.backend.common.exception;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(Long customerId){
        super("Customer not found with id : " + customerId);
    }
}
