package com.learningnotes.service.controller

import com.learningnotes.service.response.UserResponse
import com.learningnotes.service.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@Tag(name = "User REST API Endpoints", description = "Operations related to info about current user")
@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "User information", description = "Get current user info")
    fun getUserInfo(): UserResponse = userService.getUserInfo()

    @DeleteMapping
    @Operation(summary = "Delete user", description = "Delete current user from repository")
    fun deleteUser() = userService.deleteUser()
}