package com.smart_ai_retail_platform.backend.common.exception;

public class ShopNotFoundException extends RuntimeException{
    public ShopNotFoundException(Long shopId){
        super("Shop not found with id : " + shopId);
    }
}
