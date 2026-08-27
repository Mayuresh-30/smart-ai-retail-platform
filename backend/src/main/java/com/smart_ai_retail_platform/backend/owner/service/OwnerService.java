package com.smart_ai_retail_platform.backend.owner.service;

import com.smart_ai_retail_platform.backend.owner.dto.CreateOwnerRequest;
import com.smart_ai_retail_platform.backend.owner.dto.OwnerResponse;
import com.smart_ai_retail_platform.backend.owner.dto.UpdateOwnerRequest;



public interface OwnerService {
    OwnerResponse createOwner(CreateOwnerRequest request);

    OwnerResponse getOwner(Long ownerId);

    OwnerResponse updateOwner(
            Long ownerId,
            UpdateOwnerRequest request
    );
}
