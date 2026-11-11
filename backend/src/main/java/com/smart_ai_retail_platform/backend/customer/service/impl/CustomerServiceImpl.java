package com.smart_ai_retail_platform.backend.customer.service.impl;

import com.smart_ai_retail_platform.backend.common.exception.CustomerAlreadyActiveException;
import com.smart_ai_retail_platform.backend.common.exception.CustomerAlreadyInactiveException;
import com.smart_ai_retail_platform.backend.common.exception.CustomerNotFoundException;
import com.smart_ai_retail_platform.backend.common.exception.ShopNotFoundException;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerRequest;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerResponse;
import com.smart_ai_retail_platform.backend.customer.dto.CustomerUpdateRequest;
import com.smart_ai_retail_platform.backend.customer.entity.Customer;
import com.smart_ai_retail_platform.backend.customer.mapper.CustomerMapper;
import com.smart_ai_retail_platform.backend.customer.repository.CustomerRepository;
import com.smart_ai_retail_platform.backend.customer.service.CustomerService;
import com.smart_ai_retail_platform.backend.shop.entity.Shop;
import com.smart_ai_retail_platform.backend.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final CustomerMapper customerMapper;


    @Override
    @Transactional
    public CustomerResponse createCustomer(
            Long shopId,
            CustomerRequest request
    ) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() ->
                        new ShopNotFoundException(
                                 shopId
                        )
                );

        Customer customer =
                customerMapper.toCustomerEntity(request);

        customer.setShop(shop);
        customer.setActive(true);

        Customer savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toCustomerResponse(savedCustomer);
    }


    @Override
    public CustomerResponse getCustomer(
            Long shopId,
            Long customerId
    ) {

        Customer customer =
                customerRepository.findByIdAndShopId(
                        customerId,
                        shopId
                ).orElseThrow(() ->
                        new CustomerNotFoundException(customerId)
                );

        return customerMapper.toCustomerResponse(customer);
    }


    @Override
    public Page<CustomerResponse> getCustomers(
            Long shopId,
            Pageable pageable
    ) {

        verifyShopExists(shopId);

        return customerRepository
                .findAllByShopId(shopId, pageable)
                .map(customerMapper::toCustomerResponse);
    }


    @Override
    public Page<CustomerResponse> searchCustomers(
            Long shopId,
            String query,
            Pageable pageable
    ) {

        verifyShopExists(shopId);

        return customerRepository
                .searchCustomers(
                        shopId,
                        query.trim(),
                        pageable
                )
                .map(customerMapper::toCustomerResponse);
    }


    @Override
    @Transactional
    public CustomerResponse updateCustomer(
            Long shopId,
            Long customerId,
            CustomerUpdateRequest request
    ) {

        Customer customer =
                customerRepository.findByIdAndShopId(
                        customerId,
                        shopId
                ).orElseThrow(() ->
                        new CustomerNotFoundException(customerId)
                );

        customerMapper.updateEntity(request, customer);

        Customer updatedCustomer =
                customerRepository.save(customer);

        return customerMapper.toCustomerResponse(updatedCustomer);
    }


    @Override
    @Transactional
    public CustomerResponse activateCustomer(
            Long shopId,
            Long customerId
    ) {

        Customer customer =
                customerRepository.findByIdAndShopId(
                        customerId,
                        shopId
                ).orElseThrow(() ->
                        new CustomerNotFoundException(customerId)
                );

        if (customer.isActive()) {
            throw new CustomerAlreadyActiveException(customerId);
        }

        customer.setActive(true);

        Customer updatedCustomer =
                customerRepository.save(customer);

        return customerMapper.toCustomerResponse(updatedCustomer);
    }


    @Override
    @Transactional
    public CustomerResponse deactivateCustomer(
            Long shopId,
            Long customerId
    ) {

        Customer customer =
                customerRepository.findByIdAndShopId(
                        customerId,
                        shopId
                ).orElseThrow(() ->
                        new CustomerNotFoundException(customerId)
                );

        if (!customer.isActive()) {
            throw new CustomerAlreadyInactiveException(customerId);
        }

        customer.setActive(false);

        Customer updatedCustomer =
                customerRepository.save(customer);

        return customerMapper.toCustomerResponse(updatedCustomer);
    }


    private void verifyShopExists(Long shopId) {

        shopRepository.findById(shopId)
                .orElseThrow(() ->
                        new ShopNotFoundException(
                                 shopId
                        )
                );
    }
}
//later we will perform some tests and add the controllers to it
