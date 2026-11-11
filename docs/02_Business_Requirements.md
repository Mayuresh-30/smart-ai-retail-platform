# Smart AI Retail Platform

# Business Requirements

---

## Document Information

| Field | Value |
|-------|-------|
| Document | Business Requirements |
| Project | Smart AI Retail Platform |
| Version | 1.0 |
| Status | Approved |
| Sprint | Sprint 0 |
| Last Updated | YYYY-MM-DD |
| Owner | Project Team |

---

# 1. Business Overview

The Smart AI Retail Platform is a retail management system designed to help shop owners efficiently manage their daily business operations through a single web application.

The application provides secure authentication, inventory management, customer management, billing, dashboard analytics, and bill history while maintaining a scalable architecture for future AI integration.

---

# 2. Business Goals

- Simplify retail operations.
- Reduce manual billing errors.
- Manage inventory efficiently.
- Track customer purchases.
- Maintain bill history.
- Provide business insights through dashboard analytics.
- Build a scalable platform for future enhancements.

---

# 3. User Roles

## Shop Owner

The Shop Owner is the only user role in Version 1.

Responsibilities:

- Manage shop information.
- Manage product categories.
- Manage products.
- Manage customers.
- Generate bills.
- View dashboard analytics.
- View bill history.
- Manage profile.

---

# 4. Functional Requirements

## Authentication

| ID | Requirement |
|----|-------------|
| FR-001 | Shop owner can register a new account. |
| FR-002 | Shop owner can log in using email and password. |
| FR-003 | System shall authenticate users using JWT. |
| FR-004 | Unauthorized users cannot access protected resources. |
| FR-005 | Shop owner can log out. |

---

## Shop Management

| ID | Requirement |
|----|-------------|
| FR-006 | View shop information. |
| FR-007 | Update shop information. |
| FR-008 | Update profile information. |

---

## Category Management

| ID | Requirement |
|----|-------------|
| FR-009 | Create category. |
| FR-010 | Update category. |
| FR-011 | Delete category if no products exist. |
| FR-012 | View category list. |

---

## Product Management

| ID | Requirement |
|----|-------------|
| FR-013 | Create product. |
| FR-014 | Update product. |
| FR-015 | Delete product if not used in any bill. |
| FR-016 | View products. |
| FR-017 | Search product by name. |
| FR-018 | Filter products by category. |
| FR-019 | Paginate product list. |

---

## Customer Management

| ID | Requirement |
|----|-------------|
| FR-020 | Create customer. |
| FR-021 | Update customer. |
| FR-022 | Delete customer if no bills exist. |
| FR-023 | View customer list. |
| FR-024 | Search customer by name or phone. |
| FR-025 | Paginate customer list. |

---

## Billing

| ID | Requirement |
|----|-------------|
| FR-026 | Search products during billing. |
| FR-027 | Add products to bill. |
| FR-028 | Remove products from bill. |
| FR-029 | Update product quantity in bill. |
| FR-030 | Select customer or generate walk-in bill. |
| FR-031 | Select payment method. |
| FR-032 | Generate bill. |
| FR-033 | Reduce inventory after successful billing. |

---

## Dashboard

| ID | Requirement |
|----|-------------|
| FR-034 | Display today's revenue. |
| FR-035 | Display today's bills. |
| FR-036 | Display today's customers. |
| FR-037 | Display total products. |
| FR-038 | Display low stock products. |
| FR-039 | Display most sold products. |
| FR-040 | Display last three generated bills. |

---

## Bill History

| ID | Requirement |
|----|-------------|
| FR-041 | View bill history. |
| FR-042 | Search bills by bill number. |
| FR-043 | Search bills by customer. |
| FR-044 | Filter bills by date. |
| FR-045 | Paginate bill history. |
| FR-046 | View complete bill details. |

---

# 5. Business Rules

| ID | Rule |
|----|------|
| BR-001 | One owner can own only one shop. |
| BR-002 | Owner email must be unique. |
| BR-003 | Owner phone number must be unique. |
| BR-004 | Customer phone number is mandatory. |
| BR-005 | Product price must be greater than zero. |
| BR-006 | Product quantity cannot be negative. |
| BR-007 | Product prices already include GST. |
| BR-008 | Bills are immutable after creation. |
| BR-009 | Bills cannot be deleted. |
| BR-010 | Product cannot be deleted if referenced by any bill. |
| BR-011 | Category name must be unique within a shop. |
| BR-012 | Inventory shall decrease after successful billing. |
| BR-013 | Payment methods are CASH and UPI only. |
| BR-014 | Walk-in customer billing is allowed. |

---

# 6. Validation Rules

| ID | Validation |
|----|------------|
| VR-001 | First Name is required. |
| VR-002 | Last Name is required. |
| VR-003 | Shop Name is required. |
| VR-004 | Email is required and must be valid. |
| VR-005 | Password is required. |
| VR-006 | Product Name is required. |
| VR-007 | Product Price > 0. |
| VR-008 | Product Quantity >= 0. |
| VR-009 | Category Name is required. |
| VR-010 | Customer Name is required. |
| VR-011 | Customer Phone is required. |

---

# 7. Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR-001 | Responsive UI. |
| NFR-002 | Secure authentication using JWT. |
| NFR-003 | Clean Modular Monolith Architecture. |
| NFR-004 | Global Exception Handling. |
| NFR-005 | Bean Validation. |
| NFR-006 | RESTful API design. |
| NFR-007 | Maintainable and testable codebase. |
| NFR-008 | Pagination for large datasets. |
| NFR-009 | Search operations should provide fast response times for expected V1 data volumes. |

---

# 8. Out of Scope (Version 1)

- AI Features
- OAuth Login
- PDF Bill Generation
- Supplier Management
- Inventory Forecasting
- Notifications
- Reports Export
- Multi-store Management

---

# 9. Future Scope

## Version 2

- OAuth Login
- PDF Bill
- Reports
- Notifications
- Inventory Improvements

## Version 3

- AI Product Recommendation
- Sales Forecasting
- Customer Purchase Analysis
- Retail AI Assistant
- Business Growth Suggestions

---

# 10. Document Approval

| Field | Value |
|-------|-------|
| Status | Approved |
| Version | 1.0 |
| Approved During | Sprint 0 |

---

> All implementation, API design, database design, and testing must satisfy the requirements defined in this document.