package com.smart_ai_retail_platform.backend.owner.controller;

import com.smart_ai_retail_platform.backend.common.response.ApiResponse;
import com.smart_ai_retail_platform.backend.owner.dto.CreateOwnerRequest;
import com.smart_ai_retail_platform.backend.owner.dto.OwnerResponse;
import com.smart_ai_retail_platform.backend.owner.dto.UpdateOwnerRequest;
import com.smart_ai_retail_platform.backend.owner.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<ApiResponse<OwnerResponse>> createOwner(
            @Valid @RequestBody CreateOwnerRequest request
    ) {
        OwnerResponse response = ownerService.createOwner(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Owner Created Successfully", response));
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<ApiResponse<OwnerResponse>> getOwner(
            @PathVariable Long ownerId
    ) {
        OwnerResponse response = ownerService.getOwner(ownerId);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Owner Fetched Successfully", response));
    }

    @PutMapping("/{ownerId}")
    public ResponseEntity<ApiResponse<OwnerResponse>> updateOwner(
            @PathVariable Long ownerId,
            @Valid @RequestBody UpdateOwnerRequest request
    ) {
        OwnerResponse response = ownerService.updateOwner(ownerId, request);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Owner Updated Successfully", response));
    }
}
