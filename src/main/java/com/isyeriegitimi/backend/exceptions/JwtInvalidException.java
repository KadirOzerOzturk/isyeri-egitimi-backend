package com.isyeriegitimi.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException(String message) {
        super(message);
    }
}
