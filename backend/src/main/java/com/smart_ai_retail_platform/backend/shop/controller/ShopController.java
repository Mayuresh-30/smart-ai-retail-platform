package com.smart_ai_retail_platform.backend.shop.controller;

import com.smart_ai_retail_platform.backend.common.response.ApiResponse;
import com.smart_ai_retail_platform.backend.shop.dto.CreateShopRequest;
import com.smart_ai_retail_platform.backend.shop.dto.ShopResponse;
import com.smart_ai_retail_platform.backend.shop.dto.UpdateShopRequest;
import com.smart_ai_retail_platform.backend.shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners/{ownerId}/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShopResponse>> createShop(
            @PathVariable Long ownerId,
            @Valid @RequestBody CreateShopRequest request
    ) {
        ShopResponse response = shopService.createShop(ownerId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Shop Created Successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ShopResponse>> getShop(
            @PathVariable Long ownerId
    ) {
        ShopResponse response = shopService.getShop(ownerId);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Shop Fetched Successfully", response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ShopResponse>> updateShop(
            @PathVariable Long ownerId,
            @Valid @RequestBody UpdateShopRequest request
    ) {
        ShopResponse response = shopService.updateShop(ownerId, request);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Shop Updated Successfully", response));
    }
}
