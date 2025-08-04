package com.learningnotes.service.repository

import com.learningnotes.service.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional


@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String?): Optional<User>

    @Query("SELECT COUNT(u) FROM User u JOIN u.authorities a WHERE a.authority = 'ROLE_TEACHER'")
    fun countTeachers(): Long
}