package com.druiz.bosonit.backempresa.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class CustomResponseExceptionHandler extends ResponseEntityExceptionHandler {
        @ExceptionHandler(NotFoundException.class)
        public final ResponseEntity<CustomError> notFoundException(NotFoundException notFoundException){
            CustomError customError = new CustomError(new Date(), 404, notFoundException.getMessage());
            return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(UnprocesableException.class)
        public ResponseEntity<CustomError> unProcesableException(UnprocesableException unprocesableException) {
            CustomError customError = new CustomError(new Date(), 422, unprocesableException.getMessage());
            return new ResponseEntity<>(customError, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        @ExceptionHandler(BadRequest.class)
        public ResponseEntity<CustomError> badRequest(BadRequest badRequest) {
            CustomError customError = new CustomError(new Date(), 400, badRequest.getMessage());
            return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Forbidden.class)
        public final ResponseEntity<CustomError> forbidden(Forbidden forbidden){
            CustomError customError = new CustomError(new Date(), 403, forbidden.getMessage());
            return new ResponseEntity<>(customError, HttpStatus.FORBIDDEN);
        }
    }
