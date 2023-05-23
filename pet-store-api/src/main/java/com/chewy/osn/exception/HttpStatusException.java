package com.chewy.osn.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HttpStatusException extends RuntimeException {

    private int statusCode;
    private String message;

    public HttpStatusException(Throwable e) {
        this.statusCode = 500;
        this.message = e.getMessage();
    }

    public HttpStatusException(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
