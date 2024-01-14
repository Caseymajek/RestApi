package com.RestApi.RestfulApi.exception;

public class PostNotFoundException extends Exception{
    private String message;

    public PostNotFoundException(String message) {
        this.message = message;
    }
}
