package com.learningnotes.service.entity

import jakarta.persistence.Embeddable
import org.springframework.security.core.GrantedAuthority


@Embeddable
class Authority : GrantedAuthority {
    private var authority: String

    constructor(authority: String) {
        this.authority = authority
    }

    override fun getAuthority(): String? {
        return authority
    }
}