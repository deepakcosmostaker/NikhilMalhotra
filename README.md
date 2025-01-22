# Razorpay Payment Gateway Integration

This project demonstrates how to integrate the Razorpay payment gateway with a Spring Boot application. It supports creating Razorpay orders, verifying payments, and updating payment details in the database.

---

## Features
- Create orders for products with Razorpay integration.
- Verify Razorpay payments using the Razorpay signature.
- Update payment details and status in the database after successful verification.

---

## Technologies Used
- **Backend**: Spring Boot
- **Database**: JPA, Hibernate
- **Payment Gateway**: Razorpay Java SDK
- **Dependencies**: `spring-boot-starter-data-jpa`, `spring-boot-starter-web`

---

## Prerequisites
- Java 11 or higher
- Maven
- Razorpay account (for API keys)
- Database (e.g., MySQL or H2)

---

## Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd <repository-folder>
```

### 2. Configure Database
Update the database credentials in `src/main/resources/application.properties` or `application.yml`.

### 3. Add Razorpay Keys
Replace the placeholder key in the `verifyPayment` method:
```java
Utils.verifySignature(payload, razorpaySignature, "YOUR_RAZORPAY_SECRET_KEY");
```

### 4. Build and Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

---

## API Endpoints

### 1. Create Order
**Endpoint**: `POST /api/payment/create-order`

**Request Parameters**:
- `productId` (Query Parameter): The ID of the product for which the order is being created.

**Response**:
- **200 OK**: Returns the order details.
- **500 Internal Server Error**: If an error occurs.

**Example Request**:
```bash
curl -X POST "http://localhost:8080/api/payment/create-order?productId=1"
```

---

### 2. Update Payment Details
**Endpoint**: `GET /api/payment/updatePaymentDetails`

**Request Parameters**:
- `order_id` (Query Parameter): The Razorpay order ID.
- `payment_id` (Query Parameter): The Razorpay payment ID.
- `signature` (Query Parameter): The Razorpay signature.

**Response**:
- **200 OK**: "Payment details updated successfully."
- **400 Bad Request**: "Failed to update payment details."

**Example Request**:
```bash
curl -X GET "http://localhost:8080/api/payment/updatePaymentDetails?order_id=order_123&payment_id=pay_123&signature=abcd1234"
```

---

### 3. Verify Payment
**Endpoint**: `POST /api/payment/verify`

**Request Parameters**:
- `orderId` (Query Parameter): The Razorpay order ID.
- `paymentId` (Query Parameter): The Razorpay payment ID.
- `razorpaySignature` (Query Parameter): The Razorpay signature.

**Response**:
- **200 OK**: "Payment verified successfully."
- **400 Bad Request**: "Payment verification failed."
- **500 Internal Server Error**: If an error occurs during verification.

**Example Request**:
```bash
curl -X POST "http://localhost:8080/api/payment/verify" \
    -d "orderId=order_123" \
    -d "paymentId=pay_123" \
    -d "razorpaySignature=abcd1234"
```

---

## Entity Overview

### 1. `OrderDetails`
Represents an order created in the system.
- `id`: Unique identifier for the order.
- `user`: Associated user.
- `product`: Product being ordered.
- `orderId`: Razorpay order ID.
- `paymentId`: Razorpay payment ID (updated after successful payment).
- `signature`: Razorpay signature (updated after successful verification).
- `status`: Status of the order (e.g., "PENDING", "CREATED", "SUCCESS").

### 2. `Product`
Represents a product available for purchase.
- `id`: Unique identifier for the product.
- `name`: Name of the product.
- `price`: Price of the product in INR.

### 3. `User`
Represents a user in the system.
- `id`: Unique identifier for the user.
- `name`: Name of the user.
- `email`: Email address of the user.

---

## Error Handling
- **400 Bad Request**: For invalid input or failed operations.
- **500 Internal Server Error**: For unexpected errors.

---

## Future Enhancements
- Add support for webhook handling to automate payment updates.
- Implement authentication for secure API access.
- Extend error handling for better debugging.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.
