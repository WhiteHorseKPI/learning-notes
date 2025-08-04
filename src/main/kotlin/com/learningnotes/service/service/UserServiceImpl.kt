package com.learningnotes.service.service

import com.learningnotes.service.entity.Authority
import com.learningnotes.service.entity.User
import com.learningnotes.service.repository.UserRepository
import com.learningnotes.service.response.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException


@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    @Transactional(readOnly = true)
    override fun getUserInfo(): UserResponse {
        val authentication = getAuthentication()
        checkUserAuthentication(authentication)
        val user = getUserFromAuthentication(authentication)
        return UserResponse(
            user.id,
            "${user.firstName} ${user.lastName}",
            user.email,
            user.authorities!!.stream().map { auth -> auth as Authority }.toList()
        )
    }

    override fun deleteUser() {
        val authentication = getAuthentication()
        checkUserAuthentication(authentication)
        val user = getUserFromAuthentication(authentication)

        if (isLastTeacher(user)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Teacher cannot delete itself")
        }

        userRepository.delete(user)
    }

    private fun getAuthentication(): Authentication = SecurityContextHolder.getContext().authentication

    private fun checkUserAuthentication(authentication: Authentication) {
        if (!authentication.isAuthenticated || authentication.principal.equals("anonymousUser")) {
            throw AccessDeniedException("Authentication required");
        }
    }

    private fun getUserFromAuthentication(authentication: Authentication): User = authentication.principal as User

    private fun isLastTeacher(user: User): Boolean {
        val isTeacher = user.authorities!!
            .stream()
            .anyMatch { authority -> "ROLE_TEACHER" == authority.authority }

        if (isTeacher) {
            val teachersCount: Long = userRepository.countTeachers()
            return teachersCount <= 1
        }

        return false
    }
}