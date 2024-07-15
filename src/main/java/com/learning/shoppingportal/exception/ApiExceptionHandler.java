package com.learning.shoppingportal.exception;

import com.learning.shoppingportal.modal.io.ResponseWrapper;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public Object handleException(Throwable throwable){
        return ResponseEntity.badRequest().body(new ResponseWrapper(false,null,throwable.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    public Object handleThrowable(Throwable throwable){
        return ResponseEntity.internalServerError().body(new ResponseWrapper(false,null,"Something went wrong, We are looking into it don't worry"));
    }

}
