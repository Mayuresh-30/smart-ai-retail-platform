package com.smart_ai_retail_platform.backend.common.exception;

public class CustomerAlreadyActiveException extends RuntimeException{
    public CustomerAlreadyActiveException(Long customerId) {
        super("Customer is already active with id: " + customerId);
    }
}
