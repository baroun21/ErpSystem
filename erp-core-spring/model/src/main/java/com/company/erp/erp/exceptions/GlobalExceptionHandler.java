//package com.company.erp.erp.exceptions;
//
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    // Handle Entity Not Found
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleEntityNotFound(
//            EntityNotFoundException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.NOT_FOUND.value(),
//                "Not Found",
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//    }
//
//    // Handle Illegal Arguments
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgument(
//            IllegalArgumentException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                "Bad Request",
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//        return ResponseEntity.badRequest().body(error);
//    }
//
//    // Handle Validation Errors
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationErrors(
//            MethodArgumentNotValidException ex,
//            HttpServletRequest request) {
//
//        String message = ex.getHeaders()
//                .getFieldErrors()
//                .stream()
//                .map(field -> field.getField() + ": " + field.getDefaultMessage())
//                .findFirst()
//                .orElse(ex.getMessage());
//
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                "Validation Error",
//                message,
//                request.getRequestURI()
//        );
//        return ResponseEntity.badRequest().body(error);
//    }
//
//    // Handle Database Constraint Violations (e.g. unique key)
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ErrorResponse> handleConstraintViolation(
//            DataIntegrityViolationException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//                "Database Constraint Violation",
//                ex.getMostSpecificCause().getMessage(),
//                request.getRequestURI()
//        );
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
//    }
//
//    // Handle Generic Exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGenericException(
//            Exception ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "Internal Server Error",
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
//}
