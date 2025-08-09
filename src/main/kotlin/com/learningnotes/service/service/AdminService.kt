package com.learningnotes.service.service

import com.learningnotes.service.entity.Authority
import com.learningnotes.service.entity.User
import com.learningnotes.service.repository.UserRepository
import com.learningnotes.service.response.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*


@Service
class AdminService(private val userRepository: UserRepository) {
    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserResponse> {
        return userRepository
            .findAll()
            .map { UserResponse.fromUser(it) }
            .toList()
    }

    @Transactional
    fun promoteToAdmin(userId: Long): UserResponse {
        val user: Optional<User> = userRepository.findById(userId)
        if (user.isEmpty || user.get().authorities!!
                .stream()
                .anyMatch { ("ROLE_TEACHER" == it.authority)
            }) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User does not exist or already an teacher"
            )
        }
        val authorities: MutableList<Authority> = mutableListOf()
        authorities.add(Authority("ROLE_STUDENT"))
        authorities.add(Authority("ROLE_TEACHER"))
        val notNullUser = user.get()
        notNullUser.authorities = authorities
        val savedUser: User = userRepository.save(notNullUser)
        return UserResponse.fromUser(savedUser)
    }
}