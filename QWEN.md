# Brew Buy Platform - Backend API Documentation

This document provides comprehensive information about the backend REST API for the Brew Buy Platform Android application. The backend is built with Spring Boot and provides endpoints for user authentication and product management.

## Base URL

For development:
- Emulator: `http://10.0.2.2:8080`
- Physical device: `http://[YOUR_COMPUTER_IP]:8080`

For production:
- `https://[YOUR_DOMAIN]/api`

## Authentication

All endpoints except registration and login require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer [JWT_TOKEN]
```

### Register User

**Endpoint**: `POST /api/auth/register`

**Request Body**:
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "fullName": "string (optional)",
  "phone": "string (optional)"
}
```

**Response**:
```json
{
  "id": 1,
  "username": "string",
  "email": "string",
  "fullName": "string",
  "phone": "string",
  "createdAt": "2023-01-01T00:00:00",
  "updatedAt": "2023-01-01T00:00:00"
}
```

**Status Codes**:
- 200: Success
- 400: Username or email already exists

### Login User

**Endpoint**: `POST /api/auth/login`

**Request Body**:
```json
{
  "username": "string",
  "password": "string"
}
```

**Response**:
```json
{
  "id": 1,
  "username": "string",
  "email": "string",
  "fullName": "string",
  "phone": "string",
  "createdAt": "2023-01-01T00:00:00",
  "updatedAt": "2023-01-01T00:00:00"
}
```

**Status Codes**:
- 200: Success
- 400: Invalid credentials

### Logout User

**Endpoint**: `POST /api/auth/logout`

**Response**:
```json
"Logged out successfully"
```

**Status Codes**:
- 200: Success

## Product Management

All product endpoints require authentication with a valid JWT token.

### Get All Products

**Endpoint**: `GET /api/products`

**Response**:
```json
[
  {
    "id": 1,
    "name": "Product Name",
    "description": "Product Description",
    "price": 19.99,
    "quantity": 100
  }
]
```

**Status Codes**:
- 200: Success

### Get Product by ID

**Endpoint**: `GET /api/products/{id}`

**Response**:
```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Product Description",
  "price": 19.99,
  "quantity": 100
}
```

**Status Codes**:
- 200: Success
- 404: Product not found

### Create Product

**Endpoint**: `POST /api/products`

**Request Body**:
```json
{
  "name": "Product Name",
  "description": "Product Description",
  "price": 19.99,
  "quantity": 100
}
```

**Response**:
```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Product Description",
  "price": 19.99,
  "quantity": 100
}
```

**Status Codes**:
- 200: Success
- 400: Validation error

### Update Product

**Endpoint**: `PUT /api/products/{id}`

**Request Body**:
```json
{
  "name": "Updated Product Name",
  "description": "Updated Product Description",
  "price": 29.99,
  "quantity": 50
}
```

**Response**:
```json
{
  "id": 1,
  "name": "Updated Product Name",
  "description": "Updated Product Description",
  "price": 29.99,
  "quantity": 50
}
```

**Status Codes**:
- 200: Success
- 404: Product not found
- 400: Validation error

### Delete Product

**Endpoint**: `DELETE /api/products/{id}`

**Response**:
No content

**Status Codes**:
- 200: Success
- 404: Product not found

## Data Models

### User

```json
{
  "id": 1,
  "username": "string",
  "email": "string",
  "password": "string (hashed)",
  "fullName": "string",
  "phone": "string",
  "createdAt": "2023-01-01T00:00:00",
  "updatedAt": "2023-01-01T00:00:00"
}
```

### Product

```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "price": 19.99,
  "quantity": 100
}
```

## Error Handling

The API uses standard HTTP status codes:

- 200: Success
- 400: Bad Request - Invalid request parameters or validation errors
- 401: Unauthorized - Missing or invalid authentication token
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
public interface ApiService {
    @POST("api/auth/register")
    Call<User> register(@Body User user);
    
    @POST("api/auth/login")
    Call<User> login(@Body AuthRequest authRequest);
    
    @GET("api/products")
    Call<List<Product>> getProducts();
    
    @GET("api/products/{id}")
    Call<Product> getProduct(@Path("id") Long id);
    
    @POST("api/products")
    Call<Product> createProduct(@Body Product product);
    
    @PUT("api/products/{id}")
    Call<Product> updateProduct(@Path("id") Long id, @Body Product product);
    
    @DELETE("api/products/{id}")
    Call<Void> deleteProduct(@Path("id") Long id);
}
```

## Common Issues and Solutions

1. **CORS Errors**: Make sure you're using the correct IP address for your backend
2. **Network Permissions**: Ensure you have `<uses-permission android:name="android.permission.INTERNET" />` in your AndroidManifest.xml
3. **Emulator Connection**: Use 10.0.2.2 instead of localhost when connecting from Android emulator
4. **Token Expiration**: Handle JWT token expiration by redirecting to login screen

## Testing Endpoints

You can test the API endpoints using tools like Postman or curl:

```bash
# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Get products (requires authentication)
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer [JWT_TOKEN]"
```

## Admin Functionality

Note: Admin functionality is handled separately through a web application. The Android app only needs to interact with user authentication and product management endpoints as documented above.