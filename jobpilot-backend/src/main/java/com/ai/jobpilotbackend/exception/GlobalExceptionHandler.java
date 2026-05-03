package com.ai.jobpilotbackend.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * @author wzs
 * @date 2026年05月03日 17:32
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public Map<String, Object> handleRuntime(RuntimeException e) {
        return Map.of(
                "code", 500,
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();

        return Map.of(
                "code", 400,
                "message", msg
        );
    }
}
