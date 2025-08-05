package com.learningnotes.service.service

import com.learningnotes.service.entity.Authority
import com.learningnotes.service.entity.User
import com.learningnotes.service.repository.UserRepository
import com.learningnotes.service.request.PasswordUpdateRequest
import com.learningnotes.service.response.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
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

    @Transactional
    override fun updatePassword(passwordUpdateRequest: PasswordUpdateRequest) {
        val authentication = getAuthentication()
        checkUserAuthentication(authentication)
        val user = getUserFromAuthentication(authentication)

        if (!isOldPasswordCorrect(user.password, passwordUpdateRequest.oldPassword)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect")
        }

        if (!isNewPasswordConfirmed(
                passwordUpdateRequest.newPassword,
                passwordUpdateRequest.newPasswordConfirmation
            )
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "New passwords do not match")
        }

        if (!isNewPasswordDifferent(
                passwordUpdateRequest.oldPassword,
                passwordUpdateRequest.newPassword
            )
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Old and new passwords must be different")
        }

        user.password = passwordEncoder.encode(passwordUpdateRequest.newPassword)
        userRepository.save(user)
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

    private fun isOldPasswordCorrect(currentPassword: String?, oldPassword: String?): Boolean {
        return passwordEncoder.matches(oldPassword, currentPassword)
    }

    private fun isNewPasswordConfirmed(newPassword: String?, newPasswordConfirmation: String?): Boolean {
        return newPassword == newPasswordConfirmation
    }

    private fun isNewPasswordDifferent(oldPassword: String?, newPassword: String?): Boolean {
        return oldPassword != newPassword
    }
}