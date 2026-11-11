package com.smart_ai_retail_platform.backend.customer.mapper;

import com.smart_ai_retail_platform.backend.customer.dto.CustomerRequest;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerResponse;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerUpdateRequest;
import com.smart_ai_retail_platform.backend.customer.entity.Customer;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shop", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toCustomerEntity(CustomerRequest request);

    CustomerResponse toCustomerResponse(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shop", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            CustomerUpdateRequest request,
            @MappingTarget Customer customer
    );
}

//now go for the service implementation