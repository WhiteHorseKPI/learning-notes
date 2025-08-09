package com.learningnotes.service.controller

import com.learningnotes.service.response.UserResponse
import com.learningnotes.service.service.AdminService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@Tag(name = "Admin REST API Endpoints", description = "Operations related to a admin")
@RestController
@RequestMapping("/api/admin")
class AdminController(private val adminService: AdminService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
    fun getAllUsers(): List<UserResponse> {
        return adminService.getAllUsers()
    }

    @Operation(summary = "Promote user to admin", description = "Promote user to teacher role")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userId}/role")
    fun promoteToAdmin(@PathVariable @Min(1) userId: Long): UserResponse {
        return adminService.promoteToAdmin(userId)
    }
}