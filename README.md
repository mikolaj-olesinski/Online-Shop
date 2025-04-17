#Spring Boot Online Shop

This project is a comprehensive e-commerce platform built with Spring Boot. It provides full product management, shopping cart functionality, order processing, and administrative features. The system includes secure authentication and clearly separates roles for admin and regular users.

---

## User Roles and Authentication

**Two user roles:**

- `ADMIN` – full access to product, category, and order management  
- `USER` – can browse, purchase, and manage their account  

**Authentication features:**

- Role-based access control (Spring Security)  
- Password encryption  
- Session management  

---

## Product Management

- Add, edit, delete, and view products  
- Assign products to hierarchical categories  
- Upload images and detailed descriptions  
- Users can post ratings and comments
<img width="1679" alt="Zrzut ekranu 2025-04-17 o 12 45 33" src="https://github.com/user-attachments/assets/3ca35e45-73b4-414c-a19e-91d68d644cdd" />

  
<img width=![Uploading Zrzut ekranu 2025-04-17 o 12.45.33.png…]()
"1023" alt="Zrzut ekranu 2025-04-17 o 12 46 18" src="https://github.com/user-attachments/assets/28bc4aec-335a-433c-87ea-88746ae37ab4" />

---

## Category Management

- Create, update, and delete categories  
- Support for nested categories  
<img width="1678" alt="Zrzut ekranu 2025-04-17 o 12 45 49" src="https://github.com/user-attachments/assets/fa462e1e-4a9f-433b-9f63-73dff5006459" />

---

## Order Management

- Create and track orders  
- Status updates: `in progress`, `shipped`, `delivered`, `canceled`  
- View order history and details  
- Admin-user messaging system within orders  
- Generate shipping labels (PDF with iText)  

<img width="1061" alt="Zrzut ekranu 2025-04-17 o 12 46 01" src="https://github.com/user-attachments/assets/2fad1122-99a5-4839-9433-fb71b85e3c4d" />
<img width="986" alt="Zrzut ekranu 2025-04-17 o 12 46 08" src="https://github.com/user-attachments/assets/702899f8-2d9a-4f2b-bfa4-b02fcd0d92a9" />

---

## Testing

- **Unit Testing** – JUnit 5, Mockito  
- **Integration Testing** – Spring Boot Test  
- **Controller Testing** – MockMVC  
- **UI and End-to-End Testing** – Selenium  
- **Code Coverage** – Jacoco with >80% coverage on core features  

---

## UML Diagrams

- Activity diagrams  
- Class diagrams  
- Sequence diagrams
- Domain Class Diagram
- Use Case Diagram

<img width="769" alt="Zrzut ekranu 2025-04-17 o 13 00 09" src="https://github.com/user-attachments/assets/cada94e5-5479-4180-84a9-d04af8658f42" />

---

## System Architecture

Follows the **MVC (Model-View-Controller)** pattern:

- **Model**: Entity classes like `Product`, `Category`, `Order`, etc.  
- **View**: Thymeleaf templates for dynamic content  
- **Controller**: Spring MVC REST controllers  
- **Service**: Business logic  
- **Repository**: Spring Data JPA for data access  

---

## Technology Stack

### Backend

- Java 11+  
- Spring Boot 2.x  
- Spring MVC  
- Spring Security  
- Spring Data JPA with Hibernate  
- Lombok  
- iText (for PDF generation)  

### Frontend

- Thymeleaf  
- Bootstrap 5  
- HTML5, CSS3, JavaScript, jQuery  

### Database

- H2 (for development and testing)  
- MySQL / PostgreSQL (for production)  

---

## Testing Strategy

- Unit tests  
- Integration tests  
- Controller tests  
- Service and repository tests  
- End-to-end Selenium tests

---

## Shipping and Delivery

- Multiple shipping options  
- Address management  
- PDF shipping label generation  
- Delivery tracking  

---

## API Controllers

- **`CategoryController`** – Category management  
- **`ProductController`** – Product CRUD operations  
- **`OrderController`** – Order processing and updates  
- **`DataFormController`** – Customer information forms  
- **`SecurityController`** – Authentication and routing  

---

## Database Entities

- `User`  
- `Product`  
- `Category`  
- `Cart`  
- `CartItem`  
- `Order`  
- `DataForm`  
- `Message`  
- `Comment`  

