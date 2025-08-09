package com.learningnotes.service.service

import com.learningnotes.service.repository.UserRepository
import com.learningnotes.service.response.UserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AdminService(private val userRepository: UserRepository) {
    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserResponse> {
        return userRepository
            .findAll()
            .map { UserResponse.fromUser(it) }
            .toList()
    }
}