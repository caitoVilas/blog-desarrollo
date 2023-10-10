package com.blog.api.exceptions.customs;

/**
 * @author claudio.vilas
 * date 09/2023
 */

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
