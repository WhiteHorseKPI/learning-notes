package com.learningnotes.service.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size


class AuthenticationRequest {
    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    val email: String? = null

    @NotEmpty(message = "Password is mandatory")
    @Size(
        min = 5,
        max = 30,
        message = "Password must be at least 5 characters long"
    )
    val password: String? = null
}