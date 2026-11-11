# Smart AI Retail Platform — Building Plan

**Plan date:** 11 November 2026  
**Scope:** Complete the documented Version 1 retail platform, starting from the backend foundation already in the repository.  
**Architecture:** Spring Boot modular monolith with a single application and database.

This plan keeps the conventions already established in the project: feature-oriented packages; controller → service interface → service implementation → repository; request/response DTOs; MapStruct mappers; JPA entities extending `BaseEntity` where appropriate; Bean Validation; transactions around business operations; and a shared `ApiResponse`/exception pattern.

## 1. Product outcome

Version 1 should let one shop owner register, configure one shop, manage categories, products, inventory, and saved customers, create cash or UPI bills (including walk-ins), and review bill history and dashboard figures. Bills must retain the prices and product names used at checkout, and stock must not go below zero.

The existing owner, shop, and customer code is the starting point. The backend has no category, product, billing, dashboard, or authentication flows yet. There is no frontend implementation.

## 2. Design rules to use throughout

1. **Keep feature boundaries clear.** Place a feature's controller, DTOs, entity, mapper, repository, service interface, and service implementation inside that feature package. A module must not call another module's repository directly; expose a service interface or a small domain operation instead.
2. **Keep HTTP contracts separate from persistence.** Controllers accept validated request DTOs and return response DTOs; never expose JPA entities directly.
3. **Map explicitly.** Follow the existing MapStruct style (`componentModel = "spring"`, constructor injection, `unmappedTargetPolicy = ReportingPolicy.ERROR`). Explicitly ignore IDs, relationships, status, and audit fields that are assigned by the service or database.
4. **Reuse shared foundations.** Extend `BaseEntity` for persisted records that need generated IDs and audit timestamps. Use `JpaConfig` auditing. Return the common `ApiResponse` shape and route expected failures through the global exception mechanism.
5. **Keep business rules in services.** Use `@Transactional` for writes and multi-step operations, and `@Transactional(readOnly = true)` for reads. Repositories persist and query; they should not own business workflows.
6. **Enforce shop ownership at every boundary.** Resolve data under the authenticated owner's shop. Do not trust a client-supplied shop ID as proof of access.
7. **Preserve history.** A bill and its line items are immutable records of a completed sale. Store sale-time product name and price on each line, even when also retaining a product reference.
8. **Keep changes incremental.** Complete a vertical slice (model through API and focused verification) before moving to the next feature. Keep architecture and requirements docs consistent if a business decision changes.

## 3. Step-by-step implementation

### Phase 0 — Establish a reliable baseline

**Build:** Review the Maven configuration, active profiles, exception handling, and entity auditing. Make local and test configuration predictable; move database credentials out of source-controlled YAML and into environment configuration. Confirm how the test profile is selected and that test runs use the in-memory database.

**Why now:** Every feature depends on a reproducible application and safe configuration. Catching framework/build issues before adding modules avoids debugging multiple causes at once.

**Complete when:** A developer can start the backend with documented prerequisites; tests can run without the local MySQL instance; no credential is needed in committed config; exceptions and validation failures have a consistent response contract. Keep the temporary `/api/test` endpoint clearly marked until it is removed or replaced.

### Phase 1 — Finish and protect the existing owner, shop, and customer APIs

**Build:**

- Add `customer/controller/CustomerController.java` using the existing service interface. Expose create, get, paginated list, search by name/phone, update, activate, and deactivate operations under a shop-scoped route such as `/api/shops/{shopId}/customers`.
- Align `CustomerServiceImpl` and `CustomerRepository` behavior with the approved business rules. Resolve whether customer phone uniqueness is required within a shop and enforce it consistently if so. The current repository declares an existence query, but the service does not use it.
- Make exception mapping comprehensive for validation, missing owner/shop/customer, duplicate data, invalid state transitions, and unexpected failures. Use the exception type in each handler's argument consistently.
- Add API/service coverage for owner, shop, and customer success paths and expected errors. Preserve the current `OwnerServiceTest` and expand it instead of duplicating setup unnecessarily.

**Why now:** These are the account and shop data prerequisites for all later modules. The customer service already exists, so completing its API gives the frontend a usable first vertical slice and clarifies conventions for the next modules.

**Complete when:** Customer endpoints are documented by their request/response DTOs, all list/search APIs are paginated, cross-shop IDs cannot read or update another shop's customer, and common errors have stable HTTP statuses and response bodies.

### Phase 2 — Implement authentication and owner-scoped authorization

