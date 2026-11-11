package com.smart_ai_retail_platform.backend.owner.mapper;

import com.smart_ai_retail_platform.backend.owner.dto.CreateOwnerRequest;
import com.smart_ai_retail_platform.backend.owner.dto.OwnerResponse;
import com.smart_ai_retail_platform.backend.owner.dto.UpdateOwnerRequest;
import com.smart_ai_retail_platform.backend.owner.entity.Owner;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface OwnerMapper {
    /**
     * Map Request DTO to Entity
     * - Ignore id (auto-generated)
     * - Ignore createdAt/updatedAt (handled by auditing)
     * - Ignore shop (created separately)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "shop", ignore = true)
    Owner toEntity(CreateOwnerRequest requestDTO);


    OwnerResponse toResponse(Owner owner);

    //impl this cause of error 18/08/26
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "shop", ignore = true)
    void updateEntity(UpdateOwnerRequest request, @MappingTarget Owner owner);
}
