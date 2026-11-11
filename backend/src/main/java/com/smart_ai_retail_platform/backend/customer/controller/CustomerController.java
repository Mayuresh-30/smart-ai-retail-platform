package com.smart_ai_retail_platform.backend.customer.controller;

import com.smart_ai_retail_platform.backend.common.response.ApiResponse;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerRequest;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerResponse;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerUpdateRequest;
import com.smart_ai_retail_platform.backend.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops/{shopId}/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @PathVariable Long shopId,
            @Valid @RequestBody CustomerRequest request
    ) {
        CustomerResponse response = customerService.createCustomer(shopId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Customer Created Successfully", response));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(
            @PathVariable Long shopId,
            @PathVariable Long customerId
    ) {
        CustomerResponse response = customerService.getCustomer(shopId, customerId);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Customer Fetched Successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getCustomers(
            @PathVariable Long shopId,
            @RequestParam(required = false) String query,
            Pageable pageable
    ) {
        Page<CustomerResponse> response = StringUtils.hasText(query)
                ? customerService.searchCustomers(shopId, query, pageable)
                : customerService.getCustomers(shopId, pageable);

        String message = StringUtils.hasText(query)
                ? "Customers Searched Successfully"
                : "Customers Fetched Successfully";

        return ResponseEntity.ok(new ApiResponse<>(true, message, response));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long shopId,
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        CustomerResponse response = customerService.updateCustomer(shopId, customerId, request);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Customer Updated Successfully", response));
    }

    @PutMapping("/{customerId}/activate")
    public ResponseEntity<ApiResponse<CustomerResponse>> activateCustomer(
            @PathVariable Long shopId,
            @PathVariable Long customerId
    ) {
        CustomerResponse response = customerService.activateCustomer(shopId, customerId);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Customer Activated Successfully", response));
    }

    @PutMapping("/{customerId}/deactivate")
    public ResponseEntity<ApiResponse<CustomerResponse>> deactivateCustomer(
            @PathVariable Long shopId,
            @PathVariable Long customerId
    ) {
        CustomerResponse response = customerService.deactivateCustomer(shopId, customerId);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Customer Deactivated Successfully", response));
    }
}
