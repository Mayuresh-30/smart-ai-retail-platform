package com.smart_ai_retail_platform.backend.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponse {
    private Long id;
    private String shopName;
    private String description;
    private String address;
    private OwnerSummaryResponse owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
//okay dtos are made correctly , now we should design the mapper