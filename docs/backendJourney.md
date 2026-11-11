# Backend Journey

This document records what exists in the backend today, how its pieces work together, and the next work needed to reach the Version 1 retail platform. The project is intended to be a Spring Boot modular monolith: one deployable application with business features organized into modules.

## 1. Version 1 goal

Version 1 is for a shop owner to manage one shop, its products, categories, and saved customers; create bills; update stock after sales; and review sales and bill history. Saved customer management remains part of Version 1. Billing should support both a saved customer and a walk-in bill, as described in the business requirements.

The backend currently provides owner and shop REST APIs, plus customer service logic. Product, category, billing, inventory, analytics, authentication, and frontend flows are not implemented yet. The customer service does not yet have a REST controller, so customer operations are not currently exposed as HTTP endpoints.

## 2. What is built today

### Application and shared infrastructure

| File | Purpose |
|---|---|
| `backend/src/main/java/com/smart_ai_retail_platform/backend/BackendApplication.java` | Spring Boot entry point; starts the backend application. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/config/JpaConfig.java` | Enables Spring Data JPA auditing for entity timestamps. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/config/SecurityConfig.java` | Configures BCrypt password encoding and the current Spring Security filter chain. Requests are presently permitted without authentication; this is not the final JWT security design. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/shared/BaseEntity.java` | Shared ID, `createdAt`, and `updatedAt` fields inherited by entities. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/response/ApiResponse.java` | Common API response envelope: success flag, message, data, and timestamp. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/exception/GlobalExceptionHandler.java` | Maps selected application exceptions to common API responses. Extend it as modules add error cases. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/exception/ResourceNotFoundException.java` | General missing-resource exception. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/exception/ResourceAlreadyExistsException.java` | General duplicate/conflict exception. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/exception/ShopNotFoundException.java` | Shop-specific missing-resource exception. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/exception/CustomerNotFoundException.java` | Customer-specific missing-resource exception. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/exception/CustomerAlreadyExistException.java` | Customer duplicate exception type. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/exception/CustomerAlreadyActiveException.java` | Signals an attempt to activate an already active customer. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/exception/CustomerAlreadyInactiveException.java` | Signals an attempt to deactivate an already inactive customer. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/common/test/TestController.java` | Temporary endpoint for checking not-found exception handling; remove or replace with proper API documentation/health checks before production. |

### Owner module

| File | Purpose |
|---|---|
| `backend/src/main/java/com/smart_ai_retail_platform/backend/owner/controller/OwnerController.java` | REST endpoints to create, get, and update owners under `/api/owners`. Validates request bodies and wraps responses. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/owner/service/OwnerService.java` | Owner use-case contract. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/owner/service/impl/OwnerServiceImpl.java` | Implements owner creation, lookup, and update; checks duplicate email/phone and hashes the password. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/owner/repository/OwnerRepository.java` | JPA persistence and email/phone existence queries. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/owner/entity/Owner.java` | Owner database entity, including identity, contact details, encoded password, and one-to-one shop relationship. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/owner/mapper/OwnerMapper.java` | MapStruct conversions between owner entities and request/response DTOs. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/owner/dto/CreateOwnerRequest.java` | Validated input for owner creation. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/owner/dto/UpdateOwnerRequest.java` | Validated editable owner fields. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/owner/dto/OwnerResponse.java` | Public owner response shape. |

### Shop module

| File | Purpose |
|---|---|
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/controller/ShopController.java` | REST endpoints to create, fetch, and update a shop at `/api/owners/{ownerId}/shop`. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/service/ShopService.java` | Shop use-case contract. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/service/impl/ShopServiceImpl.java` | Finds the owner, enforces one shop per owner, and implements shop create/read/update. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/repository/ShopRepository.java` | JPA persistence and queries by owner. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/entity/Shop.java` | Shop entity; its unique owner foreign key enforces one shop per owner. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/mapper/ShopMapper.java` | MapStruct conversions between shop entities and DTOs. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/dto/CreateShopRequest.java` | Validated input to create a shop. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/dto/UpdateShopRequest.java` | Validated editable shop fields. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/dto/ShopResponse.java` | Shop response, including owner summary and timestamps. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/shop/dto/OwnerSummaryResponse.java` | Owner details embedded in a shop response. |

### Customer module

| File | Purpose |
|---|---|
| `backend/src/main/java/com/smart_ai_retail_platform/backend/customer/entity/Customer.java` | Saved customer entity belonging to a shop; includes name, phone, and active status. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/customer/service/CustomerService.java` | Declares customer create, get, paginated list, search, update, activate, and deactivate operations. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/customer/service/impl/CustomerServiceImpl.java` | Implements those operations, scopes reads to a shop, and uses active/inactive status rather than deleting customers. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/customer/repository/CustomerRepository.java` | JPA persistence, shop-scoped lookups and paging, and search by customer name or phone. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/customer/mapper/CustomerMapper.java` | MapStruct conversions and update mapping for customer DTOs. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/customer/dto/CustomerRequest.java` | Validated input for creating a saved customer. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/customer/dto/CustomerUpdateRequest.java` | Validated input for editing customer details. |
| `backend/src/main/java/com/smart_ai_retail_platform/backend/customer/dto/CustomerResponse.java` | Customer data returned by customer use cases. |

There is currently no `customer/controller` implementation. The service interface has endpoint ideas in comments, but those comments are not working routes. Build the controller and connect it to the frontend when exposing customer management.

### Configuration and tests

| File | Purpose |
|---|---|
| `backend/src/main/resources/application.yml` | Names the application and activates the `dev` profile by default. |
| `backend/src/main/resources/application-dev.yml` | Local MySQL and JPA development settings. Keep credentials out of committed configuration and load them from environment variables. |
| `backend/src/main/resources/application-test.yml` | H2 in-memory database settings for tests. |
| `backend/src/main/resources/application-prod.yml` | Production profile configuration; review and complete its environment-specific settings before deployment. |
| `backend/src/test/java/com/smart_ai_retail_platform/backend/BackendApplicationTests.java` | Basic application context loading test. |
| `backend/src/test/java/com/smart_ai_retail_platform/backend/owner/service/OwnerServiceTest.java` | Spring integration test that checks owner audit timestamps on creation. |

## 3. How the current request flow works

For owner and shop operations, the implemented flow is:

1. A REST controller receives a request and validates its DTO with Bean Validation.
2. The controller calls a service interface; the service implementation applies business rules inside a transaction.
3. The service uses a repository to load or save JPA entities.
4. A MapStruct mapper converts request DTOs into entities and entities into response DTOs.
5. The controller returns an `ApiResponse` with an appropriate HTTP status.
6. Exceptions are intended to be converted into consistent error responses by `GlobalExceptionHandler`.

Customer operations currently start at the service layer because there is no customer REST controller. Shop-scoped customer methods check that the customer belongs to the requested shop, and list/search methods use Spring Data pagination.

## 4. Current domain relationships

- An `Owner` has at most one `Shop`; the shop owns the relationship through a unique `owner_id` foreign key.
- A `Shop` has many `Customer` records; each customer has a required `shop_id`.
- `Owner`, `Shop`, and `Customer` inherit generated ID and audit timestamp fields from `BaseEntity`.
- Owner passwords are encoded with BCrypt before persistence.

Product, Category, Bill, BillItem, payment, and inventory entities and relationships have not been implemented yet. Follow the requirements and database design docs when adding them.

## 5. What to build next

Recommended sequence, preserving the modular monolith boundaries:

1. **Stabilize the foundation:** review profile configuration and secret handling; complete consistent exception responses and validation; ensure owner/shop/customer use cases have the intended tests. Replace the temporary test endpoint when appropriate.
2. **Finish customer API:** add a customer controller for shop-scoped create, get, list, search, update, activate, and deactivate operations. Apply paging and validation at the API boundary.
3. **Build Category:** create entity, DTOs, mapper, repository, service, controller, and tests; enforce category-name uniqueness within a shop.
4. **Build Product and inventory fields:** add shop/category relationships, validated prices and quantities, search/filter/pagination, and safe stock updates.
5. **Build Billing:** add Bill and BillItem domain objects, checkout endpoints and services, CASH/UPI payment selection, optional saved-customer association for walk-ins, and immutable bill history. Persist a bill and reduce stock atomically; reject checkout when stock is insufficient. Keep bill item descriptions/prices as sale-time snapshots so later product edits do not rewrite past bills.
6. **Build bill history and Dashboard:** add date/customer/number filters and pagination for bills, then aggregate revenue, bill counts, low stock, best sellers, and recent bills.
7. **Add authentication and authorization:** implement the documented owner registration/login/JWT behavior and restrict shop data to the authenticated owner. The current security config permits all requests and does not satisfy this requirement.
8. **Connect the frontend:** build the responsive owner workflow against the backend APIs after their request/response contracts are defined.

AI recommendations and forecasting are later roadmap work, not a prerequisite for the Version 1 retail workflow.

## 6. Empty planned modules

These backend package folders currently have no implementation files:

- `backend/src/main/java/com/smart_ai_retail_platform/backend/auth/`
- `backend/src/main/java/com/smart_ai_retail_platform/backend/category/`
- `backend/src/main/java/com/smart_ai_retail_platform/backend/product/`
- `backend/src/main/java/com/smart_ai_retail_platform/backend/billing/`
- `backend/src/main/java/com/smart_ai_retail_platform/backend/analytics/`
- `backend/src/main/java/com/smart_ai_retail_platform/backend/security/`

The `frontend/` folder is also empty. These are planned work areas, not currently functioning features.
