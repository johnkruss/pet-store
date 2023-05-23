package com.chewy.osn.exception;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.simple.SimpleHttpResponseFactory;
import jakarta.inject.Singleton;

@Controller
@Singleton
public class PetExceptionHandler {

    @Error(global = true)
    public HttpResponse<HttpErrorBody> handle(PetException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getCode());
        MutableHttpResponse<HttpErrorBody> response = SimpleHttpResponseFactory.INSTANCE.status(status);
        response.contentType(MediaType.APPLICATION_JSON_TYPE);
        response.body(buildResponse(exception));
        return response;
    }

    private HttpErrorBody buildResponse(PetException exception) {
        HttpErrorBody response = new HttpErrorBody();
        response.setMessage(exception.getMessage());
        return response;
    }
}
