package com.levelup.levelup_academy.Advice;

import com.levelup.levelup_academy.Api.ApiException;
import com.levelup.levelup_academy.Api.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailSendException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class AdviceController {

    // Our Exception
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity ApiException(ApiException e){
        String message=e.getMessage();
        return ResponseEntity.status(400).body(message);
    }

    // Server Validation Exception
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg=e.getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body(new ApiResponse(msg));
    }

    // Server Validation Exception
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> ConstraintViolationException(ConstraintViolationException e) {
        String msg =e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(msg));
    }


    // SQL Constraint Ex:(Duplicate) Exception
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponse> SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
        String msg=e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(msg));
    }

    // wrong write SQL in @column Exception
    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class )
    public ResponseEntity<ApiResponse> InvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException e){
        String msg=e.getMessage();
        return ResponseEntity.status(200).body(new ApiResponse(msg));
    }

    // Database Constraint Exception
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> DataIntegrityViolationException(DataIntegrityViolationException e){
        String msg = e.getMessage();
        
        // Check for duplicate username
        if (msg.contains("username") && (msg.contains("Duplicate") || msg.contains("duplicate"))) {
            msg = "❌ Username already exists. Please choose a different username.";
        }
        // Check for duplicate email
        else if (msg.contains("email") && (msg.contains("Duplicate") || msg.contains("duplicate"))) {
            msg = "❌ Email already registered. Please use a different email or login.";
        }
        // Generic duplicate error
        else if (msg.contains("Duplicate") || msg.contains("duplicate")) {
            msg = "❌ This information already exists in the system. Please check your username and email.";
        }
        
        return ResponseEntity.status(400).body(new ApiResponse(msg));
    }

    // Method not allowed Exception
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(msg));
    }

    // Json parse Exception
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> HttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(msg));
    }

    // TypesMisMatch Exception
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(msg));
    }

    @ExceptionHandler(value = MailSendException.class)
    public ResponseEntity<ApiResponse> MailSendException(MailSendException e) {
        // Don't fail registration just because email failed
        String msg = "Account created successfully! (Email notification may be delayed)";
        return ResponseEntity.status(200).body(new ApiResponse(msg));
    }


    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse> HttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e){
        String msg = e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(msg));
    }


    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> NoResourceFoundException(NoResourceFoundException e){
        // Don't handle static resource 404s (let Spring handle CSS/JS/images)
        // Only handle API endpoint 404s
        String resourcePath = e.getResourcePath();
        if (resourcePath != null && 
            (resourcePath.endsWith(".css") || resourcePath.endsWith(".js") || 
             resourcePath.endsWith(".png") || resourcePath.endsWith(".jpg") || 
             resourcePath.endsWith(".gif") || resourcePath.endsWith(".svg") ||
             resourcePath.endsWith(".ico") || resourcePath.endsWith(".woff") ||
             resourcePath.endsWith(".woff2") || resourcePath.endsWith(".ttf") ||
             resourcePath.endsWith(".html") || resourcePath.startsWith("/assets/"))) {
            // Let Spring handle static resources - return null to use default handling
            return null;
        }
        // Only handle API endpoint 404s
        if (resourcePath != null && resourcePath.startsWith("/api/")) {
            String msg = "Resource not found: " + resourcePath;
            return ResponseEntity.status(404).body(new ApiResponse(msg));
        }
        // For other resources, use default Spring handling
        return null;
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ApiResponse> NullPointerException(NullPointerException e){
        String msg = e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(msg));
    }

}
