package com.smart_ai_retail_platform.backend.shop.repository;

import com.smart_ai_retail_platform.backend.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop,Long> {
    Optional<Shop> findByOwnerId(Long ownerId);

    boolean existsByOwnerId(Long ownerId);
}
