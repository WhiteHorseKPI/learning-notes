package com.learningnotes.service.service

import com.learningnotes.service.request.RegisterRequest


interface AuthenticationService {
    fun register(input: RegisterRequest)
}