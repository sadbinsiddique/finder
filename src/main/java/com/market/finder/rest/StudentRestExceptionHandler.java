package com.market.finder.rest;

import com.market.finder.entity.StudentErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StudentRestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundExaptions exc) {
        StudentErrorResponse error = new StudentErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value()); // For 404 error
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(Exception exc) {
        StudentErrorResponse e = new StudentErrorResponse();

        e.setStatus(HttpStatus.BAD_REQUEST.value()); // For 400 error
        e.setMessage(exc.getMessage());
        e.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
}
