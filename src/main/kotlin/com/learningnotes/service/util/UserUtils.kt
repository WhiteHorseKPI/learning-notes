package com.learningnotes.service.util

import com.learningnotes.service.entity.User
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder


internal object UserUtils {
    fun getAuthenticatedUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated || authentication.principal == "anonymousUser"
        ) {
            throw AccessDeniedException("Authentication required")
        }

        return authentication.principal as User
    }
}