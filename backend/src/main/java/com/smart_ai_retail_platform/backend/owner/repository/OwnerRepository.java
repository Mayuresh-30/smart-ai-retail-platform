package com.smart_ai_retail_platform.backend.owner.repository;

import com.smart_ai_retail_platform.backend.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner,Long> {
    Optional<Owner> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
