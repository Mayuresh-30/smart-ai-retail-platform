package com.smart_ai_retail_platform.backend.owner.entity;

import com.smart_ai_retail_platform.backend.common.shared.BaseEntity;
import com.smart_ai_retail_platform.backend.shop.entity.Shop;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "owner",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_owner_email",
                        columnNames = {"email"}
                )
        })
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Owner extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private String password;


    @OneToOne(
            mappedBy = "owner",
            fetch = FetchType.LAZY
    )
    private Shop shop;

}
//add database level validation constraints
// for more safely integration in databse
// and adding a secure layer of validation
// except of dto layer validation