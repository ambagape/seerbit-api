package com.example.seerbitapi.error;

import com.example.seerbitapi.services.exceptions.InvalidDateException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author HP
 */
@ControllerAdvice
public class ErrorHandler {
   
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(400).body(errors);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleParseException(HttpMessageNotReadableException ex){                
        return ResponseEntity.status(422).body(ex.getMessage());
    }
    
    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity handleParseException(InvalidDateException ex){                
        return ResponseEntity.status(204).body(ex.getMessage());
    }
}
