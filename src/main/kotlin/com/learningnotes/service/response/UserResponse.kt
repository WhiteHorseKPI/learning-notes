package com.learningnotes.service.response

import com.learningnotes.service.entity.Authority
import com.learningnotes.service.entity.User

class UserResponse(
    var id: Long,
    var fullName: String?,
    var email: String?,
    var authorities: MutableList<Authority>
) {
    companion object {
        fun fromUser(user: User): UserResponse {
            return UserResponse(
                user.id,
                "${user.firstName} ${user.lastName}",
                user.email,
                user.authorities!!.stream().map { auth -> auth as Authority }.toList()
            )
        }
    }
}