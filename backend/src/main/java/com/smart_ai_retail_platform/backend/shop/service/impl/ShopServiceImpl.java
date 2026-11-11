package com.smart_ai_retail_platform.backend.shop.service.impl;

import com.smart_ai_retail_platform.backend.common.exception.ResourceAlreadyExistsException;
import com.smart_ai_retail_platform.backend.common.exception.ResourceNotFoundException;
import com.smart_ai_retail_platform.backend.owner.entity.Owner;
import com.smart_ai_retail_platform.backend.owner.repository.OwnerRepository; // Assumed repository name
import com.smart_ai_retail_platform.backend.shop.dto.CreateShopRequest;
import com.smart_ai_retail_platform.backend.shop.dto.ShopResponse;
import com.smart_ai_retail_platform.backend.shop.dto.UpdateShopRequest;
import com.smart_ai_retail_platform.backend.shop.entity.Shop;
import com.smart_ai_retail_platform.backend.shop.mapper.ShopMapper;
import com.smart_ai_retail_platform.backend.shop.repository.ShopRepository; // Assumed repository name
import com.smart_ai_retail_platform.backend.shop.service.ShopService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final OwnerRepository ownerRepository;
    private final ShopMapper shopMapper;

    @Override
    @Transactional
    public ShopResponse createShop(Long ownerId, CreateShopRequest request) {
        // 1. Fetch owner or fail with standard JPA exception
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + ownerId));

        // 2. Enforce the One-To-One Domain Invariant
        // Check if the owner already owns a shop using a derived query or checking memory
        if (shopRepository.existsByOwnerId(ownerId)) {
            throw new ResourceAlreadyExistsException("Owner with id " + ownerId + " already has an associated shop.");
        }

        // 3. Map DTO to fresh Entity
        Shop shop = shopMapper.toEntity(request);

        // 4. Establish relationship (Shop is the owning side containing the foreign key)
        shop.setOwner(owner);
        owner.setShop(shop);

        // 6. Save and map to the complete Response DTO
        Shop savedShop = shopRepository.save(shop);
        return shopMapper.toResponse(savedShop);
    }

    @Override
    @Transactional(readOnly = true)
    public ShopResponse getShop(Long ownerId) {
        // Fetch the shop based on the owning entity's ID reference
        Shop shop = shopRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found for owner id: " + ownerId));

        return shopMapper.toResponse(shop);
    }

    @Override
    @Transactional
    public ShopResponse updateShop(Long ownerId, UpdateShopRequest request) {
        // 1. Fetch current active shop state
        Shop shop = shopRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found for owner id: " + ownerId));

        // 2. Perform safe updates via MapStruct target mapping
        shopMapper.updateEntity(request, shop);

        // 3. Save modifications within transactional snapshot dirty-checking boundary
        Shop updatedShop = shopRepository.save(shop);
        return shopMapper.toResponse(updatedShop);
    }
}