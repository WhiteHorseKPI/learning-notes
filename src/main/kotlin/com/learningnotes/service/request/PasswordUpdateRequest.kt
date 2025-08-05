package com.learningnotes.service.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size


class PasswordUpdateRequest(
    @Size(min = 5, max = 30, message = "Old password must be at least 5 characters long")
    @NotEmpty(message = "Old password is mandatory")
    var oldPassword: String?,

    @Size(min = 5, max = 30, message = "New password must be at least 5 characters long")
    @NotEmpty(message = "New password is mandatory")
    var newPassword: String?,

    @Size(min = 5, max = 30, message = "Confirmed password must be at least 5 characters long")
    @NotEmpty(message = "Confirmed password is mandatory")
    var newPasswordConfirmation:  String?
)