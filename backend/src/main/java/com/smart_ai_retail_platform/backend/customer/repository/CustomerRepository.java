package com.smart_ai_retail_platform.backend.customer.repository;

import com.smart_ai_retail_platform.backend.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsByShopIdAndPhone(
            Long shopId,
            String phone
    );


    Optional<Customer> findByIdAndShopId(
            Long customerId,
            Long shopId
    );

    Page<Customer> findAllByShopId(
            Long shopId,
            Pageable pageable
    );

    Page<Customer> findAllByShopIdAndActive(
            Long shopId,
            boolean active,
            Pageable pageable
    );

    @Query("""
            SELECT c
            FROM Customer c
            WHERE c.shop.id = :shopId
              AND c.active = true
              AND (
                  LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
                  OR c.phone LIKE CONCAT('%', :query, '%')
              )
            """)
    Page<Customer> searchCustomers(
            @Param("shopId") Long shopId,
            @Param("query") String query,
            Pageable pageable
    );
}
