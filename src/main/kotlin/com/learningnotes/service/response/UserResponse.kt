package com.learningnotes.service.response

import com.learningnotes.service.entity.Authority

class UserResponse(
    var id: Long,
    var fullName: String?,
    var email: String?,
    var authorities: MutableList<Authority>
)