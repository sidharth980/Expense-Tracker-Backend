# 🔐 Authentication Guide

## **Current Status**
Your APIs are now protected by JWT authentication. Here's how to use them:

## **🔑 Authentication Flow**

### 1. **Login to Get Token**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"pass"}'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "admin",
  "role": "ADMIN"
}
```

### 2. **Use Token for Protected APIs**
```bash
curl -X GET http://localhost:8080/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## **📋 API Endpoints**

### **Public Endpoints (No Auth Required)**
- `POST /auth/login` - Login
- `POST /auth/register` - Register
- `GET /users/**` - User endpoints (temporarily public)
- `GET /expenses/**` - Expense endpoints (temporarily public)
- `GET /accounts/**` - Account endpoints (temporarily public)

### **Protected Endpoints (Auth Required)**
- `GET /users/me` - Get current user info
- All other endpoints require authentication

## **🛠️ Testing Your APIs**

### **Step 1: Login**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"pass"}'
```

### **Step 2: Copy the token from response**

### **Step 3: Use token in other requests**
```bash
# Get all users
curl -X GET http://localhost:8080/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Get current user
curl -X GET http://localhost:8080/users/me \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Get expenses for user 1
curl -X GET http://localhost:8080/expenses/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## **🔧 Making APIs Protected Again**

When you're ready to make your APIs protected, update `SecurityConfig.java`:

```java
.authorizeHttpRequests(authz -> authz
    .requestMatchers("/auth/**").permitAll()
    .requestMatchers("/actuator/**").permitAll()
    // Remove these lines to make endpoints protected:
    // .requestMatchers("/users/**").permitAll()
    // .requestMatchers("/expenses/**").permitAll()
    // .requestMatchers("/accounts/**").permitAll()
    .anyRequest().authenticated()
)
```

## **📝 Register New Users**

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"password123"}'
```

## **🔍 Troubleshooting**

### **401 Unauthorized**
- Check if token is valid
- Ensure token is in Authorization header: `Bearer YOUR_TOKEN`
- Token expires after 24 hours

### **403 Forbidden**
- User doesn't have required role
- Check user permissions

### **500 Server Error**
- Check if database migration ran successfully
- Verify JWT secret in application.properties

## **🚀 Quick Test Commands**

```bash
# 1. Start your application
./gradlew bootRun

# 2. Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"pass"}'

# 3. Test protected endpoint (replace YOUR_TOKEN)
curl -X GET http://localhost:8080/users/me \
  -H "Authorization: Bearer YOUR_TOKEN"
``` 