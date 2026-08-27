package com.smart_ai_retail_platform.backend.shop.entity;

import com.smart_ai_retail_platform.backend.common.shared.BaseEntity;
import com.smart_ai_retail_platform.backend.owner.entity.Owner;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "shop")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Shop extends BaseEntity {

    @OneToOne
    @JoinColumn(
            name = "owner_id",
            nullable = false,
            unique = true
    )
    private Owner owner;
    private String shopName;
    private String description;
    private String address;
}
