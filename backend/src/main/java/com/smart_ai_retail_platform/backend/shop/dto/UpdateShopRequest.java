package com.smart_ai_retail_platform.backend.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateShopRequest {
    @NotBlank
    @Size(max = 100)
    private String shopName;

    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 255)
    private String address;
}
