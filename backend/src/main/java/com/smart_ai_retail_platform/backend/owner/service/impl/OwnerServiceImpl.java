package com.smart_ai_retail_platform.backend.owner.service.impl;

import com.smart_ai_retail_platform.backend.common.exception.ResourceAlreadyExistsException;
import com.smart_ai_retail_platform.backend.common.exception.ResourceNotFoundException;
import com.smart_ai_retail_platform.backend.owner.dto.CreateOwnerRequest;
import com.smart_ai_retail_platform.backend.owner.dto.OwnerResponse;
import com.smart_ai_retail_platform.backend.owner.dto.UpdateOwnerRequest;
import com.smart_ai_retail_platform.backend.owner.entity.Owner;
import com.smart_ai_retail_platform.backend.owner.mapper.OwnerMapper;
import com.smart_ai_retail_platform.backend.owner.repository.OwnerRepository;
import com.smart_ai_retail_platform.backend.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Transactional
    @Override
    public OwnerResponse createOwner(CreateOwnerRequest request) {
        log.info("Creating owner with email: {}", request.getEmail());

        if (ownerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        if (ownerRepository.existsByPhone(request.getPhone())) {
            throw new ResourceAlreadyExistsException("Phone already exists");
        }

        // Map DTO to Entity
        Owner owner = ownerMapper.toEntity(request);
        owner.setPassword(passwordEncoder.encode(request.getPassword()));
        // Save - createdAt and updatedAt will be auto-set
        Owner savedOwner = ownerRepository.save(owner);

        log.info("Owner created successfully with ID: {}", savedOwner.getId());
        return ownerMapper.toResponse(savedOwner);
    }
    @Transactional(readOnly = true)
    @Override
    public OwnerResponse getOwner(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        return ownerMapper.toResponse(owner);

    }
    @Transactional
    @Override
    public OwnerResponse updateOwner(Long ownerId, UpdateOwnerRequest request) {
        log.info("Checking owner with ID: {}", ownerId);

        Owner existingOwner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found for id  "+ ownerId));

        // Update fields
        ownerMapper.updateEntity(request, existingOwner);

        // Save - updatedAt will be auto-updated
        Owner updatedOwner = ownerRepository.save(existingOwner);

        log.info("Owner updated with ID: {}", updatedOwner.getId());

        return ownerMapper.toResponse(updatedOwner);
    }
}
