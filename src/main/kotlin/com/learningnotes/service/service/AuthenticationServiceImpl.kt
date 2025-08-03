package com.learningnotes.service.service

import com.learningnotes.service.entity.Authority
import com.learningnotes.service.entity.User
import com.learningnotes.service.repository.UserRepository
import com.learningnotes.service.request.AuthenticationRequest
import com.learningnotes.service.request.RegisterRequest
import com.learningnotes.service.response.AuthenticationResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AuthenticationServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) : AuthenticationService {

    @Transactional
    override fun register(input: RegisterRequest) {
        if (isEmailRegistered(input.email)) {
            throw Exception("Email already registered")
        }
        val user: User = buildNewUser(input)
        userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun login(request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Invalid email or password") }

        val jwtToken = jwtService.generateToken(emptyMap(), user)

        return AuthenticationResponse(jwtToken)
    }

    private fun isEmailRegistered(email: String?): Boolean = userRepository.findByEmail(email).isPresent

    private fun buildNewUser(input: RegisterRequest): User {
        val user = User()
        user.id = 0
        user.firstName = input.firstName
        user.lastName = input.lastName
        user.email = input.email
        user.password = passwordEncoder.encode(input.password)
        user.authorities = initialAuthority()
        return user
    }

    private fun initialAuthority(): MutableList<Authority> {
        val isFirstUser = userRepository.count() == 0.toLong()
        val authorities: MutableList<Authority> = ArrayList()
        authorities.add(Authority("ROLE_STUDENT"))
        if (isFirstUser) {
            authorities.add(Authority("ROLE_TEACHER"))
        }
        return authorities
    }
}