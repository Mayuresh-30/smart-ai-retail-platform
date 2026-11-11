package com.smart_ai_retail_platform.backend.common.exception;

public class ResourceAlreadyExistsException extends RuntimeException{
    public ResourceAlreadyExistsException(String msg){
        super(msg);
    }
}
