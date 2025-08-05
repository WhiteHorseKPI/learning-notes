package com.learningnotes.service.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException


@ControllerAdvice
class ExceptionHandlers {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleException(e: ResponseStatusException): ResponseEntity<ExceptionResponse> {
        return buildResponseEntity(e.message, HttpStatus.valueOf(e.statusCode.value()))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ExceptionResponse> {
        return buildResponseEntity("Invalid request", HttpStatus.BAD_REQUEST)
    }

    private fun buildResponseEntity(errorMessage: String?, status: HttpStatus): ResponseEntity<ExceptionResponse> {
        val error = ExceptionResponse(
            status = status.value(),
            message = errorMessage,
            timeStamp = System.currentTimeMillis()
        )
        return ResponseEntity<ExceptionResponse>(error, status)
    }
}