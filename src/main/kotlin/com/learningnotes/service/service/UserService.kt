package com.learningnotes.service.service

import com.learningnotes.service.request.PasswordUpdateRequest
import com.learningnotes.service.response.UserResponse


interface UserService {
    fun getUserInfo(): UserResponse
    fun deleteUser()
    fun updatePassword(passwordUpdateRequest: PasswordUpdateRequest)
}