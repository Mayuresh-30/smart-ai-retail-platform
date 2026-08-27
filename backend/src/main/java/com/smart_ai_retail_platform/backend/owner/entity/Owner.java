package com.smart_ai_retail_platform.backend.owner.entity;

import com.smart_ai_retail_platform.backend.common.shared.BaseEntity;
import com.smart_ai_retail_platform.backend.shop.entity.Shop;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "owner")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Owner extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    @OneToOne(
            mappedBy = "owner",
            fetch = FetchType.LAZY
    )
    private Shop shop;

}
