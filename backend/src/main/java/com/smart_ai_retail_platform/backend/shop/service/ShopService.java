package com.smart_ai_retail_platform.backend.shop.service;

import com.smart_ai_retail_platform.backend.shop.dto.CreateShopRequest;
import com.smart_ai_retail_platform.backend.shop.dto.ShopResponse;
import com.smart_ai_retail_platform.backend.shop.dto.UpdateShopRequest;

public interface ShopService {
    ShopResponse createShop(
            Long ownerId,
            CreateShopRequest request
    );

    ShopResponse getShop(Long ownerId);

    ShopResponse updateShop(
            Long ownerId,
            UpdateShopRequest request
    );

}

