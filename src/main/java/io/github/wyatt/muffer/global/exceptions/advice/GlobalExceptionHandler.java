package io.github.wyatt.muffer.global.exceptions.advice;

import io.github.wyatt.muffer.global.exceptions.BusinessAccessDeniedException;
import io.github.wyatt.muffer.global.exceptions.BusinessNotFoundException;
import io.github.wyatt.muffer.global.exceptions.ForbiddenModifyException;
import io.github.wyatt.muffer.global.exceptions.UnauthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException is intercepted by global exception handler", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(UnauthenticationException.class)
    public ResponseEntity<?> unAuthenticationExceptionHandler(UnauthenticationException e) {
        log.error("UnauthenticatedException is intercepted by global exception handler", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(ForbiddenModifyException.class)
    public ResponseEntity<?> forbiddenModifyExceptionHandler(ForbiddenModifyException e) {
        log.error("ForbiddenModifyException is intercepted by global exception handler", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(BusinessAccessDeniedException.class)
    public ResponseEntity<?> businessAccessDeniedExceptionHandler(BusinessAccessDeniedException e) {
        log.error("BusinessAccessDeniedException is intercepted by global exception handler", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(BusinessNotFoundException.class)
    public ResponseEntity<?> businessNotFoundExceptionHandler(BusinessNotFoundException e) {
        log.error("BusinessNotFoundException is intercepted by global exception handler", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
