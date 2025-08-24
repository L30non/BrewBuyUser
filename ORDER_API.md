# Brew Buy Platform - Order Management API Documentation

This document provides comprehensive information about the Order Management REST API for the Brew Buy Platform Android application. This API allows users to create, retrieve, update, and delete orders.

## Base URL

For development:
- Emulator: `http://10.0.2.2:8080`
- Physical device: `http://[YOUR_COMPUTER_IP]:8080`

For production:
- `https://[YOUR_DOMAIN]/api`

## Authentication

All order management endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer [JWT_TOKEN]
```

To obtain a JWT token, use the Authentication API endpoints:
- Register: `POST /api/auth/register`
- Login: `POST /api/auth/login`

## Order Management Endpoints

### Create Order

**Endpoint**: `POST /api/orders`

**Request Body**:
```json
{
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "price": 19.99
    }
  ]
}
```

**Response**:
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 39.98,
  "status": "PENDING",
  "createdAt": "2023-01-01T00:00:00",
  "updatedAt": "2023-01-01T00:00:00",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 2,
      "price": 19.99
    }
  ]
}
```

**Status Codes**:
- 201: Success
- 400: Validation error
- 401: Unauthorized

### Get Order by ID

**Endpoint**: `GET /api/orders/{id}`

**Response**:
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 39.98,
  "status": "PENDING",
  "createdAt": "2023-01-01T00:00:00",
  "updatedAt": "2023-01-01T00:00:00",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 2,
      "price": 19.99
    }
  ]
}
```

**Status Codes**:
- 200: Success
- 401: Unauthorized
- 403: Forbidden (user doesn't own the order)
- 404: Order not found

### Get My Orders

**Endpoint**: `GET /api/orders/user/me`

**Response**:
```json
[
  {
    "id": 1,
    "userId": 1,
    "totalAmount": 39.98,
    "status": "PENDING",
    "createdAt": "2023-01-01T00:00:00",
    "updatedAt": "2023-01-01T00:00:00",
    "items": [
      {
        "id": 1,
        "productId": 1,
        "quantity": 2,
        "price": 19.99
      }
    ]
  }
]
```

**Status Codes**:
- 200: Success
- 401: Unauthorized

### Update Order Status

**Endpoint**: `PUT /api/orders/{id}/status?status=CONFIRMED`

**Parameters**:
- status (query parameter): New status value (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)

**Response**:
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 39.98,
  "status": "CONFIRMED",
  "createdAt": "2023-01-01T00:00:00",
  "updatedAt": "2023-01-01T01:00:00",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 2,
      "price": 19.99
    }
  ]
}
```

**Status Codes**:
- 200: Success
- 400: Invalid status value
- 401: Unauthorized
- 403: Forbidden (user doesn't own the order)
- 404: Order not found

### Delete Order

**Endpoint**: `DELETE /api/orders/{id}`

**Response**:
No content

**Status Codes**:
- 204: Success
- 401: Unauthorized
- 403: Forbidden (user doesn't own the order)
- 404: Order not found

## Data Models

### Order

```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 39.98,
  "status": "PENDING",
  "createdAt": "2023-01-01T00:00:00",
  "updatedAt": "2023-01-01T00:00:00",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 2,
      "price": 19.99
    }
  ]
}
```

### OrderItem

```json
{
  "id": 1,
  "productId": 1,
  "quantity": 2,
  "price": 19.99
}
```

## Error Handling

The API uses standard HTTP status codes:

- 200: Success (GET, PUT)
- 201: Created (POST)
- 204: No Content (DELETE)
- 400: Bad Request - Invalid request parameters or validation errors
- 401: Unauthorized - Missing or invalid authentication token
- 403: Forbidden - User doesn't have permission to access the resource
- 404: Not Found - Resource not found
- 500: Internal Server Error - Unexpected server error

## Implementation Notes for Android

1. **JWT Token Storage**: Store the JWT token securely using Android Keystore or EncryptedSharedPreferences
2. **Network Security**: Enable cleartext traffic for development (HTTP) in your network security config
3. **Retrofit Configuration**: Configure Retrofit with an interceptor to automatically add the Authorization header
4. **Error Handling**: Implement proper error handling for network failures and API errors
5. **Loading States**: Implement loading indicators for API calls
6. **Offline Support**: Consider implementing offline support with local database (Room)

## Example Retrofit Interface

```java
public interface OrderApiService {
    @POST("api/orders")
    Call<OrderResponse> createOrder(@Body CreateOrderRequest request);
    
    @GET("api/orders/{id}")
    Call<OrderResponse> getOrder(@Path("id") Long id);
    
    @GET("api/orders/user/me")
    Call<List<OrderResponse>> getMyOrders();
    
    @PUT("api/orders/{id}/status")
    Call<OrderResponse> updateOrderStatus(@Path("id") Long id, @Query("status") String status);
    
    @DELETE("api/orders/{id}")
    Call<Void> deleteOrder(@Path("id") Long id);
}
```

## Common Issues and Solutions

1. **CORS Errors**: Make sure you're using the correct IP address for your backend
2. **Network Permissions**: Ensure you have `<uses-permission android:name="android.permission.INTERNET" />` in your AndroidManifest.xml
3. **Emulator Connection**: Use 10.0.2.2 instead of localhost when connecting from Android emulator
4. **Token Expiration**: Handle JWT token expiration by redirecting to login screen
5. **Order Ownership**: Users can only access orders they've created

## Testing Endpoints

You can test the API endpoints using tools like Postman:

```bash
# Create an order (requires authentication)
curl -X POST http://localhost:8080/api/orders \\
  -H "Content-Type: application/json" \\
  -H "Authorization: Bearer [JWT_TOKEN]" \\
  -d '{"items":[{"productId":1,"quantity":2,"price":19.99}]}'
```