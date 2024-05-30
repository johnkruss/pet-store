package com.johnkruss.osn.exception

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.simple.SimpleHttpResponseFactory
import jakarta.inject.Singleton

@Controller
@Singleton
class PetExceptionHandler {
    @Error(global = true)
    fun handle(exception: PetException): HttpResponse<HttpErrorBody> {
        val status = HttpStatus.valueOf(exception.code)
        val response = SimpleHttpResponseFactory.INSTANCE.status<HttpErrorBody>(status)
        response.contentType(MediaType.APPLICATION_JSON_TYPE)
        response.body(buildResponse(exception))
        return response
    }

    private fun buildResponse(exception: PetException): HttpErrorBody {
        val response = HttpErrorBody(exception.message)
        return response
    }
}
