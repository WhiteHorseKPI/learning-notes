package com.learningnotes.service.repository

import com.learningnotes.service.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional


@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String?): Optional<User>
}