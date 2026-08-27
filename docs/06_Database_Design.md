# Smart AI Retail Platform

# Domain Model

---

## Document Information

| Field | Value |
|-------|-------|
| Document | Domain Model |
| Project | Smart AI Retail Platform |
| Version | 1.0 |
| Status | Approved |
| Sprint | Sprint 0 |
| Last Updated | YYYY-MM-DD |

---

# 1. Purpose

This document defines the core business entities of the Smart AI Retail Platform, their responsibilities, relationships, and business constraints.

The Domain Model serves as the foundation for database design, API design, backend implementation, and business logic.

---

# 2. Domain Overview

The Smart AI Retail Platform revolves around one primary business concept:

> A Shop Owner manages a Shop, which contains Products, Customers, Categories, and Bills.

Every business operation in Version 1 is performed within the context of a single shop.

---

# 3. Domain Entities

## 3.1 Owner

### Description

Represents the authenticated user of the system.

Each owner is responsible for managing exactly one shop.

### Responsibilities

- Authenticate into the application.
- Manage shop information.
- Manage products.
- Manage categories.
- Manage customers.
- Generate bills.
- Access dashboard analytics.

### Attributes

| Field | Description |
|--------|-------------|
| id | Unique identifier |
| firstName | Owner's first name |
| lastName | Owner's last name |
| email | Login email |
| phone | Contact number |
| password | Encrypted password |
| createdAt | Record creation time |
| updatedAt | Last update time |

### Business Rules

- One owner owns one shop.
- Email must be unique.
- Phone must be unique.
- Password is stored in encrypted form.

---

## 3.2 Shop

### Description

Represents the retail business managed by an owner.

A shop acts as the root container for all business data.

### Responsibilities

- Store shop information.
- Own products.
- Own categories.
- Own customers.
- Own bills.

### Attributes

| Field | Description |
|--------|-------------|
| id | Unique identifier |
| ownerId | Owner reference |
| shopName | Business name |
| description | Shop description |
| address | Shop address |
| createdAt | Record creation time |
| updatedAt | Last update time |

### Business Rules

- One shop belongs to one owner.
- One owner can own only one shop in Version 1.

---

## 3.3 Category

### Description

Represents a logical grouping of products.

### Responsibilities

- Organize products.
- Simplify searching and filtering.

### Attributes

| Field | Description |
|--------|-------------|
| id | Unique identifier |
| shopId | Shop reference |
| name | Category name |
| createdAt | Record creation time |
| updatedAt | Last update time |

### Business Rules

- Category names must be unique within a shop.
- One category contains multiple products.

---

## 3.4 Product

### Description

Represents an item available for sale.

### Responsibilities

- Maintain inventory.
- Participate in billing.
- Support search and filtering.

### Attributes

| Field | Description |
|--------|-------------|
| id | Unique identifier |
| shopId | Shop reference |
| categoryId | Category reference |
| name | Product name |
| sellingPrice | Selling price |
| quantity | Available stock |
| status | Product availability |
| createdAt | Record creation time |
| updatedAt | Last update time |

### Product Status

- ACTIVE
- OUT_OF_STOCK
- DISCONTINUED

### Business Rules

- Quantity cannot be negative.
- Selling price must be greater than zero.
- Inventory decreases after billing.
- Product cannot be deleted if referenced by any bill.
- Quantity reaching zero changes status to OUT_OF_STOCK.

---

## 3.5 Customer

### Description

Represents a customer who purchases products from a shop.

Customer information is stored to maintain billing history and improve future customer management.

### Responsibilities

- Store customer details.
- Associate bills with a customer.

### Attributes

| Field | Description |
|--------|-------------|
| id | Unique identifier |
| shopId | Shop reference |
| name | Customer name |
| phone | Customer phone |
| createdAt | Record creation time |
| updatedAt | Last update time |

### Business Rules

- Phone number is mandatory.
- Customer belongs to one shop.
- One customer can have multiple bills.

---

## 3.6 Bill

### Description

Represents a completed sales transaction.

A bill records all financial information at the time of purchase.

### Responsibilities

- Store transaction information.
- Maintain purchase history.
- Preserve financial records.

### Attributes

| Field | Description |
|--------|-------------|
| id | Unique identifier |
| shopId | Shop reference |
| customerId | Customer reference (nullable) |
| billNumber | Unique bill number |
| billDateTime | Date and time of transaction |
| paymentMethod | Payment type |
| status | Bill status |
| totalItems | Total quantity purchased |
| subtotal | Total before adjustments |
| grandTotal | Final payable amount |
| createdAt | Bill creation time |

### Payment Methods

- CASH
- UPI

### Bill Status

- PAID
- CANCELLED *(Reserved for future implementation)*

### Business Rules

- Bills are immutable.
- Bills cannot be deleted.
- Walk-in customer billing is supported.
- Every bill belongs to exactly one shop.
- Bill numbers must be unique.

---

## 3.7 Bill Item

### Description

Represents an individual product inside a bill.

Bill Item preserves historical transaction information even if the product changes later.

### Responsibilities

- Store purchased product details.
- Preserve historical pricing.

### Attributes

| Field | Description |
|--------|-------------|
| id | Unique identifier |
| billId | Bill reference |
| productId | Product reference |
| productName | Product name at purchase |
| quantity | Purchased quantity |
| priceAtPurchase | Product price during purchase |
| subtotal | Item subtotal |

### Business Rules

- Price at purchase never changes.
- Quantity must be greater than zero.
- One bill contains one or more bill items.

---

# 4. Domain Relationships

| Source | Relationship | Target |
|----------|-------------|---------|
| Owner | 1 → 1 | Shop |
| Shop | 1 → N | Categories |
| Shop | 1 → N | Products |
| Shop | 1 → N | Customers |
| Shop | 1 → N | Bills |
| Category | 1 → N | Products |
| Customer | 1 → N | Bills |
| Bill | 1 → N | Bill Items |
| Product | 1 → N | Bill Items |

---

# 5. Aggregate Roots

The following entities act as Aggregate Roots within the domain:

| Aggregate | Owns |
|-----------|------|
| Shop | Products, Categories, Customers, Bills |
| Bill | Bill Items |

Business operations should begin from an Aggregate Root to maintain consistency.

---

# 6. Domain Constraints

- Every Product belongs to one Category.
- Every Product belongs to one Shop.
- Every Category belongs to one Shop.
- Every Bill belongs to one Shop.
- Every Customer belongs to one Shop.
- Bill Items cannot exist without a Bill.
- Product quantity cannot become negative.
- Historical bills must remain unchanged.

---

# 7. Domain Invariants

The following conditions must always remain true:

- An Owner manages exactly one Shop.
- A Shop cannot exist without an Owner.
- A Bill must contain at least one Bill Item.
- Bill totals must equal the sum of Bill Item subtotals.
- Inventory updates only after successful bill generation.
- Historical purchase prices never change.

---

# 8. Future Domain Extensions

The current domain model is designed to support future additions without major redesign.

Planned future entities include:

- Supplier
- Purchase Order
- Inventory Transaction
- Notification
- Report
- AI Recommendation
- Sales Prediction
- Customer Analytics

---

# 9. Approval

| Field | Value |
|-------|-------|
| Status | Approved |
| Version | 1.0 |
| Approved During | Sprint 0 |

---

> This document defines the business domain of the Smart AI Retail Platform. Any change to an entity, relationship, or business responsibility must be reviewed before implementation.