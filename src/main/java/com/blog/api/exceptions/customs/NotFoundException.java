package com.blog.api.exceptions.customs;

/**
 * @author claudio.vilas
 * date 09/2023
 */

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
