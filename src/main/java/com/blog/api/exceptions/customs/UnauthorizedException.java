package com.blog.api.exceptions.customs;

/**
 * @author claudio.vilas
 * date 09/2023
 */


public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