**Build:** Add an `auth` module for owner registration/login/logout behavior as appropriate for stateless JWT, token creation/validation, an authentication filter, and auth request/response DTOs. Integrate Spring Security with the existing `PasswordEncoder`. Replace `anyRequest().permitAll()` with public registration/login routes and authenticated protected routes. Define how the authenticated principal resolves to the owner's single shop. Apply ownership checks consistently in service operations.

**Why here:** Adding authorization after many business endpoints exist risks leaving routes open or applying inconsistent access rules. Owner-scoped access is foundational to the single-shop domain.

**Complete when:** Invalid or missing tokens cannot use protected APIs; valid owners can access only their own shop data; passwords remain encoded; token secrets/expiry are environment-configured; tests cover public, authenticated, and unauthorized requests. For stateless JWT, document logout as client token disposal unless a revocation mechanism is deliberately designed.

### Phase 3 — Build Category vertical slice

**Build:** Add `category` package with `Category`, create/update request DTOs, response DTO, mapper, repository, service interface/implementation, controller, and module-specific exceptions only where useful. A category belongs to one shop; enforce category-name uniqueness within that shop. Provide create, update, list, get, and delete operations. Prevent deletion while products still use the category. Ensure all access is shop-scoped.

**Why now:** Products depend on categories, so categories establish a needed relationship and a repeatable implementation pattern before the larger product module.

**Complete when:** Duplicate names within a shop and cross-shop access are rejected; a category in use cannot be deleted; validation and endpoint behavior are covered; other modules use the category service boundary rather than its repository.

### Phase 4 — Build Product and inventory foundation

**Build:** Add `product` package with entity, create/update/search DTOs, response DTO, mapper, repository, service interface/implementation, controller, and tests. A product belongs to one shop and one category from that same shop. Include name, selling price, quantity, and status (`ACTIVE`, `OUT_OF_STOCK`, `DISCONTINUED`) as defined in the domain model. Validate price > 0 and quantity ≥ 0. Support search by name, category filtering, and pagination. Prevent deletion if the product has bill history; use a discontinued state when preserving history is required.

Design stock changes as explicit service operations. Keep quantity non-negative under concurrent checkouts using an appropriate transactional/locking strategy; do not rely on a read-then-write without protection. Transition to `OUT_OF_STOCK` when quantity reaches zero and define how replenishment returns a product to an available state.

**Why now:** Billing cannot be safe until products, prices, availability, and concurrency-aware stock updates exist.

**Complete when:** Product CRUD/search/filter routes are shop-scoped and paginated; a product cannot reference another shop's category; validation and not-found/conflict outcomes are stable; stock can be changed without producing negative inventory.

### Phase 5 — Define and implement billing as an atomic domain operation

**Build:** Add `billing` package with `Bill`, `BillItem`, payment/status enums, create-bill request DTO (product IDs and positive quantities, optional customer ID, payment method), response DTOs, mapper(s), repository, service interface/implementation, and controller. A bill belongs to a shop and has one or more line items. A customer reference is nullable for walk-in sales. Each line stores product ID where appropriate plus product name, quantity, price-at-purchase, and line subtotal as a historical snapshot. Store bill number, timestamp, payment method (`CASH` or `UPI`), item count, subtotal, and grand total. Bills are immutable and are not deleted.

At checkout, the service must:

1. Derive the shop from the authenticated owner.
2. Reject an empty bill, invalid quantities, inactive/unavailable products, products outside the shop, and a customer outside the shop.
3. Load the authoritative current product prices; never trust client totals/prices.
4. Calculate each line and bill totals on the server using decimal money types and a documented rounding rule.
5. Decrease stock and save the bill/items within one transaction, with concurrency protection. Any failure must leave both stock and bill state unchanged.
6. Return the completed bill as a response DTO.

**Why now:** Billing is the critical business transaction. Building it after products allows its invariants to rely on actual product and inventory behavior.

**Complete when:** Checkout is all-or-nothing, duplicate/invalid lines are handled deliberately, stock cannot oversell, totals are server-calculated, old bills do not change after product edits, and tests cover rollback and competing/insufficient-stock cases.

### Phase 6 — Build bill history and detail views

**Build:** Add shop-scoped bill list and detail endpoints. Support pagination, search by bill number and customer, and date filtering as requirements specify. Add repository queries designed for the required filters and likely indexes for shop/date, bill number, and customer association. Do not add update/delete operations for completed bills.

**Why now:** History is a direct use of persisted bills and an important operational workflow; it should be available before analytics are built on top of sales.

**Complete when:** Owners can page through their own bills, filter/search as required, open a bill with all snapshot line data, and cannot see another shop's bill.

