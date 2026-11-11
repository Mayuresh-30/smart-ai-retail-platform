package com.smart_ai_retail_platform.backend.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateRequest {


    @NotBlank(message = "Customer name is required")
    @Size(max = 50)
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+()\\- ]{7,20}$")
    private String phone;
}
