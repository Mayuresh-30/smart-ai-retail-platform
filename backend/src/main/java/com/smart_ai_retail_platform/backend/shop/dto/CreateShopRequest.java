package com.smart_ai_retail_platform.backend.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Why no ownerId?
//Because our endpoint already contains it:
//POST /api/owners/{ownerId}/shop
//to avoid duplication , already url provides owners Id
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateShopRequest {
    @NotBlank
    @Size(max = 50)
    private String shopName;

    @Size(max = 200)
    private String description;

    @NotBlank
    @Size(max = 255)
    private String address;
}
