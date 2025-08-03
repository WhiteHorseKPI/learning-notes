package com.learningnotes.service.service

import com.learningnotes.service.entity.Authority
import com.learningnotes.service.entity.User
import com.learningnotes.service.repository.UserRepository
import com.learningnotes.service.response.UserResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    @Transactional(readOnly = true)
    override fun getUserInfo(): UserResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated || authentication.principal.equals("anonymousUser")) {
            throw AccessDeniedException("Authentication required");
        }
        val user = authentication.principal as User
        return UserResponse(
            user.id,
            "${user.firstName} ${user.lastName}",
            user.email,
            user.authorities!!.stream().map { auth -> auth as Authority }.toList()
        )
    }
}