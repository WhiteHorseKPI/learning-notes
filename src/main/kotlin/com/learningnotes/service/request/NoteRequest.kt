package com.learningnotes.service.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

class NoteRequest(
    @Size(min = 3, max = 60, message = "Title size shall be in range from 3 to 60 characters")
    @NotEmpty(message = "Title is mandatory")
    val title: String?,

    @Size(min = 3, max = 256, message = "Description size shall be in range from 3 to 256 characters")
    @NotEmpty(message = "Description is mandatory")
    val description: String?
)