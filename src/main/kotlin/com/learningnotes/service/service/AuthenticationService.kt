package com.learningnotes.service.service

import com.learningnotes.service.request.AuthenticationRequest
import com.learningnotes.service.request.RegisterRequest
import com.learningnotes.service.response.AuthenticationResponse


interface AuthenticationService {
    fun register(input: RegisterRequest)
    fun login(request: AuthenticationRequest): AuthenticationResponse
}