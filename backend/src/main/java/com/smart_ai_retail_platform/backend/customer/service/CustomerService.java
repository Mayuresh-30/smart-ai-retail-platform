package com.smart_ai_retail_platform.backend.customer.service;

import com.smart_ai_retail_platform.backend.customer.dto.CustomerRequest;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerResponse;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    //endpoint eventually be -> post /api/shops/shopId/customers

    //create_customer(shopId,request)
    //get_customer(shopId,customerId)
    //get_customers(shopID,pageable)
    //update_customer(shopId, customerId, request)
    //search_customers(shopId, pageable)
    //activate_customer(shopId, customerId)
    //deactivate_customer()

    //we will construct it later after the successfull
    //completion of all the above service methods with testings
    //search_customer(shopId, query, pageable)
    //and more ...



    CustomerResponse createCustomer(
            Long shopId,
            CustomerRequest request
    );

    CustomerResponse getCustomer(
            Long shopId,
            Long customerId
    );

    Page<CustomerResponse> getCustomers(
            Long shopId,
            Pageable pageable
    );

    Page<CustomerResponse> searchCustomers(
            Long shopId,
            String query,
            Pageable pageable
    );

    CustomerResponse updateCustomer(
            Long shopId,
            Long customerId,
            CustomerUpdateRequest request
    );

    CustomerResponse activateCustomer(
            Long shopId,
            Long customerId
    );

    CustomerResponse deactivateCustomer(
            Long shopId,
            Long customerId
    );
}
