package com.smart_ai_retail_platform.backend.shop.mapper;

import com.smart_ai_retail_platform.backend.owner.entity.Owner;
import com.smart_ai_retail_platform.backend.shop.dto.CreateShopRequest;
import com.smart_ai_retail_platform.backend.shop.dto.OwnerSummaryResponse;
import com.smart_ai_retail_platform.backend.shop.dto.ShopResponse;
import com.smart_ai_retail_platform.backend.shop.dto.UpdateShopRequest;
import com.smart_ai_retail_platform.backend.shop.entity.Shop;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ShopMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Shop toEntity(CreateShopRequest request);

    ShopResponse toResponse(Shop shop);

    OwnerSummaryResponse toOwnerSummary(Owner owner);

    //also this one cause of error 18/08/26
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateEntity(
            UpdateShopRequest request,
            @MappingTarget Shop shop
    );
}
