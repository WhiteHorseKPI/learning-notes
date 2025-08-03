package com.learningnotes.service.config

import com.learningnotes.service.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userRepository: UserRepository,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService {
        userRepository.findByEmail(it)
            .orElseThrow { UsernameNotFoundException("User not found") }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager? =
        config.authenticationManager

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint = AuthenticationEntryPoint {
        request, response, ex ->
            with(response) {
                status = HttpStatus.UNAUTHORIZED.value()
                contentType = "application/json"
                setHeader("WWW-Authenticate", "")
                writer.write("{\"error\": \"Unauthorized access\"}")
            }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? = with(http) {
        authorizeHttpRequests {
            it.requestMatchers(
                    "/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**",
                    "/swagger-resources/**", "/webjars/**", "/docs").permitAll()
        }
        csrf { it.disable() }
        exceptionHandling { it.authenticationEntryPoint(authenticationEntryPoint()) }
        sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return build()
    }
}