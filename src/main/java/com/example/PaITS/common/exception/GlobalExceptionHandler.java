package com.example.PaITS.common.exception;

import com.example.PaITS.common.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiErrorResponse.ErrorDetails errorDetails = ApiErrorResponse.ErrorDetails.builder()
                .code("RESOURCE_NOT_FOUND")
                .message(ex.getMessage())
                .details(null)
                .build();

        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .error(errorDetails)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStatusTransitionException(InvalidStatusTransitionException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put("fromStatus", ex.getFromStatus());
        details.put("toStatus", ex.getToStatus());
        details.put("availableTransitions", ex.getAvailableTransitions());

        ApiErrorResponse.ErrorDetails errorDetails = ApiErrorResponse.ErrorDetails.builder()
                .code("INVALID_STATUS_TRANSITION")
                .message(ex.getMessage())
                .details(details)
                .build();

        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .error(errorDetails)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiErrorResponse.ErrorDetails errorDetails = ApiErrorResponse.ErrorDetails.builder()
                .code("VALIDATION_FAILED")
                .message("Input validation failed")
                .details(errors)
                .build();

        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .error(errorDetails)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex) {
        ApiErrorResponse.ErrorDetails errorDetails = ApiErrorResponse.ErrorDetails.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message(ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred")
                .details(null)
                .build();

        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .error(errorDetails)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
