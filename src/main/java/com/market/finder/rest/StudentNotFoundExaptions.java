package com.market.finder.rest;

public class StudentNotFoundExaptions extends RuntimeException {

    public StudentNotFoundExaptions() {}

    public StudentNotFoundExaptions(String message) {
        super(message);
    }
    public StudentNotFoundExaptions(String message, Throwable cause) {
        super(message, cause);
    }
    public StudentNotFoundExaptions(Throwable cause) {
        super(cause);
    }
}
