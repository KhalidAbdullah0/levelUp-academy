# 🚀 Quick Start Guide

## Prerequisites
- ✅ Java 17+ installed
- ✅ MySQL running on port 3306
- ✅ Database `levelup1` created

## Start the Application

### ✨ Option 1: Using Maven Wrapper (Easiest)
```bash
./mvnw spring-boot:run
```

### Option 2: Using Maven Command
```bash
mvn spring-boot:run
```

### Option 3: Using IDE
- Open project in IntelliJ IDEA or Eclipse
- Run `LevelUpAcademyApplication.java`

## 🌐 Access the Application

Once the application starts, open your browser:

### 📝 Sign Up Page
```
http://localhost:8080/index.html
```
or
```
http://localhost:8080/signup.html
```

### 🔐 Login Page
```
http://localhost:8080/login.html
```

### 📊 Dashboard (after login)
```
http://localhost:8080/dashboard.html
```

## ⚠️ Troubleshooting

### Issue: Port 8080 already in use
**Solution:** Kill the process or change the port in `application.properties`:
```properties
server.port=8081
```

### Issue: Database connection failed
**Solution:** 
1. Make sure MySQL is running:
   ```bash
   # macOS
   brew services start mysql
   
   # Or check status
   brew services list
   ```
2. Create database if not exists:
   ```sql
   CREATE DATABASE levelup1;
   ```

### Issue: Maven command not found
**Solution:** Use the Maven wrapper instead:
```bash
./mvnw spring-boot:run
```

## 📱 Test the Flow

1. **Sign Up** → Create a new player account
2. **Login** → Use your credentials  
3. **Dashboard** → See your JWT token and role
4. **Logout** → Clear session and return to login

## 🎯 Default Backend Endpoints

- **Sign Up:** `POST http://localhost:8080/api/v1/player/register`
- **Login:** `POST http://localhost:8080/api/v1/auth/login`
- **Protected APIs:** Need JWT token in Authorization header

## 🔥 Hot Reload

For development, you can use Spring Boot DevTools:
1. Make changes to HTML/CSS/JS files in `src/main/resources/static/`
2. Save the file
3. Refresh browser (no restart needed)

---

**Ready? Let's go! 🎮**
```bash
./mvnw spring-boot:run
```

Then open: **http://localhost:8080/index.html**

