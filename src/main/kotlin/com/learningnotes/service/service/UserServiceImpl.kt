package com.learningnotes.service.service

import com.learningnotes.service.entity.Authority
import com.learningnotes.service.entity.User
import com.learningnotes.service.repository.UserRepository
import com.learningnotes.service.request.PasswordUpdateRequest
import com.learningnotes.service.response.UserResponse
import com.learningnotes.service.util.UserUtils
import org.springframework.http.HttpStatus
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
        val user = UserUtils.getAuthenticatedUser()
        return UserResponse.fromUser(user)
    }

    override fun deleteUser() {
        val user = UserUtils.getAuthenticatedUser()
        if (isLastTeacher(user)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Teacher cannot delete itself")
        }
        userRepository.delete(user)
    }

    @Transactional
    override fun updatePassword(passwordUpdateRequest: PasswordUpdateRequest) {
        val user = UserUtils.getAuthenticatedUser()
        if (!isOldPasswordCorrect(user.password, passwordUpdateRequest.oldPassword)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect")
        }

        if (!isNewPasswordConfirmed(
                passwordUpdateRequest.newPassword,
                passwordUpdateRequest.newPasswordConfirmation
            )) {
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