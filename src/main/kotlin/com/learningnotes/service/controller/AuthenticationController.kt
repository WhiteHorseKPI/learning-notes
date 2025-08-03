package com.learningnotes.service.controller

import com.learningnotes.service.request.RegisterRequest
import com.learningnotes.service.service.AuthenticationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication REST API Endpoints", description = "Operations related to register & login")
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @Operation(summary = "Register a user", description = "Create new user in database")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    fun register(@RequestBody @Valid registerRequest: RegisterRequest) {
        authenticationService.register(registerRequest)
    }
}