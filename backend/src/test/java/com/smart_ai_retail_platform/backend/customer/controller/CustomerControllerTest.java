package com.smart_ai_retail_platform.backend.customer.controller;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.smart_ai_retail_platform.backend.owner.dto.CreateOwnerRequest;
import com.smart_ai_retail_platform.backend.owner.service.OwnerService;
import com.smart_ai_retail_platform.backend.shop.dto.CreateShopRequest;
import com.smart_ai_retail_platform.backend.shop.service.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ShopService shopService;

    private Long shopId;

    @BeforeEach
    void setUp() {
        shopId = createShop("Mira", "mira.test@example.com", "9876543210");
    }

    @Test
    void customerLifecycleAndPagedSearchWork() throws Exception {
        long customerId = createCustomer(shopId, "Ravi Kumar", "9123456789");

        mockMvc.perform(get(customerPath(shopId) + "?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].name").value("Ravi Kumar"))
                .andExpect(jsonPath("$.data.totalElements").value(1));

        mockMvc.perform(get(customerPath(shopId) + "?query=9123&page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].phone").value("9123456789"));

        mockMvc.perform(get(customerPath(shopId) + "/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(customerId));

        mockMvc.perform(put(customerPath(shopId) + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Ravi P.","phone":"9123456789"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Ravi P."));

        mockMvc.perform(put(customerPath(shopId) + "/" + customerId + "/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.active").value(false));

        mockMvc.perform(put(customerPath(shopId) + "/" + customerId + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.active").value(true));
    }

    @Test
    void createCustomerRejectsInvalidInputWithFieldMessage() throws Exception {
        mockMvc.perform(post(customerPath(shopId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":" ","phone":"bad-phone"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("name")));
    }

    @Test
    void missingResourcesReturnNotFoundAndCustomerCannotBeReadFromAnotherShop() throws Exception {
        long customerId = createCustomer(shopId, "Leena Das", "9988776655");
        long anotherShopId = createShop("Arun", "arun.test@example.com", "9988776654");

        mockMvc.perform(get(customerPath(shopId) + "/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));

        mockMvc.perform(get(customerPath(anotherShopId) + "/" + customerId))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(customerPath(999999L)))
                .andExpect(status().isNotFound());
    }

    @Test
    void repeatedActivationStateChangesReturnConflict() throws Exception {
        long customerId = createCustomer(shopId, "Nisha Rao", "9000000001");

        mockMvc.perform(put(customerPath(shopId) + "/" + customerId + "/deactivate"))
                .andExpect(status().isOk());

        MvcResult repeatedDeactivate = mockMvc.perform(
                        put(customerPath(shopId) + "/" + customerId + "/deactivate"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn();
        assertFalse(errorMessage(repeatedDeactivate).isBlank());

        mockMvc.perform(put(customerPath(shopId) + "/" + customerId + "/activate"))
                .andExpect(status().isOk());

        MvcResult repeatedActivate = mockMvc.perform(
                        put(customerPath(shopId) + "/" + customerId + "/activate"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn();
        assertTrue(errorMessage(repeatedActivate).contains("already active"));
    }

    private long createCustomer(Long forShopId, String name, String phone) throws Exception {
        MvcResult result = mockMvc.perform(post(customerPath(forShopId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CustomerPayload(name, phone))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .path("data")
                .path("id")
                .asLong();
    }

    private Long createShop(String firstName, String email, String phone) {
        var owner = ownerService.createOwner(CreateOwnerRequest.builder()
                .firstName(firstName)
                .lastName("Tester")
                .email(email)
                .phone(phone)
                .password("DemoPass123!")
                .build());

        var shop = shopService.createShop(owner.getId(), CreateShopRequest.builder()
                .shopName(firstName + " Shop")
                .description("Customer controller integration test")
                .address("1 Test Street")
                .build());

        return shop.getId();
    }

    private String customerPath(Long forShopId) {
        return "/api/shops/" + forShopId + "/customers";
    }

    private String errorMessage(MvcResult result) throws Exception {
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.path("message").asText();
    }

    private record CustomerPayload(String name, String phone) {
    }
}