### Phase 7 — Build dashboard/analytics read use cases

**Build:** Add the `analytics` (or dashboard) module with response DTOs and service-level read use cases for today's revenue, today's bill count, today's customers, total products, low-stock products, most-sold products, and the latest three bills. Keep these as read-only projections/queries; do not put dashboard logic into entity mappers or controllers. Define timezone and the low-stock threshold explicitly, and clarify what “today's customers” means (new customer records or distinct buyers) before implementing it.

**Why now:** Dashboard calculations depend on accurate products, customers, and completed bill history. Building earlier risks hard-coding incomplete data assumptions.

**Complete when:** Every metric is shop-scoped, date boundaries use the agreed business timezone, query results match known sample data, and response DTOs avoid returning entire entity graphs.

### Phase 8 — Add frontend and connect end-to-end workflows

**Build:** Create the frontend application in `frontend/` with owner sign-in/setup, shop profile, customer management, category/product management, inventory views, bill creation, bill history/detail, and dashboard. Keep API calls behind a small typed client/service layer, handle validation and authorization errors, and design responsive layouts. Use the backend's documented DTO contracts; avoid duplicating business calculations such as bill totals in the UI.

**Why now:** Once API contracts and core business flows are stable, the frontend can be built against real behavior rather than mock assumptions.

**Complete when:** An owner can sign in, set up a shop, add a category/product/customer, complete a saved-customer or walk-in sale, see stock update, find the bill in history, and view updated dashboard values using the UI.

### Phase 9 — Production readiness and release

**Build:** Review security, configuration, database schema evolution, logging, validation/error responses, API documentation, pagination limits, transaction boundaries, and indexes. Introduce a database migration tool before schema changes need controlled deployment; stop relying on automatic schema updates in production. Add operational health checks and deployment instructions. Check that secrets are supplied by the deployment environment.

**Why now:** Release readiness depends on the real schema and flows; doing this after core features gives a meaningful review without delaying feature design.

**Complete when:** A fresh environment can be provisioned reproducibly, production does not expose credentials or permit anonymous business access, schema changes are versioned, and the complete owner-to-sale workflow is verified.

## 4. Suggested package shape for a new feature

Use the existing feature style, adapted only when a feature genuinely needs a different boundary:

```text
<feature>/
  controller/
  dto/
  entity/
  mapper/
  repository/
  service/<Feature>Service.java
  service/impl/<Feature>ServiceImpl.java
```

For a simple read projection, avoid creating artificial entities or mappers. For billing, keep the aggregate's invariants together in the billing service/domain boundary while using product/customer service interfaces for cross-module checks or operations. Do not let billing reach into product/customer repositories directly.

## 5. Cross-cutting decisions to settle before dependent work

- **Customer phone uniqueness:** business docs say it is mandatory, while the repository has an unused existence query. Decide whether it is unique per shop and align validation, service, database constraint, and tests.
- **Delete versus deactivate:** customer service uses active/inactive state while requirements describe deletion when no bills exist. Keep deactivation as the safe default for historical integrity; update the requirement wording when that policy is confirmed.
- **Walk-in bill representation:** use a nullable customer relationship for a walk-in bill; do not create a fake customer ID. If the cashier enters a walk-in name, decide whether the bill stores that name as a transaction snapshot.
- **Money and rounding:** use decimal database/application types and document scale and rounding before billing is implemented.
- **Bill number generation:** define uniqueness and concurrency-safe generation per shop or globally before building checkout.
- **Low-stock threshold and dashboard date zone:** decide these before analytics implementation.
- **Schema evolution:** the development profile currently uses Hibernate schema update. Define a migration approach before production deployment.

## 6. Version 1 completion checklist

- [ ] Owner registration/login and protected owner-scoped APIs work.
- [ ] One owner can set up and update one shop.
- [ ] Owner can manage categories and products, with search/filter/pagination.
- [ ] Owner can create, search, update, and deactivate saved customers.
- [ ] Checkout supports saved customers and walk-ins, CASH and UPI, and safe atomic stock reduction.
- [ ] Bills preserve item and price snapshots and cannot be edited/deleted.
- [ ] Bill history supports required search, date filters, pagination, and detail view.
- [ ] Dashboard metrics match persisted sales and inventory.
- [ ] Frontend completes the owner workflow responsively.
- [ ] Configuration, schema evolution, and deployment instructions are release-ready.

AI features, supplier management, forecasting, PDF generation, exports, notifications, and multi-store support remain outside this Version 1 plan, consistent with the project vision and requirements.
