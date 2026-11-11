package com.smart_ai_retail_platform.backend.customer.entity;

import com.smart_ai_retail_platform.backend.common.shared.BaseEntity;
import com.smart_ai_retail_platform.backend.shop.entity.Shop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(
        name = "customer"
)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Customer extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_shop"))
    private Shop shop;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    private boolean active = true;

}
