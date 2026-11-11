package com.smart_ai_retail_platform.backend.common.exception;

public class CustomerAlreadyInactiveException extends RuntimeException{
    public CustomerAlreadyInactiveException (Long customerId) {
        super("Customer is already inactive with id: " + customerId);
    }
}
