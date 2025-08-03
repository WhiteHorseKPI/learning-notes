package com.learningnotes.service.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

class RegisterRequest {
    @NotEmpty(message = "First name is mandatory")
    @Size(
        min = 3,
        max = 30,
        message = "First name must be at least 3 characters long"
    )
    var firstName: String? = null

    @NotEmpty(message = "Last name is mandatory")
    @Size(
        min = 3,
        max = 30,
        message = "Last name must be at least 3 characters long"
    )
    var lastName: String? = null

    @NotEmpty(message = "Email is mandatory") @Email(message = "Invalid email format")
    var email: String? = null

    @NotEmpty(message = "Password is mandatory")
    @Size(
        min = 5,
        max = 30,
        message = "Password must be at least 5 characters long"
    )
    var password: String? = null

    fun RegisterRequest(firstName: String?, lastName: String?, email: String?, password: String?) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.password = password
    }
}