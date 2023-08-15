package com.garnet.errorhandling.exception;

import static java.util.Collections.emptyList;

import com.garnet.errorhandling.exception.interceptor.OpenAIErrorInterceptor;
import com.garnet.errorhandling.exception.model.ErrorResponse;
import com.garnet.errorhandling.exception.model.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final OpenAIErrorInterceptor openAIErrorInterceptor;

    @Autowired
    public GlobalExceptionHandler(OpenAIErrorInterceptor openAIErrorInterceptor) {
        this.openAIErrorInterceptor = openAIErrorInterceptor;
    }

    private static String extractLastFieldName(Path propertyPath) {
        Path.Node lastNode = null;
        for (Path.Node node : propertyPath) {
            lastNode = node;
        }
        return lastNode != null ? lastNode.getName() : "";
    }

    @ExceptionHandler(CriticalBusinessException.class)
    public ResponseEntity<ErrorResponse> handle(CriticalBusinessException ex, HttpServletRequest request) {
        openAIErrorInterceptor.handle(ex);

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Critical Business Exception",
            ex.getMessage(),
            request.getRequestURI(),
            emptyList()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Resource Not Found",
            ex.getMessage(),
            request.getRequestURI(),
            emptyList()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException ex, HttpServletRequest request) {
        List<FieldError> fieldErrors = extractFieldErrors(ex);

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            "Input validation failed",
            request.getRequestURI(),
            fieldErrors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private List<FieldError> extractFieldErrors(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
            .stream()
            .map(c -> new FieldError(extractLastFieldName(c.getPropertyPath()), c.getMessage()))
            .collect(Collectors.toList());
    }
}
