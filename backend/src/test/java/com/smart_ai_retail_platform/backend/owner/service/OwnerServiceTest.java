package com.smart_ai_retail_platform.backend.owner.service;

import com.smart_ai_retail_platform.backend.owner.dto.CreateOwnerRequest;
import com.smart_ai_retail_platform.backend.owner.dto.OwnerResponse;
import com.smart_ai_retail_platform.backend.owner.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerRepository ownerRepository;

    private CreateOwnerRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = CreateOwnerRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@test.com")
                .phone("1234567890")
                .password("password123")
                .build();
    }

    @Test
    void test1_createdAtShouldBeSetWhenOwnerCreated() {
        // Given: A create request
        // When: We create an owner
        OwnerResponse response = ownerService.createOwner(createRequest);

        // Then: createdAt should not be null
        assertNotNull(response.getCreatedAt(), "CreatedAt should not be null");
        assertNotNull(response.getUpdatedAt(), "UpdatedAt should not be null");

        // On creation, createdAt and updatedAt should be the same
        assertEquals(response.getCreatedAt(), response.getUpdatedAt());

        System.out.println("✅ CreatedAt: " + response.getCreatedAt());
        System.out.println("✅ UpdatedAt: " + response.getUpdatedAt());
//        System.out.println("Shop Value" + response.getFirstName());
    }

}
